package com.ace.job.recruitment.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.dto.VacancyForInterviewDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.NotificationService;
import com.ace.job.recruitment.service.VacancyService;

@Service
public class VacancyServiceImpl implements VacancyService {

	@Autowired
	private VacancyRepository vacancyRepository;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void addVacancyAndNotify(VacancyDto dto, String message) {
		Vacancy vacancy = new Vacancy(dto.getRequirement().trim(), dto.getResponsibility().trim(),
				dto.getDescription().trim(), dto.getPreference().trim(), dto.getStartWorkingDay(),
				dto.getEndWorkingDay(), dto.getStartWorkingHour(), dto.getEndWorkingHour(), dto.getSalary().trim(),
				dto.getCount(), dto.getType(), dto.isReopenStatus(), dto.isReopened(), dto.getCreatedDate(),
				dto.getCreatedTime(), dto.getUpdatedDate(), dto.getUpdatedTime(), dto.getCreatedUserId(),
				dto.getUpdatedUserId(), dto.getDueDate(), dto.isActive(), dto.isUrgent(), dto.getReopenDate(),
				dto.getReopenTime(), dto.getDepartment(), dto.getCandidates(), dto.getPosition());
		vacancyRepository.save(vacancy);
		notificationService.createNotification(message, getCurrentDateTime(), vacancy);
	}

	@Override
	public void editVacancyAndNotify(VacancyDto dto, String message) {
		Vacancy vacancy = new Vacancy(dto.getId(), dto.getRequirement().trim(), dto.getResponsibility().trim(),
				dto.getDescription().trim(), dto.getPreference().trim(), dto.getStartWorkingDay(),
				dto.getEndWorkingDay(), dto.getStartWorkingHour(), dto.getEndWorkingHour(), dto.getSalary().trim(),
				dto.getCount(), dto.getType(), dto.isReopenStatus(), dto.isReopened(), dto.getCreatedDate(),
				dto.getCreatedTime(), dto.getUpdatedDate(), dto.getUpdatedTime(), dto.getCreatedUserId(),
				dto.getUpdatedUserId(), dto.getDueDate(), dto.isActive(), dto.isUrgent(), dto.getReopenDate(),
				dto.getReopenTime(), dto.getDepartment(), dto.getCandidates(), dto.getPosition());
		vacancyRepository.save(vacancy);
		notificationService.createNotification(message, getCurrentDateTime(), vacancy);
	}

	@Override
	public List<Vacancy> getAllVacancy() {
		List<Vacancy> list = (List<Vacancy>) vacancyRepository.findAll();
		return list;
	}

	@Override
	public List<Vacancy> getUrgentVacancy() {
		List<Vacancy> list = (List<Vacancy>) vacancyRepository.findByUrgentTrue();
		return list;
	}

	@Override
	public VacancyDto getVacancyById(long id) {
		Vacancy vacancy = vacancyRepository.findById(id).get();
		VacancyDto dto = new VacancyDto(vacancy.getId(), vacancy.getRequirement(), vacancy.getResponsibility(),
				vacancy.getDescription(), vacancy.getPreference(), vacancy.getStartWorkingDay(),
				vacancy.getEndWorkingDay(), vacancy.getStartWorkingHour(), vacancy.getEndWorkingHour(),
				vacancy.getSalary(), vacancy.getCount(), vacancy.getType(), vacancy.isReopenStatus(),
				vacancy.isReopened(), vacancy.getCreatedDate(), vacancy.getCreatedTime(), vacancy.getUpdatedDate(),
				vacancy.getUpdatedTime(), vacancy.getCreatedUserId(), vacancy.getUpdatedUserId(), vacancy.getDueDate(),
				vacancy.isActive(), vacancy.isUrgent(), vacancy.getReopenDate(), vacancy.getReopenTime(),
				vacancy.getDepartment(), vacancy.getCandidates(), vacancy.getPosition());
		return dto;
	}

