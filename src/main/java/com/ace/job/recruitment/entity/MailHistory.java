package com.ace.job.recruitment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "mail_history")
public class MailHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "mail_subject", nullable = false)
	private String mailSubject;

	@Column(name = "sent_datetime", length = 20, nullable = false)
	private String sentDateTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonManagedReference
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonManagedReference
	@JoinColumn(name = "candidate_id", nullable = false)
	private Candidate candidate;

	public MailHistory() {
		super();
	}

	public MailHistory(String mailSubject, String sentDateTime, User user, Candidate candidate) {
		super();
		this.mailSubject = mailSubject;
		this.sentDateTime = sentDateTime;
		this.user = user;
		this.candidate = candidate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getSentDateTime() {
		return sentDateTime;
	}

	public void setSentDateTime(String sentDateTime) {
		this.sentDateTime = sentDateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

}