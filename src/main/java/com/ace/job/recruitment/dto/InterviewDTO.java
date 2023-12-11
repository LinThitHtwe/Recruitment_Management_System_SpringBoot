package com.ace.job.recruitment.dto;

import java.util.List;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;

public class InterviewDTO {
	private Long id;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String type;
	private String location;
	private int stage;
	private boolean status;
	private int createdUserId;
	private String createdDateTime;
	private int updatedUserId;
	private String updatedDateTime;
	private int canceledUserId;
	private String canceledDateTime;
	private Vacancy vacancy;
	private List<User> users;
	private String positionName;
	private String createdUserName;
	private String updatedUserName;
	private String canceledUsername;

	public InterviewDTO(Long id, String startDate, String endDate, String startTime, String endTime, String type,
			String location, int stage, boolean status, int createdUserId, String createdDateTime, int updatedUserId,
			String updatedDateTime, int canceledUserId, String canceledDateTime, Vacancy vacancy, List<User> users) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.location = location;
		this.stage = stage;
		this.status = status;
		this.createdUserId = createdUserId;
		this.createdDateTime = createdDateTime;
		this.updatedUserId = updatedUserId;
		this.updatedDateTime = updatedDateTime;
		this.canceledUserId = canceledUserId;
		this.canceledDateTime = canceledDateTime;
		this.vacancy = vacancy;
		this.users = users;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}

	public InterviewDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public int getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(int updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public String getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public int getCanceledUserId() {
		return canceledUserId;
	}

	public void setCanceledUserId(int canceledUserId) {
		this.canceledUserId = canceledUserId;
	}

	public String getCanceledDateTime() {
		return canceledDateTime;
	}

	public void setCanceledDateTime(String canceledDateTime) {
		this.canceledDateTime = canceledDateTime;
	}

	public Vacancy getVacancy() {
		return vacancy;
	}

	public void setVacancy(Vacancy vacancy) {
		this.vacancy = vacancy;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getCanceledUsername() {
		return canceledUsername;
	}

	public void setCanceledUsername(String canceledUsername) {
		this.canceledUsername = canceledUsername;
	}

}