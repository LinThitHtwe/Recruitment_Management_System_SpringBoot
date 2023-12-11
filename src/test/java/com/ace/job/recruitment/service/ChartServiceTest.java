package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.repository.DepartmentRepository;
import com.ace.job.recruitment.repository.PositionRepository;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.impl.ChartServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ChartServiceTest {
	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private PositionRepository positionRepository;

	@Mock
	private VacancyRepository vacancyRepository;

	@Mock
	private ChartService chartService;

	@Mock
	private CandidateRepository candidateRepository;

	@InjectMocks
	private ChartServiceImpl chartServiceImpl;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	// hr dashboard start
	@Test
	public void filterVacancyByDepartmentAndTimeTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(positionRepository.getPositionWithVacancyCount()).thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByDepartment(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByYear(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByYearMonth(anyInt(), anyInt())).thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByDepartmentAndYear(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.filterVacancyByDepartmentAndTime(0, 0, 0);
		List<ChartDTO> result2 = chartServiceImpl.filterVacancyByDepartmentAndTime(1, 0, 0);
		List<ChartDTO> result3 = chartServiceImpl.filterVacancyByDepartmentAndTime(0, 2022, 0);
		List<ChartDTO> result4 = chartServiceImpl.filterVacancyByDepartmentAndTime(0, 2023, 8);
		List<ChartDTO> result5 = chartServiceImpl.filterVacancyByDepartmentAndTime(1, 2020, 0);
		List<ChartDTO> result6 = chartServiceImpl.filterVacancyByDepartmentAndTime(1, 2023, 3);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		assertNotNull(result4);
		assertEquals(mockChartData.size(), result4.size());

		assertNotNull(result5);
		assertEquals(mockChartData.size(), result5.size());

		assertNotNull(result6);
		assertEquals(mockChartData.size(), result6.size());

		verify(positionRepository, times(1)).getPositionWithVacancyCount();
		verify(positionRepository, times(1)).getPositionVacancyCountsByDepartment(anyInt());
		verify(positionRepository, times(1)).getPositionVacancyCountsByYear(anyInt());
		verify(positionRepository, times(1)).getPositionVacancyCountsByYearMonth(anyInt(), anyInt());
		verify(positionRepository, times(1)).getPositionVacancyCountsByDepartmentAndYear(anyInt(), anyInt());
		verify(positionRepository, times(1)).getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(), anyInt(),
				anyInt());

	}

	@Test
	public void filterVacancyByPositionAndTimeTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(departmentRepository.getDepartmentWithVacancyCount()).thenReturn(mockChartData);
		when(departmentRepository.getDepartmentVacancyCountsByPosition(anyInt())).thenReturn(mockChartData);
		when(departmentRepository.getDepartmentVacancyCountsByYear(anyInt())).thenReturn(mockChartData);
		when(departmentRepository.getDepartmentVacancyCountsByYearMonth(anyInt(), anyInt())).thenReturn(mockChartData);
		when(departmentRepository.getDepartmentVacancyCountsByPositionAndYear(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(departmentRepository.getDepartmentVacancyCountsByPositionYearAndMonth(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.filterVacancyByPositionAndTime(0, 0, 0);
		List<ChartDTO> result2 = chartServiceImpl.filterVacancyByPositionAndTime(1, 0, 0);
		List<ChartDTO> result3 = chartServiceImpl.filterVacancyByPositionAndTime(0, 2022, 0);
		List<ChartDTO> result4 = chartServiceImpl.filterVacancyByPositionAndTime(0, 2023, 8);
		List<ChartDTO> result5 = chartServiceImpl.filterVacancyByPositionAndTime(1, 2020, 0);
		List<ChartDTO> result6 = chartServiceImpl.filterVacancyByPositionAndTime(1, 2023, 3);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		assertNotNull(result4);
		assertEquals(mockChartData.size(), result4.size());

		assertNotNull(result5);
		assertEquals(mockChartData.size(), result5.size());

		assertNotNull(result6);
		assertEquals(mockChartData.size(), result6.size());

		verify(departmentRepository, times(1)).getDepartmentWithVacancyCount();
		verify(departmentRepository, times(1)).getDepartmentVacancyCountsByPosition(anyInt());
		verify(departmentRepository, times(1)).getDepartmentVacancyCountsByYear(anyInt());
		verify(departmentRepository, times(1)).getDepartmentVacancyCountsByYearMonth(anyInt(), anyInt());
		verify(departmentRepository, times(1)).getDepartmentVacancyCountsByPositionAndYear(anyInt(), anyInt());
		verify(departmentRepository, times(1)).getDepartmentVacancyCountsByPositionYearAndMonth(anyInt(), anyInt(),
				anyInt());

	}

	@Test
	void showPositionDemandByYearsTest() {
		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		mockChartData.add(data1);
		mockChartData.add(data2);

		when(positionRepository.getPositionDemand()).thenReturn(mockChartData);
		when(positionRepository.getPositionDemandByStartYear(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getPositionDemandByYears(anyInt(), anyInt())).thenReturn(mockChartData);

		List<Map<String, Object>> result1 = chartServiceImpl.showPositionDemandByYears(0, 0);
		List<Map<String, Object>> result2 = chartServiceImpl.showPositionDemandByYears(2023, 0);
		List<Map<String, Object>> result3 = chartServiceImpl.showPositionDemandByYears(2020, 2023);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		verify(positionRepository, times(1)).getPositionDemand();
		verify(positionRepository, times(1)).getPositionDemandByStartYear(anyInt());
		verify(positionRepository, times(1)).getPositionDemandByYears(anyInt(), anyInt());
	}

	@Test
	void showCandidatePositionDemandByYearsTest() {
		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		// Add the sample data to the mockResult list
		mockChartData.add(data1);
		mockChartData.add(data2);

		when(positionRepository.getCandidatePositionDemand()).thenReturn(mockChartData);
		when(positionRepository.getCandidatePositionDemandByStartYear(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidatePositionDemandByYears(anyInt(), anyInt())).thenReturn(mockChartData);

		List<Map<String, Object>> result1 = chartServiceImpl.showCandidatePositionDemandByYears(0, 0);
		List<Map<String, Object>> result2 = chartServiceImpl.showCandidatePositionDemandByYears(2023, 0);
		List<Map<String, Object>> result3 = chartServiceImpl.showCandidatePositionDemandByYears(2020, 2023);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		verify(positionRepository, times(1)).getCandidatePositionDemand();
		verify(positionRepository, times(1)).getCandidatePositionDemandByStartYear(anyInt());
		verify(positionRepository, times(1)).getCandidatePositionDemandByYears(anyInt(), anyInt());
	}

	@Test
	public void getCandidateCountByPositionTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(positionRepository.getCandidateCountByPosition()).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndDepartment(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndYear(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndTime(anyInt(), anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndDepartmentAndYear(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndDepartmentAndTime(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountByPosition(0, 0, 0);
		List<ChartDTO> result2 = chartServiceImpl.getCandidateCountByPosition(1, 0, 0);
		List<ChartDTO> result3 = chartServiceImpl.getCandidateCountByPosition(0, 2022, 0);
		List<ChartDTO> result4 = chartServiceImpl.getCandidateCountByPosition(0, 2023, 8);
		List<ChartDTO> result5 = chartServiceImpl.getCandidateCountByPosition(1, 2020, 0);
		List<ChartDTO> result6 = chartServiceImpl.getCandidateCountByPosition(1, 2023, 3);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		assertNotNull(result4);
		assertEquals(mockChartData.size(), result4.size());

		assertNotNull(result5);
		assertEquals(mockChartData.size(), result5.size());

		assertNotNull(result6);
		assertEquals(mockChartData.size(), result6.size());

		verify(positionRepository, times(1)).getCandidateCountByPosition();
		verify(positionRepository, times(1)).getCandidateCountByPositionAndDepartment(anyInt());
		verify(positionRepository, times(1)).getCandidateCountByPositionAndYear(anyInt());
		verify(positionRepository, times(1)).getCandidateCountByPositionAndTime(anyInt(), anyInt());
		verify(positionRepository, times(1)).getCandidateCountByPositionAndDepartmentAndYear(anyInt(), anyInt());
		verify(positionRepository, times(1)).getCandidateCountByPositionAndDepartmentAndTime(anyInt(), anyInt(),
				anyInt());

	}

	@Test
	public void getCandidateCountByVacancyStatusTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(positionRepository.getCandidateCountByActiveVacancies()).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByUrgentVacancies()).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByActiveVacanciesAndDepartment(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByUrgentVacanciesAndDepartment(anyInt())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountByVacancyStatus(0, 0);
		List<ChartDTO> result2 = chartServiceImpl.getCandidateCountByVacancyStatus(0, 1);
		List<ChartDTO> result3 = chartServiceImpl.getCandidateCountByVacancyStatus(3, 0);
		List<ChartDTO> result4 = chartServiceImpl.getCandidateCountByVacancyStatus(3, 1);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		assertNotNull(result4);
		assertEquals(mockChartData.size(), result4.size());

		verify(positionRepository, times(1)).getCandidateCountByActiveVacancies();
		verify(positionRepository, times(1)).getCandidateCountByUrgentVacancies();
		verify(positionRepository, times(1)).getCandidateCountByActiveVacanciesAndDepartment(anyInt());
		verify(positionRepository, times(1)).getCandidateCountByUrgentVacanciesAndDepartment(anyInt());

	}

	@Test
	public void getCandidateCountBySelectionStatusTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Receieved", 12));
		mockChartData.add(new ChartDTO("Viewed", 10));
		mockChartData.add(new ChartDTO("Considering", 9));

		when(vacancyRepository.getCandidateCountsByVacancyIdForSelection(anyLong())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountBySelectionStatus(10L);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsByVacancyIdForSelection(anyLong());

	}

	@Test
	public void getCandidateCountByInterviewStatusTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Interview stage 1 Reached", 12));
		mockChartData.add(new ChartDTO("Interview stage 1 Pending", 10));
		mockChartData.add(new ChartDTO("Interview stage 1 Passed", 9));

		when(vacancyRepository.getCandidateCountsByVacancyIdForInterviews(anyLong())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountByInterviewStatus(10L);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsByVacancyIdForInterviews(anyLong());

	}

	@Test
	public void getTotalCandidateCountBySelectionStatusTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Receieved", 12));
		mockChartData.add(new ChartDTO("Viewed", 10));
		mockChartData.add(new ChartDTO("Considering", 9));

		when(vacancyRepository.getCandidateCountsForSelection()).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getTotalCandidateCountBySelectionStatus();

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsForSelection();

	}

	@Test
	public void getTotalCandidateCountByInterviewStatusTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Interview stage 1 Reached", 12));
		mockChartData.add(new ChartDTO("Interview stage 1 Pending", 10));
		mockChartData.add(new ChartDTO("Interview stage 1 Passed", 9));

		when(vacancyRepository.getCandidateCountsForInterviews()).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getTotalCandidateCountByInterviewStatus();

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsForInterviews();

	}

	@Test
	public void getEmployedCandidateCountForChartTest() {
		long employedCount = 12L;

		when(candidateRepository.getAllEmployedCandidateCount()).thenReturn(employedCount);

		long result = chartServiceImpl.getEmployedCandidateCountForChart();
		assertEquals(employedCount, result);

		verify(candidateRepository, times(1)).getAllEmployedCandidateCount();

	}

	@Test
	public void getEmployedCandidateCountByVacancyIdTest() {
		long employedCount = 12L;

		when(candidateRepository.getEmployedCandidateCountByVacancy(anyLong())).thenReturn(employedCount);

		long result = chartServiceImpl.getEmployedCandidateCountByVacancyId(anyLong());
		assertEquals(employedCount, result);

		verify(candidateRepository, times(1)).getEmployedCandidateCountByVacancy(anyLong());
	}

	// hr dashboard end

	// department dashboard start
	@Test
	public void filterVacancyByTimeForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(positionRepository.getPositionVacancyCountsByDepartment(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByDepartmentAndYear(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(positionRepository.getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.filterVacancyByTimeForInterviewer(3, 0, 0);
		List<ChartDTO> result2 = chartServiceImpl.filterVacancyByTimeForInterviewer(3, 2023, 0);
		List<ChartDTO> result3 = chartServiceImpl.filterVacancyByTimeForInterviewer(3, 2022, 2023);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		verify(positionRepository, times(1)).getPositionVacancyCountsByDepartment(anyInt());
		verify(positionRepository, times(1)).getPositionVacancyCountsByDepartmentAndYear(anyInt(), anyInt());
		verify(positionRepository, times(1)).getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(), anyInt(),
				anyInt());

	}

	@Test
	public void showPositionDemandByYearsForInterviewerTest() {
		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		mockChartData.add(data1);
		mockChartData.add(data2);

		when(positionRepository.getPositionDemandForInterviewer(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getPositionDemandByStartYearForInterviewer(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(positionRepository.getPositionDemandByYearsForInterviewer(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<Map<String, Object>> result1 = chartServiceImpl.showPositionDemandByYearsForInterviewer(3, 0, 0);
		List<Map<String, Object>> result2 = chartServiceImpl.showPositionDemandByYearsForInterviewer(3, 2023, 0);
		List<Map<String, Object>> result3 = chartServiceImpl.showPositionDemandByYearsForInterviewer(3, 2022, 2023);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		verify(positionRepository, times(1)).getPositionDemandForInterviewer(anyInt());
		verify(positionRepository, times(1)).getPositionDemandByStartYearForInterviewer(anyInt(), anyInt());
		verify(positionRepository, times(1)).getPositionDemandByYearsForInterviewer(anyInt(), anyInt(), anyInt());

	}

	@Test
	public void showCandidateTrendByYearsForInterviewerTest() {
		List<Map<String, Object>> mockChartData = new ArrayList<>();
		Map<String, Object> data1 = new HashMap<>();
		data1.put("position", "Java Developer");
		data1.put("count", 10);

		Map<String, Object> data2 = new HashMap<>();
		data2.put("position", "Android Developer");
		data2.put("count", 5);

		// Add the sample data to the mockResult list
		mockChartData.add(data1);
		mockChartData.add(data2);

		when(positionRepository.getCandidatePositionDemandForInterviewer(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateTrendByStartYearForInterviewer(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(positionRepository.getCandidateTrendByYearsForInterviewer(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<Map<String, Object>> result1 = chartServiceImpl.showCandidateTrendByYearsForInterviewer(3, 0, 0);
		List<Map<String, Object>> result2 = chartServiceImpl.showCandidateTrendByYearsForInterviewer(3, 2023, 0);
		List<Map<String, Object>> result3 = chartServiceImpl.showCandidateTrendByYearsForInterviewer(3, 2022, 2023);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		verify(positionRepository, times(1)).getCandidatePositionDemandForInterviewer(anyInt());
		verify(positionRepository, times(1)).getCandidateTrendByStartYearForInterviewer(anyInt(), anyInt());
		verify(positionRepository, times(1)).getCandidateTrendByYearsForInterviewer(anyInt(), anyInt(), anyInt());

	}

	@Test
	public void getCandidateCountByTimeForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(positionRepository.getCandidateCountByPositionAndDepartment(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndDepartmentAndYear(anyInt(), anyInt()))
				.thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByPositionAndDepartmentAndTime(anyInt(), anyInt(), anyInt()))
				.thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountByTimeForInterviewer(3, 0, 0);
		List<ChartDTO> result2 = chartServiceImpl.getCandidateCountByTimeForInterviewer(3, 2023, 0);
		List<ChartDTO> result3 = chartServiceImpl.getCandidateCountByTimeForInterviewer(3, 2022, 5);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		assertNotNull(result3);
		assertEquals(mockChartData.size(), result3.size());

		verify(positionRepository, times(1)).getCandidateCountByPositionAndDepartment(anyInt());
		verify(positionRepository, times(1)).getCandidateCountByPositionAndDepartmentAndYear(anyInt(), anyInt());
		verify(positionRepository, times(1)).getCandidateCountByPositionAndDepartmentAndTime(anyInt(), anyInt(),
				anyInt());

	}

	@Test
	public void getCandidateCountByVacancyStatusForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Java Developer", 12));
		mockChartData.add(new ChartDTO("Android Developer", 10));
		mockChartData.add(new ChartDTO("Python Developer", 9));

		when(positionRepository.getCandidateCountByActiveVacanciesAndDepartment(anyInt())).thenReturn(mockChartData);
		when(positionRepository.getCandidateCountByUrgentVacanciesAndDepartment(anyInt())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountByVacancyStatusForInterviewer(3, 0);
		List<ChartDTO> result2 = chartServiceImpl.getCandidateCountByVacancyStatusForInterviewer(3, 1);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		assertNotNull(result2);
		assertEquals(mockChartData.size(), result2.size());

		verify(positionRepository, times(1)).getCandidateCountByActiveVacanciesAndDepartment(anyInt());
		verify(positionRepository, times(1)).getCandidateCountByUrgentVacanciesAndDepartment(anyInt());

	}

	@Test
	public void getCandidateCountBySelectionStatusForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Receieved", 12));
		mockChartData.add(new ChartDTO("Viewed", 10));
		mockChartData.add(new ChartDTO("Considering", 9));

		when(vacancyRepository.getCandidateCountsByVacancyIdForSelection(anyLong())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountBySelectionStatusForInterviewer(10L);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsByVacancyIdForSelection(anyLong());

	}

	@Test
	public void getCandidateCountByInterviewStatusForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Interview stage 1 Reached", 12));
		mockChartData.add(new ChartDTO("Interview stage 1 Pending", 10));
		mockChartData.add(new ChartDTO("Interview stage 1 Passed", 9));

		when(vacancyRepository.getCandidateCountsByVacancyIdForInterviews(anyLong())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getCandidateCountByInterviewStatusForInterviewer(10L);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsByVacancyIdForInterviews(anyLong());

	}

	@Test
	public void getTotalCandidateCountBySelectionStatusForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Receieved", 12));
		mockChartData.add(new ChartDTO("Viewed", 10));
		mockChartData.add(new ChartDTO("Considering", 9));

		when(vacancyRepository.getCandidateCountsForSelectionForInterviewer(anyInt())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getTotalCandidateCountBySelectionStatusForInterviewer(3);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsForSelectionForInterviewer(anyInt());

	}

	@Test
	public void getTotalCandidateCountByInterviewStatusForInterviewerTest() {
		List<ChartDTO> mockChartData = new ArrayList<>();
		mockChartData.add(new ChartDTO("Interview stage 1 Reached", 12));
		mockChartData.add(new ChartDTO("Interview stage 1 Pending", 10));
		mockChartData.add(new ChartDTO("Interview stage 1 Passed", 9));

		when(vacancyRepository.getCandidateCountsForInterviewsForInterviewer(anyInt())).thenReturn(mockChartData);

		List<ChartDTO> result1 = chartServiceImpl.getTotalCandidateCountByInterviewStatusForInterviewer(3);

		assertNotNull(result1);
		assertEquals(mockChartData.size(), result1.size());

		verify(vacancyRepository, times(1)).getCandidateCountsForInterviewsForInterviewer(anyInt());

	}

	@Test
	public void getCurrentDepartmentIdTest() {
		Authentication authentication = mock(Authentication.class);
		AppUserDetails appUserDetails = mock(AppUserDetails.class);

		int expectedDepartmentId = 5;
		User user = new User();
		Department department = new Department();
		department.setId(expectedDepartmentId);
		user.setDepartment(department);
		when(appUserDetails.getUser()).thenReturn(user);
		when(authentication.getPrincipal()).thenReturn(appUserDetails);

		int actualDepartmentId = chartServiceImpl.getCurrentDepartmentId(authentication);

		assertEquals(expectedDepartmentId, actualDepartmentId);
	}

	@Test
	public void getEmployedCandidateCountForChartForDepartmentTest() {
		long employedCount = 12L;

		when(candidateRepository.getAllEmployedCandidateCountForDepartment(anyInt())).thenReturn(employedCount);

		long result = chartServiceImpl.getEmployedCandidateCountForChartForDepartment(anyInt());
		assertEquals(employedCount, result);

		verify(candidateRepository, times(1)).getAllEmployedCandidateCountForDepartment(anyInt());
	}
	// department dashboard end

}
