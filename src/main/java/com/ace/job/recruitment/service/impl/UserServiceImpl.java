package com.ace.job.recruitment.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.repository.UserRepository;
import com.ace.job.recruitment.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);

	}

	@Override
	public User getUserById(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	public User getUserByRole(String role) {
		return userRepository.findByRole(role);
	}

	@Override
	@Transactional
	public void changeUserStatus(int id, boolean status) {
		userRepository.updateUserStatus(id, status);
	}

	@Override
	public DataTablesOutput<User> getDataTableData(DataTablesInput input) {
		return userRepository.findAll(input);
	}

	@Override
	public List<User> getUserByEmail(String email) {
		return userRepository.findAllByEmail(email);
	}

	@Override
	public DataTablesOutput<User> getUsersForDepartmentHead(DataTablesInput input, int departmentId) {
		Specification<User> departmentFilter = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("department").get("id"), departmentId);

		return userRepository.findAll(input, departmentFilter);
	}

	@Override
	public List<User> getInterviewers(int departmentId) {
		return userRepository.findAllByDepartmentIdAndStatusIsFalse(departmentId);
	}

}
