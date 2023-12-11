package com.ace.job.recruitment.dto;

import java.time.LocalDate;

import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;

public class VacancyForInterviewDTO {
	private long id;
	private Position position;
	private Department department;
	private LocalDate createdDate;
	private LocalDate dueDate;

	public VacancyForInterviewDTO() {
		super();
	}

	public VacancyForInterviewDTO(long id, Position position, Department department, LocalDate createdDate,
			LocalDate dueDate) {
		super();
		this.id = id;
		this.position = position;
		this.department = department;
		this.createdDate = createdDate;
		this.dueDate = dueDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

}