package com.ace.job.recruitment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.service.UserService;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (userService.getUserByRole("Default-Admin") == null) {
			User defaultAdmin = new User();
			defaultAdmin.setName("default-admin");
			defaultAdmin.setEmail("defaultAdmin@gmail.com");
			defaultAdmin.setStatus(false);
			defaultAdmin.setPassword(passwordEncoder.encode("default-admin"));
			defaultAdmin.setRole("Default-Admin");
			defaultAdmin.setDepartment(null);
			userService.addUser(defaultAdmin);
		}
	}

}
