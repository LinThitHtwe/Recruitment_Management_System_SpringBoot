package com.ace.job.recruitment.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.dto.InterviewDTO;
import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.dto.VacancyForInterviewDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.InterviewService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.UserService;
import com.ace.job.recruitment.service.VacancyService;

@Controller
@RequestMapping("/hr")
public class InterviewController {
	@Autowired
	InterviewService interviewService;
	@Autowired
	VacancyService vacancyService;
	@Autowired
	UserService userService;
	@Autowired
	PositionService positionService;
	@Autowired
	DepartmentService departmentService;

	// to fill vacancy list to choose when add interview
	@PostMapping("/vacancies-for-interview")
	public ResponseEntity<List<VacancyForInterviewDTO>> chooseVacancy() {
		List<VacancyForInterviewDTO> vacancyList = new ArrayList<VacancyForInterviewDTO>();

		LocalDate currentDate = LocalDate.now();
		LocalDate threeMonthsAgo = currentDate.minus(3, ChronoUnit.MONTHS);
		vacancyList = vacancyService.getVacanciesForInterview(threeMonthsAgo);

		return ResponseEntity.ok(vacancyList);
	}

	// to fill vacancy list to choose when update interview
	@PostMapping("/vacancies-for-interview-update")
	public ResponseEntity<List<VacancyForInterviewDTO>> chooseVacancyForInterviewUpdate(
			@RequestParam("interviewId") Long interviewId) {
		Interview interview = interviewService.getInterviewById(interviewId);
		List<VacancyForInterviewDTO> vacancyList = new ArrayList<VacancyForInterviewDTO>();

		LocalDate currentDate = LocalDate.now();
		LocalDate threeMonthsAgo = currentDate.minus(3, ChronoUnit.MONTHS);
		vacancyList = vacancyService.getVacanciesForInterviewUpdate(threeMonthsAgo, interview.getVacancy().getId());

		return ResponseEntity.ok(vacancyList);
	}

	// get userids from the department of the chosen vacancy
	@GetMapping("/choose-interviwers")
	public ResponseEntity<List<User>> chooseInterviewer(@RequestParam("vacancyId") long vacancy_id) {

		// get vacancy info with the chosen vacancy id
		VacancyDto chosenVacancy = vacancyService.getVacancyById(vacancy_id);
		Department department = chosenVacancy.getDepartment();
		List<User> userList = new ArrayList<User>();
		userList = userService.getInterviewers(department.getId());

		return ResponseEntity.ok(userList);

	}

	@GetMapping("/check-interview-add-duplication")
	public ResponseEntity<String> checkInterviewDuplicationForAdd(@RequestParam("type") String type,
			@RequestParam("stage") int stage, @RequestParam("vacancyId") long vacancyId) {
		String statusMsg = null;
		List<Interview> interviewList = interviewService.getInterviewForDuplication(type, stage, vacancyId);
		if (!interviewList.isEmpty()) {
			statusMsg = "duplicated";
		} else {
			statusMsg = "proceed";
		}
		return ResponseEntity.ok(statusMsg);
	}

	@GetMapping("/check-interview-update-duplication")
	public ResponseEntity<String> checkInterviewDuplicationForUpdate(@RequestParam("type") String type,
			@RequestParam("stage") int stage, @RequestParam("vacancyId") long vacancyId,
			@RequestParam("interviewId") long interviewId) {
		String statusMsg = null;
		List<Interview> interviewList = interviewService.getInterviewForDuplicationUpdate(type, stage, vacancyId,
				interviewId);
		if (!interviewList.isEmpty()) {
			statusMsg = "duplicated";
		} else {
			statusMsg = "proceed";
		}
		System.out.println("statusMsg" + statusMsg);
		return ResponseEntity.ok(statusMsg);
	}

