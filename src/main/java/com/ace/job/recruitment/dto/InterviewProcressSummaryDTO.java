package com.ace.job.recruitment.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class InterviewProcressSummaryDTO {

	private String from_date;
	private String to_date;
	private String position_name;
	private BigInteger total_received_candidate;
	private BigInteger offer_mail;
	private BigInteger accepted_mail;
	private BigDecimal passed_interview_count;
	private BigDecimal pending_interview_count;
	private BigDecimal cancel_interview_count;
	private BigDecimal reached_interview_count;
	private BigDecimal interviewed_candidate;
	private BigDecimal left_to_view;

	public InterviewProcressSummaryDTO(String from_date, String to_date, String position_name,
			BigInteger total_received_candidate, BigInteger offer_mail, BigInteger accepted_mail,
			BigDecimal passed_interview_count, BigDecimal pending_interview_count, BigDecimal cancel_interview_count,
			BigDecimal reached_interview_count, BigDecimal interviewed_candidate, BigDecimal left_to_view) {
		super();
		this.from_date = from_date;
		this.to_date = to_date;
		this.position_name = position_name;
		this.total_received_candidate = total_received_candidate;
		this.offer_mail = offer_mail;
		this.accepted_mail = accepted_mail;
		this.passed_interview_count = passed_interview_count;
		this.pending_interview_count = pending_interview_count;
		this.cancel_interview_count = cancel_interview_count;
		this.reached_interview_count = reached_interview_count;
		this.interviewed_candidate = interviewed_candidate;
		this.left_to_view = left_to_view;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public BigInteger getTotal_received_candidate() {
		return total_received_candidate;
	}

	public void setTotal_received_candidate(BigInteger total_received_candidate) {
		this.total_received_candidate = total_received_candidate;
	}

	public BigInteger getOffer_mail() {
		return offer_mail;
	}

	public void setOffer_mail(BigInteger offer_mail) {
		this.offer_mail = offer_mail;
	}

	public BigInteger getAccepted_mail() {
		return accepted_mail;
	}

	public void setAccepted_mail(BigInteger accepted_mail) {
		this.accepted_mail = accepted_mail;
	}

	public BigDecimal getPassed_interview_count() {
		return passed_interview_count;
	}

	public void setPassed_interview_count(BigDecimal passed_interview_count) {
		this.passed_interview_count = passed_interview_count;
	}

	public BigDecimal getPending_interview_count() {
		return pending_interview_count;
	}

	public void setPending_interview_count(BigDecimal pending_interview_count) {
		this.pending_interview_count = pending_interview_count;
	}

	public BigDecimal getCancel_interview_count() {
		return cancel_interview_count;
	}

	public void setCancel_interview_count(BigDecimal cancel_interview_count) {
		this.cancel_interview_count = cancel_interview_count;
	}

	public BigDecimal getReached_interview_count() {
		return reached_interview_count;
	}

	public void setReached_interview_count(BigDecimal reached_interview_count) {
		this.reached_interview_count = reached_interview_count;
	}

	public BigDecimal getInterviewed_candidate() {
		return interviewed_candidate;
	}

	public void setInterviewed_candidate(BigDecimal interviewed_candidate) {
		this.interviewed_candidate = interviewed_candidate;
	}

	public BigDecimal getLeft_to_view() {
		return left_to_view;
	}

	public void setLeft_to_view(BigDecimal left_to_view) {
		this.left_to_view = left_to_view;
	}

}