package com.ace.job.recruitment.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "position")
public class Position {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 100, nullable = false)
	private String name;
	@Column(name = "created_datetime", length = 20, nullable = false)
	private String createdDateTime;
	@Column(name = "updated_datetime", length = 20)
	private String updatedDateTime;
	@Column(name = "created_user_id", nullable = false)
	private int createdUserId;

	@Column(name = "updated_user_id")
	private int updatedUserId;

	@Transient
	private String createdUsername;

	@Transient
	private String updatedUsername;

	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
	@JsonBackReference
	private List<Vacancy> vacancies;

	public List<Vacancy> getVacancies() {
		return vacancies;
	}

	public void setVacancies(List<Vacancy> vacancies) {
		this.vacancies = vacancies;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getUpdatedUsername() {
		return updatedUsername;
	}

	public void setUpdatedUsername(String updatedUsername) {
		this.updatedUsername = updatedUsername;
	}

	public Position() {
		super();
	}

	public Position(String name, String createdDateTime, int createdUserId) {
		super();
		this.name = name;
		this.createdDateTime = createdDateTime;
		this.createdUserId = createdUserId;
	}

	public Position(String name, String createdDateTime, String updatedDateTime, int createdUserId, int updatedUserId) {
		super();
		this.name = name;
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.createdUserId = createdUserId;
		this.updatedUserId = updatedUserId;
	}

	public Position(int id, String name, String createdDateTime, String updatedDateTime, int createdUserId,
			int updatedUserId) {
		super();
		this.id = id;
		this.name = name;
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
		this.createdUserId = createdUserId;
		this.updatedUserId = updatedUserId;
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