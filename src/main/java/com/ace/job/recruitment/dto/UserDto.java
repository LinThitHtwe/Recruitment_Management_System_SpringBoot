package com.ace.job.recruitment.dto;

public class UserDto {

	private int id;
	private String name;
	private String email;
	private String password;
	private String role;
	private boolean status;

	public UserDto(String name, String email, String password, String role, boolean status) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
	}

	public UserDto() {
		super();
	}

	public UserDto(int id, String name, String email, String password, String role, boolean status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.status = status;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
