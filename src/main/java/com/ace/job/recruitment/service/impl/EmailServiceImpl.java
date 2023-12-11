package com.ace.job.recruitment.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.model.Email;
import com.ace.job.recruitment.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender javaMailSender = null;

	@Override
	public User getCurrentUser(Authentication authentication) {
		AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
		return appUserDetails.getUser();
	}

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public String sendMailWithCCAttachment(Email email) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		String statusMsg = null;
		List<String> invalidCcAddresses = new ArrayList<>();
		String invalidAttachments = "";

		try {
			helper = new MimeMessageHelper(message, true);

			helper.setFrom("hundredpercent81@gmail.com");
			helper.setTo(email.getRecipientAddress());
			helper.setSubject(email.getSubject());
			helper.setText(email.getBody(), true);

			// for cc
			if (email.getCcAddress() != null && email.getCcAddress().length > 0) {
				List<InternetAddress> validCcAddresses = new ArrayList<>();

				for (String cc : email.getCcAddress()) {
					if (cc != null && !cc.isEmpty()) {
						try {
							InternetAddress ccAddress = new InternetAddress(cc);
							ccAddress.validate();
							validCcAddresses.add(ccAddress);
						} catch (AddressException e) {
							invalidCcAddresses.add(cc);
						}
					}
				}

				if (!invalidCcAddresses.isEmpty()) {
					statusMsg = "Invalid CC addresses: " + String.join(", ", invalidCcAddresses);
				}

				if (!validCcAddresses.isEmpty()) {
					InternetAddress[] ccAddress = validCcAddresses.toArray(new InternetAddress[0]);
					helper.setCc(ccAddress);
				}
			}

			// for attachment
			if (email.getAttachment() != null && email.getAttachment().length > 0) {
				for (String path : email.getAttachment()) {
					if (path != null && !path.isEmpty()) {
						FileSystemResource file = new FileSystemResource(path);
						if (file.exists()) {
							helper.addAttachment(file.getFilename(), file);
						} else {
							if (!invalidAttachments.isEmpty()) {
								invalidAttachments += ", ";
							}
							invalidAttachments += file.getFilename();
						}
					}
				}
			}

			if (!invalidAttachments.isEmpty()) {
				if (statusMsg == null) {
					statusMsg = "Invalid attachments: " + invalidAttachments;
				} else {
					statusMsg += " and Invalid attachments: " + invalidAttachments;
				}
			}

			// only when no error, email will be sent
			if (statusMsg == null) {
				javaMailSender.send(message);
				statusMsg = "success";

			}
		} catch (MailAuthenticationException e) {
			statusMsg = "Sorry! Mail authentication failed.";
			e.printStackTrace();
		} catch (MailPreparationException e) {
			statusMsg = "Sorry! Something went wrong when preparing email.";
			e.printStackTrace();
		} catch (MailSendException e) {
			statusMsg = "Sorry! Something went wrong when sending email.";
			e.printStackTrace();
		} catch (MessagingException e) {
			statusMsg = "Sorry! Something went wrong when sending email.";
			e.printStackTrace();

		} catch (Exception e) {
			statusMsg = "Sorry! Unexpected error occurred.";
			e.printStackTrace();
		}
		return statusMsg;

	}

	@Override
	public String sendMail(String toEmail, String subject, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		String statusMsg = null;
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom("hundredpercent81@gmail.com");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true); // Set the content type to HTML
			javaMailSender.send(message);
			statusMsg = "success";

		} catch (MailAuthenticationException e) {
			statusMsg = "Sorry! Mail authentication failed.";
			e.printStackTrace();
		} catch (MailPreparationException e) {
			statusMsg = "Sorry! Something went wrong when preparing email.";
			e.printStackTrace();
		} catch (MailSendException e) {
			statusMsg = "Sorry! Something went wrong when sending email.";
			e.printStackTrace();
		} catch (MessagingException e) {
			statusMsg = "Sorry! Something went wrong when sending email.";
			e.printStackTrace();

		} catch (Exception e) {
			statusMsg = "Sorry! Unexpected error occurred.";
			e.printStackTrace();
		}

		return statusMsg;
	}

	@Override
	public String validateCCAndAttachment(String[] ccAddresses, String[] attachments) {
		List<String> invalidCcAddresses = new ArrayList<>();
		String invalidAttachments = "";
		String statusMsg = null;

		// for cc
		if (ccAddresses != null && ccAddresses.length > 0) {
			List<InternetAddress> validCcAddresses = new ArrayList<>();

			for (String cc : ccAddresses) {
				if (cc != null && !cc.isEmpty()) {
					try {
						InternetAddress ccAddress = new InternetAddress(cc);
						ccAddress.validate();
						validCcAddresses.add(ccAddress);
					} catch (AddressException e) {
						invalidCcAddresses.add(cc);
					}
				}
			}

			if (!invalidCcAddresses.isEmpty()) {
				statusMsg = "Invalid CC addresses: " + String.join(", ", invalidCcAddresses);
			}
		}

		// for attachment
		if (attachments != null && attachments.length > 0) {
			for (String path : attachments) {
				if (path != null && !path.isEmpty()) {
					FileSystemResource file = new FileSystemResource(path);
					if (!file.exists()) {
						if (!invalidAttachments.isEmpty()) {
							invalidAttachments += ", ";
						}
						invalidAttachments += file.getFilename();
					}
				}
			}
		}

		if (!invalidAttachments.isEmpty()) {
			if (statusMsg == null) {
				statusMsg = "Invalid attachments: " + invalidAttachments;
			} else {
				statusMsg += " and Invalid attachments: " + invalidAttachments;
			}
		}
		return statusMsg;
	}

}
