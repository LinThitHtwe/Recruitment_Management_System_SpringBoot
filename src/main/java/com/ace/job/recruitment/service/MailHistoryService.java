package com.ace.job.recruitment.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.ace.job.recruitment.entity.MailHistory;

public interface MailHistoryService {
	public MailHistory storeMailHistory(MailHistory mailHistory);

	public List<MailHistory> getAllMailHistory();

	DataTablesOutput<MailHistory> getDataTableData(@Valid DataTablesInput input);

}