package com.ace.job.recruitment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.Reviews;
import com.ace.job.recruitment.repository.ReviewRepository;
import com.ace.job.recruitment.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	ReviewRepository reviewRepository;

	@Override
	public Reviews saveReview(Reviews reviews) {
		return reviewRepository.save(reviews);
	}

}
