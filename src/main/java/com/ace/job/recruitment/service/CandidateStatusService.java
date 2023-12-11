package com.ace.job.recruitment.service;

import java.util.List;

import com.ace.job.recruitment.entity.CandidateStatus;

public interface CandidateStatusService {
	CandidateStatus saveCandidateStatus(CandidateStatus candidateStatus);

	List<CandidateStatus> getAllByCandidateId(long id);

}
