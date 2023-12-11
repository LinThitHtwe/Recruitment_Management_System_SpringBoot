package com.ace.job.recruitment.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.ace.job.recruitment.dto.ChartDTO;

public interface ChartService {

	// dashboard hr chart start
	public List<ChartDTO> filterVacancyByDepartmentAndTime(int department_id, int year, int month);

	public List<ChartDTO> filterVacancyByPositionAndTime(int position_id, int year, int month);

	public List<Map<String, Object>> showPositionDemandByYears(int start_year, int end_year);

	public List<Map<String, Object>> showCandidatePositionDemandByYears(int start_year, int end_year);

	public List<ChartDTO> getCandidateCountByPosition(int department_id, int year, int month);

	public List<ChartDTO> getCandidateCountByVacancyStatus(int department_id, int urgentStatus);

	public List<ChartDTO> getCandidateCountBySelectionStatus(Long vacancy_id);

	public List<ChartDTO> getCandidateCountByInterviewStatus(Long vacancy_id);

	public List<ChartDTO> getTotalCandidateCountBySelectionStatus();

	public List<ChartDTO> getTotalCandidateCountByInterviewStatus();
	// dashboard hr chart end

	// dashboard interviewer chart start
	public List<ChartDTO> filterVacancyByTimeForInterviewer(int department_id, int year, int month);

	public List<Map<String, Object>> showPositionDemandByYearsForInterviewer(int department_id, int start_year,
			int end_year);

	public List<Map<String, Object>> showCandidateTrendByYearsForInterviewer(int department_id, int start_year,
			int end_year);

	public List<ChartDTO> getCandidateCountByTimeForInterviewer(int department_id, int year, int month);

	public List<ChartDTO> getCandidateCountByVacancyStatusForInterviewer(int department_id, int urgentStatus);

	public List<ChartDTO> getCandidateCountBySelectionStatusForInterviewer(Long vacancy_id);

	public List<ChartDTO> getCandidateCountByInterviewStatusForInterviewer(Long vacancy_id);

	public List<ChartDTO> getTotalCandidateCountBySelectionStatusForInterviewer(int department_id);

	public List<ChartDTO> getTotalCandidateCountByInterviewStatusForInterviewer(int department_id);

	public long getEmployedCandidateCountForChart();

	public long getEmployedCandidateCountForChartForDepartment(int department_id);

	public long getEmployedCandidateCountByVacancyId(long vacancy_id);

	public int getCurrentDepartmentId(Authentication authentication);
	// dashboard interviewer chart end
}