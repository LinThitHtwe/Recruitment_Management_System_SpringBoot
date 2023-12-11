package com.ace.job.recruitment.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.dto.InterviewStageAndStatusHistoryDTO;
import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Reviews;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.CandidateInterviewService;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.InterviewService;
import com.ace.job.recruitment.service.NotificationService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.ReviewService;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/interviewer")
public class InterviewerController {

	@Autowired
	CandidateService candidateService;

	@Autowired
	InterviewService interviewService;

	@Autowired
	UserService userService;

	@Autowired
	PositionService positionService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	CandidateInterviewService candidateInterviewService;

	@Autowired
	ReviewService reviewService;

	@Autowired
	NotificationService notificationService;

	public String getCurrentUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				String name = userDetails.getFullName();
				return name;
			}
		}

		return "";
	}

	public int getCurrentInterviewerDepartment() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				int departmentId = userDetails.getDepartmentId();
				return departmentId;
			}
		}

		return -1;
	}

	public int getCurrentDepartmentHeadId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				int id = userDetails.getId();
				return id;
			}
		}

		return -1;
	}

	public String getCurrentUserRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				String role = userDetails.getRole();
				return role;
			}
		}
		return "";
	}

	// Method to show candidate's history
	@GetMapping("/candidate-history/{id}")
	public String candidateHistoryView(@PathVariable("id") long id, Model model) {
		Candidate candidate = candidateService.getCandidateById(id);
		List<CandidateInterview> candidateInterviewLists = candidateInterviewService.getCandidateInterviewById(id);
		List<InterviewStageAndStatusHistoryDTO> interviewStageAndStatusHistoryList = new ArrayList<>();

		for (CandidateInterview candidateInterview : candidateInterviewLists) {
			InterviewStageAndStatusHistoryDTO stageAndStatusHistoryDTO = new InterviewStageAndStatusHistoryDTO();
			stageAndStatusHistoryDTO.setInterviewDate(candidateInterview.getInterviewDate());
			stageAndStatusHistoryDTO.setChangeStatusBy(
					(userService.getUserById(candidateInterview.getInterviewStatusChangedUserId())).getName());
			stageAndStatusHistoryDTO.setInterviewStage("Stage " + candidateInterview.getInterview().getStage());
			stageAndStatusHistoryDTO.setStatus(candidateInterview.getStatus());
			List<String> interviewers = new ArrayList<>();
			for (User user : candidateInterview.getInterview().getUsers()) {
				String interviewer = user.getName();
				interviewers.add(interviewer);
			}
			String interviewersString = String.join(", ", interviewers);
			stageAndStatusHistoryDTO.setInterviewers(interviewersString);

			interviewStageAndStatusHistoryList.add(stageAndStatusHistoryDTO);
		}

		model.addAttribute("candidateInterviewLists", interviewStageAndStatusHistoryList);
		model.addAttribute("candidateName", candidate.getName());
		return "HR/candidate-history";
	}

	@GetMapping("/current-user-role")
	@ResponseBody
	public String getCurrentUserRoleForJS() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				String role = userDetails.getRole();
				return role;
			}
		}
		return "";
	}

	// Method to show Candidate Details
	@GetMapping("/getCandidateInfo")
	public ResponseEntity<Candidate> candidateDetail(@RequestParam("id") long candidateId) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		return ResponseEntity.ok(candidate);
	}

	// Method to return to thymeleaf page
	@GetMapping("/candidates")
	public String candidatesViewHR(Model model) {
		model.addAttribute("interviewerCandidates", true);
		return "interviewer/interviewer_candidate_list";
	}

	// Method to get current user department
	@GetMapping("/current-user-department")
	public int currentUserDepartmentId() {
		return getCurrentInterviewerDepartment();
	}

	// Method to fill candidate data in table
	@GetMapping("/candidate-data")
	@ResponseBody
	public DataTablesOutput<Candidate> getCandidateDatas(@Valid DataTablesInput input,
			@RequestParam(value = "startDateFrom", required = false) String startDateFrom,
			@RequestParam(value = "endDateTo", required = false) String endDateTo,
			@RequestParam(value = "interviewStatus", required = false) String interviewStatus,
			@RequestParam(value = "stage", required = false) String stage) {

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = (startDateFrom != null && !startDateFrom.equalsIgnoreCase("All")
				&& !startDateFrom.equals("")) ? LocalDate.parse(startDateFrom, dateFormatter) : null;
		LocalDate endDate = (endDateTo != null && !endDateTo.equalsIgnoreCase("All") && !endDateTo.equals(""))
				? LocalDate.parse(endDateTo, dateFormatter)
				: null;
		String interviewStatusValue = (interviewStatus != null && !interviewStatus.equalsIgnoreCase("All")
				&& !interviewStatus.equals("")) ? interviewStatus : null;

		Integer stageInteger = null;
		if (stage != null && !stage.equalsIgnoreCase("All") && !stage.equals("")) {
			try {
				stageInteger = Integer.parseInt(stage);
			} catch (NumberFormatException e) {
				System.out.print(e);
			}
		}

		if (startDate != null || endDate != null || interviewStatusValue != null || stageInteger != null) {
			if (getCurrentUserRole().equalsIgnoreCase("Admin")
					|| getCurrentUserRole().equalsIgnoreCase("Default-Admin")) {
				System.out.print("+++++++++++IFIFIFI+++++++");
				return candidateService.getFilteredCandidateForAdminAtInterview(input, getAllCandidateIdsInInterview(),
						interviewStatusValue, startDate, endDate, stageInteger);
			} else {
				System.out.print("++++++++++ELSE+++++++");
				return candidateService.getFilteredCandidateByDepartment(input, getCurrentDepartmentHeadId(),
						getAllCandidateIdsInInterview(), interviewStatusValue, startDate, endDate, stageInteger);
			}

		}

		if (getCurrentUserRole().equalsIgnoreCase("Admin") || getCurrentUserRole().equalsIgnoreCase("Default-Admin")) {
			System.out.print("++++++++++IFIF2+++++++");
			return candidateService.getDataTableDataForAdmin(input, getAllCandidateIdsInInterview());
		}
		System.out.print("++++++++++ELSE2+++++++");

		return candidateService.getDataTableDataByDepartment(input, getCurrentInterviewerDepartment(),
				getAllCandidateIdsInInterview());

	}

	public List<Long> getAllCandidateIdsInInterview() {
		List<CandidateInterview> candidateInterviews = candidateInterviewService.getAll();

		List<Long> candidateIds = candidateInterviews.stream()
				.map(candidateInterview -> candidateInterview.getCandidate().getId()).collect(Collectors.toList());

		return candidateIds;
	}

	@GetMapping("/set-interview-status")
	@ResponseBody
	public void setInterviewStatus(@RequestParam("status") String status, @RequestParam("candidateId") long candidateId,
			@RequestParam("interviewId") long interviewId, @RequestParam("date") String date) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		Interview interview = interviewService.getInterviewById(interviewId);
		CandidateInterview candidateInterview = new CandidateInterview();
		candidateInterview.setCandidate(candidate);
		candidateInterview.setInterview(interview);
		candidateInterview.setInterviewDate(date);
		candidateInterview.setStatus(status);
		candidateInterview.setInterviewStatusChangedUserId(getCurrentDepartmentHeadId());
		candidateInterviewService.saveCandidateInterview(candidateInterview);
		if (status.equalsIgnoreCase("Passed")) {
			LocalDateTime localDateTime = LocalDateTime.now();
			String notiMessage = "Candidate Passed : " + candidate.getName() + " is given passed by "
					+ getCurrentUserName() + " At " + localDateTime;

			notificationService.createNotification(notiMessage, localDateTime, null);
		}
	}

	@GetMapping("/get-interview-id")
	@ResponseBody
	public long getInterviewId(@RequestParam("candidateId") long candidateId) {
		List<CandidateInterview> candidateInterview = candidateInterviewService.getCandidateInterviewById(candidateId);
		int lastIndex = candidateInterview.size() - 1;
		CandidateInterview lastInterview = candidateInterview.get(lastIndex);
		return lastInterview.getInterview().getId();
	}

	@GetMapping("/save-reviews")
	@ResponseBody
	public void saveReviews(@RequestParam("review") String review, @RequestParam("candidateId") long candidateId) {
		List<CandidateInterview> candidateInterview = candidateInterviewService.getCandidateInterviewById(candidateId);
		int lastIndex = candidateInterview.size() - 1;
		CandidateInterview lastInterview = candidateInterview.get(lastIndex);
		Reviews reviews = new Reviews();
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		reviews.setCandidateInterview(lastInterview);
		reviews.setReview(review);
		reviews.setReviewBy(getCurrentDepartmentHeadId());
		reviews.setReviewedDateTime(formattedDateTime);
		reviewService.saveReview(reviews);
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
