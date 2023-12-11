package com.ace.job.recruitment.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ace.job.recruitment.entity.User;

@SuppressWarnings("serial")
public class AppUserDetails implements UserDetails {

	private User user;

	public AppUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority(user.getRole()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	public int getDepartmentId() {
		return user.getDepartment().getId();
	}

	public String getRole() {
		return user.getRole();
	}

	public int getId() {
		return user.getId();
	}

	public String getFullName() {
		return user.getName();
	}

	public void setFullName(String fullName) {
		user.setName(fullName);
	}

	public String getEmail() {
		return user.getEmail();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getUsername() {
		return user.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}