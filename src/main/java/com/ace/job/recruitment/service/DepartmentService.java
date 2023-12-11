package com.ace.job.recruitment.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace.job.recruitment.entity.Department;

public interface DepartmentService {
	public int getCurrentUserId();

	Department addDepartment(Department department);

	Department updateDepartment(Department department);

	Department getDepartmentById(int id);

	Department checkDepartmentDuplicate(String name);

	Department checkDepartmentDuplicateForUpdate(String name, int id);

	List<Department> getDepartments();

	DataTablesOutput<Department> getDataTableData(@Valid DataTablesInput input);

}