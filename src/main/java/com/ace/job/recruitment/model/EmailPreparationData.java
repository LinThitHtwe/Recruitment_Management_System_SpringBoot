package com.ace.job.recruitment.model;

public class EmailPreparationData {
	private long candidate_id;
	private long interview_id;
	private String templateId, recipientAddress, subject, body, appointmentDate, appointmentDay, appointmentStartTime,
			appointmentEndTime, zoomMeetingUrl, zoomMeetingId, zoomMeetingPasscode, joinedStartDate;
	private String[] ccAddress, attachment;
	private String basicPay, projectAllowance, mealTransportAllowanceDay, mealTransportAllowanceMonth, earnLeave,
			casualLeave, medicalLeave;

	public EmailPreparationData() {
		super();
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public long getCandidate_id() {
		return candidate_id;
	}

	public void setCandidate_id(long candidate_id) {
		this.candidate_id = candidate_id;
	}

	public long getInterview_id() {
		return interview_id;
	}

	public void setInterview_id(long interview_id) {
		this.interview_id = interview_id;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String[] getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String[] getAttachment() {
		return attachment;
	}

	public void setAttachment(String[] attachment) {
		this.attachment = attachment;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentDay() {
		return appointmentDay;
	}

	public void setAppointmentDay(String appointmentDay) {
		this.appointmentDay = appointmentDay;
	}

	public String getAppointmentStartTime() {
		return appointmentStartTime;
	}

	public void setAppointmentStartTime(String appointmentStartTime) {
		this.appointmentStartTime = appointmentStartTime;
	}

	public String getAppointmentEndTime() {
		return appointmentEndTime;
	}

	public void setAppointmentEndTime(String appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}

	public String getZoomMeetingUrl() {
		return zoomMeetingUrl;
	}

	public void setZoomMeetingUrl(String zoomMeetingUrl) {
		this.zoomMeetingUrl = zoomMeetingUrl;
	}

	public String getZoomMeetingId() {
		return zoomMeetingId;
	}

	public void setZoomMeetingId(String zoomMeetingId) {
		this.zoomMeetingId = zoomMeetingId;
	}

	public String getZoomMeetingPasscode() {
		return zoomMeetingPasscode;
	}

	public void setZoomMeetingPasscode(String zoomMeetingPasscode) {
		this.zoomMeetingPasscode = zoomMeetingPasscode;
	}

	public String getJoinedStartDate() {
		return joinedStartDate;
	}

	public void setJoinedStartDate(String joinedStartDate) {
		this.joinedStartDate = joinedStartDate;
	}

	public String getBasicPay() {
		return basicPay;
	}

	public void setBasicPay(String basicPay) {
		this.basicPay = basicPay;
	}

	public String getProjectAllowance() {
		return projectAllowance;
	}

	public void setProjectAllowance(String projectAllowance) {
		this.projectAllowance = projectAllowance;
	}

	public String getMealTransportAllowanceDay() {
		return mealTransportAllowanceDay;
	}

	public void setMealTransportAllowanceDay(String mealTransportAllowanceDay) {
		this.mealTransportAllowanceDay = mealTransportAllowanceDay;
	}

	public String getMealTransportAllowanceMonth() {
		return mealTransportAllowanceMonth;
	}

	public void setMealTransportAllowanceMonth(String mealTransportAllowanceMonth) {
		this.mealTransportAllowanceMonth = mealTransportAllowanceMonth;
	}

	public String getEarnLeave() {
		return earnLeave;
	}

	public void setEarnLeave(String earnLeave) {
		this.earnLeave = earnLeave;
	}

	public String getCasualLeave() {
		return casualLeave;
	}

	public void setCasualLeave(String casualLeave) {
		this.casualLeave = casualLeave;
	}

	public String getMedicalLeave() {
		return medicalLeave;
	}

	public void setMedicalLeave(String medicalLeave) {
		this.medicalLeave = medicalLeave;
	}

}