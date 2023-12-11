package com.ace.job.recruitment.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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
import org.springframework.security.core.Authentication;
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
public class ChartControllerInterviewerTest {
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
	private ChartControllerForInterviewer chartControllerForInterviewer;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(chartControllerForInterviewer).build();
	}

	@Test
	public void getVacanciesFilteredByDepartmentAndTimeTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		int departmentId = 5;
		int selectedYear = 2023;
		int selectedMonth = 8;

		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);

		when(chartService.filterVacancyByTimeForInterviewer(departmentId, selectedYear, selectedMonth))
				.thenReturn(chartList);

		mockMvc.perform(get("/department/vacancies-by-time/{selectedYear}/{selectedMonth}", selectedYear, selectedMonth)
				.principal(authentication)).andExpect(status().isOk());
	}

	@Test
	public void showPositionDemandChartByYearsTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		int departmentId = 5;
		int selectedStartYear = 2020;
		int selectedEndYear = 2022;

		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		mockChartData.add(data1);
		mockChartData.add(data2);

		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);

		when(chartService.showPositionDemandByYearsForInterviewer(departmentId, selectedStartYear, selectedEndYear))
				.thenReturn(mockChartData);

		mockMvc.perform(get("/department/position-demand/{selectedStartYear}/{selectedEndYear}", selectedStartYear,
				selectedEndYear).principal(authentication)).andExpect(status().isOk());

		verify(chartService, times(1)).showPositionDemandByYearsForInterviewer(departmentId, selectedStartYear,
				selectedEndYear);
		verifyNoMoreInteractions(chartService);
	}

	@Test
	public void showCandidatePositionDemandByYearsTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		int departmentId = 5;
		int selectedStartYear = 2020;
		int selectedEndYear = 2022;

		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		mockChartData.add(data1);
		mockChartData.add(data2);

		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);

		when(chartService.showCandidateTrendByYearsForInterviewer(departmentId, selectedStartYear, selectedEndYear))
				.thenReturn(mockChartData);

		mockMvc.perform(get("/department/candidate-position-trend/{selectedStartYear}/{selectedEndYear}",
				selectedStartYear, selectedEndYear).principal(authentication)).andExpect(status().isOk());
	}

	@Test
	public void showCandidateCountByPositionTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		int departmentId = 5;
		int selectedYear = 2023;
		int selectedMonth = 8;

		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);

		when(chartService.getCandidateCountByTimeForInterviewer(departmentId, selectedYear, selectedMonth))
				.thenReturn(chartList);

		mockMvc.perform(
				get("/department/candidates-by-time/{selectedYear}/{selectedMonth}", selectedYear, selectedMonth)
						.principal(authentication))
				.andExpect(status().isOk());
	}

	@Test
	public void showCandidateCountByVacancyStatusTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		int departmentId = 5;
		int urgentStatus = 1;

		List<ChartDTO> chartList = Arrays.asList(new ChartDTO("Java Developer", 10),
				new ChartDTO("Android Developer", 20));

		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);

		when(chartService.getCandidateCountByVacancyStatusForInterviewer(departmentId, urgentStatus))
				.thenReturn(chartList);

		mockMvc.perform(
				get("/department/candidates-by-vacancy-status/{urgentStatus}", urgentStatus).principal(authentication))
				.andExpect(status().isOk());
	}

	@Test
	public void showVacancyOptionsByPositionTest() throws Exception {
		List<Map<Long, String>> mockChartMapList = new ArrayList<>();
		Map<Long, String> chartMap1 = new HashMap<>();
		chartMap1.put(1L, "Java Developer 22.08.2023");
		mockChartMapList.add(chartMap1);

		int departmentId = 5;
		Authentication authentication = mock(Authentication.class);
		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);
		Department mockDepartment = new Department();

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
		when(departmentService.getDepartmentById(departmentId)).thenReturn(mockDepartment);
		when(vacancyService.getVacancyByDepartmentAndPosition(mockDepartment, mockPosition)).thenReturn(vacancyList);

		mockMvc.perform(get("/department/vacancies-by-position/{positionId}", 1).principal(authentication))
				.andExpect(status().isOk());

	}

	@Test
	public void showCandidateCountByVacancyEmptyListTest() throws Exception {
		List<ChartDTO> mockChartList = new ArrayList<>();
		long employedCount = 0L;

		int departmentId = 5;
		Authentication authentication = mock(Authentication.class);
		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);

		when(chartService.getTotalCandidateCountBySelectionStatusForInterviewer(departmentId))
				.thenReturn(mockChartList);
		when(chartService.getTotalCandidateCountByInterviewStatusForInterviewer(departmentId))
				.thenReturn(mockChartList);
		when(chartService.getCandidateCountBySelectionStatusForInterviewer(anyLong())).thenReturn(mockChartList);
		when(chartService.getCandidateCountByInterviewStatusForInterviewer(anyLong())).thenReturn(mockChartList);
		when(chartService.getEmployedCandidateCountForChartForDepartment(departmentId)).thenReturn(employedCount);
		when(chartService.getEmployedCandidateCountByVacancyId(anyLong())).thenReturn(employedCount);

		mockMvc.perform(get("/department/candidates-by-status/{vacancyId}", 0).principal(authentication));

		mockMvc.perform(get("/department/candidates-by-status/{vacancyId}", 1).principal(authentication));
	}
}
