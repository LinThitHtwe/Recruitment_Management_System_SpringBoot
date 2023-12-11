package com.ace.job.recruitment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.repository.DepartmentRepository;
import com.ace.job.recruitment.repository.PositionRepository;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.ChartService;

@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	PositionRepository positionRepository;
	@Autowired
	VacancyRepository vacancyRepository;
	@Autowired
	CandidateRepository candidateRepository;

	// dashboard hr chart start

	@Override
	public List<ChartDTO> filterVacancyByDepartmentAndTime(int department_id, int year, int month) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (department_id == 0 && year == 0 && month == 0) {
			chartList = positionRepository.getPositionWithVacancyCount();
		} else if (department_id > 0 && year == 0 && month == 0) {
			chartList = positionRepository.getPositionVacancyCountsByDepartment(department_id);
		} else if (department_id == 0 && year > 0 && month == 0) {
			chartList = positionRepository.getPositionVacancyCountsByYear(year);
		} else if (department_id == 0 && year > 0 && month > 0) {
			chartList = positionRepository.getPositionVacancyCountsByYearMonth(year, month);
		} else if (department_id > 0 && year > 0 && month == 0) {
			chartList = positionRepository.getPositionVacancyCountsByDepartmentAndYear(department_id, year);
		} else if (department_id > 0 && year > 0 && month > 0) {
			chartList = positionRepository.getPositionVacancyCountsByDepartmentYearAndMonth(department_id, year, month);
		}
		return chartList;
	}

	@Override
	public List<ChartDTO> filterVacancyByPositionAndTime(int position_id, int year, int month) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (position_id == 0 && year == 0 && month == 0) {
			chartList = departmentRepository.getDepartmentWithVacancyCount();
		} else if (position_id > 0 && year == 0 && month == 0) {
			chartList = departmentRepository.getDepartmentVacancyCountsByPosition(position_id);
		} else if (position_id == 0 && year > 0 && month == 0) {
			chartList = departmentRepository.getDepartmentVacancyCountsByYear(year);
		} else if (position_id == 0 && year > 0 && month > 0) {
			chartList = departmentRepository.getDepartmentVacancyCountsByYearMonth(year, month);
		} else if (position_id > 0 && year > 0 && month == 0) {
			chartList = departmentRepository.getDepartmentVacancyCountsByPositionAndYear(position_id, year);
		} else if (position_id > 0 && year > 0 && month > 0) {
			chartList = departmentRepository.getDepartmentVacancyCountsByPositionYearAndMonth(position_id, year, month);
		}

		return chartList;
	}

	@Override
	public List<Map<String, Object>> showPositionDemandByYears(int start_year, int end_year) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (start_year == 0 && end_year == 0) {
			mapList = positionRepository.getPositionDemand();
		} else if (start_year > 0 && end_year == 0) {
			mapList = positionRepository.getPositionDemandByStartYear(start_year);
		}

		else if (start_year > 0 && end_year > 0) {
			mapList = positionRepository.getPositionDemandByYears(start_year, end_year);
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> showCandidatePositionDemandByYears(int start_year, int end_year) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (start_year == 0 && end_year == 0) {
			mapList = positionRepository.getCandidatePositionDemand();
		} else if (start_year > 0 && end_year == 0) {
			mapList = positionRepository.getCandidatePositionDemandByStartYear(start_year);
		} else if (start_year > 0 && end_year > 0) {
			mapList = positionRepository.getCandidatePositionDemandByYears(start_year, end_year);
		}
		return mapList;
	}

	@Override
	public List<ChartDTO> getCandidateCountByPosition(int department_id, int year, int month) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (department_id == 0 && year == 0 && month == 0) {
			chartList = positionRepository.getCandidateCountByPosition();
		} else if (department_id > 0 && year == 0 && month == 0) {
			chartList = positionRepository.getCandidateCountByPositionAndDepartment(department_id);
		} else if (department_id == 0 && year > 0 && month == 0) {
			chartList = positionRepository.getCandidateCountByPositionAndYear(year);
		} else if (department_id == 0 && year > 0 && month > 0) {
			chartList = positionRepository.getCandidateCountByPositionAndTime(year, month);
		} else if (department_id > 0 && year > 0 && month == 0) {
			chartList = positionRepository.getCandidateCountByPositionAndDepartmentAndYear(department_id, year);
		} else if (department_id > 0 && year > 0 && month > 0) {
			chartList = positionRepository.getCandidateCountByPositionAndDepartmentAndTime(department_id, year, month);
		}
		return chartList;
	}

	@Override
	public List<ChartDTO> getCandidateCountByVacancyStatus(int department_id, int urgentStatus) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (department_id == 0 && urgentStatus == 0) {
			chartList = positionRepository.getCandidateCountByActiveVacancies();
		} else if (department_id == 0 && urgentStatus > 0) {
			chartList = positionRepository.getCandidateCountByUrgentVacancies();
		} else if (department_id > 0 && urgentStatus == 0) {
			chartList = positionRepository.getCandidateCountByActiveVacanciesAndDepartment(department_id);
		} else if (department_id > 0 && urgentStatus > 0) {
			chartList = positionRepository.getCandidateCountByUrgentVacanciesAndDepartment(department_id);
		}
		return chartList;
	}

	@Override
	public List<ChartDTO> getCandidateCountBySelectionStatus(Long vacancy_id) {
		return vacancyRepository.getCandidateCountsByVacancyIdForSelection(vacancy_id);
	}

	@Override
	public List<ChartDTO> getCandidateCountByInterviewStatus(Long vacancy_id) {
		return vacancyRepository.getCandidateCountsByVacancyIdForInterviews(vacancy_id);
	}

	@Override
	public List<ChartDTO> getTotalCandidateCountBySelectionStatus() {
		return vacancyRepository.getCandidateCountsForSelection();
	}

	@Override
	public List<ChartDTO> getTotalCandidateCountByInterviewStatus() {
		return vacancyRepository.getCandidateCountsForInterviews();
	}

	// dashboard hr chart end

	// dashboard interviewer chart start

	@Override
	public List<ChartDTO> filterVacancyByTimeForInterviewer(int department_id, int year, int month) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (year == 0 && month == 0) {
			chartList = positionRepository.getPositionVacancyCountsByDepartment(department_id);
		} else if (year > 0 && month == 0) {
			chartList = positionRepository.getPositionVacancyCountsByDepartmentAndYear(department_id, year);
		} else if (year > 0 && month > 0) {
			chartList = positionRepository.getPositionVacancyCountsByDepartmentYearAndMonth(department_id, year, month);
		}
		return chartList;
	}

	@Override
	public List<Map<String, Object>> showPositionDemandByYearsForInterviewer(int department_id, int start_year,
			int end_year) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (start_year == 0 && end_year == 0) {
			mapList = positionRepository.getPositionDemandForInterviewer(department_id);
		} else if (start_year > 0 && end_year == 0) {
			mapList = positionRepository.getPositionDemandByStartYearForInterviewer(department_id, start_year);
		} else if (start_year > 0 && end_year > 0) {
			mapList = positionRepository.getPositionDemandByYearsForInterviewer(department_id, start_year, end_year);
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> showCandidateTrendByYearsForInterviewer(int department_id, int start_year,
			int end_year) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (start_year == 0 && end_year == 0) {
			mapList = positionRepository.getCandidatePositionDemandForInterviewer(department_id);
		} else if (start_year > 0 && end_year == 0) {
			mapList = positionRepository.getCandidateTrendByStartYearForInterviewer(department_id, start_year);
		} else if (start_year > 0 && end_year > 0) {
			mapList = positionRepository.getCandidateTrendByYearsForInterviewer(department_id, start_year, end_year);
		}
		return mapList;
	}

	@Override
	public List<ChartDTO> getCandidateCountByTimeForInterviewer(int department_id, int year, int month) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (year == 0 && month == 0) {
			chartList = positionRepository.getCandidateCountByPositionAndDepartment(department_id);
		} else if (year > 0 && month == 0) {
			chartList = positionRepository.getCandidateCountByPositionAndDepartmentAndYear(department_id, year);
		} else if (year > 0 && month > 0) {
			chartList = positionRepository.getCandidateCountByPositionAndDepartmentAndTime(department_id, year, month);
		}
		return chartList;
	}

	@Override
	public List<ChartDTO> getCandidateCountByVacancyStatusForInterviewer(int department_id, int urgentStatus) {
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		if (urgentStatus == 0) {
			chartList = positionRepository.getCandidateCountByActiveVacanciesAndDepartment(department_id);
		} else if (urgentStatus > 0) {
			chartList = positionRepository.getCandidateCountByUrgentVacanciesAndDepartment(department_id);
		}
		return chartList;
	}

	@Override
	public List<ChartDTO> getCandidateCountBySelectionStatusForInterviewer(Long vacancy_id) {
		return vacancyRepository.getCandidateCountsByVacancyIdForSelection(vacancy_id);
	}

	@Override
	public List<ChartDTO> getCandidateCountByInterviewStatusForInterviewer(Long vacancy_id) {
		return vacancyRepository.getCandidateCountsByVacancyIdForInterviews(vacancy_id);
	}

	@Override
	public List<ChartDTO> getTotalCandidateCountBySelectionStatusForInterviewer(int department_id) {
		return vacancyRepository.getCandidateCountsForSelectionForInterviewer(department_id);
	}

	@Override
	public List<ChartDTO> getTotalCandidateCountByInterviewStatusForInterviewer(int department_id) {
		return vacancyRepository.getCandidateCountsForInterviewsForInterviewer(department_id);
	}

	@Override
	public long getEmployedCandidateCountForChart() {
		return candidateRepository.getAllEmployedCandidateCount();
	}

	@Override
	public long getEmployedCandidateCountForChartForDepartment(int department_id) {
		return candidateRepository.getAllEmployedCandidateCountForDepartment(department_id);
	}

	@Override
	public long getEmployedCandidateCountByVacancyId(long vacancy_id) {
		return candidateRepository.getEmployedCandidateCountByVacancy(vacancy_id);
	}

	@Override
	public int getCurrentDepartmentId(Authentication authentication) {
		AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
		return appUserDetails.getUser().getDepartment().getId();
	}

	// dashboard interviewer chart end

}