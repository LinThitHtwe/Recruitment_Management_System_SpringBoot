package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.ace.job.recruitment.model.EmailInbox;
import com.ace.job.recruitment.service.MailInboxService;

class MailInboxControllerTest {

	@Mock
	private MailInboxService mailInboxService;

	@Mock
	private Model model;

	@InjectMocks
	private MailInboxController mailInboxController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testReadMail24hour_NoEmails() throws MessagingException {
		List<EmailInbox> emptyEmailList = new ArrayList<>();
		when(mailInboxService.readEmailsLast24Hours()).thenReturn(emptyEmailList);

		String result = mailInboxController.readMail24hour(model);

		verify(model).addAttribute("noMailMsg", "There is no incoming mail in the last 24 hours.");
		verify(model).addAttribute("emailInboxList", emptyEmailList);
		assertEquals("email/mail-inbox", result);
	}

	@Test
	void testReadMail24hour_WithEmails() throws MessagingException {
		List<EmailInbox> emailList = new ArrayList<>();
		emailList.add(new EmailInbox());
		when(mailInboxService.readEmailsLast24Hours()).thenReturn(emailList);

		String result = mailInboxController.readMail24hour(model);

		verify(model, never()).addAttribute(eq("noMailMsg"), anyString());
		verify(model).addAttribute("emailInboxList", emailList);
		assertEquals("email/mail-inbox", result);
	}
}
