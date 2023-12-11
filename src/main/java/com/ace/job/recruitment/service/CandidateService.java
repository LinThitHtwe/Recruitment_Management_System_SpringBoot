package com.ace.job.recruitment.service;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace.job.recruitment.entity.Candidate;

public interface CandidateService {
	Candidate addCandidate(Candidate candidate);

	List<Candidate> getAllCandidate();

	Candidate getCandidateById(long id);

	List<Candidate> getCandidates(List<Long> id);

	DataTablesOutput<Candidate> getDataTableData(@Valid DataTablesInput input);

	DataTablesOutput<Candidate> getCancelCandidates(@Valid DataTablesInput input);

	DataTablesOutput<Candidate> getCandidatesByVacancyId(@Valid DataTablesInput input, long vacancyId);

	DataTablesOutput<Candidate> getDataTableDataByDepartment(@Valid DataTablesInput input, int departmentId,
			List<Long> candidateIds);

	DataTablesOutput<Candidate> getFilteredCandidateByDepartment(@Valid DataTablesInput input, int departmentId,
			List<Long> candidateIds, String interviewStatus, LocalDate startDate, LocalDate endDate,
			Integer interviewStage);

	DataTablesOutput<Candidate> getFilteredCandidateForAdminAtInterview(@Valid DataTablesInput input,
			List<Long> candidateIds, String interviewStatus, LocalDate startDate, LocalDate endDate,
			Integer interviewStage);

	DataTablesOutput<Candidate> getDataTableDataForAdmin(@Valid DataTablesInput input, List<Long> candidateIds);

	DataTablesOutput<Candidate> getAllCandidatesWithFilters(DataTablesInput input, String candidateStatus,
			String interviewStatus, LocalDate startDate, LocalDate endDate, Integer interviewStage);

	DataTablesOutput<Candidate> getAllCandidatesInVacancyWithFilters(DataTablesInput input, String candidateStatus,
			String interviewStatus, Integer interviewStage, long vacancyId);

	DataTablesOutput<Candidate> getAllCancelCandidatesWithFilters(DataTablesInput input, LocalDate startDate,
			LocalDate endDate, Integer interviewStage);

	List<Candidate> checkCandidateDuplication(String email, long vacancyId);

}