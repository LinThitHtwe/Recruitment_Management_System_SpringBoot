package com.ace.job.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.entity.CandidateInterview;

@Repository
public interface CandidateInterviewRepository extends JpaRepository<CandidateInterview, Long> {
	List<CandidateInterview> findByCandidateId(long candidateId);

	List<CandidateInterview> findByCandidateIdAndInterviewStatus(Long candidateId, boolean interviewStatus);

	List<CandidateInterview> findReviewsByCandidateId(Long candidateId);
}
