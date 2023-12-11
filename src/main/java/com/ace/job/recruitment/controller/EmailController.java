package com.ace.job.recruitment.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.CandidateStatus;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.MailHistory;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.model.Email;
import com.ace.job.recruitment.model.EmailPreparationData;
import com.ace.job.recruitment.model.EmailTemplate;
import com.ace.job.recruitment.other.EmailTemplateLoader;
import com.ace.job.recruitment.service.CandidateInterviewService;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.CandidateStatusService;
import com.ace.job.recruitment.service.EmailService;
import com.ace.job.recruitment.service.InterviewService;
import com.ace.job.recruitment.service.MailHistoryService;
import com.ace.job.recruitment.service.VacancyService;

@Controller
@RequestMapping("/senior-hr")
public class EmailController {
	@Autowired
	private EmailTemplateLoader loader;
	@Autowired
	private EmailService emailService;
	@Autowired
	private MailHistoryService mailHistoryService;
	@Autowired
	private CandidateService candidateService;
	@Autowired
	private CandidateInterviewService candidateInterviewService;
	@Autowired
	private InterviewService interviewService;
	@Autowired
	private VacancyService vacancyService;

	@Autowired
	private CandidateStatusService candidateStatusService;

	// for email preparation form
	@GetMapping("/email-prepare-form/{templateId}")
	public String prepareEmailForm(@PathVariable("templateId") String templateId, @RequestParam("type") String type,
			@RequestParam("candidateId") Long candidateId, @RequestParam("stage") int stage, Model model,
			@RequestParam(value = "vacancyId", required = false) Long vacancyId,
			@RequestParam(value = "isRecalled", required = false) Integer isRecalled) {
		EmailPreparationData emailPreparation = new EmailPreparationData();
		Candidate candidate = candidateService.getCandidateById(candidateId);
		if (emailPreparation != null) {
			EmailTemplate template = loader.getEmailTemplate(templateId);
			if (template != null) {
				System.out.println("templateId" + templateId);
				model.addAttribute("templateId", templateId);

				long interviewId = 0;
				Interview lastInterview = new Interview();
				if (stage != 0 && (type != null && !type.equalsIgnoreCase(""))) {
					List<Interview> interviews = interviewService.getInterviewByTypeAndStageAndStatusAndVacancyId(type,
							stage, true, vacancyId);
					if (!interviews.isEmpty()) {
						int lastIndex = interviews.size() - 1;
						lastInterview = interviews.get(lastIndex);
						interviewId = lastInterview.getId();
					}
				}
				model.addAttribute("isRecalled", isRecalled);
				model.addAttribute("minDate", lastInterview.getStartDate());
				model.addAttribute("maxDate", lastInterview.getEndDate());
				model.addAttribute("vacancyId", vacancyId);
				model.addAttribute("candidate_id", candidateId);
				model.addAttribute("interview_id", interviewId);
				model.addAttribute("subject", template.getSubject());
				model.addAttribute("recipientAddress", candidate.getEmail());
				model.addAttribute("stage", stage);
				model.addAttribute("type", type);
				model.addAttribute("templateId", templateId);
			}
		}

		return "email/email-prepare-form";
	}

