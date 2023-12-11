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

@Entity
@Table(name = "password_reset")
public class PasswordReset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 10, nullable = true)
	private String resetToken;

	@Column(name = "stored_timestamp", nullable = false)
	private Long expireTimestamp;

	@Column(nullable = false)
	private boolean status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public PasswordReset() {
		super();
	}

	public PasswordReset(long id, String resetToken, Long expireTimestamp, boolean status, User user) {
		super();
		this.id = id;
		this.resetToken = resetToken;
		this.expireTimestamp = expireTimestamp;
		this.status = status;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Long getExpireTimestamp() {
		return expireTimestamp;
	}

	public void setExpireTimestamp(Long expireTimestamp) {
		this.expireTimestamp = expireTimestamp;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}