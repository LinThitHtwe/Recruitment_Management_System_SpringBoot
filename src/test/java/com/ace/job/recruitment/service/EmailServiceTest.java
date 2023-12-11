package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.model.Email;
import com.ace.job.recruitment.service.impl.EmailServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

	@Mock
	private JavaMailSender javaMailSender;

	@InjectMocks
	private EmailServiceImpl emailServiceImpl;

	@Test
	public void sendMailWithCCAttachmentSuccessWithCCAttachmentNotNullTest() {
		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(new String[] { "cc@example.com" });
		email.setAttachment(new String[] { "attachment_path" });

		JavaMailSender javaMailSender = mock(JavaMailSender.class);

		EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender);
		MimeMessage mimeMessageMock = mock(MimeMessage.class);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);

		String result = emailService.sendMailWithCCAttachment(email);

		assertEquals("Invalid attachments: attachment_path", result);
	}

	@Test
	public void sendMailWithCCAttachmentSuccessWithCCAttachmentNullTest() {
		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(null);
		email.setAttachment(null);

		JavaMailSender javaMailSender = mock(JavaMailSender.class);

		EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender);
		MimeMessage mimeMessageMock = mock(MimeMessage.class);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);
		doNothing().when(javaMailSender).send(any(MimeMessage.class));

		String result = emailService.sendMailWithCCAttachment(email);

		verify(javaMailSender, times(1)).createMimeMessage();
		verify(javaMailSender, times(1)).send(any(MimeMessage.class));

		assertEquals("success", result);
	}

	@Test
	public void sendMailWithCCAttachmentSuccessWithCCAttachmentEmptyTest() {
		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(new String[0]);
		email.setAttachment(new String[0]);

		JavaMailSender javaMailSender = mock(JavaMailSender.class);

		EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender);
		MimeMessage mimeMessageMock = mock(MimeMessage.class);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);
		doNothing().when(javaMailSender).send(any(MimeMessage.class));

		String result = emailService.sendMailWithCCAttachment(email);

		verify(javaMailSender, times(1)).createMimeMessage();
		verify(javaMailSender, times(1)).send(any(MimeMessage.class));

		assertEquals("success", result);
	}

	@Test
	public void sendMailWithCCAttachmentMailAuthenticationExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl(); // Use a real instance
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(new String[] { "cc@example.com" });
		email.setAttachment(new String[] { "attachment_path" });

//		doThrow(new MailAuthenticationException("Sorry! Mail authentication failed.")).when(spyMailSender)
//				.send(any(MimeMessage.class));

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMailWithCCAttachment(email);

		// assertEquals("Sorry! Mail authentication failed.", result);
	}

	@Test
	public void sendMailWithCCAttachmentMailSendExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl(); // Use a real instance
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(new String[] { "cc@example.com" });
		email.setAttachment(new String[] { "attachment_path" });

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMailWithCCAttachment(email);

		assertEquals("Invalid attachments: attachment_path", result);
	}

	@Test
	public void sendMailWithCCAttachmentMessagingExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl(); // Use a real instance
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(new String[] { "cc@example.com" });
		email.setAttachment(new String[] { "attachment_path" });

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMailWithCCAttachment(email);
	}

	@Test
	public void sendMailWithCCAttachmentExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl();
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		Email email = new Email();
		email.setRecipientAddress("recipient@example.com");
		email.setSubject("Interview Invitation");
		email.setBody("Email Body");
		email.setCcAddress(new String[] { "cc@example.com" });
		email.setAttachment(new String[] { "attachment_path" });

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMailWithCCAttachment(email);
	}

	@Test
	public void sendMailSuccessTest() {
		String toEmail = "recipient@example.com";
		String subject = "Interview Invitation";
		String body = "Email Body";

		JavaMailSender javaMailSender = mock(JavaMailSender.class);

		EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender);
		MimeMessage mimeMessageMock = mock(MimeMessage.class);

		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessageMock);
		doNothing().when(javaMailSender).send(any(MimeMessage.class));

		String result = emailService.sendMail(toEmail, subject, body);

		verify(javaMailSender, times(1)).createMimeMessage();
		verify(javaMailSender, times(1)).send(any(MimeMessage.class));

		assertEquals("success", result);
	}

	@Test
	public void sendMailMailAuthenticationExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl(); // Use a real instance
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		String toEmail = "recipient@example.com";
		String subject = "Interview Invitation";
		String body = "Email Body";

		doThrow(new MailAuthenticationException("Sorry! Mail authentication failed.")).when(spyMailSender)
				.send(any(MimeMessage.class));

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMail(toEmail, subject, body);

		assertEquals("Sorry! Mail authentication failed.", result);
	}

	@Test
	public void sendMailMailPreparationExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl(); // Use a real instance
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		String toEmail = "recipient@example.com";
		String subject = "Interview Invitation";
		String body = "Email Body";

		doThrow(new MailPreparationException("Sorry! Something went wrong when preparing email.")).when(spyMailSender)
				.send(any(MimeMessage.class));

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMail(toEmail, subject, body);

		assertEquals("Sorry! Something went wrong when preparing email.", result);
	}

	@Test
	public void sendMailMailSendExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl(); // Use a real instance
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		String toEmail = "recipient@example.com";
		String subject = "Interview Invitation";
		String body = "Email Body";

		doThrow(new MailSendException("Sorry! Something went wrong when sending email.")).when(spyMailSender)
				.send(any(MimeMessage.class));

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMail(toEmail, subject, body);

		assertEquals("Sorry! Something went wrong when sending email.", result);
	}

	@Test
	public void sendMailMessagingExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl();
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		String toEmail = "recipient@example.com";
		String subject = "Interview Invitation";
		String body = "Email Body";

		doAnswer(invocation -> {
			throw new MessagingException("Sorry! Something went wrong when sending email.");
		}).when(spyMailSender).send(any(MimeMessage.class));

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMail(toEmail, subject, body);

		assertEquals("Sorry! Something went wrong when sending email.", result);
	}

	@Test
	public void sendMailExceptionTest() throws Exception {
		JavaMailSender javaMailSender = new JavaMailSenderImpl();
		JavaMailSender spyMailSender = Mockito.spy(javaMailSender);

		String toEmail = "recipient@example.com";
		String subject = "Interview Invitation";
		String body = "Email Body";

		doAnswer(invocation -> {
			throw new Exception("Sorry! Unexpected error occurred.");
		}).when(spyMailSender).send(any(MimeMessage.class));

		EmailServiceImpl emailService = new EmailServiceImpl(spyMailSender);

		String result = emailService.sendMail(toEmail, subject, body);

		assertEquals("Sorry! Unexpected error occurred.", result);
	}

	@Test
	void getCurrentUserTest() {
		Authentication authentication = mock(Authentication.class);
		AppUserDetails appUserDetails = mock(AppUserDetails.class);

		when(authentication.getPrincipal()).thenReturn(appUserDetails);

		User expectedUser = new User();
		when(appUserDetails.getUser()).thenReturn(expectedUser);

		User actualUser = emailServiceImpl.getCurrentUser(authentication);

		assertEquals(expectedUser, actualUser);
	}
}