	// show email draft
	@PostMapping("/show-emailbody-draft")
	public ResponseEntity<String> showEmailBodyDraft(
			@ModelAttribute("emailPreparation") EmailPreparationData emailPreparation) {
		String body = null;

		// load template from xml
		EmailTemplate template = new EmailTemplate();
		if (emailPreparation != null) {
			template = loader.getEmailTemplate(emailPreparation.getTemplateId());

			if (template != null) {
				Candidate candidate = candidateService.getCandidateById(emailPreparation.getCandidate_id());
				// replace placeholders from body with values
				body = template.getBody().replace("{CandidateName}", candidate.getName());
				if (candidate.isGender() == false) {
					body = body.replace("{HonourablePrefix}", "Mr");
				} else {
					body = body.replace("{HonourablePrefix}", "Ms");
				}

				// replace placeholders for interview invitations
				if (emailPreparation.getTemplateId().equals("3") || emailPreparation.getTemplateId().equals("4")) {

					Interview interview = interviewService.getInterviewById(emailPreparation.getInterview_id());

					if (interview.getEndDate() != null) {
						body = body.replace("{StartDate}", (interview.getStartDate()).toString()).replace("{EndDate}",
								(interview.getEndDate()).toString());
					} else {
						body = body.replace("From {StartDate} To {EndDate}", (interview.getStartDate()).toString());
					}

					body = body.replace("{StartTime}", interview.getStartTime()).replace("{EndTime}",
							interview.getEndTime());

					if (emailPreparation.getAppointmentDate() != null
							&& !emailPreparation.getAppointmentDate().equals("")) {
						body = body.replace("<b>[AppointmentDate]</b>", emailPreparation.getAppointmentDate());
					}

					if (emailPreparation.getAppointmentDay() != null
							&& !emailPreparation.getAppointmentDay().equals("")) {
						body = body.replace("<b>[AppointmentDay]</b>", emailPreparation.getAppointmentDay());
					}

					if (emailPreparation.getAppointmentStartTime() != null
							&& !emailPreparation.getAppointmentStartTime().equals("")) {
						body = body.replace("<b>[AppointmentStartTime]</b>",
								emailPreparation.getAppointmentStartTime());
					}

					if (emailPreparation.getAppointmentEndTime() != null
							&& !emailPreparation.getAppointmentEndTime().equals("")) {
						body = body.replace("<b>[AppointmentEndTime]</b>", emailPreparation.getAppointmentEndTime());
					}

					if (emailPreparation.getTemplateId().equals("3")) {
						body = body.replace("{Location}", interview.getLocation());
					}

					if (emailPreparation.getTemplateId().equals("4")) {
						if (emailPreparation.getZoomMeetingUrl() != null
								&& !emailPreparation.getZoomMeetingUrl().equals("")) {
							body = body.replace("<b>[ZoomURL]</b>", emailPreparation.getZoomMeetingUrl());
						}
						if (emailPreparation.getZoomMeetingId() != null
								&& !emailPreparation.getZoomMeetingId().equals("")) {
							body = body.replace("<b>[MeetingID]</b>", emailPreparation.getZoomMeetingId());
						}
						if (emailPreparation.getZoomMeetingPasscode() != null
								&& !emailPreparation.getZoomMeetingPasscode().equals("")) {
							body = body.replace("<b>[MeetingPasscode]</b>", emailPreparation.getZoomMeetingPasscode());
						}
					}

				}

				// replace placeholders for job offer
				else if (emailPreparation.getTemplateId().equals("5")) {
					body = body.replace("{Position}", candidate.getVacancy().getPosition().getName())
							.replace("{Department}", candidate.getVacancy().getDepartment().getName())
							.replace("{StartWorkingHour}", candidate.getVacancy().getStartWorkingHour())
							.replace("{EndWorkingHour}", candidate.getVacancy().getEndWorkingHour())
							.replace("{StartWorkingDay}", candidate.getVacancy().getStartWorkingDay())
							.replace("{EndWorkingDay}", candidate.getVacancy().getEndWorkingDay());

					if (emailPreparation.getJoinedStartDate() != null
							&& !emailPreparation.getJoinedStartDate().equals("")) {
						body = body.replace("<b>[JoinedStartDate]</b>", emailPreparation.getJoinedStartDate());
					}
					if (emailPreparation.getBasicPay() != null && !emailPreparation.getBasicPay().equals("")) {
						body = body.replace("<b>[BasicPay]</b>", emailPreparation.getBasicPay());
					}
					if (emailPreparation.getProjectAllowance() != null
							&& !emailPreparation.getProjectAllowance().equals("")) {
						body = body.replace("<b>[ProjectAllowance]</b>", emailPreparation.getProjectAllowance());
					}
					if (emailPreparation.getMealTransportAllowanceDay() != null
							&& !emailPreparation.getMealTransportAllowanceDay().equals("")) {
						body = body.replace("<b>[MealTransportAllowanceDay]</b>",
								emailPreparation.getMealTransportAllowanceDay());
					}
					if (emailPreparation.getMealTransportAllowanceMonth() != null
							&& !emailPreparation.getMealTransportAllowanceMonth().equals("")) {
						body = body.replace("<b>[MealTransportAllowanceMonth]</b>",
								emailPreparation.getMealTransportAllowanceMonth());
					}
					if (emailPreparation.getEarnLeave() != null && !emailPreparation.getEarnLeave().equals("")) {
						body = body.replace("<b>[EarnLeave]</b>", emailPreparation.getEarnLeave());
					}
					if (emailPreparation.getCasualLeave() != null && !emailPreparation.getCasualLeave().equals("")) {
						body = body.replace("<b>[CasualLeave]</b>", emailPreparation.getCasualLeave());
					}
					if (emailPreparation.getCasualLeave() != null && !emailPreparation.getCasualLeave().equals("")) {
						body = body.replace("<b>[MedicatlLeave]</b>", emailPreparation.getMedicalLeave());
					}
				}
			}
		}
		return ResponseEntity.ok(body);
	}

