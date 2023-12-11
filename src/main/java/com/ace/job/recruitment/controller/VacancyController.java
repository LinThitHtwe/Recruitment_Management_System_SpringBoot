package com.ace.job.recruitment.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Notification;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.NotificationService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.UserService;
import com.ace.job.recruitment.service.VacancyService;

@Controller
@RequestMapping("/hr")
public class VacancyController {

	@Autowired
	private VacancyService vacancyService;

	@Autowired
	VacancyRepository vacancyRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private UserService userService;

	@ModelAttribute("vacancy")
	public VacancyDto getVacancyModel() {
		VacancyDto vacancy = new VacancyDto();
		vacancy.setStartWorkingDay("Monday");
		vacancy.setEndWorkingDay("Friday");
		return vacancy;
	}

	@GetMapping("/departments-for-user")
	@ResponseBody
	public List<Department> getAllDepartments() {
		List<Department> list = departmentService.getDepartments();
		return list;
	}

	@ModelAttribute("vacancyList")
	public List<Vacancy> getVacancyList() {
		List<Vacancy> list = vacancyService.getAllVacancy();
		return list;
	}

	@ModelAttribute("departmentList")
	public List<Department> getDepartmentList() {
		List<Department> list = departmentService.getDepartments();
		return list;
	}

	@ModelAttribute("positionList")
	public List<Position> getPositionList() {
		List<Position> list = positionService.getAllPositions();
		return list;
	}

	// Response body start

	@GetMapping("/notifications")
	@ResponseBody
	public List<Notification> responseNotifications(@AuthenticationPrincipal AppUserDetails userDetails) {
		List<Notification> list = notificationService.getAllNotifications();
		List<Notification> currentList = notificationService.getAllByUserId(userDetails.getId());
		List<Notification> notifications = new ArrayList<>();

		for (Notification notification : list) {
			if (!currentList.contains(notification)) {
				notifications.add(notification);
			}
		}

		return notifications;
	}

	@GetMapping("/notifications/count")
	@ResponseBody
	public int responseNotificationsCount(@AuthenticationPrincipal AppUserDetails userDetails) {
		List<Notification> list = notificationService.getAllNotifications();
		List<Notification> currentList = notificationService.getAllByUserId(userDetails.getId());
		List<Notification> notifications = new ArrayList<>();

		for (Notification notification : list) {
			if (!currentList.contains(notification)) {
				notifications.add(notification);
			}
		}
		return notifications.size();
	}

