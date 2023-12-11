package com.ace.job.recruitment.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "candidate_interview")
public class CandidateInterview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "status", length = 20, nullable = false)
	private String status;

	@Column(name = "interview_date", length = 15)
	private String interviewDate;

	@Column(name = "interview_status_changed_user_id", nullable = false)
	private int interviewStatusChangedUserId;

	@ManyToOne
	@JoinColumn(name = "interview_id", nullable = false)
	@JsonManagedReference
	private Interview interview;

	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = false)
	@JsonBackReference
	private Candidate candidate;

	@OneToMany(mappedBy = "candidateInterview", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Reviews> reviews = new ArrayList<>();

	public List<Reviews> getReviews() {
		return reviews;
	}

	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}

	public int getInterviewStatusChangedUserId() {
		return interviewStatusChangedUserId;
	}

	public void setInterviewStatusChangedUserId(int interviewStatusChangedUserId) {
		this.interviewStatusChangedUserId = interviewStatusChangedUserId;
	}

	public Interview getInterview() {
		return interview;
	}

	public void setInterview(Interview interview) {
		this.interview = interview;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}
}