	@PostMapping("/add-interview")
	public ResponseEntity<String> addInterviewPost(@ModelAttribute("interviewDTO") InterviewDTO interviewDTO,

			@RequestParam("vacancyId") long vacancyId, @RequestParam("interviewerId") Integer[] interviewerId,
			Authentication authentication) {
		String location = null;
		LocalDate endDate = null;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(interviewDTO.getStartDate(), dateFormatter);
		if (interviewDTO.getLocation() != null && !interviewDTO.getLocation().equals("")) {
			location = interviewDTO.getLocation();
		}
		if (interviewDTO.getEndDate() != null && !interviewDTO.getEndDate().equals("")) {
			endDate = LocalDate.parse(interviewDTO.getEndDate(), dateFormatter);
		}
		Interview interview = new Interview(startDate, endDate, interviewDTO.getStartTime(), interviewDTO.getEndTime(),
				interviewDTO.getType(), location, interviewDTO.getStage());

		// status
		interview.setStatus(true);

		// format datetime
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		interview.setCreatedDateTime(formattedDateTime);

		// created user id
		interview.setCreatedUserId(interviewService.getCurrentUserId(authentication));

		// for vacancy
		Vacancy vacancy = vacancyService.getVacancyByIdForInterview(vacancyId);
		interview.setVacancy(vacancy);

		// for interviewers
		List<User> userList = new ArrayList<>();
		for (int userId : interviewerId) {
			User user = userService.getUserById(userId);
			userList.add(user);
		}

		interview.setUsers(userList);

		// store in db
		interviewService.addInterview(interview);

		return ResponseEntity.ok().build();
	}

	// show list of interview
	@GetMapping("/interview-list")
	public String showInterviewList(Model model) {
		List<Interview> interviewList = new ArrayList<Interview>();
		interviewList = interviewService.getAllInterviews();
		model.addAttribute("interviewList", interviewList);
		return "interview/interview-list";
	}

	@GetMapping("/interviews-data")
	@ResponseBody
	public DataTablesOutput<Interview> getDataTableData(@Valid DataTablesInput input,
			@RequestParam("startDateFrom") Optional<String> startDateFrom,
			@RequestParam("endDateTo") Optional<String> endDateTo) {
		if (startDateFrom.isPresent() || endDateTo.isPresent()) {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate startDate = startDateFrom.map(s -> LocalDate.parse(s, dateFormatter)).orElse(null);
			LocalDate endDate = endDateTo.map(s -> LocalDate.parse(s, dateFormatter)).orElse(null);
			return interviewService.getDataTableDataDate(input, startDate, endDate);
		} else {
			return interviewService.getDataTableData(input);
		}
	}

	// show interview details

