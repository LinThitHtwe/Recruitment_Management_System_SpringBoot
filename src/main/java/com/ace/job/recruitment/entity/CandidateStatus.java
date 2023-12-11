package com.ace.job.recruitment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "candidate_status")
public class CandidateStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String date;

	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	@JsonManagedReference
	private Status status;

	@Column(name = "change_status_user_id", nullable = false)
	private int changeStatusUserId;

	public int getChangeStatusUserId() {
		return changeStatusUserId;
	}

	public void setChangeStatusUserId(int changeStatusUserId) {
		this.changeStatusUserId = changeStatusUserId;
	}

	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = false)
	@JsonBackReference
	private Candidate candidate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}
}