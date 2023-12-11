package com.ace.job.recruitment.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(com.sun.mail.util.MailConnectException.class)
	public ModelAndView handleConnectionTimeoutException(com.sun.mail.util.MailConnectException ex) {
		ModelAndView modelAndView = new ModelAndView("connection-timeout-exception");
		return modelAndView;
	}
}
