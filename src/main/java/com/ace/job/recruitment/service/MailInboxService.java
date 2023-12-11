package com.ace.job.recruitment.service;

import java.io.IOException;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.ace.job.recruitment.model.EmailInbox;

public interface MailInboxService {
	public int getAttachmentCount(Message message) throws MessagingException, IOException;

	public List<EmailInbox> readEmailsLast24Hours() throws MessagingException;

	public String getEmailContent(Message message) throws MessagingException, IOException;
}
