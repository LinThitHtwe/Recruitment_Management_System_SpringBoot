package com.ace.job.recruitment.config;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private int userId;

	public CustomAuthenticationToken(Object principal, Object credentials, int userId,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
