package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.repository.PasswordResetRepository;
import com.ace.job.recruitment.service.impl.PasswordResetServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

	@Mock
	private PasswordResetRepository passwordResetRepository;

	@InjectMocks
	private PasswordResetServiceImpl passwordResetServiceImpl;

	@Test
	public void getByIdTest() {
		User user = new User();
		PasswordReset passwordReset = new PasswordReset(1, "234908", 2397423947L, false, user);

		when(passwordResetRepository.findById(1L)).thenReturn(Optional.of(passwordReset));

		PasswordReset actualResult = passwordResetServiceImpl.getById(1L);
		verify(passwordResetRepository).findById(1L);
		assertEquals(passwordReset, actualResult);

	}

	@Test
	public void getByUserTest() {
		User user = new User();
		List<PasswordReset> expectedResult = new ArrayList<PasswordReset>();
		PasswordReset passwordReset = new PasswordReset(1, "234908", 2397423947L, false, user);
		expectedResult.add(passwordReset);

		when(passwordResetRepository.findByUser(user)).thenReturn(expectedResult);

		List<PasswordReset> actualResult = passwordResetServiceImpl.getByUser(user);
		verify(passwordResetRepository).findByUser(user);
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void getByResetTokenTest() {
		User user = new User();
		PasswordReset passwordReset = new PasswordReset(1, "234908", 2397423947L, false, user);

		when(passwordResetRepository.findByResetToken("234908")).thenReturn(passwordReset);

		PasswordReset actualResult = passwordResetServiceImpl.getByResetToken("234908");
		verify(passwordResetRepository).findByResetToken("234908");
		assertEquals(passwordReset, actualResult);
	}

	@Test
	public void getPasswordResetByNotNullTokensTest() {
		List<PasswordReset> expectedResult = new ArrayList<PasswordReset>();

		when(passwordResetRepository.findNotNullTokens()).thenReturn(expectedResult);

		List<PasswordReset> actualResult = passwordResetServiceImpl.getPasswordResetByNotNullTokens();
		verify(passwordResetRepository).findNotNullTokens();
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void storePasswordResetTest() {
		User user = new User();
		PasswordReset passwordReset = new PasswordReset(1, "234908", 2397423947L, false, user);

		when(passwordResetRepository.save(any())).thenReturn(passwordReset);

		PasswordReset savedPasswordReset = passwordResetServiceImpl.storePasswordReset(passwordReset);

		verify(passwordResetRepository, times(1)).save(passwordReset);

		assertEquals(passwordReset, savedPasswordReset);
	}

	@Test
	public void updatePasswordResetTest() {
		User user = new User();
		PasswordReset passwordReset = new PasswordReset(1, "234908", 2397423947L, false, user);

		when(passwordResetRepository.save(any())).thenReturn(passwordReset);

		PasswordReset updatedPasswordReset = passwordResetServiceImpl.updatePasswordReset(passwordReset);

		verify(passwordResetRepository, times(1)).save(passwordReset);

		assertEquals(passwordReset, updatedPasswordReset);
	}

}
