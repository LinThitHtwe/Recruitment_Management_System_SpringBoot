package com.ace.job.recruitment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ace.job.recruitment.dto.ReviewViewerDTO;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.Reviews;
import com.ace.job.recruitment.service.CandidateInterviewService;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/department-head")
public class ReviewViewerController {

	@Autowired
	CandidateInterviewService candidateInterviewService;

	@Autowired
	UserService userService;

	@Autowired
	CandidateService candidateService;

	@GetMapping("/candidate/{id}")
	public String showReviews(@PathVariable("id") long id, Model model) {
		List<ReviewViewerDTO> reviews = new ArrayList<>();
		List<CandidateInterview> candidateInterviews = candidateInterviewService.getAllReviewsByCandidateId(id);
		for (CandidateInterview candidateInterview : candidateInterviews) {
			List<Reviews> candidateReviews = candidateInterview.getReviews();
			for (int j = 0; j < candidateReviews.size(); j++) {
				ReviewViewerDTO reviewViewerDTO = new ReviewViewerDTO();
				reviewViewerDTO.setReview(candidateReviews.get(j).getReview());
				reviewViewerDTO
						.setReviewerName(userService.getUserById(candidateReviews.get(j).getReviewBy()).getName());
				reviewViewerDTO.setReviewedDateTime(candidateReviews.get(j).getReviewedDateTime());
				reviews.add(reviewViewerDTO);
			}
		}

		model.addAttribute("reviews", reviews);
		model.addAttribute("candidateName", candidateService.getCandidateById(id).getName());

		return "interviewer/review";
	}

}
