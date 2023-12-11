package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.CandidateService;
import com.ace.job.recruitment.service.VacancyService;

public class HRCandidateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CandidateService candidateService;

	@MockBean
	private VacancyService vacancyService;
	@Autowired
	private HRCandidateController hrCandidateController;

	@Test
	public void testGetCurrentUserIdAuthenticated() {
		AppUserDetails mockUserDetails = mock(AppUserDetails.class);
		when(mockUserDetails.getId()).thenReturn(123);
		Authentication mockAuthentication = mock(Authentication.class);
		when(mockAuthentication.isAuthenticated()).thenReturn(true);
		when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		HRCandidateController hrCandidateController = new HRCandidateController();
		int result = hrCandidateController.getCurrentUserId();
		assertEquals(123, result);
		verify(mockAuthentication).isAuthenticated();
		verify(mockAuthentication).getPrincipal();
		verify(mockUserDetails).getId();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetCurrentUserIdNotAuthenticated() {
		Authentication mockAuthentication = mock(Authentication.class);
		when(mockAuthentication.isAuthenticated()).thenReturn(false);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		HRCandidateController hrCandidateController = new HRCandidateController();
		int result = hrCandidateController.getCurrentUserId();
		assertEquals(0, result);
		verify(mockAuthentication).isAuthenticated();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetCurrentUserRoleForJSAuthenticated() {
		AppUserDetails mockUserDetails = mock(AppUserDetails.class);
		when(mockUserDetails.getRole()).thenReturn("ROLE_USER");
		Authentication mockAuthentication = mock(Authentication.class);
		when(mockAuthentication.isAuthenticated()).thenReturn(true);
		when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		HRCandidateController hrCandidateController = new HRCandidateController();
		String result = hrCandidateController.getCurrentUserRoleForJS();
		assertEquals("ROLE_USER", result);
		verify(mockAuthentication).isAuthenticated();
		verify(mockAuthentication).getPrincipal();
		verify(mockUserDetails).getRole();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetCurrentUserRoleForJSNotAuthenticated() {
		Authentication mockAuthentication = mock(Authentication.class);
		when(mockAuthentication.isAuthenticated()).thenReturn(false);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		HRCandidateController hrCandidateController = new HRCandidateController();
		String result = hrCandidateController.getCurrentUserRoleForJS();
		assertEquals("", result);
		verify(mockAuthentication).isAuthenticated();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testCanceledCandidateView() {
		HRCandidateController hrCandidateController = new HRCandidateController();
		String result = hrCandidateController.canceledCandidateView();
	}

}