	// send mail
	@PostMapping("/send-mail")
	public ResponseEntity<String> sendMail(@ModelAttribute("emailPreparation") EmailPreparationData emailPreparation,
			Authentication authentication, @RequestParam("candidate_id") long candidateId,
			@RequestParam("templateId") String templateId,
			@RequestParam(value = "vacancyId", required = false) Long vacancyId,
			@RequestParam(value = "isRecalled", required = false) Integer isRecalled) {
		String statusMsg = null;
		String body = null;
		Candidate candidate = candidateService.getCandidateById(candidateId);

		// load template from xml
		EmailTemplate template = new EmailTemplate();
		if (emailPreparation != null) {
			template = loader.getEmailTemplate(emailPreparation.getTemplateId());

			if (template != null) {
				// for mail history
				User user = emailService.getCurrentUser(authentication);
				LocalDateTime currentDateTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				String formattedDateTime = currentDateTime.format(formatter);

				MailHistory mailHistory = new MailHistory(emailPreparation.getSubject(), formattedDateTime, user,
						candidate);

				// replace placeholders from body with values
				body = template.getBody().replace("{CandidateName}", candidate.getName());
				if (candidate.isGender() == false) {
					body = body.replace("{HonourablePrefix}", "Mr");
				} else {
					body = body.replace("{HonourablePrefix}", "Ms");
				}

				// replace placeholders for interview invitations
				if (emailPreparation.getTemplateId().equals("3") || emailPreparation.getTemplateId().equals("4")) {
					Interview interview = interviewService.getInterviewById(emailPreparation.getInterview_id());
					if (interview.getEndDate() != null) {
						body = body.replace("{StartDate}", (interview.getStartDate()).toString()).replace("{EndDate}",
								(interview.getEndDate()).toString());
					} else {
						body = body.replace("From {StartDate} To {EndDate}", (interview.getStartDate()).toString());
					}

					body = body.replace("{StartTime}", interview.getStartTime())
							.replace("{EndTime}", interview.getEndTime())
							.replace("<b>[AppointmentDate]</b>", emailPreparation.getAppointmentDate())
							.replace("<b>[AppointmentDay]</b>", emailPreparation.getAppointmentDay())
							.replace("<b>[AppointmentStartTime]</b>", emailPreparation.getAppointmentStartTime())
							.replace("<b>[AppointmentEndTime]</b>", emailPreparation.getAppointmentEndTime());
					if (emailPreparation.getTemplateId().equals("3")) {

						body = body.replace("{Location}", interview.getLocation());
					}

					if (emailPreparation.getTemplateId().equals("4")) {
						body = body.replace("<b>[ZoomURL]</b>", emailPreparation.getZoomMeetingUrl())
								.replace("<b>[MeetingID]</b>", emailPreparation.getZoomMeetingId())
								.replace("<b>[MeetingPasscode]</b>", emailPreparation.getZoomMeetingPasscode());
					}

				}

				// replace placeholders for job offer
				else if (emailPreparation.getTemplateId().equals("5")) {
					body = body.replace("{Position}", candidate.getVacancy().getPosition().getName())
							.replace("{Department}", candidate.getVacancy().getDepartment().getName())
							.replace("{StartWorkingHour}", candidate.getVacancy().getStartWorkingHour())
							.replace("{EndWorkingHour}", candidate.getVacancy().getEndWorkingHour())
							.replace("{StartWorkingDay}", candidate.getVacancy().getStartWorkingDay())
							.replace("{EndWorkingDay}", candidate.getVacancy().getEndWorkingDay())
							.replace("<b>[JoinedStartDate]</b>", emailPreparation.getJoinedStartDate())
							.replace("<b>[BasicPay]</b>", emailPreparation.getBasicPay())
							.replace("<b>[ProjectAllowance]</b>", emailPreparation.getProjectAllowance())
							.replace("<b>[MealTransportAllowanceDay]</b>",
									emailPreparation.getMealTransportAllowanceDay())
							.replace("<b>[MealTransportAllowanceMonth]</b>",
									emailPreparation.getMealTransportAllowanceMonth())
							.replace("<b>[EarnLeave]</b>", emailPreparation.getEarnLeave())
							.replace("<b>[CasualLeave]</b>", emailPreparation.getCasualLeave())
							.replace("<b>[MedicatlLeave]</b>", emailPreparation.getMedicalLeave());

				}

				Email email = new Email(emailPreparation.getRecipientAddress(), emailPreparation.getCcAddress(),
						emailPreparation.getSubject(), body, emailPreparation.getAttachment());
				statusMsg = emailService.sendMailWithCCAttachment(email);
				if (statusMsg.equals("success")) {
					MailHistory storedMailHistory = mailHistoryService.storeMailHistory(mailHistory);

					if (templateId.equalsIgnoreCase("5")) {
						candidate.setMailSent(true);
						candidateService.addCandidate(candidate);
					}
					if (templateId.equalsIgnoreCase("4") || templateId.equalsIgnoreCase("3")) {
						CandidateInterview candidateInterview = new CandidateInterview();
						System.out.print("_______" + isRecalled + "______");
						if (isRecalled != null && isRecalled != 0) {
							System.out.print("_______recalled______");
							Vacancy vacancy = vacancyService.getVacancyByIdWithEntity(vacancyId.longValue());
							Candidate newCandidate = new Candidate(candidate.getName(), candidate.getDob(),
									candidate.isGender(), candidate.getPhone(), candidate.getEducation(),
									candidate.getEmail(), candidate.getTechSkill(), candidate.getLanguageSkill(),
									candidate.getMainTech(), candidate.getExp(), candidate.getLevel(),
									candidate.getExpectedSalary(), candidate.getSubmitDate(), candidate.getSubmitTime(),
									candidate.getFile(), candidate.getFiletype(), false, 0, false);
							newCandidate.setVacancy(vacancy);

							Candidate savedCandidate = candidateService.addCandidate(newCandidate);

							if (savedCandidate != null) {
								candidateInterview.setCandidate(savedCandidate);
								List<CandidateStatus> candidateStatus = candidateStatusService
										.getAllByCandidateId(candidateId);
								for (CandidateStatus cs : candidateStatus) {
									CandidateStatus newCandidateStatus = new CandidateStatus();
									newCandidateStatus.setCandidate(newCandidate);
									newCandidateStatus.setChangeStatusUserId(cs.getChangeStatusUserId());
									newCandidateStatus.setDate(cs.getDate());
									newCandidateStatus.setStatus(cs.getStatus());
									candidateStatusService.saveCandidateStatus(newCandidateStatus);
								}
							}

							candidate.isRecall(true);
							candidateService.addCandidate(candidate);

						} else {
							candidateInterview.setCandidate(candidate);
						}

						Interview interview = interviewService.getInterviewById(emailPreparation.getInterview_id());
						candidateInterview.setInterview(interview);
						candidateInterview.setStatus("Reached");
						candidateInterview
								.setInterviewStatusChangedUserId((emailService.getCurrentUser(authentication)).getId());
						candidateInterviewService.saveCandidateInterview(candidateInterview);
					}
				}
			}
		}
		return ResponseEntity.ok(statusMsg);

	}

