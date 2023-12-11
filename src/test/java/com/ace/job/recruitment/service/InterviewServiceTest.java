package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.repository.InterviewRepository;
import com.ace.job.recruitment.service.impl.InterviewServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

	@Mock
	private InterviewRepository interviewRepository;

	@InjectMocks
	private InterviewServiceImpl interviewServiceImpl;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getCurrentUserIdTest() {
		Authentication authentication = mock(Authentication.class);
		AppUserDetails appUserDetails = mock(AppUserDetails.class);

		int expectedUserId = 1;
		when(appUserDetails.getId()).thenReturn(expectedUserId);
		when(authentication.getPrincipal()).thenReturn(appUserDetails);

		int result = interviewServiceImpl.getCurrentUserId(authentication);
		verify(authentication, times(1)).getPrincipal();
		verify(appUserDetails, times(1)).getId();
		assertEquals(expectedUserId, result);
	}

	@Test
	public void addInterviewTest() {
		Interview interview = new Interview();
		interview.setId(1L);

		when(interviewRepository.save(any())).thenReturn(interview);

		Interview savedInterview = interviewServiceImpl.addInterview(interview);

		verify(interviewRepository, times(1)).save(interview);

		assertEquals(interview, savedInterview);
	}

	@Test
	public void updateInterviewTest() {
		Interview interview = new Interview();
		interview.setId(1L);

		when(interviewRepository.save(any())).thenReturn(interview);

		Interview savedInterview = interviewServiceImpl.updateInterview(interview);

		verify(interviewRepository, times(1)).save(interview);

		assertEquals(interview, savedInterview);
	}

	@Test
	public void getAllInterviewsTest() {
		Interview interview1 = new Interview();
		interview1.setId(1);
		Interview interview2 = new Interview();
		interview2.setId(2);

		List<Interview> expectedResult = new ArrayList<Interview>();
		expectedResult.add(interview1);
		expectedResult.add(interview2);
		when(interviewRepository.findAll()).thenReturn(expectedResult);

		List<Interview> actualResult = interviewServiceImpl.getAllInterviews();
		verify(interviewRepository).findAll();
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void getInterviewByIdTest() {
		Interview interview = new Interview();
		interview.setId(1);

		when(interviewRepository.findById(1L)).thenReturn(Optional.of(interview));

		Interview actualResult = interviewServiceImpl.getInterviewById(1L);
		verify(interviewRepository).findById(1L);
		assertEquals(interview, actualResult);

	}

	@Test
	public void getInterviewByTypeStageVacancyIdStatusTest() {
		Vacancy vacancy = new Vacancy();
		vacancy.setId(1);
		Interview interview = new Interview();
		interview.setId(1);
		interview.setType("Online");
		interview.setStage(1);
		interview.setVacancy(vacancy);
		interview.setStatus(true);

		List<Interview> expectedResult = new ArrayList<Interview>();
		when(interviewRepository.findAllByTypeAndStageAndVacancyIdAndStatus("Online", 1, 1, true))
				.thenReturn(expectedResult);

		List<Interview> actualResult = interviewServiceImpl.getInterviewByTypeStageVacancyIdStatus("Online", 1, 1,
				true);
		verify(interviewRepository).findAllByTypeAndStageAndVacancyIdAndStatus("Online", 1, 1, true);
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void getInterviewByTypeAndStageAndStatusTest() {
		Interview interview = new Interview();
		interview.setId(1);
		interview.setType("Online");
		interview.setStage(1);
		interview.setStatus(true);

		List<Interview> expectedResult = new ArrayList<Interview>();
		when(interviewRepository.findByTypeAndStageAndStatus("Online", 1, false)).thenReturn(expectedResult);

		List<Interview> actualResult = interviewServiceImpl.getInterviewByTypeAndStageAndStatus("Online", 1, false);
		verify(interviewRepository).findByTypeAndStageAndStatus("Online", 1, false);
		assertEquals(expectedResult, actualResult);

	}

}
