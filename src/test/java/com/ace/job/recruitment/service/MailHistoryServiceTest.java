package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.entity.MailHistory;
import com.ace.job.recruitment.repository.MailHistoryRepository;
import com.ace.job.recruitment.service.impl.MailHistoryServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MailHistoryServiceTest {

	@Mock
	MailHistoryRepository mailHistoryRepository;

	@InjectMocks
	private MailHistoryServiceImpl mailHistoryServiceImpl;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void storeMailHistoryTest() {
		MailHistory mailHistory = new MailHistory();
		mailHistory.setId(1);
		mailHistory.setMailSubject("Interview Invitation");

		when(mailHistoryRepository.save(any(MailHistory.class))).thenReturn(mailHistory);

		MailHistory result = mailHistoryServiceImpl.storeMailHistory(mailHistory);

		assertEquals(mailHistory, result);
		verify(mailHistoryRepository, times(1)).save(mailHistory);
	}

	@Test
	public void getAllMailHistoryTest() {
		MailHistory mailHistory = new MailHistory();
		mailHistory.setId(1);
		mailHistory.setMailSubject("Interview Invitation");

		List<MailHistory> expectedResult = new ArrayList<MailHistory>();
		expectedResult.add(mailHistory);
		expectedResult.add(mailHistory);
		when(mailHistoryRepository.findAll()).thenReturn(expectedResult);

		List<MailHistory> actualResult = mailHistoryServiceImpl.getAllMailHistory();
		verify(mailHistoryRepository).findAll();
		assertEquals(expectedResult, actualResult);

	}
}
