package com.ace.job.recruitment.other;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.ace.job.recruitment.model.EmailTemplate;
import com.ace.job.recruitment.model.EmailTemplates;

@Component
public class EmailTemplateLoader {
	private EmailTemplates emailTemplates;
	private static final String defaultTemplateId = "1";

	public EmailTemplateLoader(ResourceLoader resourceLoader) {
	}

	@PostConstruct
	public void init() throws IOException, JAXBException {
		try {
			// Load xml file from the static/xmlfiles directory
			ClassPathResource resource = new ClassPathResource("static/xmlfiles/emailtemplate.xml");

			// Parse xml file with JAXB
			JAXBContext jaxbContext = JAXBContext.newInstance(EmailTemplates.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			emailTemplates = (EmailTemplates) jaxbUnmarshaller.unmarshal(resource.getInputStream());
		} catch (IOException | JAXBException e) {
			// Handle exceptions
			emailTemplates = createDefaultTemplates();
			e.printStackTrace(); // Or log the exception as appropriate
		}
	}

	public EmailTemplate getEmailTemplate(String templateId) {
		// Check if emailTemplates is null or empty
		if (emailTemplates == null || emailTemplates.getTemplates() == null
				|| emailTemplates.getTemplates().isEmpty()) {
			return getDefaultTemplate();
		}

		// Find email template by id
		EmailTemplate template = emailTemplates.getTemplates().stream()
				.filter(t -> t.getId().trim().equals(templateId.trim())) // <-- Trim both templateId and id from XML
				.findFirst().orElse(null);

		// Return the template or if there isn't, get default template
		return (template != null) ? template : getDefaultTemplate();
	}

	public EmailTemplate getDefaultTemplate() {
		return createDefaultTemplate();
	}

	private EmailTemplate createDefaultTemplate() {
		EmailTemplate template = new EmailTemplate();
		template.setId(defaultTemplateId);
		template.setSubject("Subject");
		template.setBody("<p>Email Content</p>");
		return template;
	}

	private EmailTemplates createDefaultTemplates() {
		EmailTemplates templates = new EmailTemplates();
		// Create an empty list for templates to avoid NullPointerException
		templates.setTemplates(new ArrayList<>());
		templates.getTemplates().add(createDefaultTemplate());
		return templates;
	}
}