package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.repository.DepartmentRepository;
import com.ace.job.recruitment.repository.UserRepository;

@SpringBootTest
@Transactional
@DirtiesContext
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	public void testAddUser() {
		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		User user = new User();
		user.setName("User Name");
		user.setEmail("user@example.com");
		user.setPassword("123456");
		user.setDepartment(department);
		user.setRole("hr");
		user.setStatus(true);
		User savedUser = userService.addUser(user);

		assertNotNull(savedUser.getId());
	}

	@Test
	public void testUpdateUser() {

		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		User user = new User();
		user.setName("User Name");
		user.setEmail("user@example.com");
		user.setPassword("123456");
		user.setDepartment(department);
		user.setRole("hr");
		user.setStatus(true);
		User savedUser = userService.addUser(user);

		// Modify user properties
		savedUser.setName("Updated Name");
		User updatedUser = userService.updateUser(savedUser);

		assertEquals("Updated Name", updatedUser.getName());
	}

	@Test
	public void testGetUserById() {
		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		User user = new User();
		user.setName("User Name");
		user.setEmail("user@example.com");
		user.setPassword("123456");
		user.setDepartment(department);
		user.setRole("hr");
		user.setStatus(true);
		User savedUser = userService.addUser(user);

		User retrievedUser = userService.getUserById(savedUser.getId());

		assertNotNull(retrievedUser);
		assertEquals(savedUser.getId(), retrievedUser.getId());
	}

	@Test
	public void testChangeUserStatus() {
		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		User user = new User();
		user.setName("User Name");
		user.setEmail("user@example.com");
		user.setPassword("123456");
		user.setDepartment(department);
		user.setRole("hr");
		user.setStatus(true);
		User savedUser = userService.addUser(user);

		userService.changeUserStatus(savedUser.getId(), false);

		User updatedUser = userRepository.findById(savedUser.getId()).orElse(null);

		assertNotNull(updatedUser);
	}
}