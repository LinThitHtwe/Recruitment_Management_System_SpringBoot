package com.ace.job.recruitment.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.dto.DepartmentDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.service.DepartmentService;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/admin")
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;

	@Autowired
	UserService userService;

	// Method to add department
	@PostMapping("/add-department")
	public ResponseEntity<String> createDepartmentPost(@ModelAttribute("departmentDTO") DepartmentDTO departmentDTO,
			Model model) {

		// check name duplication
		Department departmentWithSameName = departmentService.checkDepartmentDuplicate(departmentDTO.getName());
		if (departmentWithSameName != null) {
			String errorMessage = departmentDTO.getName() + " already exists";
			Map<String, String> errorResponse = new HashMap<>();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		// data from form
		Department department = new Department();
		department.setName(departmentDTO.getName());
		department.setAddress(departmentDTO.getAddress());

		// format datetime
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		department.setCreatedDateTime(formattedDateTime);

		// created user id
		department.setCreatedUserId(departmentService.getCurrentUserId());

		// store in db
		departmentService.addDepartment(department);

		return ResponseEntity.ok().build();
	}

	// Method to check duplicate department at adding
	@PostMapping("/duplicate-department-add")
	@ResponseBody
	public boolean duplicateDepartmentAdd(@RequestParam("name") String departmentName) {
		Department departmentWithSameName = departmentService.checkDepartmentDuplicate(departmentName);
		return departmentWithSameName != null;
	}

	@GetMapping("/department-data")
	@ResponseBody
	public DataTablesOutput<Department> getDataTableData(@Valid DataTablesInput input) {
		return departmentService.getDataTableData(input);
	}

	// Method to return department list html page
	@GetMapping("/department-list")
	public String showAllDepartments(Model model) {
		return "department/department-list";
	}

	// Method to show department detail
	@GetMapping("/department-detail")
	public ResponseEntity<DepartmentDTO> showDepartmentDetail(@RequestParam("id") int id) {
		Department department = departmentService.getDepartmentById(id);
		if (department != null) {
			User createdUser = userService.getUserById(department.getCreatedUserId());
			User updatedUser = new User();
			if (department.getUpdatedUserId() != 0) {

				updatedUser = userService.getUserById(department.getUpdatedUserId());
			} else {
				updatedUser = null;
			}

			DepartmentDTO departmentDetail = new DepartmentDTO(department.getId(), department.getName(),
					department.getAddress(), department.getCreatedUserId(), department.getCreatedDateTime(),
					department.getUpdatedUserId(), department.getUpdatedDateTime());

			if (createdUser != null) {
				departmentDetail.setCreatedUsername(createdUser.getName());
			}

			// Check if updatedUserId is 0, if so, set updatedUsername to "-"
			if (updatedUser != null) {
				departmentDetail.setUpdatedUsername(updatedUser.getName());
			} else {
				departmentDetail.setUpdatedUsername("-");
			}

			return ResponseEntity.ok(departmentDetail);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Method to call departments for users
	@GetMapping("/departments-for-user")
	@ResponseBody
	public List<Department> getAllDepartments() {
		List<Department> list = departmentService.getDepartments();
		return list;
	}

	// Method to update department
	@PostMapping("/update-department")
	public ResponseEntity<String> updateDepartmentPost(@ModelAttribute("oneDepartment") DepartmentDTO oneDepartment,
			Model model) {

		// check name duplication
		Department departmentWithSameName = departmentService.checkDepartmentDuplicateForUpdate(oneDepartment.getName(),
				oneDepartment.getId());
		if (departmentWithSameName != null) {
			String errorMessage = oneDepartment.getName() + " already exists.";
			Map<String, String> errorResponse = new HashMap<>();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}

		// data from form
		Department department = new Department();
		department = departmentService.getDepartmentById(oneDepartment.getId());
		department.setName(oneDepartment.getName());
		department.setAddress(oneDepartment.getAddress());

		// format datetime
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		department.setUpdatedDateTime(formattedDateTime);

		// updated user id
		department.setUpdatedUserId(departmentService.getCurrentUserId());

		// update in db
		departmentService.updateDepartment(department);

		return ResponseEntity.ok().build();
	}

	// Method to check duplicate department at update
	@PostMapping("/duplicate-department-update")
	@ResponseBody
	public boolean duplicateDepartmentUpdate(@RequestParam("name") String departmentName, @RequestParam("id") int id) {

		// check duplicate name except for current name
		Department departmentWithSameName = departmentService.checkDepartmentDuplicateForUpdate(departmentName, id);
		return departmentWithSameName != null;
	}

}