package com.ace.job.recruitment.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.repository.DepartmentRepository;
import com.ace.job.recruitment.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	@Override
	public Department addDepartment(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public Department updateDepartment(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public Department getDepartmentById(int id) {
		return departmentRepository.findById(id).get();
	}

	@Override
	public Department checkDepartmentDuplicate(String name) {
		return departmentRepository.findByName(name);
	}

	@Override
	public Department checkDepartmentDuplicateForUpdate(String name, int id) {
		return departmentRepository.findByNameAndIdNot(name, id);
	}

	@Override
	public List<Department> getDepartments() {
		return departmentRepository.findAll();
	}

	@Override
	public DataTablesOutput<Department> getDataTableData(@Valid DataTablesInput input) {
		return departmentRepository.findAll(input);
	}

	@Override
	public int getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			// Get the principal (user) object from the authentication
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				// Cast the principal to your AppUserDetails UserDetails class
				AppUserDetails userDetails = (AppUserDetails) principal;

				// Access the user's ID from the UserDetails object
				int userId = userDetails.getId();
				return userId;
			}
		}

		return -1;
	}

}