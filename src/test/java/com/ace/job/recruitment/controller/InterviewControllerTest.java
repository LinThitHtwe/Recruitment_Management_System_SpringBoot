package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ace.job.recruitment.dto.InterviewDTO;
import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.InterviewService;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.UserService;
import com.ace.job.recruitment.service.VacancyService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class InterviewControllerTest {
	private MockMvc mockMvc;

	@Mock
	private InterviewService interviewService;
	@Mock
	private VacancyService vacancyService;
	@Mock
	private UserService userService;
	@Mock
	private PositionService positionService;
	@Mock
	private DepartmentService departmentService;

	@InjectMocks
	private InterviewController interviewController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(interviewController).build();
	}

	@Test
	public void chooseInterviewerFoundTest() {
		long vacancyId = 1L;
		VacancyDto mockVacancyDTO = mock(VacancyDto.class);
		Department mockDepartment = mock(Department.class);
		User mockUser1 = new User();
		User mockUser2 = new User();

		when(vacancyService.getVacancyById(vacancyId)).thenReturn(mockVacancyDTO);
		when(mockVacancyDTO.getDepartment()).thenReturn(mockDepartment);

		ResponseEntity<List<User>> response = interviewController.chooseInterviewer(vacancyId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void chooseInterviewerNotFoundTest() {
		long vacancyId = 1L;
		VacancyDto mockVacancyDTO = mock(VacancyDto.class);
		Department mockDepartment = mock(Department.class);
		User mockUser1 = new User();
		User mockUser2 = new User();

		when(vacancyService.getVacancyById(vacancyId)).thenReturn(mockVacancyDTO);
		when(mockVacancyDTO.getDepartment()).thenReturn(mockDepartment);

		ResponseEntity<List<User>> response = interviewController.chooseInterviewer(vacancyId);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(vacancyService, times(1)).getVacancyById(anyLong());
	}

	@Test
	public void addInterviewPostValidInputTest() throws Exception {
		Authentication authentication = mock(Authentication.class);
		InterviewDTO interviewDTO = new InterviewDTO();
		interviewDTO.setStartDate("2023-08-10");
		interviewDTO.setEndDate("2023-08-11");

		when(vacancyService.getVacancyByIdForInterview(anyLong())).thenReturn(new Vacancy());
		when(userService.getUserById(anyInt())).thenReturn(new User());

		when(interviewService.getCurrentUserId(authentication)).thenReturn(1);

		mockMvc.perform(post("/hr/add-interview").param("vacancyId", "1").param("interviewerId", "1")
				.flashAttr("interviewDTO", interviewDTO).principal(authentication)).andExpect(status().isOk());
		verify(interviewService, times(1)).addInterview(any());
	}

	@Test
	public void showInterviewListTest() throws Exception {
		List<Interview> interviewList = new ArrayList<>();
		Interview interview1 = new Interview();
		interview1.setId(1);
		interview1.setStage(1);
		interviewList.add(interview1);

		Interview interview2 = new Interview();
		interview2.setId(2);
		interview2.setStage(1);
		interviewList.add(interview2);

		when(interviewService.getAllInterviews()).thenReturn(interviewList);

		mockMvc.perform(MockMvcRequestBuilders.get("/hr/interview-list"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("interview/interview-list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("interviewList"));

		verify(interviewService, times(1)).getAllInterviews();
	}

	@Test
	public void getInterviewDetailFoundUpdateCancelIdsExist() throws Exception {
		Interview interview = new Interview();
		interview.setId(1L);
		interview.setStartDate(LocalDate.now());
		interview.setEndDate(LocalDate.now());
		interview.setUpdatedUserId(2);
		interview.setCanceledUserId(3);

		when(interviewService.getInterviewById(1L)).thenReturn(interview);

		when(userService.getUserById(anyInt())).thenReturn(new User());

		User updatedUser = new User();
		updatedUser.setName("Mya");
		when(userService.getUserById(2)).thenReturn(updatedUser);

		User canceledUser = new User();
		canceledUser.setName("Aye");
		when(userService.getUserById(3)).thenReturn(canceledUser);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/hr/interview-detail").param("id", "1").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedUserName").value("Mya"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.canceledUsername").value("Aye"));

		verify(interviewService, times(1)).getInterviewById(1L);
		verify(userService, times(1)).getUserById(2);
		verify(userService, times(1)).getUserById(3);

	}

	@Test
	public void getInterviewDetailFoundUpdateCancelIdsNotExist() throws Exception {
		Interview interview = new Interview();
		interview.setId(1L);
		interview.setStartDate(LocalDate.now());
		interview.setEndDate(LocalDate.now());
		interview.setUpdatedUserId(0);
		interview.setCanceledUserId(0);

		when(interviewService.getInterviewById(1L)).thenReturn(interview);

		when(userService.getUserById(anyInt())).thenReturn(new User());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/hr/interview-detail").param("id", "1").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedUserName").doesNotExist())
				.andExpect(MockMvcResultMatchers.jsonPath("$.canceledUsername").doesNotExist());

		verify(interviewService, times(1)).getInterviewById(1L);
		verify(userService, times(1)).getUserById(anyInt());

	}

	@Test
    public void getInterviewDetailNotFoundTest() throws Exception {
        when(interviewService.getInterviewById(1L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/hr/interview-detail")
            .param("id", "1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound()); 

        verify(interviewService, times(1)).getInterviewById(1L);

        verifyNoInteractions(userService);
    }

	@Test
	public void cancelInterviewTest() throws Exception {
		Long interviewId = 1L;

		// Mock InterviewService
		when(interviewService.getInterviewById(interviewId)).thenReturn(Mockito.mock(Interview.class));

		// Perform the cancel-interview request
		mockMvc.perform(get("/hr/cancel-interview").param("id", interviewId.toString())).andExpect(status().isOk());

		verify(interviewService.getInterviewById(interviewId)).setStatus(false);
		verify(interviewService.getInterviewById(interviewId)).setCanceledUserId(anyInt());
		verify(interviewService).updateInterview((Interview) any(Interview.class));
	}

}
