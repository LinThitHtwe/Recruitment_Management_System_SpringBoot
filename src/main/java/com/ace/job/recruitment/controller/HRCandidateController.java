package com.ace.job.recruitment.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.dto.InterviewStageAndStatusHistoryDTO;
import com.ace.job.recruitment.dto.SelectionStatusHistoryDTO;
import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.CandidateStatus;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.Notification;
import com.ace.job.recruitment.entity.Status;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.CandidateInterviewService;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.CandidateStatusService;
import com.ace.job.recruitment.service.InterviewService;
import com.ace.job.recruitment.service.NotificationService;
import com.ace.job.recruitment.service.StatusService;
import com.ace.job.recruitment.service.UserService;
import com.ace.job.recruitment.service.VacancyService;

@Controller
public class HRCandidateController {

	@Autowired
	CandidateService candidateService;

	@Autowired
	UserService userService;

	@Autowired
	CandidateStatusService candidateStatusService;

	@Autowired
	StatusService statusService;

	@Autowired
	InterviewService interviewService;

	@Autowired
	VacancyService vacancyService;

	@Autowired
	CandidateInterviewService candidateInterviewService;
	
	@Autowired
	NotificationService notificationService;

	@GetMapping("/hr/candidates/{notiId}")
	public String showAllCandidatesFromNoti(@PathVariable("notiId") long notiId,
			@AuthenticationPrincipal AppUserDetails userDetails) {
		List<User> user = new ArrayList<>();
		user.add(userService.getUserById(userDetails.getId()));
		Notification notification = notificationService.getNotificationById(notiId);
		notificationService.updateNotificationStatus(notification, user);
		return "HR/candidate_hr_list";
	}

