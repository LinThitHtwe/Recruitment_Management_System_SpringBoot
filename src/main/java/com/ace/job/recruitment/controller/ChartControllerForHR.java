package com.ace.job.recruitment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.service.ChartService;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.VacancyService;

@RestController
@RequestMapping("/hr")
public class ChartControllerForHR {
	@Autowired
	PositionService positionService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	VacancyService vacancyService;
	@Autowired
	ChartService chartService;

	@GetMapping("/vacancies-filter-by-department-time/{selectedDepartment}/{selectedYear}/{selectedMonth}")
	public Map<String, Long> getVacanciesFilteredByDepartmentAndTime(
			@PathVariable("selectedDepartment") int selectedDepartment, @PathVariable("selectedYear") int selectedYear,
			@PathVariable("selectedMonth") int selectedMonth) {
		Map<String, Long> vacancyByDepartmentAndTime = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.filterVacancyByDepartmentAndTime(selectedDepartment, selectedYear, selectedMonth);
		for (ChartDTO chart : chartList) {
			vacancyByDepartmentAndTime.put(chart.getName(), chart.getCount());
		}
		return vacancyByDepartmentAndTime;
	}

	@GetMapping("/vacancies-filter-by-position-time/{selectedPosition}/{selectedYear}/{selectedMonth}")
	public Map<String, Long> getVacanciesFilteredByPositionAndTime(
			@PathVariable("selectedPosition") int selectedPosition, @PathVariable("selectedYear") int selectedYear,
			@PathVariable("selectedMonth") int selectedMonth) {
		Map<String, Long> vacancyByPositionAndTime = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.filterVacancyByPositionAndTime(selectedPosition, selectedYear, selectedMonth);
		for (ChartDTO chart : chartList) {
			vacancyByPositionAndTime.put(chart.getName(), chart.getCount());
		}
		return vacancyByPositionAndTime;
	}

	@GetMapping("/position-demand/{selectedStartYear}/{selectedEndYear}")
	public List<Map<String, Object>> showPositionDemandChartByYears(
			@PathVariable("selectedStartYear") int selectedStartYear,
			@PathVariable("selectedEndYear") int selectedEndYear) {
		List<Map<String, Object>> chartMapList = new ArrayList<Map<String, Object>>();
		chartMapList = chartService.showPositionDemandByYears(selectedStartYear, selectedEndYear);
		return chartMapList;
	}

	@GetMapping("/candidate-position-trend/{selectedStartYear}/{selectedEndYear}")
	public List<Map<String, Object>> showCandidatePositionDemandByYears(
			@PathVariable("selectedStartYear") int selectedStartYear,
			@PathVariable("selectedEndYear") int selectedEndYear) {
		List<Map<String, Object>> chartMapList = new ArrayList<Map<String, Object>>();
		chartMapList = chartService.showCandidatePositionDemandByYears(selectedStartYear, selectedEndYear);
		return chartMapList;
	}

	@GetMapping("/candidates-by-position/{selectedDepartment}/{selectedYear}/{selectedMonth}")
	public Map<String, Long> showCandidateCountByPosition(@PathVariable("selectedDepartment") int selectedDepartment,
			@PathVariable("selectedYear") int selectedYear, @PathVariable("selectedMonth") int selectedMonth) {
		Map<String, Long> chartMap = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.getCandidateCountByPosition(selectedDepartment, selectedYear, selectedMonth);
		for (ChartDTO chart : chartList) {
			chartMap.put(chart.getName(), chart.getCount());
		}
		return chartMap;

	}

	@GetMapping("/candidates-by-vacancy-status/{selectedDepartment}/{urgentStatus}")
	public Map<String, Long> showCandidateCountByVacancyStatus(
			@PathVariable("selectedDepartment") int selectedDepartment,
			@PathVariable("urgentStatus") int urgentStatus) {
		Map<String, Long> chartMap = new HashMap<>();
		List<ChartDTO> chartList = new ArrayList<ChartDTO>();
		chartList = chartService.getCandidateCountByVacancyStatus(selectedDepartment, urgentStatus);
		for (ChartDTO chart : chartList) {
			chartMap.put(chart.getName(), chart.getCount());
		}
		return chartMap;
	}

	@GetMapping("/vacancies-by-position/{positionId}")
	public List<Map<Long, String>> showVacancyOptionsByPosition(@PathVariable("positionId") int positionId) {
		List<Map<Long, String>> chartMapList = new ArrayList<Map<Long, String>>();
		List<Vacancy> vacancyList = new ArrayList<Vacancy>();
		Position position = positionService.getPositionById(positionId);
		vacancyList = position.getVacancies();
		for (Vacancy vacancy : vacancyList) {
			Map<Long, String> chartMap = new HashMap<>();
			chartMap.put(vacancy.getId(),
					position.getName() + " " + vacancy.getCreatedDate() + " " + vacancy.getDepartment().getName());
			chartMapList.add(chartMap);
		}

		return chartMapList;
	}

	@GetMapping("/candidates-by-status/{vacancyId}")
	public Map<String, Long> showCandidateCountByVacancy(@PathVariable("vacancyId") Long vacancyId) {
		Map<String, Long> chartMap = new HashMap<>();
		if (vacancyId == 0) {
			List<ChartDTO> chartList1 = chartService.getTotalCandidateCountBySelectionStatus();
			if (!chartList1.isEmpty()) {
				for (ChartDTO chart1 : chartList1) {
					chartMap.put(chart1.getName(), chart1.getCount());
				}
			}

			List<ChartDTO> chartList2 = chartService.getTotalCandidateCountByInterviewStatus();
			if (!chartList2.isEmpty()) {
				for (ChartDTO chart2 : chartList2) {
					chartMap.put(chart2.getName(), chart2.getCount());
				}
			}

			long employedCandidates = chartService.getEmployedCandidateCountForChart();
			if (employedCandidates > 0) {
				chartMap.put("Employed", employedCandidates);
			}

		} else if (vacancyId > 0) {
			List<ChartDTO> chartList3 = chartService.getCandidateCountBySelectionStatus(vacancyId);
			if (!chartList3.isEmpty()) {
				for (ChartDTO chart3 : chartList3) {
					chartMap.put(chart3.getName(), chart3.getCount());
				}
			}

			List<ChartDTO> chartList4 = chartService.getCandidateCountByInterviewStatus(vacancyId);
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