package com.ace.job.recruitment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.CandidateStatus;
import com.ace.job.recruitment.repository.CandidateStatusRepository;
import com.ace.job.recruitment.service.CandidateStatusService;

@Service
public class CandidateStatusServiceImpl implements CandidateStatusService {

	@Autowired
	CandidateStatusRepository candidateStatusRepository;

	@Override
	public CandidateStatus saveCandidateStatus(CandidateStatus candidateStatus) {
		return candidateStatusRepository.save(candidateStatus);
	}

	@Override
	public List<CandidateStatus> getAllByCandidateId(long id) {
		return candidateStatusRepository.findByCandidateId(id);
	}

}
