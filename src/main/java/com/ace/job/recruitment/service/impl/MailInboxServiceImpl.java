package com.ace.job.recruitment.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.springframework.stereotype.Service;

import com.ace.job.recruitment.model.EmailInbox;
import com.ace.job.recruitment.service.MailInboxService;

@Service
public class MailInboxServiceImpl implements MailInboxService {

	@Override
	public int getAttachmentCount(Message message) throws MessagingException, IOException {
		int attachmentCount = 0;
		Object content = message.getContent();

		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;

			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
					attachmentCount++;
				}
			}
		}

		return attachmentCount;
	}

	@Override
	public List<EmailInbox> readEmailsLast24Hours() throws MessagingException {
		Properties properties = new Properties();
		properties.put("mail.store.protocol", "imaps");

		Session session = Session.getDefaultInstance(properties);

		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", "hundredpercent81@gmail.com", "ogxrwgklepcjsoyk");

		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);

		// Calculate the date 24 hours ago
		long currentTimeMillis = System.currentTimeMillis();
		long twentyFourHoursAgo = currentTimeMillis - (24 * 60 * 60 * 1000); // 24 hours in milliseconds
		Date sinceDate = new Date(twentyFourHoursAgo);

		SearchTerm searchTerm = new ReceivedDateTerm(ComparisonTerm.GE, sinceDate);

		Message[] messages = inbox.search(searchTerm);

		// Sort the messages in descending order based on the sent date
		Arrays.sort(messages, (msg1, msg2) -> {
			try {
				return msg2.getSentDate().compareTo(msg1.getSentDate());
			} catch (MessagingException e) {
				e.printStackTrace();
				return 0;
			}
		});

		List<EmailInbox> emailInfoList = new ArrayList<>();

		for (Message message : messages) {
			try {
				String subject = message.getSubject();
				Address[] fromAddresses = message.getFrom();

				String sender = "";
				if (fromAddresses.length > 0) {
					sender = fromAddresses[0].toString();
				}

				String emailContent = getEmailContent(message);

				int maxPreviewLength = 150; // Adjust the length as needed

				// Truncate the email body content for the preview
				String emailPreview = emailContent.substring(0, Math.min(emailContent.length(), maxPreviewLength));

				// Count the number of attachments
				int attachmentCount = getAttachmentCount(message);

				Date sentDate = message.getSentDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss");
				String formattedSentDate = dateFormat.format(sentDate);

				// Create an EmailInfo object and add it to the list
				EmailInbox emailInfo = new EmailInbox();
				emailInfo.setSentDate(formattedSentDate);
				emailInfo.setSender(sender);
				emailInfo.setSubject(subject);
				emailInfo.setBody(emailContent);
				emailInfo.setAttachmentCount(attachmentCount);
				emailInfo.setBodyPreview(emailPreview + "...");

				emailInfoList.add(emailInfo);

			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}
		}

		inbox.close(false);
		store.close();

		return emailInfoList;
	}

	@Override
	public String getEmailContent(Message message) throws MessagingException, IOException {
		Object content = message.getContent();

		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;

			StringBuilder plainTextContent = new StringBuilder();
			StringBuilder htmlContent = new StringBuilder();
			Set<String> processedContent = new HashSet<>();

			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);

				if (bodyPart.isMimeType("text/plain")) {
					String plainText = bodyPart.getContent().toString();
					if (!processedContent.contains(plainText)) {
						plainTextContent.append(plainText);
						processedContent.add(plainText);
					}
				} else if (bodyPart.isMimeType("text/html")) {
					String html = bodyPart.getContent().toString();
					if (!processedContent.contains(html)) {
						htmlContent.append(html);
						processedContent.add(html);
					}
				}
			}

			// Return HTML content if available, else return plain text content
			if (htmlContent.length() > 0) {
				return htmlContent.toString();
			} else {
				return plainTextContent.toString();
			}
		} else if (content instanceof String) {
			return (String) content;
		}

		return "Unsupported email content type";
	}

}