	public int getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				int id = userDetails.getId();
				return id;
			}
		}

		return 0;
	}

	@GetMapping("/hr/current-user-role")
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

	@GetMapping("/senior-hr/canceled-candidates")
	public String canceledCandidateView() {
		return "HR/cancel_candidate_table";
	}

	@GetMapping("/senior-hr/canceled-candidate-data")
	@ResponseBody
	public DataTablesOutput<Candidate> getCanceledCandidateData(@Valid DataTablesInput input,
			@RequestParam(value = "startDateFrom", required = false) String startDateFrom,
			@RequestParam(value = "endDateTo", required = false) String endDateTo,
			@RequestParam(value = "stage", required = false) String stage) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = (startDateFrom != null && !startDateFrom.equalsIgnoreCase("All")
				&& !startDateFrom.equals("")) ? LocalDate.parse(startDateFrom, dateFormatter) : null;
		LocalDate endDate = (endDateTo != null && !endDateTo.equalsIgnoreCase("All") && !endDateTo.equals(""))
				? LocalDate.parse(endDateTo, dateFormatter)
				: null;

		Integer stageInteger = null;
		if (stage != null && !stage.equalsIgnoreCase("All") && !stage.equals("")) {
			try {
				stageInteger = Integer.parseInt(stage);
			} catch (NumberFormatException e) {
				System.out.print(e);
			}
		}

		if (startDate != null || endDate != null || stageInteger != null) {

			return candidateService.getAllCancelCandidatesWithFilters(input, startDate, endDate, stageInteger);
		}
		return candidateService.getCancelCandidates(input);
	}

	// Method to get candidate data for table
	@GetMapping("/hr/candidate-data")
	@ResponseBody
	public DataTablesOutput<Candidate> getCandidateDatas(@Valid DataTablesInput input,
			@RequestParam(value = "selectionStatus", required = false) String selectionStatus,
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
		String selectionStatusValue = (selectionStatus != null && !selectionStatus.equalsIgnoreCase("All")
				&& !selectionStatus.equals("")) ? selectionStatus : null;
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

		if (startDate != null || endDate != null || selectionStatusValue != null || interviewStatusValue != null
				|| stageInteger != null) {

			return candidateService.getAllCandidatesWithFilters(input, selectionStatusValue, interviewStatusValue,
					startDate, endDate, stageInteger);
		}

		return candidateService.getDataTableData(input);
	}

	// Method to return to front end page
	@GetMapping("/hr/candidates")
	public String candidatesViewHR(Model model) {
		model.addAttribute("hrCandidates", true);
		return "HR/candidate_hr_list";
	}

	@GetMapping("/hr/candidate-vacancy/{id}")
	public String candidatesViewHRByVacancy(Model model, @PathVariable("id") long vacancyId) {
		return "HR/candidate_list_by_vacancy";
	}

	@GetMapping("/hr/candidate-data-by-vacancy/{id}")
	@ResponseBody
	public DataTablesOutput<Candidate> getCandidateDatasByVacancy(@Valid DataTablesInput input,
			@RequestParam(value = "selectionStatus", required = false) String selectionStatus,
			@RequestParam(value = "interviewStatus", required = false) String interviewStatus,
			@RequestParam(value = "stage", required = false) String stage,
			@PathVariable(value = "id", required = false) long vacancyId) {

		String selectionStatusValue = (selectionStatus != null && !selectionStatus.equalsIgnoreCase("All")
				&& !selectionStatus.equals("")) ? selectionStatus : null;
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

		if (selectionStatusValue != null || interviewStatusValue != null || stageInteger != null) {

			return candidateService.getAllCandidatesInVacancyWithFilters(input, selectionStatusValue,
					interviewStatusValue, stageInteger, vacancyId);
		}

		return candidateService.getCandidatesByVacancyId(input, vacancyId);
	}

	// Method to show Candidate Details
	@GetMapping("/hr/getCandidateInfo")
	public ResponseEntity<Candidate> candidateDetail(@RequestParam("id") long candidateId) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		return ResponseEntity.ok(candidate);
	}

	@GetMapping("/hr/get-active-vacancy")
	public ResponseEntity<?> getActiveVacancyForRecallingUser() {
		List<Vacancy> activeVacancies = vacancyService.getAllActiveVacancy();

		if (activeVacancies == null || activeVacancies.isEmpty()) {
			String errorMessage = "No active vacancies found.";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
		}
		return ResponseEntity.ok(activeVacancies);
	}

	// Method to show candidate's history
	@GetMapping("/hr/candidate-history/{id}")
	public String candidateHistoryView(@PathVariable("id") long id, Model model) {
		List<CandidateStatus> candidateStatusLists = candidateStatusService.getAllByCandidateId(id);
		List<SelectionStatusHistoryDTO> selectionStatusHistory = new ArrayList<>();

		for (CandidateStatus candidateStatus : candidateStatusLists) {
			SelectionStatusHistoryDTO statusHistoryDTO = new SelectionStatusHistoryDTO();
			statusHistoryDTO.setSelectionStatusChangeDate(candidateStatus.getDate());
			statusHistoryDTO.setSelectionStatus(candidateStatus.getStatus().getName());
			if (candidateStatus.getChangeStatusUserId() != 0) {
				statusHistoryDTO.setChangedUserName(
						(userService.getUserById(candidateStatus.getChangeStatusUserId())).getName());
			}

			selectionStatusHistory.add(statusHistoryDTO);
		}

		Candidate candidate = candidateService.getCandidateById(id);
		model.addAttribute("candidateStatusList", selectionStatusHistory);
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

	// Method to change candidate selection status
	@GetMapping("/hr/changeSelectionStatus")
	@ResponseBody
	public void changeSelectionStatus(@RequestParam("candidateId") long candidateId,
			@RequestParam("statusId") int statusId, Authentication authentication) {
		Status status = statusService.getStatusById(statusId);
		Candidate candidate = candidateService.getCandidateById(candidateId);

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		CandidateStatus candidateStatus = new CandidateStatus();
		candidateStatus.setCandidate(candidate);
		candidateStatus.setChangeStatusUserId(interviewService.getCurrentUserId(authentication));
		candidateStatus.setDate(currentDateTime.format(formatter));
		candidateStatus.setStatus(status);

		candidateStatusService.saveCandidateStatus(candidateStatus);
	}

	// Method to get interview stages
	@GetMapping("/hr/interview-stages")
	@ResponseBody
	public ResponseEntity<Integer> interviewStages(@RequestParam("candidateId") long candidateId) {
		List<CandidateInterview> candidateInterviews = candidateInterviewService
				.getCandidateInterviewByIdAndInterviewStatus(candidateId, true);
		int returnValue = 1;
		if (candidateInterviews != null && !candidateInterviews.isEmpty()) {
			CandidateInterview lastInterview = candidateInterviews.get(candidateInterviews.size() - 1);
			int stage = lastInterview.getInterview().getStage();
			if (stage == 1) {
				returnValue = 2;
			} else if (stage == 2) {
				returnValue = 3;
			} else if (stage == 3) {
				returnValue = 4;
			}
		}

		return ResponseEntity.ok(returnValue);
	}

	// method to get choose interview
	@GetMapping("/hr/choose-interview")
	@ResponseBody
	public ResponseEntity<String> chooseInterview(@RequestParam("interviewType") String type,
			@RequestParam("candidateId") long candidateId, @RequestParam("vacancyId") long vacancyId,
			@RequestParam("stage") int stage) {

		List<Interview> interviews = interviewService.getInterviewByTypeStageVacancyIdStatus(type, stage, vacancyId,
				true);
		List<Interview> inactiveInterviews = interviewService.getInterviewByTypeStageVacancyIdStatus(type, stage,
				vacancyId, false);
		Vacancy vacancy = vacancyService.getVacancyByIdForInterview(vacancyId);
		String statusMsg = "";
		if (interviews.isEmpty()) {
			statusMsg = "You haven't created " + type + " stage " + stage + "  interview for "
					+ vacancy.getPosition().getName() + ".";
			if (!inactiveInterviews.isEmpty()) {
				statusMsg = "Interview for " + type + " stage " + stage + "  for " + vacancy.getPosition().getName()
						+ " is inactive.";
			}
			return ResponseEntity.badRequest().body(statusMsg);
		} else {

			return ResponseEntity.ok("Interview selection successful");
		}
	}

	// method to get candidate interview list
	@GetMapping("/hr/get-candidate-interview-list")
	@ResponseBody
	public ResponseEntity<List<CandidateInterview>> getCandidateInerviewList(
			@RequestParam("candidateId") long candidateId) {
		List<CandidateInterview> candidateInterviewLists = candidateInterviewService
				.getCandidateInterviewById(candidateId);

		if (candidateInterviewLists == null) {
			return ResponseEntity.ok(Collections.emptyList());
		} else {
			return ResponseEntity.ok(candidateInterviewLists);
		}
	}

	@PostMapping("/hr/employ-candidate")
	@ResponseBody
	public ResponseEntity<String> employCandidate(@RequestParam("candidateId") long candidateId) {
		Candidate candidate = candidateService.getCandidateById(candidateId);
		if (candidate != null) {
			candidate.setEmploy(true);
			candidate.setEmployedUserId(getCurrentUserId());
			candidateService.addCandidate(candidate);
			return ResponseEntity.ok("Candidate employed successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found.");
		}
	}

}
