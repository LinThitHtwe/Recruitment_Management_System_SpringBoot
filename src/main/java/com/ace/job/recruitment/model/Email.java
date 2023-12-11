package com.ace.job.recruitment.model;

public class Email {
	private String recipientAddress;
	private String[] ccAddress;
	private String subject;
	private String body;
	private String[] attachment;

	public Email() {
		super();
	}

	public Email(String recipientAddress, String[] ccAddress, String subject, String body, String[] attachment) {
		super();
		this.recipientAddress = recipientAddress;
		this.ccAddress = ccAddress;
		this.subject = subject;
		this.body = body;
		this.attachment = attachment;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public String[] getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String[] getAttachment() {
		return attachment;
	}

	public void setAttachment(String[] attachment) {
		this.attachment = attachment;
	}

}