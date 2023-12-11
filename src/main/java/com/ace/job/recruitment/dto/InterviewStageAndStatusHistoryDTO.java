package com.ace.job.recruitment.dto;

public class InterviewStageAndStatusHistoryDTO {

	private String interviewDate;
	private String changeStatusBy;
	private String status;
	private String interviewers;
	private String interviewStage;

	public String getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getChangeStatusBy() {
		return changeStatusBy;
	}

	public void setChangeStatusBy(String changeStatusBy) {
		this.changeStatusBy = changeStatusBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInterviewers() {
		return interviewers;
	}

	public void setInterviewers(String interviewers) {
		this.interviewers = interviewers;
	}

	public String getInterviewStage() {
		return interviewStage;
	}

	public void setInterviewStage(String interviewStage) {
		this.interviewStage = interviewStage;
	}
}
