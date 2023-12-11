package com.ace.job.recruitment.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.service.ChartService;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.VacancyService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ChartControllerHRTest {
	private MockMvc mockMvc;

	@Mock
	private ChartService chartService;

	@Mock
	private PositionService positionService;

	@Mock
	private DepartmentService departmentService;

	@Mock
	private VacancyService vacancyService;

	@InjectMocks
	private ChartControllerForHR chartControllerForHR;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(chartControllerForHR).build();
	}

	@Test
	public void getVacanciesFilteredByDepartmentAndTimeTest() throws Exception {
		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		Map<String, Long> expectedResult = new HashMap<>();
		expectedResult.put("Java Developer", 10L);
		expectedResult.put("Android Developer", 20L);

		when(chartService.filterVacancyByDepartmentAndTime(anyInt(), anyInt(), anyInt())).thenReturn(chartList);

		mockMvc.perform(
				get("/hr/vacancies-filter-by-department-time/{selectedDepartment}/{selectedYear}/{selectedMonth}", 1,
						2023, 8))
				.andExpect(status().isOk());
//		verify(chartService, times(1)).filterVacancyByDepartmentAndTime(1, 2023, 8);
		verifyNoMoreInteractions(chartService);
	}

	@Test
	public void getVacanciesFilteredByPositionAndTimeTest() throws Exception {
		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		Map<String, Long> expectedResult = new HashMap<>();
		expectedResult.put("Java Developer", 10L);
		expectedResult.put("Android Developer", 20L);

		when(chartService.filterVacancyByPositionAndTime(anyInt(), anyInt(), anyInt())).thenReturn(chartList);

		mockMvc.perform(get("/hr/vacancies-filter-by-position-time/{selectedPosition}/{selectedYear}/{selectedMonth}",
				1, 2023, 8)).andExpect(status().isOk());

//		verify(chartService, times(1)).filterVacancyByPositionAndTime(1, 2023, 8);
		verifyNoMoreInteractions(chartService);
	}

	@Test
	public void showPositionDemandChartByYearsTest() throws Exception {
		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		mockChartData.add(data1);
		mockChartData.add(data2);

		when(chartService.showPositionDemandByYears(anyInt(), anyInt())).thenReturn(mockChartData);

		mockMvc.perform(get("/hr/position-demand/{selectedStartYear}/{selectedEndYear}", 2022, 2023))
				.andExpect(status().isOk());

		verify(chartService, times(1)).showPositionDemandByYears(2022, 2023);
		verifyNoMoreInteractions(chartService);
	}

	@Test
	public void showCandidatePositionDemandByYearsTest() throws Exception {
		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		mockChartData.add(data1);
		mockChartData.add(data2);

		when(chartService.showCandidatePositionDemandByYears(anyInt(), anyInt())).thenReturn(mockChartData);

		mockMvc.perform(get("/hr/candidate-position-trend/{selectedStartYear}/{selectedEndYear}", 2022, 2023))
				.andExpect(status().isOk());

		verify(chartService, times(1)).showCandidatePositionDemandByYears(2022, 2023);
		verifyNoMoreInteractions(chartService);
	}

	@Test
	public void showCandidateCountByPositionTest() throws Exception {
		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		Map<String, Long> expectedResult = new HashMap<>();
		expectedResult.put("Java Developer", 10L);
		expectedResult.put("Android Developer", 20L);

		when(chartService.getCandidateCountByPosition(anyInt(), anyInt(), anyInt())).thenReturn(chartList);

		mockMvc.perform(
				get("/hr/candidates-by-position/{selectedDepartment}/{selectedYear}/{selectedMonth}", 1, 2023, 8))
				.andExpect(status().isOk());

		verifyNoMoreInteractions(chartService);
	}

	@Test
	public void showCandidateCountByVacancyStatusTest() throws Exception {
		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		Map<String, Long> expectedResult = new HashMap<>();
		expectedResult.put("Java Developer", 10L);
		expectedResult.put("Android Developer", 20L);

		when(chartService.getCandidateCountByVacancyStatus(anyInt(), anyInt())).thenReturn(chartList);

		mockMvc.perform(get("/hr/candidates-by-vacancy-status/{selectedDepartment}/{urgentStatus}", 1, 0))
				.andExpect(status().isOk());
	}

	@Test
	public void showVacancyOptionsByPositionTest() throws Exception {
		List<Map<Long, String>> mockChartMapList = new ArrayList<>();
		Map<Long, String> chartMap1 = new HashMap<>();
		chartMap1.put(1L, "Java Developer 22.08.2023 Banking");
		mockChartMapList.add(chartMap1);

		Department mockDepartment = new Department();
		mockDepartment.setName("Banking");

		Position mockPosition = new Position();
		mockPosition.setName("Java Developer");
		List<Vacancy> vacancyList = new ArrayList<>();
		Vacancy vacancy = new Vacancy();
		vacancy.setId(1L);
		vacancy.setCreatedDate(LocalDate.of(2023, 8, 22));
		vacancy.setDepartment(mockDepartment);

		vacancyList.add(vacancy);
		mockPosition.setVacancies(vacancyList);

		when(positionService.getPositionById(1)).thenReturn(mockPosition);

		mockMvc.perform(get("/hr/vacancies-by-position/{positionId}", 1)).andExpect(status().isOk());
	}

	@Test
	public void showCandidateCountByVacancyEmptyListTest() throws Exception {
		List<ChartDTO> mockChartList = new ArrayList<>();
		long employedCount = 0L;

		when(chartService.getTotalCandidateCountBySelectionStatus()).thenReturn(mockChartList);
		when(chartService.getTotalCandidateCountByInterviewStatus()).thenReturn(mockChartList);
		when(chartService.getCandidateCountBySelectionStatus(anyLong())).thenReturn(mockChartList);
		when(chartService.getCandidateCountByInterviewStatus(anyLong())).thenReturn(mockChartList);
		when(chartService.getEmployedCandidateCountForChart()).thenReturn(employedCount);
		when(chartService.getEmployedCandidateCountByVacancyId(anyLong())).thenReturn(employedCount);

		mockMvc.perform(get("/hr/candidates-by-status/{vacancyId}", 0)).andExpect(status().isOk());
		mockMvc.perform(get("/hr/candidates-by-status/{vacancyId}", 1)).andExpect(status().isOk());

	}

}
