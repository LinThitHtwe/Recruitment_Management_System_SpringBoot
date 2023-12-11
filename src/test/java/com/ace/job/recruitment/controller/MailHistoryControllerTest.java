package com.ace.job.recruitment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.service.MailHistoryService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MailHistoryControllerTest {
	@Mock
	private MailHistoryService mailHistoryService;

	@InjectMocks
	private MailHistoryController mailHistoryController;

	@Test
	void showViewTest() {
		String viewName = mailHistoryController.showView();
		assertEquals("HR/mail_history", viewName);
	}

}
