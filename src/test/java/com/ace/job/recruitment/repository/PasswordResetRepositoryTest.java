package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;

@SpringBootTest
public class PasswordResetRepositoryTest {
	@Mock
	private PasswordResetRepository passwordResetRepository;

	@Test
	public void findByUserTest() {
		User user = new User();
		List<PasswordReset> expectedResult = new ArrayList<PasswordReset>();
		expectedResult.add(new PasswordReset(1, "234908", 2397423947L, false, user));
		when(passwordResetRepository.findByUser(user)).thenReturn(expectedResult);

		List<PasswordReset> actualResult = passwordResetRepository.findByUser(user);
		verify(passwordResetRepository).findByUser(user);
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void findByResetTokenTest() {
		User user = new User();
		String token = "245678";
		PasswordReset expectedResult = new PasswordReset(1, "234908", 2397423947L, false, user);
		when(passwordResetRepository.findByResetToken(token)).thenReturn(expectedResult);

		PasswordReset actualResult = passwordResetRepository.findByResetToken(token);
		verify(passwordResetRepository).findByResetToken(token);
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void findNotNullTokensTest() {
		User user = new User();
		List<PasswordReset> expectedResult = new ArrayList<PasswordReset>();
		expectedResult.add(new PasswordReset(1, "234908", 2397423947L, false, user));
		when(passwordResetRepository.findNotNullTokens()).thenReturn(expectedResult);

		List<PasswordReset> actualResult = passwordResetRepository.findNotNullTokens();
		verify(passwordResetRepository).findNotNullTokens();
		assertEquals(expectedResult, actualResult);
	}

}
