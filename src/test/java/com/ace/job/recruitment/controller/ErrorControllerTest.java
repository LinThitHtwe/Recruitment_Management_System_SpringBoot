package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

class ErrorControllerTest {

	@InjectMocks
	private ErrorController errorController;

	@Mock
	private Model model;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testNotFoundPage() {
		String result = errorController.notFoundPage();

		assertEquals("404", result);
	}

	@Test
	void testNotAuthorizePage() {
		String result = errorController.notAuthorizePage();

		assertEquals("403", result);
	}
}
