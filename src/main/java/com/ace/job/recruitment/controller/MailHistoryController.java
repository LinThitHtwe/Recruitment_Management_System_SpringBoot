package com.ace.job.recruitment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.entity.MailHistory;
import com.ace.job.recruitment.service.MailHistoryService;

@Controller
@RequestMapping("/hr")
public class MailHistoryController {

	@Autowired
	MailHistoryService mailHistoryService;

	@GetMapping("/mail-history")
	public String showView() {
		return "HR/mail_history";
	}

	@GetMapping("/mail-history-data")
	@ResponseBody
	public DataTablesOutput<MailHistory> tableData(@Valid DataTablesInput input) {
		return mailHistoryService.getDataTableData(input);
	}

}
