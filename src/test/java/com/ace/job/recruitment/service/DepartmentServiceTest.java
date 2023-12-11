package com.ace.job.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.repository.DepartmentRepository;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.impl.DepartmentServiceImpl;

public class DepartmentServiceTest {

	@InjectMocks
	private DepartmentService departmentService = new DepartmentServiceImpl();

	@Mock
	private DepartmentRepository departmentRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddDepartment() {
		// Create a sample Department
		Department department = new Department();
		department.setId(1);
		department.setName("Engineering");

		// Mock the repository's save method
		when(departmentRepository.save(any(Department.class))).thenReturn(department);

		// Call the service method
		Department savedDepartment = departmentService.addDepartment(department);

		// Verify the save method was called with the correct argument
		verify(departmentRepository, times(1)).save(department);

		// Assert that the returned department matches the expected department
		assertThat(savedDepartment).isEqualTo(department);
	}

	@Test
	public void testUpdateDepartment() {
		// Create a sample Department
		Department department = new Department();
		department.setId(1);
		department.setName("Engineering");

		// Mock the repository's save method
		when(departmentRepository.save(any(Department.class))).thenReturn(department);

		// Call the service method to update the department
		Department updatedDepartment = departmentService.updateDepartment(department);

		// Verify the save method was called with the correct argument
		verify(departmentRepository, times(1)).save(department);

		// Assert that the returned department matches the expected department
		assertThat(updatedDepartment).isEqualTo(department);
	}

	@Test
	public void testGetAllDepartments() {
		// Create some sample Departments
		Department department1 = new Department();
		department1.setId(1);
		department1.setName("Engineering");

		Department department2 = new Department();
		department2.setId(2);
		department2.setName("Human Resources");

		// Mock the repository's findAll method
		when(departmentRepository.findAll()).thenReturn(Arrays.asList(department1, department2));

		// Call the service method
		List<Department> allDepartments = departmentService.getDepartments();

		// Verify the findAll method was called
		verify(departmentRepository, times(1)).findAll();

		// Assert that the returned list contains all the expected departments
		assertThat(allDepartments).containsExactlyInAnyOrder(department1, department2);
	}

	@Test
	public void testGetDepartmentById() {
		// Create a sample Department
		Department department = new Department();
		department.setId(1);
		department.setName("Engineering");

		// Mock the repository's findById method
		when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

		// Call the service method
		Department foundDepartment = departmentService.getDepartmentById(1);

		// Verify the findById method was called with the correct argument
		verify(departmentRepository, times(1)).findById(1);

		// Assert that the returned department matches the expected department
		assertThat(foundDepartment).isEqualTo(department);
	}

	@Test
	public void testCheckDepartmentDuplicate() {
		// Create a sample Department
		Department department = new Department();
		department.setId(1);
		department.setName("Engineering");

		// Mock the repository's findByName method
		when(departmentRepository.findByName("Engineering")).thenReturn(department);

		// Call the service method
		Department foundDepartment = departmentService.checkDepartmentDuplicate("Engineering");

		// Verify the findByName method was called with the correct argument
		verify(departmentRepository, times(1)).findByName("Engineering");

		// Assert that the returned department matches the expected department
		assertThat(foundDepartment).isEqualTo(department);
	}

	@Test
    public void testCheckDepartmentDuplicate_NotFound() {
        // Mock the repository's findByName method to return null
        when(departmentRepository.findByName("Engineering")).thenReturn(null);

        // Call the service method
        Department foundDepartment = departmentService.checkDepartmentDuplicate("Engineering");

        // Verify the findByName method was called with the correct argument
        verify(departmentRepository, times(1)).findByName("Engineering");

        // Assert that the returned department is null
        assertThat(foundDepartment).isNull();
    }

	@Test
	public void testCheckDepartmentDuplicateForUpdate() {
		// Create a sample Department
		Department department = new Department();
		department.setId(1);
		department.setName("Engineering");

		// Mock the repository's findByNameAndIdNot method
		when(departmentRepository.findByNameAndIdNot("Engineering", 1)).thenReturn(department);

		// Call the service method
		Department foundDepartment = departmentService.checkDepartmentDuplicateForUpdate("Engineering", 1);

		// Verify the findByNameAndIdNot method was called with the correct arguments
		verify(departmentRepository, times(1)).findByNameAndIdNot("Engineering", 1);

		// Assert that the returned department matches the expected department
		assertThat(foundDepartment).isEqualTo(department);
	}

	@Test
	public void testCheckDepartmentDuplicateForUpdate_SameDepartment() {
		// Create a sample Department
		Department department = new Department();
		department.setId(1);
		department.setName("Engineering");

		// Mock the repository's findByNameAndIdNot method to return null (same
		// department)
		when(departmentRepository.findByNameAndIdNot("Engineering", 1)).thenReturn(null);

		// Call the service method
		Department foundDepartment = departmentService.checkDepartmentDuplicateForUpdate("Engineering", 1);

		// Verify the findByNameAndIdNot method was called with the correct arguments
		verify(departmentRepository, times(1)).findByNameAndIdNot("Engineering", 1);

		// Assert that the returned department is null (same department)
		assertThat(foundDepartment).isNull();
	}

}