	@PostMapping("/validate-cc-attachment")
	public ResponseEntity<String> validateCCAndAttachment(
			@ModelAttribute("emailPreparation") EmailPreparationData emailPreparation) {
		String statusMsg = null;
		statusMsg = emailService.validateCCAndAttachment(emailPreparation.getCcAddress(),
				emailPreparation.getAttachment());
		if (statusMsg == null) {
			statusMsg = "success";
		}

		if (emailPreparation.getCcAddress() == null) {
			System.out.println("cc is null");
		}
		if (emailPreparation.getAttachment() == null) {
			System.out.println("attach null");
		}
		return ResponseEntity.ok(statusMsg);
	}

	@GetMapping("/customize-email/{candidate-id}")
	public String customizeEmailbody(@ModelAttribute("emailPreparation") EmailPreparationData emailPreparation,
			Model model, @PathVariable("candidate-id") long candidateId, @RequestParam("interview_id") long interviewId,
			@RequestParam(value = "vacancyId", required = false) Long vacancyId,
			@RequestParam(value = "isRecalled", required = false) Integer isRecalled) {
		String body = null;
		// load template from xml
		EmailTemplate template = new EmailTemplate();
		if (emailPreparation != null) {
			template = loader.getEmailTemplate(emailPreparation.getTemplateId());

			if (template != null) {
				Candidate candidate = candidateService.getCandidateById(candidateId);
				// replace placeholders from body with values
				body = template.getBody().replace("{CandidateName}", candidate.getName());
				if (candidate.isGender() == false) {
					body = body.replace("{HonourablePrefix}", "Mr");
				} else {
					body = body.replace("{HonourablePrefix}", "Ms");
				}

				// replace placeholders for interview invitations
				if (emailPreparation.getTemplateId().equals("3") || emailPreparation.getTemplateId().equals("4")) {

					Interview interview = interviewService.getInterviewById(emailPreparation.getInterview_id());
					if (interview.getEndDate() != null) {
						body = body.replace("{StartDate}", (interview.getStartDate()).toString()).replace("{EndDate}",
								(interview.getEndDate()).toString());
					} else {
						body = body.replace("From {StartDate} To {EndDate}", (interview.getStartDate()).toString());
					}

					body = body.replace("{StartTime}", interview.getStartTime()).replace("{EndTime}",
							interview.getEndTime());
					if (emailPreparation.getAppointmentDate() != null
							&& !emailPreparation.getAppointmentDate().equals("")) {
						body = body.replace("<b>[AppointmentDate]</b>", emailPreparation.getAppointmentDate());
					}

					if (emailPreparation.getAppointmentDay() != null
							&& !emailPreparation.getAppointmentDay().equals("")) {
						body = body.replace("<b>[AppointmentDay]</b>", emailPreparation.getAppointmentDay());
					}

					if (emailPreparation.getAppointmentStartTime() != null
							&& !emailPreparation.getAppointmentStartTime().equals("")) {
						body = body.replace("<b>[AppointmentStartTime]</b>",
								emailPreparation.getAppointmentStartTime());
					}

					if (emailPreparation.getAppointmentEndTime() != null
							&& !emailPreparation.getAppointmentEndTime().equals("")) {
						body = body.replace("<b>[AppointmentEndTime]</b>", emailPreparation.getAppointmentEndTime());
					}

					if (emailPreparation.getTemplateId().equals("3")) {
						body = body.replace("{Location}", interview.getLocation());
					}

					if (emailPreparation.getTemplateId().equals("4")) {
						if (emailPreparation.getZoomMeetingUrl() != null
								&& !emailPreparation.getZoomMeetingUrl().equals("")) {
							body = body.replace("<b>[ZoomURL]</b>", emailPreparation.getZoomMeetingUrl());
						}
						if (emailPreparation.getZoomMeetingId() != null
								&& !emailPreparation.getZoomMeetingId().equals("")) {
							body = body.replace("<b>[MeetingID]</b>", emailPreparation.getZoomMeetingId());
						}
						if (emailPreparation.getZoomMeetingPasscode() != null
								&& !emailPreparation.getZoomMeetingPasscode().equals("")) {
							body = body.replace("<b>[MeetingPasscode]</b>", emailPreparation.getZoomMeetingPasscode());
						}
					}
				}

				// replace placeholders for job offer
				else if (emailPreparation.getTemplateId().equals("5")) {
					body = body.replace("{Position}", candidate.getVacancy().getPosition().getName())
							.replace("{Department}", candidate.getVacancy().getDepartment().getName())
							.replace("{StartWorkingHour}", candidate.getVacancy().getStartWorkingHour())
							.replace("{EndWorkingHour}", candidate.getVacancy().getEndWorkingHour())
							.replace("{StartWorkingDay}", candidate.getVacancy().getStartWorkingDay())
							.replace("{EndWorkingDay}", candidate.getVacancy().getEndWorkingDay());

					if (emailPreparation.getJoinedStartDate() != null
							&& !emailPreparation.getJoinedStartDate().equals("")) {
						body = body.replace("<b>[JoinedStartDate]</b>", emailPreparation.getJoinedStartDate());
					}
					if (emailPreparation.getBasicPay() != null && !emailPreparation.getBasicPay().equals("")) {
						body = body.replace("<b>[BasicPay]</b>", emailPreparation.getBasicPay());
					}
					if (emailPreparation.getProjectAllowance() != null
							&& !emailPreparation.getProjectAllowance().equals("")) {
						body = body.replace("<b>[ProjectAllowance]</b>", emailPreparation.getProjectAllowance());
					}
					if (emailPreparation.getMealTransportAllowanceDay() != null
							&& !emailPreparation.getMealTransportAllowanceDay().equals("")) {
						body = body.replace("<b>[MealTransportAllowanceDay]</b>",
								emailPreparation.getMealTransportAllowanceDay());
					}
					if (emailPreparation.getMealTransportAllowanceMonth() != null
							&& !emailPreparation.getMealTransportAllowanceMonth().equals("")) {
						body = body.replace("<b>[MealTransportAllowanceMonth]</b>",
								emailPreparation.getMealTransportAllowanceMonth());
					}
					if (emailPreparation.getEarnLeave() != null && !emailPreparation.getEarnLeave().equals("")) {
						body = body.replace("<b>[EarnLeave]</b>", emailPreparation.getEarnLeave());
					}
					if (emailPreparation.getCasualLeave() != null && !emailPreparation.getCasualLeave().equals("")) {
						body = body.replace("<b>[CasualLeave]</b>", emailPreparation.getCasualLeave());
					}
					if (emailPreparation.getCasualLeave() != null && !emailPreparation.getCasualLeave().equals("")) {
						body = body.replace("<b>[MedicatlLeave]</b>", emailPreparation.getMedicalLeave());
					}
				}
				Email email = new Email(emailPreparation.getRecipientAddress(), emailPreparation.getCcAddress(),
						emailPreparation.getSubject(), body, emailPreparation.getAttachment());
				model.addAttribute("email", email);
				model.addAttribute("candidateId", candidate.getId());
				if (interviewId != 0) {
					Interview interview = interviewService.getInterviewById(interviewId);
					model.addAttribute("vacancyId", interview.getVacancy().getId());
				}

				model.addAttribute("interviewId", interviewId);
				model.addAttribute("isRecalled", isRecalled);
			}
			model.addAttribute("templateId", emailPreparation.getTemplateId());
		}

		return "email/customize-mail";
	}

