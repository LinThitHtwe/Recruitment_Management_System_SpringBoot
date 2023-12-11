package com.ace.job.recruitment.controller;

import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.model.EmailTemplate;
import com.ace.job.recruitment.other.EmailTemplateLoader;
import com.ace.job.recruitment.service.EmailService;
import com.ace.job.recruitment.service.PasswordResetService;
import com.ace.job.recruitment.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	PasswordResetService passwordResetService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailTemplateLoader loader;

	@GetMapping("signin")
	public String loginPage(Authentication authentication) {
		if (authentication != null) {
			AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();

			if (appUserDetails.getRole().equalsIgnoreCase("Department-head")
					|| appUserDetails.getRole().equalsIgnoreCase("Interviewer")) {
				return "redirect:/interviewer/candidates";
			}
			if (appUserDetails.getRole().equalsIgnoreCase("Default-Admin")
					|| appUserDetails.getRole().equalsIgnoreCase("Admin")
					|| appUserDetails.getRole().equalsIgnoreCase("Junior-Hr")
					|| appUserDetails.getRole().equalsIgnoreCase("Senior-Hr")) {

				return "redirect:/hr/dashboard";
			}
		}

		return "login";
	}

	@PostMapping("/validate-email")
	public ResponseEntity<String[]> validateEmail(@RequestParam("email") String email) {
		String[] statusMsg = new String[2];
		statusMsg[0] = null;
		List<User> userList = userService.getUserByEmail(email);

		if (userList.size() > 0) {
			User user = userList.get(0);
			// for reset token
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int randomNumber = random.nextInt(10); // Generates a random number between 0 and 9
				sb.append(randomNumber);
			}

			// for expire timestamp
			long currentTimeSeconds = System.currentTimeMillis() / 1000;
			long expirationTimeSeconds = 15 * 60; // 15 minutes in seconds
			long codeExpirationTimeSeconds = currentTimeSeconds + expirationTimeSeconds;

			PasswordReset passwordReset = new PasswordReset();
			passwordReset.setResetToken(sb.toString());
			passwordReset.setUser(user);
			passwordReset.setExpireTimestamp(codeExpirationTimeSeconds);
			passwordReset.setStatus(false);
			PasswordReset storedPS = passwordResetService.storePasswordReset(passwordReset);
			if (storedPS != null) {
				statusMsg[0] = email;
				statusMsg[1] = "Email validation success! An email with reset code will be sent to you.";
			} else {
				statusMsg[1] = "Something went wrong! Please try again.";
			}

		} else {
			statusMsg[1] = "Incorrect email! Please try again.";
		}

		return ResponseEntity.ok(statusMsg);
	}

	@PostMapping("/send-reset-mail")
	public ResponseEntity<String> sendResetMail(@RequestParam("validatedEmail") String email)
			throws MessagingException {
		EmailTemplate template = loader.getEmailTemplate("2");
		String body = null;
		String statusMsg = null;
		List<User> userList = userService.getUserByEmail(email);
		User user = userList.get(0);
		List<PasswordReset> passwordReset = passwordResetService.getByUser(user);
		if (!passwordReset.isEmpty()) {
			PasswordReset lastPasswordReset = passwordReset.get(passwordReset.size() - 1);
			long currentTimeSeconds = System.currentTimeMillis() / 1000;
			long codeExpirationTimeSeconds = lastPasswordReset.getExpireTimestamp();
			if (lastPasswordReset.getResetToken() != null && lastPasswordReset.isStatus() == false
					&& (currentTimeSeconds < codeExpirationTimeSeconds)) {
				body = template.getBody();
				body = template.getBody().replace("[resetCode]", lastPasswordReset.getResetToken());
				statusMsg = emailService.sendMail(email, template.getSubject(), body);
			} else if (lastPasswordReset.isStatus() == true) {

				statusMsg = "You have already reset your password with this code.";
			} else {
				statusMsg = "Sorry! Reset code has expired.";
			}
		} else {
			statusMsg = "Something went wrong! Please try again!";
		}

		return ResponseEntity.ok(statusMsg);
	}

	@PostMapping("/resend-reset-mail")
	public ResponseEntity<String> resendResetMail(@RequestParam("validatedEmail") String email)
			throws MessagingException {
		EmailTemplate template = loader.getEmailTemplate("2");
		String body = null;
		String statusMsg = null;
		List<User> userList = userService.getUserByEmail(email);
		User user = userList.get(0);
		List<PasswordReset> passwordReset = passwordResetService.getByUser(user);
		if (!passwordReset.isEmpty()) {
			PasswordReset lastPasswordReset = passwordReset.get(passwordReset.size() - 1);
			// for reset token
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int randomNumber = random.nextInt(10); // Generates a random number between 0 and 9
				sb.append(randomNumber);
			}
			lastPasswordReset.setResetToken(sb.toString());

			// for expire timestamp
			long currentTimeSeconds = System.currentTimeMillis() / 1000;
			long expirationTimeSeconds = 15 * 60; // 15 minutes in seconds
			long codeExpirationTimeSeconds = currentTimeSeconds + expirationTimeSeconds;
			lastPasswordReset.setExpireTimestamp(codeExpirationTimeSeconds);

			// update password reset with new token and expire time
			PasswordReset updatedPasswordReset = passwordResetService.updatePasswordReset(lastPasswordReset);
			if (updatedPasswordReset != null) {
				body = template.getBody();
				body = template.getBody().replace("[resetCode]", lastPasswordReset.getResetToken());
				statusMsg = emailService.sendMail(email, template.getSubject(), body);
			} else {
				statusMsg = "Something went wrong! Please try again.";
			}

		}

		else {
			statusMsg = "Something went wrong! Please try again!";
		}

		return ResponseEntity.ok(statusMsg);

	}

	@PostMapping("/validate-reset-token")
	public ResponseEntity<Long> validateResetToken(@RequestParam("resetToken") String resetToken) {
		long passwordResetId = 0;
		PasswordReset passwordReset = new PasswordReset();
		if (resetToken != null && !resetToken.equals("")) {
			passwordReset = passwordResetService.getByResetToken(resetToken);
			if (passwordReset != null) {
				long currentTimeSeconds = System.currentTimeMillis() / 1000;
				long codeExpirationTimeSeconds = passwordReset.getExpireTimestamp();

				if (currentTimeSeconds <= codeExpirationTimeSeconds && passwordReset.isStatus() == false) {
					passwordResetId = passwordReset.getId();
				}
			}
		}
		return ResponseEntity.ok(passwordResetId);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Boolean> resetPassword(@RequestParam("reset-id") long resetId,
			@RequestParam("password") String password) {
		boolean status = false;
		PasswordReset passwordReset = new PasswordReset();
		passwordReset = passwordResetService.getById(resetId);
		User user = passwordReset.getUser();
		user.setPassword(passwordEncoder.encode(password));
		User updatedUser = userService.updateUser(user);
		if (updatedUser != null) {
			passwordReset.setStatus(true);
			PasswordReset updatedPasswordReset = passwordResetService.updatePasswordReset(passwordReset);
			if (updatedPasswordReset != null) {
				status = true;
			}
		}
		return ResponseEntity.ok(status);
	}

}