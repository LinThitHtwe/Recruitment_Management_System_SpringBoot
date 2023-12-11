package com.ace.job.recruitment.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.repository.UserRepository;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		boolean isUserBanned = userIsBanned(request);

		if (isUserBanned) {
			response.sendRedirect("/signin?error");
			return;
		}

		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("Admin") || authority.getAuthority().equals("Default-Admin")
					|| authority.getAuthority().equals("Senior-HR") || authority.getAuthority().equals("Junior-HR")) {
				response.sendRedirect("/hr/dashboard");
				return;
			} else if (authority.getAuthority().equals("Interviewer")
					|| authority.getAuthority().equals("Department-head")) {
				response.sendRedirect("/department/dashboard");
				return;
			}
		}

		throw new IllegalStateException("User has no valid role");
	}

	private boolean userIsBanned(HttpServletRequest request) {
		String username = request.getParameter("username");
		User user = userRepository.findAllByEmailAndStatusIsFalse(username);
		if (user.isStatus()) {
			return true;
		}
		return false;
	}

}