	@Override
	public LocalDateTime getCurrentDateTime() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");
		String formattedDateTime = currentDateTime.format(formatter);
		return LocalDateTime.parse(formattedDateTime, formatter);
	}

	@Override
	public LocalDate getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = currentDate.format(formatter);
		return LocalDate.parse(formattedDate, formatter);
	}

	@Override
	public LocalTime getCurrentTime() {
		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm");
		String formattedTime = currentTime.format(formatter);
		return LocalTime.parse(formattedTime, formatter);
	}

	@Override
	public String convertToFormattedTime(String timeString) {
		LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
		return localTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
	}

	@Override
	public void markVacanciesAsInactiveAfter30Days() {
		LocalDate currentDate = LocalDate.now();
		List<Vacancy> activeVacancies = vacancyRepository.findByActiveTrue();

		for (Vacancy vacancy : activeVacancies) {
			if (vacancy.getCreatedDate().isBefore(currentDate)) {
				vacancy.setActive(false);
				vacancyRepository.save(vacancy);
			}
		}
	}

	@Override
	public int calculateDaysLeft(LocalDate createdDate) {
		LocalDate currentDate = LocalDate.now();
		LocalDate expirationDate = createdDate.plusDays(30);
		return (int) ChronoUnit.DAYS.between(currentDate, expirationDate);
	}

	@Override
	public List<String> trimArray(String[] array) {
		List<String> trimmedArray = new ArrayList<>();
		for (String index : array) {
			if (!index.isEmpty()) {
				trimmedArray.add(index);
			}
		}
		return trimmedArray;
	}

	@Override
	public void updateVacancy(VacancyDto dto) {
		Vacancy vacancy = new Vacancy(dto.getId(), dto.getRequirement().trim(), dto.getResponsibility().trim(),
				dto.getDescription().trim(), dto.getPreference().trim(), dto.getStartWorkingDay(),
				dto.getEndWorkingDay(), dto.getStartWorkingHour(), dto.getEndWorkingHour(), dto.getSalary().trim(),
				dto.getCount(), dto.getType(), dto.isReopenStatus(), dto.isReopened(), dto.getCreatedDate(),
				dto.getCreatedTime(), dto.getUpdatedDate(), dto.getUpdatedTime(), dto.getCreatedUserId(),
				dto.getUpdatedUserId(), dto.getDueDate(), dto.isActive(), dto.isUrgent(), dto.getReopenDate(),
				dto.getReopenTime(), dto.getDepartment(), dto.getCandidates(), dto.getPosition());
		vacancyRepository.save(vacancy);
	}

	@Override
	public Vacancy getVacancyByIdForInterview(long id) {
		return vacancyRepository.findById(id).get();
	}

	@Override
	public List<VacancyForInterviewDTO> getVacanciesForInterview() {
		return vacancyRepository.getVacanciesForInterview();
	}

	@Override
	public Vacancy getVacancyByIdWithEntity(long id) {
		return vacancyRepository.findById(id).get();
	}

	@Override
	public List<Vacancy> getAllActiveVacancy() {
		return vacancyRepository.findByActiveTrue();
	}

	@Override
	public List<Vacancy> getVacancyByDepartmentAndPosition(Department department, Position position) {
		return vacancyRepository.findByDepartmentAndPosition(department, position);
	}

	@Override
	public DataTablesOutput<Vacancy> getVacancyByDateFilter(@Valid DataTablesInput input, LocalDate startDateFrom,
			LocalDate endDateTo) {
		Specification<Vacancy> vacancyDateRangeSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
				criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDateFrom),
				criteriaBuilder.lessThan(root.get("createdDate"), endDateTo.plusDays(1)),
				criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), endDateTo));
		return vacancyRepository.findAll(input, vacancyDateRangeSpecification);
	}

	public DataTablesOutput<Vacancy> getVacancyByStatus(DataTablesInput input, String status) {
		Specification<Vacancy> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if ("Active".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("active")));
			} else if ("Inactive".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isFalse(root.get("active")));
			} else if ("Urgent".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("urgent")));
			} else if ("Reopen".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.or(criteriaBuilder.isTrue(root.get("reopenStatus")),
								criteriaBuilder.isTrue(root.get("reopened"))));
			}

			return predicate;
		};

		return vacancyRepository.findAll(input, spec);
	}

	@Override
	public DataTablesOutput<Vacancy> getVacancyByDateFilterAndStatus(DataTablesInput input, LocalDate startDateFrom,
			LocalDate endDateTo, String status) {
		Specification<Vacancy> dateFilterSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
				criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDateFrom),
				criteriaBuilder.lessThan(root.get("createdDate"), endDateTo.plusDays(1)),
				criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), endDateTo));

		Specification<Vacancy> statusSpecification = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if ("Active".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("active")));
			} else if ("Inactive".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isFalse(root.get("active")));
			} else if ("Urgent".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("urgent")));
			} else if ("Reopen".equalsIgnoreCase(status)) {
				predicate = criteriaBuilder.and(predicate,
						criteriaBuilder.or(criteriaBuilder.isTrue(root.get("reopenStatus")),
								criteriaBuilder.isTrue(root.get("reopened"))));
			}

			return predicate;
		};

		Specification<Vacancy> combinedSpecification = dateFilterSpecification.and(statusSpecification);

		return vacancyRepository.findAll(input, combinedSpecification);
	}

	@Override
	public List<VacancyForInterviewDTO> getVacanciesForInterview(LocalDate threeMonthsAgo) {
		return vacancyRepository.getVacanciesForInterview(threeMonthsAgo);
	}

	@Override
	public List<VacancyForInterviewDTO> getVacanciesForInterviewUpdate(LocalDate threeMonthsAgo, long vacancyId) {
		return vacancyRepository.getVacanciesForInterviewUpdate(threeMonthsAgo, vacancyId);
	}

}