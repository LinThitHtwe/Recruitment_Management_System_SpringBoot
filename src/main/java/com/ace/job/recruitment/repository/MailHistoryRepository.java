package com.ace.job.recruitment.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.job.recruitment.entity.MailHistory;

public interface MailHistoryRepository
		extends JpaRepository<MailHistory, Long>, DataTablesRepository<MailHistory, Long> {

}