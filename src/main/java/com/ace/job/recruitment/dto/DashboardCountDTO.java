package com.ace.job.recruitment.dto;

public class DashboardCountDTO {
	private long vacancyCount;
	private int activeVacancyCount;
	private int urgentVacancyCount;
	private long candidateCount;
	private long employedCandidateCount;

	public DashboardCountDTO() {
		super();
	}

	public long getVacancyCount() {
		return vacancyCount;
	}

	public void setVacancyCount(long vacancyCount) {
		this.vacancyCount = vacancyCount;
	}

	public int getActiveVacancyCount() {
		return activeVacancyCount;
	}

	public void setActiveVacancyCount(int activeVacancyCount) {
		this.activeVacancyCount = activeVacancyCount;
	}

	public int getUrgentVacancyCount() {
		return urgentVacancyCount;
	}

	public void setUrgentVacancyCount(int urgentVacancyCount) {
		this.urgentVacancyCount = urgentVacancyCount;
	}

	public long getCandidateCount() {
		return candidateCount;
	}

	public void setCandidateCount(long candidateCount) {
		this.candidateCount = candidateCount;
	}

	public long getEmployedCandidateCount() {
		return employedCandidateCount;
	}

	public void setEmployedCandidateCount(long employedCandidateCount) {
		this.employedCandidateCount = employedCandidateCount;
	}

}