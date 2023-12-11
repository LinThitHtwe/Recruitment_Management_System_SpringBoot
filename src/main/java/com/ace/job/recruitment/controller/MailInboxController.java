package com.ace.job.recruitment.controller;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ace.job.recruitment.model.EmailInbox;
import com.ace.job.recruitment.service.MailInboxService;

@Controller
@RequestMapping("/hr")
public class MailInboxController {
	@Autowired
	MailInboxService mailInboxService;

	@GetMapping("/email-inbox")
	public String readMail24hour(Model model) throws MessagingException {
		String noMailMsg = null;
		List<EmailInbox> emailInboxList = new ArrayList<EmailInbox>();
		emailInboxList = mailInboxService.readEmailsLast24Hours();
		if (emailInboxList.isEmpty()) {
			noMailMsg = "There is no incoming mail in the last 24 hours.";
		}
		for (EmailInbox inbox : emailInboxList) {
			System.out.println("_____body" + inbox.getBody()+"_______");
		}
		model.addAttribute("noMailMsg", noMailMsg);
		model.addAttribute("emailInboxList", emailInboxList);
		return "email/mail-inbox";
	}
}
