package com.ace.job.recruitment.model;

import javax.xml.bind.annotation.XmlRootElement;

//for each email template
@XmlRootElement(name = "template")
public class EmailTemplate {

	private String id;

	private String subject;

	private String body;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}