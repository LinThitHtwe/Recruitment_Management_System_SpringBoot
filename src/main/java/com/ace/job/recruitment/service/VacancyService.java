package com.ace.job.recruitment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.dto.VacancyForInterviewDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;

public interface VacancyService {

	void addVacancyAndNotify(VacancyDto dto, String message);

	void editVacancyAndNotify(VacancyDto dto, String message);

	void updateVacancy(VacancyDto dto);

	List<Vacancy> getAllVacancy();

	List<Vacancy> getUrgentVacancy();

	VacancyDto getVacancyById(long id);

	LocalDateTime getCurrentDateTime();

	LocalDate getCurrentDate();

	LocalTime getCurrentTime();

	String convertToFormattedTime(String timeString);

	void markVacanciesAsInactiveAfter30Days();

	int calculateDaysLeft(LocalDate createdDate);

	List<String> trimArray(String[] array);

	Vacancy getVacancyByIdForInterview(long id);

	List<VacancyForInterviewDTO> getVacanciesForInterview();

	Vacancy getVacancyByIdWithEntity(long id);

	List<Vacancy> getAllActiveVacancy();

	DataTablesOutput<Vacancy> getVacancyByDateFilter(@Valid DataTablesInput input, LocalDate startDateFrom,
			LocalDate endDateTo);

	DataTablesOutput<Vacancy> getVacancyByStatus(@Valid DataTablesInput input, String status);

	DataTablesOutput<Vacancy> getVacancyByDateFilterAndStatus(@Valid DataTablesInput input, LocalDate startDateFrom,
			LocalDate endDateTo, String status);

	// for chart
	List<Vacancy> getVacancyByDepartmentAndPosition(Department department, Position position);

	List<VacancyForInterviewDTO> getVacanciesForInterview(LocalDate threeMonthsAgo);

	List<VacancyForInterviewDTO> getVacanciesForInterviewUpdate(LocalDate threeMonthsAgo, long vacancyId);
}