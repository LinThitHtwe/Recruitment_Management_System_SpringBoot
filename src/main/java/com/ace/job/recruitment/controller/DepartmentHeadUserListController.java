package com.ace.job.recruitment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/department-head")
public class DepartmentHeadUserListController {
	@Autowired
	private UserService userService;

	public int getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			if (principal instanceof AppUserDetails) {
				AppUserDetails userDetails = (AppUserDetails) principal;
				int id = userDetails.getId();
				return id;
			}
		}

		return -1;
	}

	@GetMapping("/users")
	public String userTable() {
		return "department/department-interviewer-table";
	}

	@GetMapping("/user-data")
	@ResponseBody
	public DataTablesOutput<User> getDataTableData(@Valid DataTablesInput input) {
		User user = userService.getUserById(getCurrentUserId());
		return userService.getUsersForDepartmentHead(input, user.getDepartment().getId());
	}
}
