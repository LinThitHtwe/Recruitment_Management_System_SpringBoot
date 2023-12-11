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

@Entity
@Table(name = "reviews")
public class Reviews {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "review", length = 300, nullable = false)
	private String review;
	@Column(name = "review_by", nullable = false)
	private int reviewBy;
	@Column(name = "reviewed_datetime", length = 20, nullable = false)
	private String reviewedDateTime;

	public String getReviewedDateTime() {
		return reviewedDateTime;
	}

	public void setReviewedDateTime(String reviewedDateTime) {
		this.reviewedDateTime = reviewedDateTime;
	}

	@ManyToOne
	@JoinColumn(name = "candidate_interview_id", nullable = false)
	@JsonBackReference
	private CandidateInterview candidateInterview;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getReviewBy() {
		return reviewBy;
	}

	public void setReviewBy(int reviewBy) {
		this.reviewBy = reviewBy;
	}

	public CandidateInterview getCandidateInterview() {
		return candidateInterview;
	}

	public void setCandidateInterview(CandidateInterview candidateInterview) {
		this.candidateInterview = candidateInterview;
	}
}