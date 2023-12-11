package com.ace.job.recruitment.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.repository.PositionRepository;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/hr")
public class PositionController {

	@Autowired
	PositionService positionService;

	@Autowired
	UserService userService;

	@Autowired
	PositionRepository positionRepository;

	// Method to return the front end page
	@GetMapping("/position-table")
	public String positionTable() {
		return "position/position_table";
	}

	// Method for adding position
	@PostMapping("/add-position")
	public ResponseEntity<String> addPosition(@ModelAttribute("position") Position position,
			Authentication authentication) {
		List<Position> positionWithSameName = positionService.getPositionByName(position.getName());
		if (!positionWithSameName.isEmpty()) {
			String errorMessage = position.getName() + " already exists";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		Position newPosition = new Position(position.getName(), formattedDateTime,
				positionService.getCurrentUserId(authentication));
		positionService.addPosition(newPosition);
		return ResponseEntity.ok().build();
	}

	// Method for checking position duplication during adding stage
	@PostMapping("/duplicate-position")
	@ResponseBody
	public boolean duplicatePosition(@RequestParam("name") String positionName) {
		List<Position> positionWithSameName = positionService.getPositionByName(positionName);
		return !positionWithSameName.isEmpty();
	}

	// Method for checking position duplication during update stage
	@PostMapping("/duplicate-position-update")
	@ResponseBody
	public boolean duplicatePositionUpdate(@RequestParam("name") String positionName,
			@RequestParam("id") int positionId) {
		List<Position> positionWithSameName = positionService.getPositionByName(positionName);
		return positionWithSameName.stream().anyMatch(position -> position.getId() != positionId);
	}

	// Method to update position
	@PostMapping("/update-position/{id}")
	public ResponseEntity<String> updatePosition(@PathVariable int id,
			@ModelAttribute("positionName") String positionName, Authentication authentication) {
		Position existingPosition = positionService.getPositionById(id);
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		existingPosition.setName(positionName);
		existingPosition.setUpdatedDateTime(formattedDateTime);
		existingPosition.setUpdatedUserId(positionService.getCurrentUserId(authentication));
		positionService.updatePosition(existingPosition);
		return ResponseEntity.ok().build();
	}

	// Method to populate the data-tabe with datas
	@GetMapping("/positions-data")
	@ResponseBody
	public DataTablesOutput<Position> getDataTableData(@Valid DataTablesInput input) {
		return positionService.getDataTableData(input);
	}

	// Method to show position details
	@GetMapping("/position-detail")
	public ResponseEntity<Position> positionDetail(@RequestParam("id") int positionId) {
		Position positionDetail = positionService.getPositionById(positionId);

		if (positionDetail == null) {
			return ResponseEntity.notFound().build();
		}

		User createdUser = userService.getUserById(positionDetail.getCreatedUserId());
		User updatedUser = null;

		int updatedUserId = positionDetail.getUpdatedUserId();
		if (updatedUserId != 0) {
			updatedUser = userService.getUserById(updatedUserId);
		}

		positionDetail.setCreatedUsername(createdUser != null ? createdUser.getName() : null);
		positionDetail.setUpdatedUsername(updatedUser != null ? updatedUser.getName() : "-");

		return ResponseEntity.ok(positionDetail);
	}

}