	@GetMapping("/interview-detail")
	public ResponseEntity<Object> getInterviewDetail(@RequestParam("id") Long id) {
		Interview interview = interviewService.getInterviewById(id);

		if (interview != null) {
			LocalDate startDate = interview.getStartDate();
			if (interview.getEndDate() != null) {
				LocalDate endDate = interview.getEndDate();

				InterviewDTO interviewDetail = new InterviewDTO(interview.getId(), interview.getStartDate().toString(),
						interview.getEndDate().toString(), interview.getStartTime(), interview.getEndTime(),
						interview.getType(), interview.getLocation(), interview.getStage(), interview.isStatus(),
						interview.getCreatedUserId(), interview.getCreatedDateTime(), interview.getUpdatedUserId(),
						interview.getUpdatedDateTime(), interview.getCanceledUserId(), interview.getCanceledDateTime(),
						interview.getVacancy(), interview.getUsers());
				interviewDetail.setCreatedUserName((userService.getUserById(interview.getCreatedUserId())).getName());
				if (interview.getUpdatedUserId() != 0) {
					interviewDetail
							.setUpdatedUserName((userService.getUserById(interview.getUpdatedUserId())).getName());
				}
				if (interview.getCanceledUserId() != 0) {
					interviewDetail
							.setCanceledUsername((userService.getUserById(interview.getCanceledUserId())).getName());
				}
				return ResponseEntity.ok(interviewDetail);
			} else {
				InterviewDTO interviewDetail = new InterviewDTO(interview.getId(), interview.getStartDate().toString(),
						null, interview.getStartTime(), interview.getEndTime(), interview.getType(),
						interview.getLocation(), interview.getStage(), interview.isStatus(),
						interview.getCreatedUserId(), interview.getCreatedDateTime(), interview.getUpdatedUserId(),
						interview.getUpdatedDateTime(), interview.getCanceledUserId(), interview.getCanceledDateTime(),
						interview.getVacancy(), interview.getUsers());
				interviewDetail.setCreatedUserName((userService.getUserById(interview.getCreatedUserId())).getName());
				if (interview.getUpdatedUserId() != 0) {
					interviewDetail
							.setUpdatedUserName((userService.getUserById(interview.getUpdatedUserId())).getName());
				}
				if (interview.getCanceledUserId() != 0) {
					interviewDetail
							.setCanceledUsername((userService.getUserById(interview.getCanceledUserId())).getName());
				}
				return ResponseEntity.ok(interviewDetail);
			}

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// update interview

	// update interview
	@PostMapping("/update-interview")
	public ResponseEntity<String> updateInterview(@ModelAttribute("interviewDTO") InterviewDTO interviewDTO,
			@RequestParam("vacancyId") long vacancyId, @RequestParam("interviewerId") Integer[] interviewerId) {
		String location = null;
		Interview interview = interviewService.getInterviewById(interviewDTO.getId());

		// data from form
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(interviewDTO.getStartDate(), dateFormatter);

		if (interviewDTO.getEndDate() != null && !interviewDTO.getEndDate().equals("")) {
			LocalDate endDate = LocalDate.parse(interviewDTO.getEndDate(), dateFormatter);
			interview.setEndDate(endDate);
		} else {
			interview.setEndDate(null);
		}

		if (interviewDTO.getLocation() != null && !interviewDTO.getLocation().equals("")) {
			location = interviewDTO.getLocation();
		}

		interview.setStartDate(startDate);
		interview.setStartTime(interviewDTO.getStartTime());
		interview.setEndTime(interviewDTO.getEndTime());
		interview.setStage(interviewDTO.getStage());
		interview.setType(interviewDTO.getType());
		if (interview.getType().equalsIgnoreCase("Online")) {
			interview.setLocation(null);
		} else {
			interview.setLocation(location);
		}

		// for vacancy
		Vacancy vacany = vacancyService.getVacancyByIdForInterview(vacancyId);
		interview.setVacancy(vacany);

		// for interviewers
		List<User> userList = new ArrayList<User>();
		for (int userId : interviewerId) {
			User user = userService.getUserById(userId);
			userList.add(user);
		}
		interview.setUsers(userList);

		// format datetime for update datetime
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(dateTimeFormatter);
		interview.setUpdatedDateTime(formattedDateTime);

		// updated user id
		interview.setUpdatedUserId(0);

		// update in db
		interviewService.updateInterview(interview);

		return ResponseEntity.ok().build();

	}

	// cancel interview
	@GetMapping("/cancel-interview")
	public ResponseEntity<String> cancelInterview(@RequestParam("id") Long id, Authentication authentication) {
		Interview interview = interviewService.getInterviewById(id);
		interview.setStatus(false);

		// format datetime for cancel datetime
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(dateTimeFormatter);
		interview.setCanceledDateTime(formattedDateTime);

		// updated user id
		interview.setCanceledUserId(interviewService.getCurrentUserId(authentication));
		interviewService.updateInterview(interview);
		return ResponseEntity.ok().build();

	}

	@GetMapping("/position-for-sort")
	@ResponseBody
	public List<Position> getAllPositions() {
		List<Position> list = positionService.getAllPositions();
		return list;
	}

	@GetMapping("/department-for-sort")
	@ResponseBody
	public List<Department> getAllDepartment() {
		List<Department> list = departmentService.getDepartments();
		return list;
	}
}