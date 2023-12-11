package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.dto.DashboardCountDTO;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.impl.DashboardServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

	@Mock
	VacancyRepository vacancyRepository;

	@Mock
	CandidateRepository candidateRepository;

	@InjectMocks
	private DashboardServiceImpl dashboardServiceImpl;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getMinYearTest() {
		int minYear = 2023;

		when(vacancyRepository.getMinYear()).thenReturn(minYear);
		int result = dashboardServiceImpl.getMinYear();
		assertEquals(result, minYear);
	}

	@Test
	public void getDashboardCountsYearZeroTest() {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		dashboardCountDTO.setVacancyCount(10);

		when(vacancyRepository.getAllVacancyCount()).thenReturn(10L);
		when(vacancyRepository.getAllActiveVacancyCount()).thenReturn(0);
		when(vacancyRepository.getAllUrgentVacancyCount()).thenReturn(0);
		when(candidateRepository.getAllCandidateCount()).thenReturn(0L);
		when(candidateRepository.getAllEmployedCandidateCount()).thenReturn(0L);

		DashboardCountDTO result = dashboardServiceImpl.getDashboardCounts(0);
		assertEquals(dashboardCountDTO.getVacancyCount(), result.getVacancyCount());
		assertEquals(dashboardCountDTO.getActiveVacancyCount(), result.getActiveVacancyCount());
		assertEquals(dashboardCountDTO.getUrgentVacancyCount(), result.getUrgentVacancyCount());
		assertEquals(dashboardCountDTO.getCandidateCount(), result.getCandidateCount());
		assertEquals(dashboardCountDTO.getEmployedCandidateCount(), result.getEmployedCandidateCount());

	}

	@Test
	public void getDashboardCountsYearNotZeroTest() {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		dashboardCountDTO.setVacancyCount(10);

		when(vacancyRepository.getAllVacancyCountByYear(2023)).thenReturn(10L);
		when(vacancyRepository.getAllActiveVacancyCountByYear(2023)).thenReturn(0);
		when(vacancyRepository.getAllUrgentVacancyCountByYear(2023)).thenReturn(0);
		when(candidateRepository.getAllCandidateCountByYear(2023)).thenReturn(0L);
		when(candidateRepository.getAllEmployedCandidateCountByYear(2023)).thenReturn(0L);

		DashboardCountDTO result = dashboardServiceImpl.getDashboardCounts(2023);
		assertEquals(dashboardCountDTO.getVacancyCount(), result.getVacancyCount());
		assertEquals(dashboardCountDTO.getActiveVacancyCount(), result.getActiveVacancyCount());
		assertEquals(dashboardCountDTO.getUrgentVacancyCount(), result.getUrgentVacancyCount());
		assertEquals(dashboardCountDTO.getCandidateCount(), result.getCandidateCount());
		assertEquals(dashboardCountDTO.getEmployedCandidateCount(), result.getEmployedCandidateCount());

	}

	@Test
	public void getDashboardCountsForDepartmentYearZeroTest() {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		dashboardCountDTO.setVacancyCount(10);
		int year = 0;
		int department = 1;

		when(vacancyRepository.getAllVacancyCountForDepartment(department)).thenReturn(10L);
		when(vacancyRepository.getAllActiveVacancyCountForDepartment(department)).thenReturn(0);
		when(vacancyRepository.getAllUrgentVacancyCountForDepartment(department)).thenReturn(0);
		when(candidateRepository.getAllCandidateCountForDepartment(department)).thenReturn(0L);
		when(candidateRepository.getAllEmployedCandidateCountForDepartment(department)).thenReturn(0L);

		DashboardCountDTO result = dashboardServiceImpl.getDashboardCountsForDepartment(year, department);
		assertEquals(dashboardCountDTO.getVacancyCount(), result.getVacancyCount());
		assertEquals(dashboardCountDTO.getActiveVacancyCount(), result.getActiveVacancyCount());
		assertEquals(dashboardCountDTO.getUrgentVacancyCount(), result.getUrgentVacancyCount());
		assertEquals(dashboardCountDTO.getCandidateCount(), result.getCandidateCount());
		assertEquals(dashboardCountDTO.getEmployedCandidateCount(), result.getEmployedCandidateCount());

	}

	@Test
	public void getDashboardCountsForDepartmentYearNotZeroTest() {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		dashboardCountDTO.setVacancyCount(10);
		int year = 2023;
		int department = 1;

		when(vacancyRepository.getAllVacancyCountByYearForDepartment(year, department)).thenReturn(10L);
		when(vacancyRepository.getAllActiveVacancyCountByYearForDepartment(year, department)).thenReturn(0);
		when(vacancyRepository.getAllUrgentVacancyCountByYearForDepartment(year, department)).thenReturn(0);
		when(candidateRepository.getAllCandidateCountByYearForDepartment(year, department)).thenReturn(0L);
		when(candidateRepository.getAllEmployedCandidateCountByYearForDepartment(year, department)).thenReturn(0L);

		DashboardCountDTO result = dashboardServiceImpl.getDashboardCountsForDepartment(year, department);
		assertEquals(dashboardCountDTO.getVacancyCount(), result.getVacancyCount());
		assertEquals(dashboardCountDTO.getActiveVacancyCount(), result.getActiveVacancyCount());
		assertEquals(dashboardCountDTO.getUrgentVacancyCount(), result.getUrgentVacancyCount());
		assertEquals(dashboardCountDTO.getCandidateCount(), result.getCandidateCount());
		assertEquals(dashboardCountDTO.getEmployedCandidateCount(), result.getEmployedCandidateCount());

	}

}
