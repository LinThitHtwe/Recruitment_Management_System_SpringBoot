package com.ace.job.recruitment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.repository.CandidateInterviewRepository;
import com.ace.job.recruitment.service.CandidateInterviewService;

@Service
public class CandidateInterviewServiceImpl implements CandidateInterviewService {

	@Autowired
	CandidateInterviewRepository candidateInterviewRepository;

	@Override
	public CandidateInterview saveCandidateInterview(CandidateInterview candidateInterview) {
		return candidateInterviewRepository.save(candidateInterview);
	}

	public List<CandidateInterview> getCandidateInterviewById(long candidateId) {
		return candidateInterviewRepository.findByCandidateId(candidateId);
	}

	@Override
	public List<CandidateInterview> getAll() {
		return candidateInterviewRepository.findAll();
	}

	@Override
	public List<CandidateInterview> getCandidateInterviewByIdAndInterviewStatus(long candidateId, boolean status) {
		return candidateInterviewRepository.findByCandidateIdAndInterviewStatus(candidateId, status);
	}

	@Override
	public List<CandidateInterview> getAllReviewsByCandidateId(long candidateId) {
		return candidateInterviewRepository.findReviewsByCandidateId(candidateId);
	}

}
