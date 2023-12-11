package com.ace.job.recruitment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ace.job.recruitment.entity.Candidate;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;

public class VacancyDto {

	private long id;
	private String requirement;
	private String responsibility;
	private String description;
	private String preference;
	private String startWorkingDay;
	private String endWorkingDay;
	private String startWorkingHour;
	private String endWorkingHour;
	private String salary;
	private int count;
	private String type;
	private boolean reopenStatus;
	private boolean reopened;
	private LocalDate createdDate;
	private LocalTime createdTime;
	private LocalDate updatedDate;
	private LocalTime updatedTime;
	private int createdUserId;
	private int updatedUserId;
	private LocalDate dueDate;
	private boolean active;
	private boolean urgent;
	private LocalDate reopenDate;
	private LocalTime reopenTime;
	private Department department;
	private List<Candidate> candidates;
	private Position position;

	public VacancyDto() {
	}

	public VacancyDto(String requirement, String responsibility, String description, String preference,
			String startWorkingDay, String endWorkingDay, String startWorkingHour, String endWorkingHour, String salary,
			int count, String type, boolean reopenStatus, boolean reopened, LocalDate createdDate,
			LocalTime createdTime, LocalDate updatedDate, LocalTime updatedTime, int createdUserId, int updatedUserId,
			LocalDate dueDate, boolean active, boolean urgent, LocalDate reopenDate, LocalTime reopenTime,
			Department department, List<Candidate> candidates, Position position) {
		super();
		this.requirement = requirement;
		this.responsibility = responsibility;
		this.description = description;
		this.preference = preference;
		this.startWorkingDay = startWorkingDay;
		this.endWorkingDay = endWorkingDay;
		this.startWorkingHour = startWorkingHour;
		this.endWorkingHour = endWorkingHour;
		this.salary = salary;
		this.count = count;
		this.type = type;
		this.reopenStatus = reopenStatus;
		this.createdDate = createdDate;
		this.createdTime = createdTime;
		this.updatedDate = updatedDate;
		this.updatedTime = updatedTime;
		this.createdUserId = createdUserId;
		this.updatedUserId = updatedUserId;
		this.dueDate = dueDate;
		this.reopenDate = reopenDate;
		this.reopenTime = reopenTime;
		this.department = department;
		this.candidates = candidates;
		this.position = position;
		this.active = active;
		this.urgent = urgent;
		this.reopened = reopened;
	}

	public VacancyDto(long id, String requirement, String responsibility, String description, String preference,
			String startWorkingDay, String endWorkingDay, String startWorkingHour, String endWorkingHour, String salary,
			int count, String type, boolean reopenStatus, boolean reopened, LocalDate createdDate,
			LocalTime createdTime, LocalDate updatedDate, LocalTime updatedTime, int createdUserId, int updatedUserId,
			LocalDate dueDate, boolean active, boolean urgent, LocalDate reopenDate, LocalTime reopenTime,
			Department department, List<Candidate> candidates, Position position) {
		super();
		this.id = id;
		this.requirement = requirement;
		this.responsibility = responsibility;
		this.description = description;
		this.preference = preference;
		this.startWorkingDay = startWorkingDay;
		this.endWorkingDay = endWorkingDay;
		this.startWorkingHour = startWorkingHour;
		this.endWorkingHour = endWorkingHour;
		this.salary = salary;
		this.count = count;
		this.type = type;
		this.reopenStatus = reopenStatus;
		this.createdDate = createdDate;
		this.createdTime = createdTime;
		this.updatedDate = updatedDate;
		this.updatedTime = updatedTime;
		this.createdUserId = createdUserId;
		this.updatedUserId = updatedUserId;
		this.dueDate = dueDate;
		this.reopenDate = reopenDate;
		this.reopenTime = reopenTime;
		this.department = department;
		this.candidates = candidates;
		this.position = position;
		this.active = active;
		this.urgent = urgent;
		this.reopened = reopened;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	public String getStartWorkingDay() {
		return startWorkingDay;
	}

	public void setStartWorkingDay(String startWorkingDay) {
		this.startWorkingDay = startWorkingDay;
	}

	public String getEndWorkingDay() {
		return endWorkingDay;
	}

	public void setEndWorkingDay(String endWorkingDay) {
		this.endWorkingDay = endWorkingDay;
	}

	public String getStartWorkingHour() {
		return startWorkingHour;
	}

	public void setStartWorkingHour(String startWorkingHour) {
		this.startWorkingHour = startWorkingHour;
	}

	public String getEndWorkingHour() {
		return endWorkingHour;
	}

	public void setEndWorkingHour(String endWorkingHour) {
		this.endWorkingHour = endWorkingHour;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isReopenStatus() {
		return reopenStatus;
	}

	public void setReopenStatus(boolean reopenStatus) {
		this.reopenStatus = reopenStatus;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

	public LocalTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}

	public int getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(int updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getReopenDate() {
		return reopenDate;
	}

	public void setReopenDate(LocalDate reopenDate) {
		this.reopenDate = reopenDate;
	}

	public LocalTime getReopenTime() {
		return reopenTime;
	}

	public void setReopenTime(LocalTime reopenTime) {
		this.reopenTime = reopenTime;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	public boolean isReopened() {
		return reopened;
	}

	public void setReopened(boolean reopened) {
		this.reopened = reopened;
	}
}