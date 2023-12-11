package com.ace.job.recruitment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.service.ChartService;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.VacancyService;

@RestController
@RequestMapping("/department")
public class ChartControllerForInterviewer {
	@Autowired
	PositionService positionService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	VacancyService vacancyService;
	@Autowired
	ChartService chartService;

	@GetMapping("/vacancies-by-time/{selectedYear}/{selectedMonth}")
	public Map<String, Long> getVacanciesFilteredByDepartmentAndTime(@PathVariable("selectedYear") int selectedYear,
			@PathVariable("selectedMonth") int selectedMonth, Authentication authentication) {
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		Map<String, Long> vacancyByTime = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.filterVacancyByTimeForInterviewer(departmentId, selectedYear, selectedMonth);
		for (ChartDTO chart : chartList) {
			vacancyByTime.put(chart.getName(), chart.getCount());
		}
		return vacancyByTime;
	}

	@GetMapping("/position-demand/{selectedStartYear}/{selectedEndYear}")
	public List<Map<String, Object>> showPositionDemandChartByYears(
			@PathVariable("selectedStartYear") int selectedStartYear,
			@PathVariable("selectedEndYear") int selectedEndYear, Authentication authentication) {
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		List<Map<String, Object>> chartMapList = new ArrayList<Map<String, Object>>();
		chartMapList = chartService.showPositionDemandByYearsForInterviewer(departmentId, selectedStartYear,
				selectedEndYear);
		return chartMapList;
	}

	@GetMapping("/candidate-position-trend/{selectedStartYear}/{selectedEndYear}")
	public List<Map<String, Object>> showCandidatePositionDemandByYears(
			@PathVariable("selectedStartYear") int selectedStartYear,
			@PathVariable("selectedEndYear") int selectedEndYear, Authentication authentication) {
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		List<Map<String, Object>> chartMapList = new ArrayList<Map<String, Object>>();
		chartMapList = chartService.showCandidateTrendByYearsForInterviewer(departmentId, selectedStartYear,
				selectedEndYear);
		return chartMapList;
	}

	@GetMapping("/candidates-by-time/{selectedYear}/{selectedMonth}")
	public Map<String, Long> showCandidateCountByPosition(@PathVariable("selectedYear") int selectedYear,
			@PathVariable("selectedMonth") int selectedMonth, Authentication authentication) {
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		Map<String, Long> chartMap = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.getCandidateCountByTimeForInterviewer(departmentId, selectedYear, selectedMonth);
		for (ChartDTO chart : chartList) {
			chartMap.put(chart.getName(), chart.getCount());
		}
		return chartMap;

	}

	@GetMapping("/candidates-by-vacancy-status/{urgentStatus}")
	public Map<String, Long> showCandidateCountByVacancyStatus(@PathVariable("urgentStatus") int urgentStatus,
			Authentication authentication) {
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		Map<String, Long> chartMap = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.getCandidateCountByVacancyStatusForInterviewer(departmentId, urgentStatus);
		for (ChartDTO chart : chartList) {
			chartMap.put(chart.getName(), chart.getCount());
		}
		return chartMap;
	}

	@GetMapping("/vacancies-by-position/{positionId}")
	public List<Map<Long, String>> showVacancyOptionsByPosition(@PathVariable("positionId") int positionId,
			Authentication authentication) {
		List<Map<Long, String>> chartMapList = new ArrayList<Map<Long, String>>();
		List<Vacancy> vacancyList = new ArrayList<Vacancy>();
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		Department department = departmentService.getDepartmentById(departmentId);
		Position position = positionService.getPositionById(positionId);
		vacancyList = vacancyService.getVacancyByDepartmentAndPosition(department, position);
		for (Vacancy vacancy : vacancyList) {
			Map<Long, String> chartMap = new HashMap<>();
			chartMap.put(vacancy.getId(), position.getName() + " " + vacancy.getCreatedDate());
			chartMapList.add(chartMap);
		}

		return chartMapList;
	}

	@GetMapping("/candidates-by-status/{vacancyId}")
	public Map<String, Long> showCandidateCountByVacancy(@PathVariable("vacancyId") Long vacancyId,
			Authentication authentication) {
		int departmentId = chartService.getCurrentDepartmentId(authentication);
		Map<String, Long> chartMap = new HashMap<>();
		if (vacancyId == 0) {
			List<ChartDTO> chartList1 = chartService
					.getTotalCandidateCountBySelectionStatusForInterviewer(departmentId);
			if (!chartList1.isEmpty()) {
				for (ChartDTO chart1 : chartList1) {
					chartMap.put(chart1.getName(), chart1.getCount());
				}
			}

			List<ChartDTO> chartList2 = chartService
					.getTotalCandidateCountByInterviewStatusForInterviewer(departmentId);
			if (!chartList2.isEmpty()) {
				for (ChartDTO chart2 : chartList2) {
					chartMap.put(chart2.getName(), chart2.getCount());
				}
			}

			long employedCandidates = chartService.getEmployedCandidateCountForChartForDepartment(departmentId);
			if (employedCandidates > 0) {
				chartMap.put("Employed", employedCandidates);
			}
		} else if (vacancyId > 0) {
			List<ChartDTO> chartList3 = chartService.getCandidateCountBySelectionStatusForInterviewer(vacancyId);
			if (!chartList3.isEmpty()) {
				for (ChartDTO chart3 : chartList3) {
					chartMap.put(chart3.getName(), chart3.getCount());
				}
			}

			List<ChartDTO> chartList4 = chartService.getCandidateCountByInterviewStatusForInterviewer(vacancyId);
			if (!chartList4.isEmpty()) {
				for (ChartDTO chart4 : chartList4) {
					chartMap.put(chart4.getName(), chart4.getCount());
				}
			}

			long employedCandidates = chartService.getEmployedCandidateCountByVacancyId(vacancyId);
			if (employedCandidates > 0) {
				chartMap.put("Employed", employedCandidates);
			}
		}
		return chartMap;
	}
}