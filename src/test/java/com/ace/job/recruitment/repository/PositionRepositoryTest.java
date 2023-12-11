package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Position;

@SpringBootTest
public class PositionRepositoryTest {

	@Mock
	private PositionRepository positionRepository;

	@Test
	public void findByNameTest() {
		Position position = new Position();
		position.setId(1);
		position.setName("Java Developer");
		List<Position> expectedResults = new ArrayList<Position>();
		expectedResults.add(position);

		when(positionRepository.findByName("Java Developer")).thenReturn(expectedResults);
		List<Position> positions = positionRepository.findByName("Java Developer");

		assertEquals(expectedResults, positions);
	}

	@Test
	public void testFindByAnyFieldContaining() {
		Position position = new Position();
		position.setId(1);
		position.setName("Java Developer");
		List<Position> expectedResults = new ArrayList<Position>();
		expectedResults.add(position);

		when(positionRepository.findByAnyFieldContaining("Java")).thenReturn(expectedResults);
		List<Position> positions = positionRepository.findByAnyFieldContaining("Java");

		assertEquals(expectedResults, positions);
	}

	@Test
	public void getDepartmentWithVacancyCountTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getPositionWithVacancyCount()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getPositionWithVacancyCount();

		verify(positionRepository).getPositionWithVacancyCount();

		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionVacancyCountsByDepartmentTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getPositionVacancyCountsByDepartment(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getPositionVacancyCountsByDepartment(anyInt());

		verify(positionRepository).getPositionVacancyCountsByDepartment(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionVacancyCountsByYearTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getPositionVacancyCountsByYear(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getPositionVacancyCountsByYear(anyInt());

		verify(positionRepository).getPositionVacancyCountsByYear(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionVacancyCountsByYearMonthTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getPositionVacancyCountsByYearMonth(anyInt(), anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getPositionVacancyCountsByYearMonth(anyInt(), anyInt());
		verify(positionRepository).getPositionVacancyCountsByYearMonth(anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionVacancyCountsByDepartmentAndYearTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getPositionVacancyCountsByDepartmentAndYear(anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getPositionVacancyCountsByDepartmentAndYear(anyInt(),
				anyInt());
		verify(positionRepository).getPositionVacancyCountsByDepartmentAndYear(anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionVacancyCountsByDepartmentYearAndMonthTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(), anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(),
				anyInt(), anyInt());
		verify(positionRepository).getPositionVacancyCountsByDepartmentYearAndMonth(anyInt(), anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionDemandTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getPositionDemand()).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getPositionDemand();

		verify(positionRepository).getPositionDemand();

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getPositionDemandByStartYearTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getPositionDemandByStartYear(anyInt())).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getPositionDemandByStartYear(anyInt());

		verify(positionRepository).getPositionDemandByStartYear(anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getPositionDemandByYearsTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getPositionDemandByYears(anyInt(), anyInt())).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getPositionDemandByYears(anyInt(), anyInt());

		verify(positionRepository).getPositionDemandByYears(anyInt(), anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidatePositionDemandTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getCandidatePositionDemand()).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getCandidatePositionDemand();

		verify(positionRepository).getCandidatePositionDemand();

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidatePositionDemandByStartYearTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getCandidatePositionDemandByStartYear(anyInt())).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getCandidatePositionDemandByStartYear(anyInt());

		verify(positionRepository).getCandidatePositionDemandByStartYear(anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidatePositionDemandByYearsTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getCandidatePositionDemandByYears(anyInt(), anyInt())).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getCandidatePositionDemandByYears(anyInt(),
				anyInt());

		verify(positionRepository).getCandidatePositionDemandByYears(anyInt(), anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidateCountByPositionTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByPosition()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByPosition();

		verify(positionRepository).getCandidateCountByPosition();

		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByPositionAndDepartmentTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByPositionAndDepartment(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByPositionAndDepartment(anyInt());

		verify(positionRepository).getCandidateCountByPositionAndDepartment(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByPositionAndYearTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByPositionAndYear(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByPositionAndYear(anyInt());

		verify(positionRepository).getCandidateCountByPositionAndYear(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByPositionAndTimeTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByPositionAndTime(anyInt(), anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByPositionAndTime(anyInt(), anyInt());
		verify(positionRepository).getCandidateCountByPositionAndTime(anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByPositionAndDepartmentAndYearTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByPositionAndDepartmentAndYear(anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByPositionAndDepartmentAndYear(anyInt(),
				anyInt());
		verify(positionRepository).getCandidateCountByPositionAndDepartmentAndYear(anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByPositionAndDepartmentAndTimeTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByPositionAndDepartmentAndTime(anyInt(), anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByPositionAndDepartmentAndTime(anyInt(),
				anyInt(), anyInt());
		verify(positionRepository).getCandidateCountByPositionAndDepartmentAndTime(anyInt(), anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByActiveVacanciesTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByActiveVacancies()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByActiveVacancies();

		verify(positionRepository).getCandidateCountByActiveVacancies();

		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByUrgentVacanciesTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByUrgentVacancies()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByUrgentVacancies();

		verify(positionRepository).getCandidateCountByUrgentVacancies();

		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByActiveVacanciesAndDepartmentTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByActiveVacanciesAndDepartment(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByActiveVacanciesAndDepartment(anyInt());
		verify(positionRepository).getCandidateCountByActiveVacanciesAndDepartment(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountByUrgentVacanciesAndDepartmentTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(positionRepository.getCandidateCountByUrgentVacanciesAndDepartment(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = positionRepository.getCandidateCountByUrgentVacanciesAndDepartment(anyInt());
		verify(positionRepository).getCandidateCountByUrgentVacanciesAndDepartment(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getPositionDemandForInterviewerTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getPositionDemandForInterviewer(anyInt())).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getPositionDemandForInterviewer(anyInt());

		verify(positionRepository).getPositionDemandForInterviewer(anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getPositionDemandByStartYearForInterviewerTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getPositionDemandByStartYearForInterviewer(anyInt(), anyInt()))
				.thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getPositionDemandByStartYearForInterviewer(anyInt(),
				anyInt());

		verify(positionRepository).getPositionDemandByStartYearForInterviewer(anyInt(), anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getPositionDemandByYearsForInterviewerTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getPositionDemandByYearsForInterviewer(anyInt(), anyInt(), anyInt()))
				.thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getPositionDemandByYearsForInterviewer(anyInt(),
				anyInt(), anyInt());

		verify(positionRepository).getPositionDemandByYearsForInterviewer(anyInt(), anyInt(), anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidatePositionDemandForInterviewerTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getCandidatePositionDemandForInterviewer(anyInt())).thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getCandidatePositionDemandForInterviewer(anyInt());

		verify(positionRepository).getCandidatePositionDemandForInterviewer(anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidateTrendByStartYearForInterviewerTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getCandidateTrendByStartYearForInterviewer(anyInt(), anyInt()))
				.thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getCandidateTrendByStartYearForInterviewer(anyInt(),
				anyInt());

		verify(positionRepository).getCandidateTrendByStartYearForInterviewer(anyInt(), anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

	@Test
	public void getCandidateTrendByYearsForInterviewerTest() {
		Map<String, Object> result1 = new HashMap<>();
		result1.put("positionName", "Java Developer");
		result1.put("year", 2023);
		result1.put("count", 10);

		List<Map<String, Object>> expectedResult = new ArrayList<>();
		expectedResult.add(result1);

		when(positionRepository.getCandidateTrendByYearsForInterviewer(anyInt(), anyInt(), anyInt()))
				.thenReturn(expectedResult);
		List<Map<String, Object>> actualResult = positionRepository.getCandidateTrendByYearsForInterviewer(anyInt(),
				anyInt(), anyInt());

		verify(positionRepository).getCandidateTrendByYearsForInterviewer(anyInt(), anyInt(), anyInt());

		assertEquals(expectedResult.size(), actualResult.size());
		assertEquals(expectedResult.get(0).get("positionName"), actualResult.get(0).get("positionName"));
		assertEquals(expectedResult.get(0).get("year"), actualResult.get(0).get("year"));
		assertEquals(expectedResult.get(0).get("count"), actualResult.get(0).get("count"));
	}

}
