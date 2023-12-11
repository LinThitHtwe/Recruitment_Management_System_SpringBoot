package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Department;

@SpringBootTest
public class DepartmentRepositoryTest {
	@Mock
	private DepartmentRepository departmentRepository;

	@Test
	public void findByNameTest() {
		Department department = new Department();
		department.setName("Banking");
		Department expectedResult = department;
		when(departmentRepository.findByName("Banking")).thenReturn(expectedResult);

		Department actualResult = departmentRepository.findByName("Banking");
		verify(departmentRepository).findByName("Banking");
		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void testFindByNameAndIdNot() {
		Department department = new Department();
		department.setId(1);
		department.setName("Banking");

		when(departmentRepository.findByNameAndIdNot("Banking", 1)).thenReturn(null);
		Department result = departmentRepository.findByNameAndIdNot("Banking", 1);
		verify(departmentRepository).findByNameAndIdNot("Banking", 1);
		assertEquals(result, null);
	}

	@Test
	public void getDepartmentWithVacancyCountTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(departmentRepository.getDepartmentWithVacancyCount()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = departmentRepository.getDepartmentWithVacancyCount();
		verify(departmentRepository).getDepartmentWithVacancyCount();
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getDepartmentVacancyCountsByPositionTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(departmentRepository.getDepartmentVacancyCountsByPosition(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = departmentRepository.getDepartmentVacancyCountsByPosition(anyInt());
		verify(departmentRepository).getDepartmentVacancyCountsByPosition(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getDepartmentVacancyCountsByYearTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(departmentRepository.getDepartmentVacancyCountsByYear(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = departmentRepository.getDepartmentVacancyCountsByYear(anyInt());
		verify(departmentRepository).getDepartmentVacancyCountsByYear(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getDepartmentVacancyCountsByYearMonthTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(departmentRepository.getDepartmentVacancyCountsByYearMonth(anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = departmentRepository.getDepartmentVacancyCountsByYearMonth(anyInt(), anyInt());
		verify(departmentRepository).getDepartmentVacancyCountsByYearMonth(anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getDepartmentVacancyCountsByPositionAndYearTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(departmentRepository.getDepartmentVacancyCountsByPositionAndYear(anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = departmentRepository.getDepartmentVacancyCountsByPositionAndYear(anyInt(),
				anyInt());
		verify(departmentRepository).getDepartmentVacancyCountsByPositionAndYear(anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getDepartmentVacancyCountsByPositionYearAndMonthTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(departmentRepository.getDepartmentVacancyCountsByPositionYearAndMonth(anyInt(), anyInt(), anyInt()))
				.thenReturn(expectedResults);

		List<ChartDTO> actualResults = departmentRepository.getDepartmentVacancyCountsByPositionYearAndMonth(anyInt(),
				anyInt(), anyInt());
		verify(departmentRepository).getDepartmentVacancyCountsByPositionYearAndMonth(anyInt(), anyInt(), anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

}
