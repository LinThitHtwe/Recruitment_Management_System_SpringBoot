package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.service.UserService;

class UserUpdateControllerTest {

	@Mock
	private UserService userService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserUpdateController userUpdateController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testShowUserDetail() {
		User user = new User();
		when(userService.getUserById(anyInt())).thenReturn(user);

		ResponseEntity<User> responseEntity = userUpdateController.showUserDetail();

		assertEquals(user, responseEntity.getBody());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	void testShowView() {
		String result = userUpdateController.showView();

		assertEquals("admin/user-update", result);
	}

	@Test
	void testUpdateProfile_PasswordMismatch() {
		User existingUser = new User();
		existingUser.setPassword("encoded_password"); // Set the encoded password
		when(userService.getUserById(anyInt())).thenReturn(existingUser);

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

		HttpServletResponse response = mock(HttpServletResponse.class);
		String result = userUpdateController.updateProfile("wrong_old_password", "new_password", response);

		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		assertEquals("Old password does not match.", result);
	}

	@Test
	void testUpdateProfile_PasswordMatch() {
		User existingUser = new User();
		existingUser.setPassword("encoded_password"); // Set the encoded password
		when(userService.getUserById(anyInt())).thenReturn(existingUser);

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(passwordEncoder.encode(anyString())).thenReturn("new_encoded_password"); // Mock the encoded new password

		HttpServletResponse response = mock(HttpServletResponse.class);
		String result = userUpdateController.updateProfile("old_password", "new_password", response);

	}
}
