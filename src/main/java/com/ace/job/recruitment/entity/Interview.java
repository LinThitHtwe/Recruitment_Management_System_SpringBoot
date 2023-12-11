package com.ace.job.recruitment.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "interview")
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "start_time", length = 20, nullable = false)
	private String startTime;

	@Column(name = "end_time", length = 20, nullable = false)
	private String endTime;

	@Column(name = "type", length = 20, nullable = false)
	private String type;

	@Column(name = "location", length = 255)
	private String location;

	@Column(name = "stage", nullable = false)
	private int stage;

	@Column(name = "status", nullable = false)
	private boolean status;

	@Column(name = "created_datetime", length = 20, nullable = false)
	private String createdDateTime;

	@Column(name = "updated_datetime", length = 20)
	private String updatedDateTime;

	@Column(name = "created_user_id", nullable = false)
	private int createdUserId;

	@Column(name = "updated_user_id")
	private int updatedUserId;

	@Column(name = "canceled_datetime", length = 20)
	private String canceledDateTime;

	@Column(name = "canceled_user_id")
	private int canceledUserId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vacany_id", nullable = false)
	@JsonManagedReference
	private Vacancy vacancy;

	@JsonIgnoreProperties("interviews")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "interview_user", joinColumns = @JoinColumn(name = "interview_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users = new ArrayList<User>();

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<CandidateInterview> candidateInterviews = new ArrayList<>();

	public List<CandidateInterview> getCandidateInterviews() {
		return candidateInterviews;
	}

	public void setCandidateInterviews(List<CandidateInterview> candidateInterviews) {
		this.candidateInterviews = candidateInterviews;
	}

	@Transient
	public String getPositionName() {
		if (vacancy != null && vacancy.getPosition() != null) {
			return vacancy.getPosition().getName();
		}
		return null;
	}

	public Interview() {
		super();
	}

	public Interview(LocalDate startDate, LocalDate endDate, String startTime, String endTime, String type,
			String location, int stage) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.location = location;
		this.stage = stage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
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

	public String getCanceledDateTime() {
		return canceledDateTime;
	}

	public void setCanceledDateTime(String canceledDateTime) {
		this.canceledDateTime = canceledDateTime;
	}

	public int getCanceledUserId() {
		return canceledUserId;
	}

	public void setCanceledUserId(int canceledUserId) {
		this.canceledUserId = canceledUserId;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Vacancy getVacancy() {
		return vacancy;
	}

	public void setVacancy(Vacancy vacancy) {
		this.vacancy = vacancy;
	}

}