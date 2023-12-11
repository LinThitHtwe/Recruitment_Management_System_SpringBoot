package com.ace.job.recruitment.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//for all email templates
@XmlRootElement(name = "emailTemplates")
public class EmailTemplates {

	private List<EmailTemplate> templates;

	@XmlElement(name = "template")
	public List<EmailTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<EmailTemplate> templates) {
		this.templates = templates;
	}

}