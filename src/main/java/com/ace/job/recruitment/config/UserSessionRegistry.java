package com.ace.job.recruitment.config;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;

public class UserSessionRegistry extends SessionRegistryImpl {

	public void expireUserSession(String sessionId) {
		SessionInformation sessionInformation = getSessionInformation(sessionId);
		if (sessionInformation != null) {
			sessionInformation.expireNow();
		}
	}
}
