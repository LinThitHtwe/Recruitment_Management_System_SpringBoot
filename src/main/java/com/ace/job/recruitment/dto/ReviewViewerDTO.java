package com.ace.job.recruitment.dto;

public class ReviewViewerDTO {

	private String reviewerName;

	private String review;

	private String reviewedDateTime;

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getReviewedDateTime() {
		return reviewedDateTime;
	}

	public void setReviewedDateTime(String reviewedDateTime) {
		this.reviewedDateTime = reviewedDateTime;
	}

}
