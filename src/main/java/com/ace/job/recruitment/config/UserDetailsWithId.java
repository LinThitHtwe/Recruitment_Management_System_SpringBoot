package com.ace.job.recruitment.config;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsWithId extends UserDetails {
	int getId();

	void setId(int id);
}