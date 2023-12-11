package com.ace.job.recruitment.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace.job.recruitment.entity.User;

public interface UserService {
	User addUser(User user);

	User updateUser(User user);

	User getUserById(int id);

	User getUserByRole(String role);

	@Transactional
	void changeUserStatus(int id, boolean status);

	DataTablesOutput<User> getDataTableData(@Valid DataTablesInput input);

	List<User> getUserByEmail(String email);

	DataTablesOutput<User> getUsersForDepartmentHead(@Valid DataTablesInput input, int departmentId);

	List<User> getInterviewers(int departmentId);

}
