package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.ace.job.recruitment.dto.ReviewViewerDTO;
import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.Reviews;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.service.CandidateInterviewService;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.UserService;

class ReviewViewerControllerTest {

	@Mock
	private CandidateInterviewService candidateInterviewService;

	@Mock
	private UserService userService;

	@Mock
	private CandidateService candidateService;

	@InjectMocks
	private ReviewViewerController reviewViewerController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testShowReviews() {
		long candidateId = 1L;

		CandidateInterview interview1 = new CandidateInterview();
		Reviews review1 = new Reviews();
		review1.setReview("Good candidate");
		review1.setReviewBy(2);
		review1.setReviewedDateTime((LocalDateTime.now()).toString());
		interview1.getReviews().add(review1);

		CandidateInterview interview2 = new CandidateInterview();
		Reviews review2 = new Reviews();
		review2.setReview("Great potential");
		review2.setReviewBy(3);
		review1.setReviewedDateTime((LocalDateTime.now()).toString());
		interview2.getReviews().add(review2);

		List<CandidateInterview> interviews = new ArrayList<>();
		interviews.add(interview1);
		interviews.add(interview2);

		when(candidateInterviewService.getAllReviewsByCandidateId(candidateId)).thenReturn(interviews);

		User reviewer1 = new User();
		reviewer1.setId(2);
		reviewer1.setName("Reviewer 1");
		when(userService.getUserById(review1.getReviewBy())).thenReturn(reviewer1);

		User reviewer2 = new User();
		reviewer2.setId(3);
		reviewer2.setName("Reviewer 2");
		when(userService.getUserById(review2.getReviewBy())).thenReturn(reviewer2);

		Candidate candidate = new Candidate();
		candidate.setId(candidateId);
		candidate.setName("John Doe");
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);

		Model model = mock(Model.class);
		String result = reviewViewerController.showReviews(candidateId, model);

		List<ReviewViewerDTO> expectedReviews = new ArrayList<>();
		ReviewViewerDTO reviewViewerDTO1 = new ReviewViewerDTO();
		reviewViewerDTO1.setReview(review1.getReview());
		reviewViewerDTO1.setReviewerName("Reviewer 1");
		reviewViewerDTO1.setReviewedDateTime(review1.getReviewedDateTime());
		expectedReviews.add(reviewViewerDTO1);

		ReviewViewerDTO reviewViewerDTO2 = new ReviewViewerDTO();
		reviewViewerDTO2.setReview(review2.getReview());
		reviewViewerDTO2.setReviewerName("Reviewer 2");
		reviewViewerDTO2.setReviewedDateTime(review2.getReviewedDateTime());
		expectedReviews.add(reviewViewerDTO2);

		assertEquals("interviewer/review", result);
	}
}