	@PostMapping("/inactive")
	public ResponseEntity<String> inactiveVacancy(@RequestParam("vacancyId") long vacancyId) {
		try {
			VacancyDto vacancydto = vacancyService.getVacancyById(vacancyId);

			if (vacancydto == null) {
				return new ResponseEntity<>("Vacancy not found", HttpStatus.NOT_FOUND);
			}

			vacancydto.setActive(false);
			vacancydto.setUrgent(false);
			vacancyService.updateVacancy(vacancydto);

			return new ResponseEntity<>("Vacancy marked as inactive", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/testvacancy")
	@ResponseBody
	public DataTablesOutput<Vacancy> test(@Valid DataTablesInput input) {
		return vacancyService.getVacancyByStatus(input, "Inactive");
	}

	@GetMapping("/table-data")
	@ResponseBody
	public DataTablesOutput<Vacancy> getDataTableData(@Valid DataTablesInput input,
			@RequestParam(value = "startDateFrom", required = false) String startDateFrom,
			@RequestParam(value = "endDateTo", required = false) String endDateTo,
			@RequestParam(value = "status", required = false) String status) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = startDateFrom != null ? LocalDate.parse(startDateFrom, dateFormatter) : null;
		LocalDate endDate = endDateTo != null ? LocalDate.parse(endDateTo, dateFormatter) : null;
		String vacancyStatus = (status != null && !status.equalsIgnoreCase("All")) ? status : null;
		System.out.print("___status__" + status + "___");
		if ((startDate != null || endDate != null) && vacancyStatus != null) {
			return vacancyService.getVacancyByDateFilterAndStatus(input, startDate, endDate, vacancyStatus);
		} else if (vacancyStatus != null) {
			return vacancyService.getVacancyByStatus(input, vacancyStatus);
		} else if (startDate != null || endDate != null) {
			return vacancyService.getVacancyByDateFilter(input, startDate, endDate);
		}

		return vacancyRepository.findAll(input);
	}

	@GetMapping("/setup-add-vacancy")
	public String setupAddVacancy(HttpSession session) {
		return "vacancy/vacancy_form";
	}

	@PostMapping("/add-vacancy")
	public String addVacancy(@ModelAttribute("vacancy") @Validated VacancyDto dto,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) {
		dto.setCreatedUserId(userDetails.getId());
		dto.setReopenStatus(false);
		dto.setCreatedDate(LocalDate.now());
		dto.setCreatedTime(LocalTime.now());
		dto.setDueDate(LocalDate.now().plusDays(30));
		dto.setActive(true);
		if (dto.isUrgent()) {
			dto.setUrgent(true);
		} else {
			dto.setUrgent(false);
		}
		// Create and send notification
		String notificationMessage = "Vacancy-Created: " + userDetails.getFullName() + " created "
				+ dto.getPosition().getName() + " at " + vacancyService.getCurrentTime() + " on "
				+ vacancyService.getCurrentDate();
		vacancyService.addVacancyAndNotify(dto, notificationMessage);
		redirectAttributes.addFlashAttribute("addSuccess", true);
		return "redirect:/hr/setup-add-vacancy";
	}

	@GetMapping("/vacancy-list")
	public String showAllVacancyList() {
		return "vacancy/vacancy_list";
	}

	@GetMapping("/vacancy-info/{id}/{notiId}")
	public ModelAndView showVacancyInfo(@PathVariable("id") long id, @PathVariable("notiId") long notiId,
			@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
		VacancyDto vacancy = vacancyService.getVacancyById(id);
		List<User> user = new ArrayList<>();
		user.add(userService.getUserById(userDetails.getId()));
		Notification notification = notificationService.getNotificationById(notiId);
		vacancy.setStartWorkingHour(vacancyService.convertToFormattedTime(vacancy.getStartWorkingHour()));
		vacancy.setEndWorkingHour(vacancyService.convertToFormattedTime(vacancy.getEndWorkingHour()));
		notificationService.updateNotificationStatus(notification, user);
		return new ModelAndView("vacancy/vacancy_info", "vacancyInfo", vacancy);
	}

	@GetMapping("/vacancy-info/{id}")
	public ModelAndView vacancyInfo(@PathVariable("id") long id, @AuthenticationPrincipal AppUserDetails userDetails,
			Model model) {
		VacancyDto vacancy = vacancyService.getVacancyById(id);
		List<User> user = new ArrayList<>();
		user.add(userService.getUserById(userDetails.getId()));
		vacancy.setStartWorkingHour(vacancyService.convertToFormattedTime(vacancy.getStartWorkingHour()));
		vacancy.setEndWorkingHour(vacancyService.convertToFormattedTime(vacancy.getEndWorkingHour()));
		return new ModelAndView("vacancy/vacancy_info", "vacancyInfo", vacancy);
	}

	@GetMapping("/setup-edit-vacancy/{id}")
	public ModelAndView setupEditVacancy(@PathVariable("id") long id, HttpSession session) {
		VacancyDto vacancy = vacancyService.getVacancyById(id);
		session.setAttribute("recentVacancy", vacancy);
		return new ModelAndView("vacancy/vacancy_update_form", "vacancyById", vacancy);
	}

	@PostMapping("/update-vacancy")
	public String updateVacancy(@ModelAttribute("vacancyById") @Validated VacancyDto dto, HttpSession session,
			@AuthenticationPrincipal AppUserDetails userDetails, RedirectAttributes redirectAttributes) {
		VacancyDto recent = (VacancyDto) session.getAttribute("recentVacancy");
		dto.setId(recent.getId());
		dto.setUpdatedUserId(userDetails.getId());
		dto.setReopenStatus(recent.isReopenStatus());
		dto.setCreatedDate(recent.getCreatedDate());
		dto.setCreatedTime(recent.getCreatedTime());
		dto.setDueDate(recent.getDueDate());
		dto.setUpdatedDate(LocalDate.now());
		dto.setUpdatedTime(LocalTime.now());
		dto.setActive(true);
		dto.setReopenDate(recent.getReopenDate());
		dto.setReopenTime(recent.getReopenTime());
		String notificationMessage = "Vacancy-Updated: " + userDetails.getFullName() + " updated "
				+ recent.getPosition().getName() + " of " + recent.getDepartment().getName() + " department" + " at "
				+ vacancyService.getCurrentTime() + " on " + vacancyService.getCurrentDate();
		vacancyService.editVacancyAndNotify(dto, notificationMessage);
		redirectAttributes.addFlashAttribute("updateSuccess", true);
		return "redirect:/hr/vacancy-list";
	}

	@GetMapping("/setup-reopen-vacancy/{id}")
	public ModelAndView setupReopenVacancy(@PathVariable("id") long id, Model model, HttpSession session) {
		VacancyDto vacancy = vacancyService.getVacancyById(id);
		session.setAttribute("recent", vacancy);
		model.addAttribute("reopen", true);
		return new ModelAndView("vacancy/reopen-vacancy-form", "vacancyById", vacancy);
	}

	@PostMapping("/reopen-vacancy")
	public String reopenVacancy(@ModelAttribute("vacancyById") @Validated VacancyDto dto,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails,
			HttpSession session) {
		VacancyDto recentVacancy = (VacancyDto) session.getAttribute("recent");
		recentVacancy.setReopened(true);
		vacancyService.updateVacancy(recentVacancy);

		dto.setReopened(false);
		dto.setReopenStatus(true);
		dto.setReopenDate(LocalDate.now());
		dto.setReopenTime(LocalTime.now());
		dto.setCreatedDate(LocalDate.now());
		dto.setCreatedTime(LocalTime.now());
		dto.setDueDate(LocalDate.now().plusDays(30));
		dto.setActive(true);
		dto.setCreatedUserId(userDetails.getId());
		dto.setUpdatedDate(null);
		dto.setUpdatedTime(null);
		dto.setUpdatedUserId(0);
		dto.setCandidates(null);
		// System.out.print("_____________" + dto.getDepartment().getAddress() +
		// "_____________");
		// Create and send notification
		String notificationMessage = "Vacancy-Reopened: " + userDetails.getFullName() + " reopened "
				+ dto.getPosition().getName() + " vacancy of " + dto.getDepartment().getName() + " at "
				+ vacancyService.getCurrentTime() + " on " + vacancyService.getCurrentDate();
		vacancyService.addVacancyAndNotify(dto, notificationMessage);
		redirectAttributes.addFlashAttribute("reopenSuccess", true);
		return "redirect:/hr/vacancy-list";
	}

	@GetMapping("/reopen-vacancy/{id}")
	public String reopenVacancy(@PathVariable("id") long id, @AuthenticationPrincipal AppUserDetails userDetails,
			RedirectAttributes redirectAttributes) {
		VacancyDto dto = vacancyService.getVacancyById(id);
		dto.setReopened(true);
		vacancyService.updateVacancy(dto);

		dto.setReopened(false);
		dto.setReopenStatus(true);
		dto.setReopenDate(LocalDate.now());
		dto.setReopenTime(LocalTime.now());
		dto.setCreatedDate(LocalDate.now());
		dto.setCreatedTime(LocalTime.now());
		dto.setDueDate(LocalDate.now().plusDays(30));
		dto.setActive(true);
		dto.setCreatedUserId(userDetails.getId());
		dto.setUpdatedDate(null);
		dto.setUpdatedTime(null);
		dto.setUpdatedUserId(0);
		dto.setUrgent(false);
		dto.setCandidates(null);
		String notificationMessage = "Vacancy-Reopened: " + userDetails.getFullName() + " reopened "
				+ dto.getPosition().getName() + " vacancy of " + dto.getDepartment().getName() + " at "
				+ vacancyService.getCurrentTime() + " on " + vacancyService.getCurrentDate();
		vacancyService.addVacancyAndNotify(dto, notificationMessage);
		redirectAttributes.addFlashAttribute("reopenSuccess", true);
		return "redirect:/hr/vacancy-list";
	}
}