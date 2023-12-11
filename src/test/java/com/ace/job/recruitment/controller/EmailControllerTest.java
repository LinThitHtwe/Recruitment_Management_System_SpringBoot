package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.CandidateInterview;
import com.ace.job.recruitment.entity.CandidateStatus;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.MailHistory;
import com.ace.job.recruitment.entity.Position;
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

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {
	private MockMvc mockMvc;

	@Mock
	private EmailTemplateLoader loader;
	@Mock
	private EmailService emailService;
	@Mock
	private MailHistoryService mailHistoryService;
	@Mock
	private CandidateService candidateService;
	@Mock
	private CandidateInterviewService candidateInterviewService;
	@Mock
	private InterviewService interviewService;
	@Mock
	private VacancyService vacancyService;
	@Mock
	private CandidateStatusService candidateStatusService;

	@InjectMocks
	private EmailController emailController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
	}

	@Test
	void prepareEmailFormSucessTest() {
		Integer isRecalled = 1;
		Long candidateId = 1L;
		int stage = 1;
		String templateId = "3";
		String type = "online";
		boolean status = true;
		long vacancyId = 1L;

		Candidate candidate = new Candidate();
		candidate.setEmail("test@gmail.com");

		EmailTemplate template = new EmailTemplate();
		template.setSubject("Subject of the email template");

		Interview interview = new Interview();
		interview.setId(1);

		List<Interview> interviews = new ArrayList<>();
		interviews.add(interview);

		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(loader.getEmailTemplate(templateId)).thenReturn(template);
		Model model = mock(Model.class);

		String result = emailController.prepareEmailForm(templateId, type, candidateId, stage, model, vacancyId,
				isRecalled);

		assertEquals("email/email-prepare-form", result);
	}

	@Test
	void prepareEmailFormTemplateNullTest() {
		Integer isRecalled = 1;
		Long candidateId = 1L;
		int stage = 1;
		String templateId = "3";
		String type = "online";
		boolean status = true;
		long vacancyId = 1L;

		Candidate candidate = new Candidate();
		candidate.setEmail("test@gmail.com");

		EmailTemplate template = new EmailTemplate();
		template.setSubject("Subject of the email template");

		Interview interview = new Interview();
		interview.setId(1);

		List<Interview> interviews = new ArrayList<>();
		interviews.add(interview);

		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(loader.getEmailTemplate(templateId)).thenReturn(null);

		Model model = mock(Model.class);

		String result = emailController.prepareEmailForm(templateId, type, candidateId, stage, model, vacancyId,
				isRecalled);

		assertEquals("email/email-prepare-form", result);
	}

	@Test
	void prepareEmailFormStageZeroTest() {
		Integer isRecalled = 1;
		Long candidateId = 1L;
		int stage = 0;
		String templateId = "3";
		String type = "online";
		boolean status = true;
		long vacancyId = 1L;

		Candidate candidate = new Candidate();
		candidate.setEmail("test@gmail.com");

		EmailTemplate template = new EmailTemplate();
		template.setSubject("Subject of the email template");

		Interview interview = new Interview();
		interview.setId(1);

		List<Interview> interviews = new ArrayList<>();
		interviews.add(interview);

		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(loader.getEmailTemplate(templateId)).thenReturn(template);

		Model model = mock(Model.class);

		String result = emailController.prepareEmailForm(templateId, type, candidateId, stage, model, vacancyId,
				isRecalled);

		assertEquals("email/email-prepare-form", result);
	}

	@Test
	void prepareEmailFormTypeNullTest() {
		Integer isRecalled = 1;
		Long candidateId = 1L;
		int stage = 1;
		String templateId = "3";
		String type = null;
		boolean status = true;
		long vacancyId = 1L;

		Candidate candidate = new Candidate();
		candidate.setEmail("test@gmail.com");

		EmailTemplate template = new EmailTemplate();
		template.setSubject("Subject of the email template");

		Interview interview = new Interview();
		interview.setId(1);

		List<Interview> interviews = new ArrayList<>();
		interviews.add(interview);

		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(loader.getEmailTemplate(templateId)).thenReturn(template);

		Model model = mock(Model.class);

		String result = emailController.prepareEmailForm(templateId, type, candidateId, stage, model, vacancyId,
				isRecalled);

		assertEquals("email/email-prepare-form", result);
	}

	@Test
	void prepareEmailFormTypeEmptyStringTest() {
		Integer isRecalled = 1;
		Long candidateId = 1L;
		int stage = 1;
		String templateId = "3";
		String type = "";
		boolean status = true;
		long vacancyId = 1L;

		Candidate candidate = new Candidate();
		candidate.setEmail("test@gmail.com");

		EmailTemplate template = new EmailTemplate();
		template.setSubject("Subject of the email template");

		Interview interview = new Interview();
		interview.setId(1);

		List<Interview> interviews = new ArrayList<>();
		interviews.add(interview);

		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(loader.getEmailTemplate(templateId)).thenReturn(template);

		Model model = mock(Model.class);

		String result = emailController.prepareEmailForm(templateId, type, candidateId, stage, model, vacancyId,
				isRecalled);

		assertEquals("email/email-prepare-form", result);
	}

	@Test
	void showEmailBodyDraftEmailPreparationNullTest() {
		EmailPreparationData emailPreparation = null;

		Model model = mock(Model.class);

		ResponseEntity<String> response = emailController.showEmailBodyDraft(null);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void showEmailBodyDraftTemplateNullTest() {
		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("3");

		Model model = mock(Model.class);
		when(loader.getEmailTemplate(anyString())).thenReturn(null);

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId3AllReplacedTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> {Location}");

		Candidate candidate = new Candidate();
		candidate.setName("John Doe");
		candidate.setGender(false);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");
		interview.setLocation("MICT");

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("3");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setAppointmentDate("20.03.2023");
		testData.setAppointmentDay("Friday");
		testData.setAppointmentStartTime("01:00");
		testData.setAppointmentEndTime("02:00");

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr John Doe. Interview on 2023-08-15 2023-08-16. 01:00 02:00 20.03.2023 Friday 01:00 02:00 MICT";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId3ValuesNullTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on From {StartDate} To {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> {Location}");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");
		interview.setLocation("MICT");

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("3");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setAppointmentDate(null);
		testData.setAppointmentDay(null);
		testData.setAppointmentStartTime(null);
		testData.setAppointmentEndTime(null);

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Interview on 2023-08-15. 01:00 02:00 <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> MICT";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId3ValuesEmptyTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> {Location}");

		Candidate candidate = new Candidate();
		candidate.setName("Aye");
		candidate.setGender(true);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");
		interview.setLocation("MICT");

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("3");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setAppointmentDate("");
		testData.setAppointmentDay("");
		testData.setAppointmentStartTime("");
		testData.setAppointmentEndTime("");

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Ms Aye. Interview on 2023-08-15 2023-08-16. 01:00 02:00 <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> MICT";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId4AllReplacedTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> <b>[ZoomURL]</b> <b>[MeetingID]</b> <b>[MeetingPasscode]</b>");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("4");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setAppointmentDate("20.03.2023");
		testData.setAppointmentDay("Friday");
		testData.setAppointmentStartTime("01:00");
		testData.setAppointmentEndTime("02:00");
		testData.setZoomMeetingUrl("zoom.com");
		testData.setZoomMeetingId("123");
		testData.setZoomMeetingPasscode("111");

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Interview on 2023-08-15 2023-08-16. 01:00 02:00 20.03.2023 Friday 01:00 02:00 zoom.com 123 111";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId4ValuesNullTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> <b>[ZoomURL]</b> <b>[MeetingID]</b> <b>[MeetingPasscode]</b>");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("4");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setAppointmentDate(null);
		testData.setAppointmentDay(null);
		testData.setAppointmentStartTime(null);
		testData.setAppointmentEndTime(null);
		testData.setZoomMeetingUrl(null);
		testData.setZoomMeetingId(null);
		testData.setZoomMeetingPasscode(null);

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Interview on 2023-08-15 2023-08-16. 01:00 02:00 <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> <b>[ZoomURL]</b> <b>[MeetingID]</b> <b>[MeetingPasscode]</b>";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId4ValuesEmptyTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> <b>[ZoomURL]</b> <b>[MeetingID]</b> <b>[MeetingPasscode]</b>");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("4");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setAppointmentDate("");
		testData.setAppointmentDay("");
		testData.setAppointmentStartTime("");
		testData.setAppointmentEndTime("");
		testData.setZoomMeetingUrl("");
		testData.setZoomMeetingId("");
		testData.setZoomMeetingPasscode("");

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Interview on 2023-08-15 2023-08-16. 01:00 02:00 <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> <b>[ZoomURL]</b> <b>[MeetingID]</b> <b>[MeetingPasscode]</b>";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId5AllReplacedTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. {Position} {Department} {StartWorkingHour} {EndWorkingHour} {StartWorkingDay} {EndWorkingDay} <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>");

		Position position = new Position();
		position.setName("Java Developer");

		Department department = new Department();
		department.setName("Banking");

		Vacancy vacancy = new Vacancy();
		vacancy.setPosition(position);
		vacancy.setDepartment(department);
		vacancy.setStartWorkingHour("09:00");
		vacancy.setEndWorkingHour("04:00");
		vacancy.setStartWorkingDay("Monday");
		vacancy.setEndWorkingDay("Friday");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);
		candidate.setVacancy(vacancy);

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("5");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setJoinedStartDate("23.09.2023");
		testData.setBasicPay("1000");
		testData.setProjectAllowance("1000");
		testData.setMealTransportAllowanceDay("1000");
		testData.setMealTransportAllowanceMonth("1000");
		testData.setEarnLeave("10");
		testData.setCasualLeave("10");
		testData.setMedicalLeave("10");

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Java Developer Banking 09:00 04:00 Monday Friday 23.09.2023 1000 1000 1000 1000 10 10 10";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId5ValuesNullTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. {Position} {Department} {StartWorkingHour} {EndWorkingHour} {StartWorkingDay} {EndWorkingDay} <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>");

		Position position = new Position();
		position.setName("Java Developer");

		Department department = new Department();
		department.setName("Banking");

		Vacancy vacancy = new Vacancy();
		vacancy.setPosition(position);
		vacancy.setDepartment(department);
		vacancy.setStartWorkingHour("09:00");
		vacancy.setEndWorkingHour("04:00");
		vacancy.setStartWorkingDay("Monday");
		vacancy.setEndWorkingDay("Friday");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);
		candidate.setVacancy(vacancy);

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("5");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setJoinedStartDate(null);
		testData.setBasicPay(null);
		testData.setProjectAllowance(null);
		testData.setMealTransportAllowanceDay(null);
		testData.setMealTransportAllowanceMonth(null);
		testData.setEarnLeave(null);
		testData.setCasualLeave(null);
		testData.setMedicalLeave(null);

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Java Developer Banking 09:00 04:00 Monday Friday <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	public void showEmailBodyDraftForTemplateId5ValuesEmptyTest() {
		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. {Position} {Department} {StartWorkingHour} {EndWorkingHour} {StartWorkingDay} {EndWorkingDay} <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>");

		Position position = new Position();
		position.setName("Java Developer");

		Department department = new Department();
		department.setName("Banking");

		Vacancy vacancy = new Vacancy();
		vacancy.setPosition(position);
		vacancy.setDepartment(department);
		vacancy.setStartWorkingHour("09:00");
		vacancy.setEndWorkingHour("04:00");
		vacancy.setStartWorkingDay("Monday");
		vacancy.setEndWorkingDay("Friday");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);
		candidate.setVacancy(vacancy);

		when(loader.getEmailTemplate(anyString())).thenReturn(template);
		when(candidateService.getCandidateById(anyLong())).thenReturn(candidate);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("5");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setJoinedStartDate("");
		testData.setBasicPay("");
		testData.setProjectAllowance("");
		testData.setMealTransportAllowanceDay("");
		testData.setMealTransportAllowanceMonth("");
		testData.setEarnLeave("");
		testData.setCasualLeave("");
		testData.setMedicalLeave("");

		ResponseEntity<String> response = emailController.showEmailBodyDraft(testData);

		String expectedBody = "Hello, Mr Kyaw. Java Developer Banking 09:00 04:00 Monday Friday <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBody, response.getBody());
	}

	@Test
	void sendMailEmailPreparationNullTest() {
		Integer isRecalled = 1;
		long candidateId = 1L;
		String templateId = "3";
		long vacancyId = 1L;
		EmailPreparationData emailPreparation = null;
		Authentication authentication = mock(Authentication.class);
		Model model = mock(Model.class);

		ResponseEntity<String> response = emailController.sendMail(emailPreparation, authentication, candidateId,
				templateId, vacancyId, isRecalled);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	void sendMailTemplateNullTest() {
		Integer isRecalled = 1;
		long candidateId = 1L;
		String templateId = "3";
		long vacancyId = 1L;
		EmailPreparationData emailPreparation = new EmailPreparationData();
		emailPreparation.setTemplateId("3");
		Authentication authentication = mock(Authentication.class);
		Model model = mock(Model.class);
		when(loader.getEmailTemplate("3")).thenReturn(null);
		ResponseEntity<String> response = emailController.sendMail(emailPreparation, authentication, candidateId,
				templateId, vacancyId, isRecalled);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	public void sendMailTemplateId3Test() {
		Integer isRecalled = 1;
		String templateId = "3";
		long vacancyId = 1L;
		Authentication authentication = mock(Authentication.class);
		Model model = mock(Model.class);
		EmailPreparationData emailPreparation = new EmailPreparationData();
		emailPreparation.setInterview_id(1);
		emailPreparation.setTemplateId("3");
		emailPreparation.setSubject("Onsite Interview Invitation");
		emailPreparation.setRecipientAddress("test@gmail.com");
		emailPreparation.setAppointmentDate("20.03.2023");
		emailPreparation.setAppointmentDay("Friday");
		emailPreparation.setAppointmentStartTime("01:00");
		emailPreparation.setAppointmentEndTime("02:00");

		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on From {StartDate} To {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> {Location}");

		Vacancy vacancy = new Vacancy();
		vacancy.setId(1L);

		User user = new User();
		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);
		candidate.setVacancy(vacancy);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");
		interview.setLocation("MICT");

		Email email = new Email();
		MailHistory mailHistory = new MailHistory();
		long candidateId = 1;

		Candidate savedCandidate = new Candidate();
		savedCandidate.setId(2L);
		savedCandidate.setVacancy(vacancy);

		CandidateInterview candidateInterview = new CandidateInterview();

		CandidateStatus candidateStatus = new CandidateStatus();
		candidateStatus.setId(3L);

		CandidateStatus newCandidateStatus = new CandidateStatus();
		newCandidateStatus.setId(4L);
		newCandidateStatus.setCandidate(savedCandidate);

		List<CandidateStatus> candidateStatusList = new ArrayList<CandidateStatus>();
		candidateStatusList.add(candidateStatus);

		when(loader.getEmailTemplate("3")).thenReturn(template);
		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("success");
		when(mailHistoryService.storeMailHistory(any(MailHistory.class))).thenReturn(mailHistory);
		when(candidateInterviewService.saveCandidateInterview(any(CandidateInterview.class)))
				.thenReturn(candidateInterview);
		when(vacancyService.getVacancyByIdWithEntity(vacancyId)).thenReturn(vacancy);
		ResponseEntity<String> response = emailController.sendMail(emailPreparation, authentication, candidateId,
				templateId, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

	}

	@Test
	public void sendMailFailTest() {
		Integer isRecalled = 1;
		String templateId = "3";
		long vacancyId = 1L;
		Authentication authentication = mock(Authentication.class);
		Model model = mock(Model.class);
		EmailPreparationData emailPreparation = new EmailPreparationData();
		emailPreparation.setInterview_id(1);
		emailPreparation.setTemplateId("3");
		emailPreparation.setSubject("Onsite Interview Invitation");
		emailPreparation.setRecipientAddress("test@gmail.com");
		emailPreparation.setAppointmentDate("20.03.2023");
		emailPreparation.setAppointmentDay("Friday");
		emailPreparation.setAppointmentStartTime("01:00");
		emailPreparation.setAppointmentEndTime("02:00");

		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> {Location}");

		User user = new User();
		Candidate candidate = new Candidate();
		candidate.setName("Aye");
		candidate.setGender(true);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");
		interview.setLocation("MICT");

		Email email = new Email();
		MailHistory mailHistory = new MailHistory();
		long candidateId = 1;

		CandidateInterview candidateInterview = new CandidateInterview();

		when(loader.getEmailTemplate("3")).thenReturn(template);
		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("Email send error");
		ResponseEntity<String> response = emailController.sendMail(emailPreparation, authentication, candidateId,
				templateId, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Email send error", response.getBody());

	}

	@Test
	public void sendMailTemplateId4Test() {
		Integer isRecalled = 1;
		String templateId = "4";
		long vacancyId = 1L;
		Authentication authentication = mock(Authentication.class);
		Model model = mock(Model.class);
		EmailPreparationData emailPreparation = new EmailPreparationData();
		emailPreparation.setInterview_id(1);
		emailPreparation.setTemplateId("4");
		emailPreparation.setSubject("Online Interview Invitation");
		emailPreparation.setRecipientAddress("test@gmail.com");
		emailPreparation.setAppointmentDate("20.03.2023");
		emailPreparation.setAppointmentDay("Friday");
		emailPreparation.setAppointmentStartTime("01:00");
		emailPreparation.setAppointmentEndTime("02:00");
		emailPreparation.setZoomMeetingUrl("zoom.com");
		emailPreparation.setZoomMeetingId("123");
		emailPreparation.setZoomMeetingPasscode("111");

		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. Interview on {StartDate} {EndDate}. {StartTime} {EndTime} <b>[AppointmentDate]</b> <b>[AppointmentDay]</b> <b>[AppointmentStartTime]</b> <b>[AppointmentEndTime]</b> <b>[ZoomURL]</b> <b>[MeetingID]</b> <b>[MeetingPasscode]</b>");

		User user = new User();
		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");

		Email email = new Email();
		MailHistory mailHistory = new MailHistory();
		long candidateId = 1;

		CandidateInterview candidateInterview = new CandidateInterview();

		when(loader.getEmailTemplate("4")).thenReturn(template);
		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(interviewService.getInterviewById(anyLong())).thenReturn(interview);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("success");
		when(mailHistoryService.storeMailHistory(any(MailHistory.class))).thenReturn(mailHistory);
		when(candidateInterviewService.saveCandidateInterview(any(CandidateInterview.class)))
				.thenReturn(candidateInterview);

		ResponseEntity<String> response = emailController.sendMail(emailPreparation, authentication, candidateId,
				templateId, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

	}

	@Test
	public void sendMailTemplateId5Test() {
		Integer isRecalled = 1;
		String templateId = "5";
		long vacancyId = 1L;
		Authentication authentication = mock(Authentication.class);
		Model model = mock(Model.class);

		Position position = new Position();
		position.setName("Java Developer");

		Department department = new Department();
		department.setName("Banking");

		Vacancy vacancy = new Vacancy();
		vacancy.setPosition(position);
		vacancy.setDepartment(department);
		vacancy.setStartWorkingHour("09:00");
		vacancy.setEndWorkingHour("04:00");
		vacancy.setStartWorkingDay("Monday");
		vacancy.setEndWorkingDay("Friday");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);
		candidate.setVacancy(vacancy);

		EmailPreparationData emailPreparation = new EmailPreparationData();
		emailPreparation.setInterview_id(1);
		emailPreparation.setTemplateId("5");
		emailPreparation.setSubject("job offer");
		emailPreparation.setRecipientAddress("test@gmail.com");
		emailPreparation.setJoinedStartDate("23.09.2023");
		emailPreparation.setBasicPay("1000");
		emailPreparation.setProjectAllowance("1000");
		emailPreparation.setMealTransportAllowanceDay("1000");
		emailPreparation.setMealTransportAllowanceMonth("1000");
		emailPreparation.setEarnLeave("10");
		emailPreparation.setCasualLeave("10");
		emailPreparation.setMedicalLeave("10");

		EmailTemplate template = new EmailTemplate();
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. {Position} {Department} {StartWorkingHour} {EndWorkingHour} {StartWorkingDay} {EndWorkingDay} <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>");

		User user = new User();

		Interview interview = new Interview();
		interview.setStartDate(LocalDate.of(2023, 8, 15));
		interview.setEndDate(LocalDate.of(2023, 8, 16));
		interview.setStartTime("01:00");
		interview.setEndTime("02:00");

		Email email = new Email();
		MailHistory mailHistory = new MailHistory();
		long candidateId = 1;

		CandidateInterview candidateInterview = new CandidateInterview();

		when(loader.getEmailTemplate("5")).thenReturn(template);
		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("success");
		when(mailHistoryService.storeMailHistory(any(MailHistory.class))).thenReturn(mailHistory);

		ResponseEntity<String> response = emailController.sendMail(emailPreparation, authentication, candidateId,
				templateId, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

		verify(loader, times(1)).getEmailTemplate(anyString());
		verify(emailService, times(1)).getCurrentUser(authentication);
		verify(emailService, times(1)).sendMailWithCCAttachment(any(Email.class));
		verify(mailHistoryService, times(1)).storeMailHistory(any(MailHistory.class));
	}

	@Test
	void customizeEmailbodyEmailPreparationNullTest() {
		Integer isRecalled = 1;
		EmailPreparationData emailPreparation = null;
		long candidateId = 1L;
		long interviewId = 1L;
		long vacancyId = 1L;

		Model model = mock(Model.class);

		String result = emailController.customizeEmailbody(emailPreparation, model, candidateId, interviewId, vacancyId,
				isRecalled);

		assertEquals("email/customize-mail", result);
	}

	@Test
	void customizeEmailbodyTemplateNullTest() {
		Integer isRecalled = 1;
		EmailPreparationData emailPreparation = new EmailPreparationData();
		emailPreparation.setTemplateId("3");
		long candidateId = 1L;
		long interviewId = 1L;
		long vacancyId = 1L;

		Model model = mock(Model.class);
		when(loader.getEmailTemplate(anyString())).thenReturn(null);

		String result = emailController.customizeEmailbody(emailPreparation, model, candidateId, interviewId, vacancyId,
				isRecalled);
		assertEquals("email/customize-mail", result);

	}

	@Test
	public void customizeEmailbodyForTemplateId5ValuesNullTest() {
		Integer isRecalled = 1;
		Model model = mock(Model.class);
		EmailTemplate template = new EmailTemplate();
		long candidateId = 1L;
		long interviewId = 1L;
		long vacancyId = 1L;
		template.setBody(
				"Hello, {HonourablePrefix} {CandidateName}. {Position} {Department} {StartWorkingHour} {EndWorkingHour} {StartWorkingDay} {EndWorkingDay} <b>[JoinedStartDate]</b> <b>[BasicPay]</b> <b>[ProjectAllowance]</b> <b>[MealTransportAllowanceDay]</b> <b>[MealTransportAllowanceMonth]</b> <b>[EarnLeave]</b> <b>[CasualLeave]</b> <b>[MedicatlLeave]</b>");

		Position position = new Position();
		position.setName("Java Developer");

		Department department = new Department();
		department.setName("Banking");

		Vacancy vacancy = new Vacancy();
		vacancy.setPosition(position);
		vacancy.setDepartment(department);
		vacancy.setStartWorkingHour("09:00");
		vacancy.setEndWorkingHour("04:00");
		vacancy.setStartWorkingDay("Monday");
		vacancy.setEndWorkingDay("Friday");

		Candidate candidate = new Candidate();
		candidate.setName("Kyaw");
		candidate.setGender(false);
		candidate.setVacancy(vacancy);

		EmailPreparationData testData = new EmailPreparationData();
		testData.setTemplateId("5");
		testData.setCandidate_id(1L);
		testData.setInterview_id(1L);
		testData.setJoinedStartDate(null);
		testData.setBasicPay(null);
		testData.setProjectAllowance(null);
		testData.setMealTransportAllowanceDay(null);
		testData.setMealTransportAllowanceMonth(null);
		testData.setEarnLeave(null);
		testData.setCasualLeave(null);
		testData.setMedicalLeave(null);

	}

	@Test
	public void sendCustomizedMailTemplateId3Test() {
		Integer isRecalled = 1;
		Authentication authentication = mock(Authentication.class);
		long candidateId = 1;
		long interviewId = 1;
		Interview interview = new Interview();
		Candidate candidate = new Candidate();
		String templateId = "3";
		long vacancyId = 1L;

		Email email = new Email();

		User user = new User();

		MailHistory mailHistory = new MailHistory();

		CandidateInterview candidateInterview = new CandidateInterview();

		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("success");
		when(mailHistoryService.storeMailHistory(any(MailHistory.class))).thenReturn(mailHistory);
		when(interviewService.getInterviewById(interviewId)).thenReturn(interview);
		when(candidateInterviewService.saveCandidateInterview(any(CandidateInterview.class)))
				.thenReturn(candidateInterview);

		ResponseEntity<String> response = emailController.sendCustomizedMail(email, candidateId, templateId,
				interviewId, authentication, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

	}

	@Test
	public void sendCustomizedMailTemplateId4Test() {
		Integer isRecalled = 1;
		Authentication authentication = mock(Authentication.class);
		long candidateId = 1;
		long interviewId = 1;
		Interview interview = new Interview();
		Candidate candidate = new Candidate();
		String templateId = "4";
		long vacancyId = 1L;

		Email email = new Email();

		User user = new User();

		MailHistory mailHistory = new MailHistory();

		CandidateInterview candidateInterview = new CandidateInterview();

		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("success");
		when(mailHistoryService.storeMailHistory(any(MailHistory.class))).thenReturn(mailHistory);
		when(interviewService.getInterviewById(interviewId)).thenReturn(interview);
		when(candidateInterviewService.saveCandidateInterview(any(CandidateInterview.class)))
				.thenReturn(candidateInterview);

		ResponseEntity<String> response = emailController.sendCustomizedMail(email, candidateId, templateId,
				interviewId, authentication, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

	}

	@Test
	public void sendCustomizedMailTemplateId5Test() {
		Integer isRecalled = 1;
		Authentication authentication = mock(Authentication.class);
		long candidateId = 1;
		long interviewId = 1;
		Interview interview = new Interview();
		Candidate candidate = new Candidate();
		String templateId = "5";
		long vacancyId = 1L;

		Email email = new Email();

		User user = new User();

		MailHistory mailHistory = new MailHistory();

		CandidateInterview candidateInterview = new CandidateInterview();

		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("success");
		when(mailHistoryService.storeMailHistory(any(MailHistory.class))).thenReturn(mailHistory);
		when(candidateService.addCandidate(any(Candidate.class))).thenReturn(candidate);

		ResponseEntity<String> response = emailController.sendCustomizedMail(email, candidateId, templateId,
				interviewId, authentication, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("success", response.getBody());

	}

	@Test
	public void sendCustomizedMailFailTest() {
		Integer isRecalled = 1;
		Authentication authentication = mock(Authentication.class);
		long candidateId = 1;
		long interviewId = 1;
		Interview interview = new Interview();
		Candidate candidate = new Candidate();
		String templateId = "5";
		long vacancyId = 1L;

		Email email = new Email();

		User user = new User();

		MailHistory mailHistory = new MailHistory();

		CandidateInterview candidateInterview = new CandidateInterview();

		when(emailService.getCurrentUser(authentication)).thenReturn(user);
		when(candidateService.getCandidateById(candidateId)).thenReturn(candidate);
		when(emailService.sendMailWithCCAttachment((Email) any(Email.class))).thenReturn("Email send error");

		ResponseEntity<String> response = emailController.sendCustomizedMail(email, candidateId, templateId,
				interviewId, authentication, vacancyId, isRecalled);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Email send error", response.getBody());

	}

}
