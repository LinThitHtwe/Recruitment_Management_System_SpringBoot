package com.ace.job.recruitment.dto;

public class PositionDTO {
	private int id;
	private String name;
	private String createdDateTime;
	private String updatedDateTime;
	private int createdUserId;
	private int updatedUserId;

	public PositionDTO(String name, String createdDateTime, int createdUserId) {
		super();
		this.name = name;
		this.createdDateTime = createdDateTime;
		this.createdUserId = createdUserId;
	}

	public PositionDTO() {
		super();
	}

	public PositionDTO(int id, String name, String createdDateTime, String updatedDateTime, int createdUserId,
			int updatedUserId) {
		super();
		this.id = id;
		this.name = name;
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.createdUserId = createdUserId;
		this.updatedUserId = updatedUserId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
}
