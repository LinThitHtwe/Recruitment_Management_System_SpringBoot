package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.dto.VacancyForInterviewDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;

@SpringBootTest
public class VacancyRepositoryTest {
	@Mock
	private VacancyRepository vacancyRepository;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private PositionRepositoryTest positionRepository;

	@Test
	public void findByUrgentTrueTest() {
		Vacancy vacancy = new Vacancy();
		vacancy.setId(1);
		vacancy.setUrgent(true);

		List<Vacancy> vacancyList = new ArrayList<Vacancy>();
		vacancyList.add(vacancy);

		when(vacancyRepository.findByUrgentTrue()).thenReturn(vacancyList);
		List<Vacancy> result = vacancyRepository.findByUrgentTrue();

		assertEquals(vacancyList, result);
	}

	@Test
	public void findByActiveTrueTest() {
		Vacancy vacancy = new Vacancy();
		vacancy.setId(1);
		vacancy.setActive(true);

		List<Vacancy> vacancyList = new ArrayList<Vacancy>();
		vacancyList.add(vacancy);

		when(vacancyRepository.findByActiveTrue()).thenReturn(vacancyList);
		List<Vacancy> result = vacancyRepository.findByActiveTrue();

		assertEquals(vacancyList, result);
	}

	@Test
	public void getMinYearTest() {
		int minYear = 2020;
		when(vacancyRepository.getMinYear()).thenReturn(minYear);
		int result = vacancyRepository.getMinYear();

		assertEquals(minYear, result);
	}

	@Test
	public void getAllVacancyCountTest() {
		long count = 20L;
		when(vacancyRepository.getAllVacancyCount()).thenReturn(count);
		long result = vacancyRepository.getAllVacancyCount();

		assertEquals(count, result);
	}

	@Test
	public void getAllActiveVacancyCountTest() {
		int count = 20;
		when(vacancyRepository.getAllActiveVacancyCount()).thenReturn(count);
		int result = vacancyRepository.getAllActiveVacancyCount();

		assertEquals(count, result);
	}

	@Test
	public void getAllUrgentVacancyCountTest() {
		int count = 20;
		when(vacancyRepository.getAllUrgentVacancyCount()).thenReturn(count);
		int result = vacancyRepository.getAllUrgentVacancyCount();

		assertEquals(count, result);
	}

