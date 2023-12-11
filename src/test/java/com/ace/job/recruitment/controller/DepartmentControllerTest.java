package com.ace.job.recruitment.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ace.job.recruitment.dto.DepartmentDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

	private MockMvc mockMvc;

	@Mock
	private DepartmentService departmentService;

	@Mock
	private UserService userService;

	@InjectMocks
	private DepartmentController departmentController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
	}

	@Test
	public void createDepartmentPostValidInputTest() throws Exception {
		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setName("Banking");
		departmentDTO.setAddress("MICT");
		when(departmentService.checkDepartmentDuplicate("Banking")).thenReturn(null);
		mockMvc.perform(post("/admin/add-department").flashAttr("departmentDTO", departmentDTO))
				.andExpect(status().isOk());

		verify(departmentService, times(1)).addDepartment(any(Department.class));
	}

	@Test
	public void createDepartmentPostDuplicateInputTest() throws Exception {
		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setName("Banking");
		departmentDTO.setAddress("MICT");

		when(departmentService.checkDepartmentDuplicate("Banking")).thenReturn(new Department());

		mockMvc.perform(post("/admin/add-department").flashAttr("departmentDTO", departmentDTO))
				.andExpect(status().isBadRequest()).andExpect(content().string(containsString("already exists")));

		verify(departmentService, times(1)).checkDepartmentDuplicate("Banking");
	}

	@Test
    public void duplicateDepartmentAddDuplicateNotExistTest() throws Exception {
        when(departmentService.checkDepartmentDuplicate("Banking"))
            .thenReturn(null);

        mockMvc.perform(post("/admin/duplicate-department-add")
                .param("name", "Banking"))
                .andExpect(status().isOk());
    }

	@Test
	public void showAllDepartmentsTest() throws Exception {
		mockMvc.perform(get("/admin/department-list")).andExpect(status().isOk())
				.andExpect(view().name("department/department-list"));
	}

	@Test
	public void showDepartmentDetailDepartmentExistTest() throws Exception {
		Department department = new Department();
		department.setId(1);
		department.setName("Banking");

		User createdUser = new User();
		createdUser.setName("Mya");
	}

	@Test
	public void showDepartmentDetailDepartmentUpdatedTest() throws Exception {
		Department department = new Department();
		department.setId(1);
		department.setName("Banking");
		department.setUpdatedUserId(1);
		department.setCreatedUserId(2);
		department.setUpdatedUsername("Aye");

		User updatedUser = new User();
		updatedUser.setId(1);
		updatedUser.setName("Aye");

		when(departmentService.getDepartmentById(1)).thenReturn(department);
		when(userService.getUserById(department.getCreatedUserId())).thenReturn(null);
		when(userService.getUserById(department.getUpdatedUserId())).thenReturn(updatedUser);

		mockMvc.perform(get("/admin/department-detail").param("id", "1")).andExpect(status().isOk());

		verify(departmentService, times(1)).getDepartmentById(1);
		verify(userService, times(1)).getUserById(department.getCreatedUserId());
	}

	@Test
    public void showDepartmentDetailDepartmentNotExistTest() throws Exception {
        when(departmentService.getDepartmentById(1)).thenReturn(null);

        mockMvc.perform(get("/admin/department-detail")
                .param("id", "1"))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).getDepartmentById(1);
    }

	@Test
	public void updateDepartmentPostValidInputTest() throws Exception {
		DepartmentDTO oneDepartment = new DepartmentDTO();
		oneDepartment.setId(1);
		oneDepartment.setName("Banking");
		oneDepartment.setAddress("MICT");

		when(departmentService.checkDepartmentDuplicateForUpdate("Banking", 1)).thenReturn(null);

		Department existingDepartment = new Department();
		existingDepartment.setId(1);
		when(departmentService.getDepartmentById(1)).thenReturn(existingDepartment);
		mockMvc.perform(post("/admin/update-department").flashAttr("oneDepartment", oneDepartment))
				.andExpect(status().isOk());

		verify(departmentService, times(1)).updateDepartment(any(Department.class));
	}

	@Test
	public void updateDepartmentPostDuplicateInputTest() throws Exception {
		DepartmentDTO oneDepartment = new DepartmentDTO();
		oneDepartment.setId(1);
		oneDepartment.setName("Banking");
		oneDepartment.setAddress("MICT");

		when(departmentService.checkDepartmentDuplicateForUpdate("Banking", 1)).thenReturn(new Department());

		mockMvc.perform(post("/admin/update-department").flashAttr("oneDepartment", oneDepartment))
				.andExpect(status().isBadRequest()).andExpect(content().string(containsString("already exists")));

		verify(departmentService, times(1)).checkDepartmentDuplicateForUpdate("Banking", 1);
	}

	@Test
    public void duplicateDepartmentUpdateDuplicateExistTest() throws Exception {
        when(departmentService.checkDepartmentDuplicateForUpdate("Banking",1))
            .thenReturn(new Department());

        mockMvc.perform(post("/admin/duplicate-department-update")
                .param("name", "Banking").param("id", "1"))
                .andExpect(status().isOk());
        
    }

	@Test
    public void duplicateDepartmentUpdateDuplicateNotExistTest() throws Exception {
        when(departmentService.checkDepartmentDuplicateForUpdate("Banking",1))
            .thenReturn(null);

        mockMvc.perform(post("/admin/duplicate-department-update")
                .param("name", "Banking").param("id", "1"))
                .andExpect(status().isOk());
    }
}