package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.Vacancy;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class InterviewRepositoryTest {

	@Mock
	private InterviewRepository interviewRepository;

	@Test
	public void findAllByTypeAndStageAndVacancyIdAndStatusTest() {
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

		List<Interview> actualResult = interviewRepository.findAllByTypeAndStageAndVacancyIdAndStatus("Online", 1, 1,
				true);
		verify(interviewRepository).findAllByTypeAndStageAndVacancyIdAndStatus("Online", 1, 1, true);
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void findByTypeAndStageAndStatusTest() {
		Interview interview = new Interview();
		interview.setId(1);
		interview.setType("Online");
		interview.setStage(1);
		interview.setStatus(true);
		List<Interview> expectedResult = new ArrayList<Interview>();
		when(interviewRepository.findByTypeAndStageAndStatus("Online", 1, true)).thenReturn(expectedResult);

		List<Interview> actualResult = interviewRepository.findByTypeAndStageAndStatus("Online", 1, true);
		verify(interviewRepository).findByTypeAndStageAndStatus("Online", 1, true);
		assertEquals(expectedResult, actualResult);

	}

}