	@Test
	public void getAllVacancyCountByYearTest() {
		long count = 20L;
		when(vacancyRepository.getAllVacancyCountByYear(anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllVacancyCountByYear(anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllActiveVacancyCountByYearTest() {
		int count = 20;
		when(vacancyRepository.getAllActiveVacancyCountByYear(anyInt())).thenReturn(count);
		int result = vacancyRepository.getAllActiveVacancyCountByYear(anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllUrgentVacancyCountByYearTest() {
		int count = 20;
		when(vacancyRepository.getAllUrgentVacancyCountByYear(anyInt())).thenReturn(count);
		int result = vacancyRepository.getAllUrgentVacancyCountByYear(anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllVacancyCountForDepartmentTest() {
		long count = 20L;
		when(vacancyRepository.getAllVacancyCountForDepartment(anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllVacancyCountForDepartment(anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllActiveVacancyCountForDepartmentTest() {
		int count = 20;
		when(vacancyRepository.getAllActiveVacancyCountForDepartment(anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllActiveVacancyCountForDepartment(anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllUrgentVacancyCountForDepartmentTest() {
		int count = 20;
		when(vacancyRepository.getAllUrgentVacancyCountForDepartment(anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllUrgentVacancyCountForDepartment(anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllVacancyCountByYearForDepartmentTest() {
		long count = 20L;
		when(vacancyRepository.getAllVacancyCountByYearForDepartment(anyInt(), anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllVacancyCountByYearForDepartment(anyInt(), anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllActiveVacancyCountByYearForDepartmentTest() {
		int count = 20;
		when(vacancyRepository.getAllActiveVacancyCountByYearForDepartment(anyInt(), anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllActiveVacancyCountByYearForDepartment(anyInt(), anyInt());

		assertEquals(count, result);
	}

	@Test
	public void getAllUrgentVacancyCountByYearForDepartmentTest() {
		int count = 20;
		when(vacancyRepository.getAllUrgentVacancyCountByYearForDepartment(anyInt(), anyInt())).thenReturn(count);
		long result = vacancyRepository.getAllUrgentVacancyCountByYearForDepartment(anyInt(), anyInt());

		assertEquals(count, result);
	}

	// for vacancyList for interview add
	@Test
	public void getVacanciesForInterviewTest() {
		List<VacancyForInterviewDTO> expectedResults = new ArrayList<>();
		Department department = new Department();
		department.setId(1);
		Position position = new Position();
		position.setId(1);

		VacancyForInterviewDTO vacancy = new VacancyForInterviewDTO();
		expectedResults.add(new VacancyForInterviewDTO(5, position, department, LocalDate.now(), LocalDate.now()));

		when(vacancyRepository.getVacanciesForInterview()).thenReturn(expectedResults);

		List<VacancyForInterviewDTO> actualResults = vacancyRepository.getVacanciesForInterview();
		verify(vacancyRepository).getVacanciesForInterview();
		assertEquals(expectedResults.size(), actualResults.size());

		assertEquals(expectedResults.get(0).getDepartment().getId(), actualResults.get(0).getDepartment().getId());
		assertEquals(expectedResults.get(0).getPosition().getId(), actualResults.get(0).getPosition().getId());
	}

	// for chart start
	@Test
	public void getCandidateCountsForSelectionTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(vacancyRepository.getCandidateCountsForSelection()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = vacancyRepository.getCandidateCountsForSelection();
		verify(vacancyRepository).getCandidateCountsForSelection();
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountsForInterviewsTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(vacancyRepository.getCandidateCountsForInterviews()).thenReturn(expectedResults);

		List<ChartDTO> actualResults = vacancyRepository.getCandidateCountsForInterviews();
		verify(vacancyRepository).getCandidateCountsForInterviews();
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountsByVacancyIdForSelectionTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(vacancyRepository.getCandidateCountsByVacancyIdForSelection(anyLong())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = vacancyRepository.getCandidateCountsByVacancyIdForSelection(anyLong());
		verify(vacancyRepository).getCandidateCountsByVacancyIdForSelection(anyLong());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountsByVacancyIdForInterviewsTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(vacancyRepository.getCandidateCountsByVacancyIdForInterviews(anyLong())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = vacancyRepository.getCandidateCountsByVacancyIdForInterviews(anyLong());
		verify(vacancyRepository).getCandidateCountsByVacancyIdForInterviews(anyLong());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountsForSelectionForInterviewerTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(vacancyRepository.getCandidateCountsForSelectionForInterviewer(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = vacancyRepository.getCandidateCountsForSelectionForInterviewer(anyInt());
		verify(vacancyRepository).getCandidateCountsForSelectionForInterviewer(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void getCandidateCountsForInterviewsForInterviewerTest() {
		List<ChartDTO> expectedResults = new ArrayList<>();
		expectedResults.add(new ChartDTO("Java Developer", 5));
		expectedResults.add(new ChartDTO("Android Developer", 10));
		when(vacancyRepository.getCandidateCountsForInterviewsForInterviewer(anyInt())).thenReturn(expectedResults);

		List<ChartDTO> actualResults = vacancyRepository.getCandidateCountsForInterviewsForInterviewer(anyInt());
		verify(vacancyRepository).getCandidateCountsForInterviewsForInterviewer(anyInt());
		assertEquals(expectedResults.size(), actualResults.size());
		for (int i = 0; i < expectedResults.size(); i++) {
			assertEquals(expectedResults.get(i).getName(), actualResults.get(i).getName());
			assertEquals(expectedResults.get(i).getCount(), actualResults.get(i).getCount());
		}
	}

	@Test
	public void findByDepartmentAndPositionTest() {
		List<Vacancy> expectedResults = new ArrayList<>();
		Vacancy vacancy = new Vacancy();
		Department department = new Department();
		department.setId(1);
		Position position = new Position();
		position.setId(1);
		vacancy.setPosition(position);
		vacancy.setDepartment(department);

		expectedResults.add(vacancy);
		when(vacancyRepository.findByDepartmentAndPosition(department, position)).thenReturn(expectedResults);

		List<Vacancy> actualResults = vacancyRepository.findByDepartmentAndPosition(department, position);
		verify(vacancyRepository).findByDepartmentAndPosition(department, position);
		assertEquals(expectedResults.size(), actualResults.size());

		assertEquals(expectedResults.get(0).getDepartment().getId(), actualResults.get(0).getDepartment().getId());
		assertEquals(expectedResults.get(0).getPosition().getId(), actualResults.get(0).getPosition().getId());
	}

	// for chart end

}
