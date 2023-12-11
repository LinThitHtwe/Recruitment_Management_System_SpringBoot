package com.ace.job.recruitment.dto;

import java.util.List;

import com.ace.job.recruitment.entity.User;

public class UserSearchResponseDTO {
	private List<User> content;
	private List<Integer> pageNumbers;

	public List<User> getContent() {
		return content;
	}

	public void setContent(List<User> content) {
		this.content = content;
	}

	public List<Integer> getPageNumbers() {
		return pageNumbers;
	}

	public void setPageNumbers(List<Integer> pageNumbers) {
		this.pageNumbers = pageNumbers;
	}
}
