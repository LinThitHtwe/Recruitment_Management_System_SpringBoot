package com.ace.job.recruitment.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/profile")
public class UserUpdateController {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public int getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			// Get the principal (user) object from the authentication
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				// Cast the principal to your AppUserDetails UserDetails class
				AppUserDetails userDetails = (AppUserDetails) principal;

				// Access the user's ID from the UserDetails object
				int userId = userDetails.getId();
				return userId;
			}
		}

		return -1;
	}

	@GetMapping("/userDetail")
	public ResponseEntity<User> showUserDetail() {
		User user = userService.getUserById(getCurrentUserId());
		return ResponseEntity.ok(user);
	}

	@GetMapping
	public String showView() {
		return "admin/user-update";
	}

	@PostMapping("/update")
	@ResponseBody
	public String updateProfile(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("password") String password, HttpServletResponse response) {
		User existingUser = userService.getUserById(getCurrentUserId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(oldPassword, existingUser.getPassword())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "Old password does not match.";
		} else {
			existingUser.setPassword(passwordEncoder.encode(password));
			// Save the updated user with the new password
			userService.updateUser(existingUser);
			return "Password updated successfully.";
		}
	}
}