	@PostMapping("/send-customized-mail")
	public ResponseEntity<String> sendCustomizedMail(@ModelAttribute("email") Email email,
			@RequestParam("candidateId") long candidateId, @RequestParam("templateId") String templateId,
			@RequestParam("interviewId") long interviewId, Authentication authentication,
			@RequestParam(value = "vacancyId", required = false) Long vacancyId,
			@RequestParam(value = "isRecalled", required = false) Integer isRecalled) {
		String statusMsg = null;

		// for mail history
		User user = emailService.getCurrentUser(authentication);
		Candidate candidate = candidateService.getCandidateById(candidateId);
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);

		MailHistory mailHistory = new MailHistory(email.getSubject(), formattedDateTime, user, candidate);

		statusMsg = emailService.sendMailWithCCAttachment(email);
		if (statusMsg.equals("success")) {
			MailHistory storedMailHistory = mailHistoryService.storeMailHistory(mailHistory);
		}
		if (templateId.equalsIgnoreCase("5")) {
			candidate.setMailSent(true);
			candidateService.addCandidate(candidate);
		}
		if (templateId.equalsIgnoreCase("4") || templateId.equalsIgnoreCase("3")) {
			CandidateInterview candidateInterview = new CandidateInterview();
			System.out.print("___is Recalled Value____" + isRecalled + "______");
			if (isRecalled != null && isRecalled != 0) {
				System.out.print("_______recalled______");

				Vacancy vacancy = vacancyService.getVacancyByIdWithEntity(vacancyId.longValue());
				Candidate newCandidate = new Candidate(candidate.getName(), candidate.getDob(), candidate.isGender(),
						candidate.getPhone(), candidate.getEducation(), candidate.getEmail(), candidate.getTechSkill(),
						candidate.getLanguageSkill(), candidate.getMainTech(), candidate.getExp(), candidate.getLevel(),
						candidate.getExpectedSalary(), candidate.getSubmitDate(), candidate.getSubmitTime(),
						candidate.getFile(), candidate.getFiletype(), false, 0, false);
				newCandidate.setVacancy(vacancy);

				Candidate savedCandidate = candidateService.addCandidate(newCandidate);

				if (savedCandidate != null) {
					candidateInterview.setCandidate(savedCandidate);
					List<CandidateStatus> candidateStatus = candidateStatusService.getAllByCandidateId(candidateId);
					for (CandidateStatus cs : candidateStatus) {
						CandidateStatus newCandidateStatus = new CandidateStatus();
						newCandidateStatus.setCandidate(newCandidate);
						newCandidateStatus.setChangeStatusUserId(cs.getChangeStatusUserId());
						newCandidateStatus.setDate(cs.getDate());
						newCandidateStatus.setStatus(cs.getStatus());
						candidateStatusService.saveCandidateStatus(newCandidateStatus);
					}
				}

				candidate.isRecall(true);
				candidateService.addCandidate(candidate);

			} else {
				candidateInterview.setCandidate(candidate);
			}

			Interview interview = interviewService.getInterviewById(interviewId);
			candidateInterview.setInterview(interview);
			candidateInterview.setStatus("Reached");
			candidateInterview.setInterviewStatusChangedUserId((emailService.getCurrentUser(authentication)).getId());
			candidateInterviewService.saveCandidateInterview(candidateInterview);
		}

		return ResponseEntity.ok(statusMsg);
	}

}