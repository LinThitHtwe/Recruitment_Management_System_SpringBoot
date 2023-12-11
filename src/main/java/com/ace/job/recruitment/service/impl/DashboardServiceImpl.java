package com.ace.job.recruitment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.dto.DashboardCountDTO;
import com.ace.job.recruitment.repository.CandidateRepository;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	VacancyRepository vacancyRepository;

	@Autowired
	CandidateRepository candidateRepository;

	@Override
	public int getMinYear() {
		return vacancyRepository.getMinYear();
	}

	@Override
	public DashboardCountDTO getDashboardCounts(int year) {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		if (year == 0) {
			dashboardCountDTO.setVacancyCount(vacancyRepository.getAllVacancyCount());
			dashboardCountDTO.setActiveVacancyCount(vacancyRepository.getAllActiveVacancyCount());
			dashboardCountDTO.setUrgentVacancyCount(vacancyRepository.getAllUrgentVacancyCount());
			dashboardCountDTO.setCandidateCount(candidateRepository.getAllCandidateCount());
			dashboardCountDTO.setEmployedCandidateCount(candidateRepository.getAllEmployedCandidateCount());
		}
		if (year > 0) {
			dashboardCountDTO.setVacancyCount(vacancyRepository.getAllVacancyCountByYear(year));
			dashboardCountDTO.setActiveVacancyCount(vacancyRepository.getAllActiveVacancyCountByYear(year));
			dashboardCountDTO.setUrgentVacancyCount(vacancyRepository.getAllUrgentVacancyCountByYear(year));
			dashboardCountDTO.setCandidateCount(candidateRepository.getAllCandidateCountByYear(year));
			dashboardCountDTO.setEmployedCandidateCount(candidateRepository.getAllEmployedCandidateCountByYear(year));
		}
		return dashboardCountDTO;
	}

	@Override
	public DashboardCountDTO getDashboardCountsForDepartment(int year, int department) {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		if (year == 0) {
			dashboardCountDTO.setVacancyCount(vacancyRepository.getAllVacancyCountForDepartment(department));
			dashboardCountDTO
					.setActiveVacancyCount(vacancyRepository.getAllActiveVacancyCountForDepartment(department));
			dashboardCountDTO
					.setUrgentVacancyCount(vacancyRepository.getAllUrgentVacancyCountForDepartment(department));
			dashboardCountDTO.setCandidateCount(candidateRepository.getAllCandidateCountForDepartment(department));
			dashboardCountDTO.setEmployedCandidateCount(
					candidateRepository.getAllEmployedCandidateCountForDepartment(department));
		}
		if (year > 0) {
			dashboardCountDTO
					.setVacancyCount(vacancyRepository.getAllVacancyCountByYearForDepartment(year, department));
			dashboardCountDTO.setActiveVacancyCount(
					vacancyRepository.getAllActiveVacancyCountByYearForDepartment(year, department));
			dashboardCountDTO.setUrgentVacancyCount(
					vacancyRepository.getAllUrgentVacancyCountByYearForDepartment(year, department));
			dashboardCountDTO
					.setCandidateCount(candidateRepository.getAllCandidateCountByYearForDepartment(year, department));
			dashboardCountDTO.setEmployedCandidateCount(
					candidateRepository.getAllEmployedCandidateCountByYearForDepartment(year, department));
		}
		return dashboardCountDTO;
	}

}