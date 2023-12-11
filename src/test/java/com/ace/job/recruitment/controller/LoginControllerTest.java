package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.EmailTemplate;
import com.ace.job.recruitment.other.EmailTemplateLoader;
import com.ace.job.recruitment.service.EmailService;
import com.ace.job.recruitment.service.PasswordResetService;
import com.ace.job.recruitment.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@Mock
	private EmailService emailService;

	@Mock
	private PasswordResetService passwordResetService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private EmailTemplateLoader loader;

	@InjectMocks
	private LoginController loginController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}

	@Test
	public void loginPageTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/signin")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("login"));
	}

	@Test
	public void validateEmailValidEmailTest() {
		String email = "test@example.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset(1, "234908", 2397423947L, false, user);
		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.storePasswordReset((PasswordReset) any(PasswordReset.class)))
				.thenReturn(passwordReset);

		ResponseEntity<String[]> response = loginController.validateEmail(email);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(email, response.getBody()[0]);
		assertEquals("Email validation success! An email with reset code will be sent to you.", response.getBody()[1]);

		verify(userService, times(1)).getUserByEmail(email);
	}

	@Test
	public void validateEmailInvalidEmailTest() {
		String email = "test@example.com";
		User user = new User();
		when(userService.getUserByEmail(email)).thenReturn(Collections.emptyList());
		ResponseEntity<String[]> response = loginController.validateEmail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Incorrect email! Please try again.", response.getBody()[1]);

		verify(userService, times(1)).getUserByEmail(email);
	}

	@Test
	public void validateEmailStoreFailTest() {
		String email = "test@example.com";
		User user = new User();
		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.storePasswordReset((PasswordReset) any(PasswordReset.class))).thenReturn(null);

		ResponseEntity<String[]> response = loginController.validateEmail(email);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Something went wrong! Please try again.", response.getBody()[1]);

		verify(userService, times(1)).getUserByEmail(email);
	}

	@Test
	public void validateResetTokenValidTest() {
		String resetToken = "123454";
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setId(1);
		passwordReset.setResetToken(resetToken);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 + 3600);
		passwordReset.setStatus(false);
		when(passwordResetService.getByResetToken(resetToken)).thenReturn(passwordReset);

		ResponseEntity<Long> response = loginController.validateResetToken(resetToken);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1L, response.getBody());

		verify(passwordResetService, times(1)).getByResetToken(resetToken);
	}

	@Test
	public void validateResetTokenNullTest() {
		String resetToken = null;
		ResponseEntity<Long> response = loginController.validateResetToken(resetToken);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0L, response.getBody());

	}

	@Test
	public void validateResetTokenEmptyStringTest() {
		String resetToken = "";
		ResponseEntity<Long> response = loginController.validateResetToken(resetToken);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0L, response.getBody());

	}

	@Test
	public void validateResetTokenExpiredTest() {
		String resetToken = "123454";
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 - 3600);
		when(passwordResetService.getByResetToken(resetToken)).thenReturn(passwordReset);

		ResponseEntity<Long> response = loginController.validateResetToken(resetToken);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0L, response.getBody());

		verify(passwordResetService, times(1)).getByResetToken(resetToken);
	}

	@Test
	public void validateResetTokenInvalidStatusTest() {
		String resetToken = "123454";
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setStatus(true);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 + 3600);
		when(passwordResetService.getByResetToken(resetToken)).thenReturn(passwordReset);

		ResponseEntity<Long> response = loginController.validateResetToken(resetToken);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0L, response.getBody());

		verify(passwordResetService, times(1)).getByResetToken(resetToken);
	}

	@Test
	public void validateResetTokenNotFoundTest() {
		String resetToken = "243435";
		when(passwordResetService.getByResetToken(resetToken)).thenReturn(null);

		ResponseEntity<Long> response = loginController.validateResetToken(resetToken);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0L, response.getBody());

		verify(passwordResetService, times(1)).getByResetToken(resetToken);
	}

	@Test
	public void resetPasswordSuccessTest() {
		long resetId = 1L;
		String newPassword = "newpassword";

		User user = new User();
		user.setId(1);

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(user);

		when(passwordResetService.getById(resetId)).thenReturn(passwordReset);

		when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
		when(userService.updateUser(user)).thenReturn(user);
		when(passwordResetService.updatePasswordReset(passwordReset)).thenReturn(passwordReset);

		ResponseEntity<Boolean> response = loginController.resetPassword(resetId, newPassword);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody());

		verify(passwordResetService, times(1)).getById(resetId);
		verify(passwordResetService, times(1)).updatePasswordReset(passwordReset);
		verify(userService, times(1)).updateUser(user);
		verify(passwordEncoder, times(1)).encode(newPassword);
	}

	@Test
	public void resetPasswordFailTest() {
		long resetId = 1L;
		String newPassword = "newpassword";

		User user = new User();
		user.setId(1);

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(user);

		when(passwordResetService.getById(resetId)).thenReturn(passwordReset);

		when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
		when(userService.updateUser(user)).thenReturn(null);

		ResponseEntity<Boolean> response = loginController.resetPassword(resetId, newPassword);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody());

		verify(passwordResetService, times(1)).getById(resetId);
		verify(userService, times(1)).updateUser(user);
		verify(passwordEncoder, times(1)).encode(newPassword);
	}

	@Test
	public void resetPasswordUpdatePRFailTest() {
		long resetId = 1L;
		String newPassword = "newpassword";

		User user = new User();
		user.setId(1);

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(user);

		when(passwordResetService.getById(resetId)).thenReturn(passwordReset);

		when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
		when(userService.updateUser(user)).thenReturn(user);
		when(passwordResetService.updatePasswordReset(passwordReset)).thenReturn(null);

		ResponseEntity<Boolean> response = loginController.resetPassword(resetId, newPassword);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody());

		verify(passwordResetService, times(1)).getById(resetId);
		verify(passwordResetService, times(1)).updatePasswordReset(passwordReset);
		verify(userService, times(1)).updateUser(user);
		verify(passwordEncoder, times(1)).encode(newPassword);
	}

	@Test
	public void sendResetMailValidTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		EmailTemplate template = new EmailTemplate();
		template.setBody("[resetCode]");
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setResetToken("123456");
		passwordReset.setStatus(false);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 + 900);

		when(loader.getEmailTemplate("2")).thenReturn(template);
		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));
		when(emailService.sendMail("test@gmail.com", null, "123456")).thenReturn("success");

		ResponseEntity<String> response = loginController.sendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
		verify(emailService, times(1)).sendMail("test@gmail.com", null, "123456");
	}

	@Test
	public void sendResetMailPREmptyTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.emptyList());

		ResponseEntity<String> response = loginController.sendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Something went wrong! Please try again!", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
	}

	@Test
	public void sendResetMailTokenNullTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setResetToken(null);
		passwordReset.setStatus(false);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 + 900);

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));

		ResponseEntity<String> response = loginController.sendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Sorry! Reset code has expired.", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
	}

	@Test
	public void sendResetMailResetStatusTrueTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setResetToken("123456");
		passwordReset.setStatus(true);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 + 900);

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));

		ResponseEntity<String> response = loginController.sendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("You have already reset your password with this code.", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
	}

	@Test
	public void sendResetMailTokenExpiredTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setResetToken("123456");
		passwordReset.setStatus(false);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000);

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));

		ResponseEntity<String> response = loginController.sendResetMail(email);
		System.out.println(System.currentTimeMillis() / 1000);
		System.out.println(passwordReset.getExpireTimestamp());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Sorry! Reset code has expired.", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
	}

	@Test
	public void sendResetMailTokenExpiredBothConditionsTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setResetToken(null);
		passwordReset.setStatus(false);
		passwordReset.setExpireTimestamp(System.currentTimeMillis() / 1000 - 900);

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));

		ResponseEntity<String> response = loginController.sendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Sorry! Reset code has expired.", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
	}

	@Test
	public void resendResetMailSuccessTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		user.setEmail(email);

		EmailTemplate template = new EmailTemplate();
		template.setBody("[resetCode]");

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setStatus(false);

		when(loader.getEmailTemplate("2")).thenReturn(template);
		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));
		when(passwordResetService.updatePasswordReset(any())).thenReturn(passwordReset);

		ResponseEntity<String> response = loginController.resendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
		verify(passwordResetService, times(1)).updatePasswordReset(any());
	}

	@Test
	public void resendResetMailUpdatePRFailTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setStatus(false);

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.singletonList(passwordReset));
		when(passwordResetService.updatePasswordReset(any())).thenReturn(null);

		ResponseEntity<String> response = loginController.resendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Something went wrong! Please try again.", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);
		verify(passwordResetService, times(1)).updatePasswordReset(any());
	}

	@Test
	public void resendResetMailPREmptylTest() throws MessagingException {
		String email = "test@gmail.com";
		User user = new User();
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setStatus(false);

		when(userService.getUserByEmail(email)).thenReturn(Collections.singletonList(user));
		when(passwordResetService.getByUser(user)).thenReturn(Collections.emptyList());

		ResponseEntity<String> response = loginController.resendResetMail(email);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Something went wrong! Please try again!", response.getBody());

		verify(userService, times(1)).getUserByEmail(email);
		verify(passwordResetService, times(1)).getByUser(user);

	}

}
