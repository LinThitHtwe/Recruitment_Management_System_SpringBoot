package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.ace.job.recruitment.dto.DashboardCountDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.ChartService;
import com.ace.job.recruitment.service.DashboardService;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.PositionService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class DashboardControllerTest {
	@Mock
	Model model;
	@Mock
	Authentication authentication;
	@Mock
	private DepartmentService departmentService;
	@Mock
	private PositionService positionService;
	@Mock
	private DashboardService dashboardService;
	@Mock
	private ChartService chartService;

	@InjectMocks
	private DashboardController dashboardController;

	@Test
	public void showDashboardHRTest() {
		int minYear = 2020;

		List<Department> mockDepartmentList = new ArrayList<>();
		Department department = new Department();
		department.setName("Banking");
		mockDepartmentList.add(department);

		List<Position> mockPositionList = new ArrayList<>();
		Position position = new Position();
		position.setName("Java Developer");
		mockPositionList.add(position);

		when(dashboardService.getMinYear()).thenReturn(minYear);
		when(departmentService.getDepartments()).thenReturn(mockDepartmentList);
		when(positionService.getAllPositions()).thenReturn(mockPositionList);

		String viewName = dashboardController.showDashboardHR(model);

		verify(dashboardService, times(1)).getMinYear();
		verify(departmentService, times(1)).getDepartments();
		verify(positionService, times(1)).getAllPositions();
		verify(model, times(1)).addAttribute("minYear", minYear);
		verify(model, times(1)).addAttribute("departmentListForChart", mockDepartmentList);
		verify(model, times(1)).addAttribute("positionListForChart", mockPositionList);

		assertEquals("dashboard/dashboard", viewName);
	}

	@Test
	public void getDashboardCountsTest() {
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();
		dashboardCountDTO.setVacancyCount(10L);
		when(dashboardService.getDashboardCounts(anyInt())).thenReturn(dashboardCountDTO);

		ResponseEntity<DashboardCountDTO> response = dashboardController.getDashboardCounts(anyInt());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void showDashboardByDepartmentTest() {
		int minYear = 2020;

		AppUserDetails userDetails = mock(AppUserDetails.class);
		User user = mock(User.class);
		Department department = mock(Department.class);
		when(dashboardService.getMinYear()).thenReturn(minYear);
		when(userDetails.getUser()).thenReturn(user);
		when(user.getDepartment()).thenReturn(department);
		when(department.getName()).thenReturn("Banking");
		when(authentication.getPrincipal()).thenReturn(userDetails);

		List<Position> mockPositionList = new ArrayList<>();
		Position position = new Position();
		position.setName("Java Developer");
		mockPositionList.add(position);
		when(positionService.getAllPositions()).thenReturn(mockPositionList);

		String viewName = dashboardController.showDashboardByDepartment(model, authentication);

		verify(dashboardService, times(1)).getMinYear();
		verify(positionService, times(1)).getAllPositions();
		verify(model, times(1)).addAttribute("minYear", minYear);
		verify(model, times(1)).addAttribute("positionListForChart", mockPositionList);
		verify(model, times(1)).addAttribute("departmentName", "Banking");
		assertEquals("dashboard/dashboard-department", viewName);
	}

	@Test
	public void getDashboardCountsForDepartment() {
		int year = 2023;
		int departmentId = 1;
		AppUserDetails userDetails = mock(AppUserDetails.class);
		Authentication authentication = mock(Authentication.class);
		DashboardCountDTO dashboardCountDTO = new DashboardCountDTO();

		when(chartService.getCurrentDepartmentId(authentication)).thenReturn(departmentId);
		when(dashboardService.getDashboardCountsForDepartment(year, departmentId)).thenReturn(dashboardCountDTO);

		ResponseEntity<DashboardCountDTO> response = dashboardController.getDashboardCountsForDepartment(year,
				authentication);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
