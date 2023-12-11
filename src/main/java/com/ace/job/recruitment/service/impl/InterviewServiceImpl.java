package com.ace.job.recruitment.service.impl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.repository.InterviewRepository;
import com.ace.job.recruitment.service.InterviewService;

@Service
public class InterviewServiceImpl implements InterviewService {
	@Autowired
	InterviewRepository interviewRepository;

	@Override
	public int getCurrentUserId(Authentication authentication) {
		AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
		return appUserDetails.getId();
	}

	@Override
	public Interview addInterview(Interview interview) {
		return interviewRepository.save(interview);
	}

	@Override
	public Interview updateInterview(Interview interview) {
		return interviewRepository.save(interview);
	}

	@Override
	public List<Interview> getAllInterviews() {
		return interviewRepository.findAll();
	}

	@Override
	public Interview getInterviewById(Long id) {
		return interviewRepository.findById(id).get();
	}

	@Override
	public DataTablesOutput<Interview> getDataTableData(@Valid DataTablesInput input) {
		return interviewRepository.findAll(input);
	}

	@Override
	public List<Interview> getInterviewByTypeStageVacancyIdStatus(String type, int stage, long vacancyId,
			boolean status) {
		return interviewRepository.findAllByTypeAndStageAndVacancyIdAndStatus(type, stage, vacancyId, status);
	}

	@Override
	public List<Interview> getInterviewByTypeAndStageAndStatus(String type, int stage, boolean status) {
		return interviewRepository.findByTypeAndStageAndStatus(type, stage, status);
	}

	@Override
	public DataTablesOutput<Interview> getDataTableDataDate(DataTablesInput input, LocalDate startDate,
			LocalDate endDate) {
		Specification<Interview> dateRangeSpecification = (root, query, criteriaBuilder) -> {
			Predicate startDatePredicate = criteriaBuilder.between(root.get("startDate"), startDate, endDate);

			Predicate endDatePredicate = criteriaBuilder.or(criteriaBuilder.isNull(root.get("endDate")),
					criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));

			return criteriaBuilder.and(startDatePredicate, endDatePredicate);
		};

		return interviewRepository.findAll(input, dateRangeSpecification);
	}

	@Override
	public List<Interview> checkInterviewForExpired() {
		return interviewRepository.getInterviewForExpired();
	}

	@Override
	public List<Interview> getInterviewForDuplication(String type, int stage, long vacancyId) {
		return interviewRepository.getInterviewForDuplication(type, stage, vacancyId);
	}

	@Override
	public List<Interview> getInterviewForDuplicationUpdate(String type, int stage, long vacancyId, long interviewId) {
		return interviewRepository.getInterviewForDuplicationUpdate(type, stage, vacancyId, interviewId);
	}

	@Override
	public List<Interview> getInterviewByTypeAndStageAndStatusAndVacancyId(String type, int stage, boolean status,
			long vacancyId) {

		return interviewRepository.findAllByTypeAndStageAndVacancyIdAndStatus(type, stage, vacancyId, status);
	}

}