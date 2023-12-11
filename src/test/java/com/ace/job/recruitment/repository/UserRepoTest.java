package com.ace.job.recruitment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.User;

@SpringBootTest
@Transactional
@DirtiesContext
public class UserRepoTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	public void testFindAllByEmail() {

		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		// Save sample users
		User user1 = new User();
		user1.setName("user1");
		user1.setEmail("user1@example.com");
		user1.setPassword("123456");
		user1.setDepartment(department);
		user1.setRole("hr");
		user1.setStatus(true);

		User user2 = new User();
		user2.setName("user2");
		user2.setEmail("user2@example.com");
		user2.setPassword("1234567");
		user2.setDepartment(department);
		user2.setRole("hr");
		user2.setStatus(false);

		userRepository.saveAll(Arrays.asList(user1, user2));

		// Test repository method
		List<User> result = userRepository.findAllByEmail("user1@example.com");

		assertEquals(1, result.size());
		assertEquals("user1@example.com", result.get(0).getEmail());
	}

	@Test
	public void testFindByEmail() {

		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		// Save a sample user
		User user = new User();
		user.setName("User Name");
		user.setEmail("user@example.com");
		user.setPassword("123456");
		user.setDepartment(department);
		user.setRole("hr");
		user.setStatus(true);
		userRepository.save(user);
		// Test repository method
		User result = userRepository.findByEmail("user@example.com");

		assertNotNull(result);
		assertEquals("user@example.com", result.getEmail());
	}

	@Test
	public void testUpdateUserStatus() {

		Department department = new Department();
		department.setAddress("place");
		department.setCreatedDateTime("02/03/22");
		department.setCreatedUserId(1);
		department.setName("Dname");
		departmentRepository.save(department);

		// Save a sample user
		User user = new User();
		user.setName("User Name");
		user.setEmail("user@example.com");
		user.setPassword("123456");
		user.setDepartment(department);
		user.setRole("hr");
		user.setStatus(true);
		userRepository.save(user);

		// Test repository method
		userRepository.updateUserStatus(user.getId(), false);

		// Fetch the user after status update
		User updatedUser = userRepository.findById(user.getId()).orElse(null);
		assertNotNull(updatedUser);
		System.out.println(updatedUser.isStatus());

	}
}