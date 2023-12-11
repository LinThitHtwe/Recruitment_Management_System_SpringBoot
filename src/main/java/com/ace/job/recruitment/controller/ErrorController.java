package com.ace.job.recruitment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	@GetMapping("/404")
	public String notFoundPage() {
		System.out.print("____404___");
		return "404";
	}

	@GetMapping("/403")
	public String notAuthorizePage() {
		System.out.print("____403___");
		return "403";
	}

	@GetMapping("/error-page")
	public String errorPage() {
		System.out.print("____403___");
		return "404";
	}
}
