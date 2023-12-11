package com.ace.job.recruitment.controller;

import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.model.EmailTemplate;
import com.ace.job.recruitment.other.EmailTemplateLoader;
import com.ace.job.recruitment.service.AppUserDetailsService;
import com.ace.job.recruitment.service.EmailService;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/admin")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	AppUserDetailsService appUserDetailsService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailTemplateLoader loader;

	@Autowired
	private SessionRegistry userSessionRegistry;

	// Get Current User Role
	public String getCurrentUserRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				String role = userDetails.getRole();
				return role;
			}
		}

		return "";
	}

	@PostMapping("/add-user")
	public ResponseEntity<String> addUser(@ModelAttribute("user") User user) throws MessagingException {
		String randomPassword = generateRandomPassword();
		String statusMsg = null;
		user.setPassword(passwordEncoder.encode(randomPassword));

		EmailTemplate template = loader.getEmailTemplate("1");
		String emailBody = template.getBody().replace("[role]", user.getRole()).replace("[password]", randomPassword);
		statusMsg = emailService.sendMail(user.getEmail(), "Welcome to our platform!", emailBody);

		if (statusMsg.equals("success")) {
			userService.addUser(user);
		}

		return ResponseEntity.ok(statusMsg);
	}

	@PostMapping("/resend-password-mail")
	public ResponseEntity<String> resendPasswordMail(@ModelAttribute("user") User user) throws MessagingException {
		String statusMsg = null;
		String randomPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(randomPassword));

		// resend the email with the generated password
		EmailTemplate template = loader.getEmailTemplate("1");
		String emailBody = template.getBody().replace("[password]", randomPassword);
		statusMsg = emailService.sendMail(user.getEmail(), "Welcome to our platform!", emailBody);

		if (statusMsg.equals("success")) {
			userService.addUser(user);
		}

		return ResponseEntity.ok(statusMsg);
	}

	// Method to randomize password for email sent and new user
	private String generateRandomPassword() {
		String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String allChars = upperCaseLetters + lowerCaseLetters + numbers;

		int passwordLength = 6;
		Random random = new Random();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < passwordLength; i++) {
			int randomIndex = random.nextInt(allChars.length());
			password.append(allChars.charAt(randomIndex));
		}

		return password.toString();
	}

	// Method to check duplicate email
	@PostMapping("/duplicate-email")
	@ResponseBody
	public String duplicateEmail(@RequestParam("email") String email) {
		List<User> usersWithSameEmail = userService.getUserByEmail(email);

		if (!usersWithSameEmail.isEmpty()) {
			return "Email already exists";
		} else {
			try {
				InternetAddress emailAddress = new InternetAddress(email);
				emailAddress.validate();
				return "Valid email";
			} catch (AddressException e) {
				return "Invalid email format";
			}
		}
	}

	// Method to return front end page
	@GetMapping("users")
	public String showUserTable(Model model) {
		String currentUserRole = getCurrentUserRole();
		model.addAttribute("currentRole", currentUserRole);
		model.addAttribute("showStatusButton", currentUserRole.equals("Admin"));
		return "admin/user_table";
	}

	// Method to populate front end page with datas
	@GetMapping("/users-data")
	@ResponseBody
	public DataTablesOutput<User> getDataTableData(@Valid DataTablesInput input) {

		return userService.getDataTableData(input);
	}

	@GetMapping("/change-user-status")
	@ResponseBody
	public ResponseEntity<String> changeUserStatus(@RequestParam("status") boolean status,
			@RequestParam("userId") int id, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (status) {

				for (Object userDetail : userSessionRegistry.getAllPrincipals()) {
					AppUserDetails appUserDetails = (AppUserDetails) userDetail;
					if (appUserDetails.getId() == id) {

						for (SessionInformation sinfo : userSessionRegistry.getAllSessions(appUserDetails, false)) {
							sinfo.expireNow();
						}
					}

				}

			}

			userService.changeUserStatus(id, status);

			return ResponseEntity.ok("User status updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user status");
		}
	}

	@GetMapping("/get-user-by-id")
	@ResponseBody
	public User getUserForUpdate(@RequestParam("id") int id) {
		return userService.getUserById(id);
	}

	@PostMapping("/update-user")
	public ResponseEntity<String> updateUser(@ModelAttribute("user") User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.addUser(user);
		return ResponseEntity.ok("User added successfully");
	}

	@GetMapping("/permit-user")
	@ResponseBody
	public void permitUser(@RequestParam("userId") int userId, @RequestParam("permitTo") String permitTo) {
		User user = userService.getUserById(userId);
		user.setRole(permitTo);
		userService.addUser(user);
	}

}
