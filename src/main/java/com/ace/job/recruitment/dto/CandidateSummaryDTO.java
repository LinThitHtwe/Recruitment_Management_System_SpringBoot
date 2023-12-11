package com.ace.job.recruitment.dto;

import java.math.BigInteger;
import java.time.LocalDate;

public class CandidateSummaryDTO {
	private BigInteger no;
	private String name;
	private LocalDate submit_date;
	private String interview_date;
	private String gender_text;
	private String department_name;
	private String position_name;

	private String dob;
	private String phone;
	private String education;
	private String email;
	private String tech_skill;
	private String language_skill;
	private String main_tech;
	private String exp;
	private String level;
	private String expected_salary;

	private String combine_status;

	private String interview_stage;
	private String candidate_special_status;

	public CandidateSummaryDTO(BigInteger no, String name, LocalDate submit_date, String interview_date,
			String gender_text, String department_name, String position_name, String dob, String phone,
			String education, String email, String tech_skill, String language_skill, String main_tech, String exp,
			String level, String expected_salary, String combine_status, String interview_stage,
			String candidate_special_status) {
		super();
		this.no = no;
		this.name = name;
		this.submit_date = submit_date;
		this.interview_date = interview_date;
		this.gender_text = gender_text;
		this.department_name = department_name;
		this.position_name = position_name;
		this.dob = dob;
		this.phone = phone;
		this.education = education;
		this.email = email;
		this.tech_skill = tech_skill;
		this.language_skill = language_skill;
		this.main_tech = main_tech;
		this.exp = exp;
		this.level = level;
		this.expected_salary = expected_salary;
		this.combine_status = combine_status;
		this.interview_stage = interview_stage;
		this.candidate_special_status = candidate_special_status;
	}

	public BigInteger getNo() {
		return no;
	}

	public void setNo(BigInteger no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getSubmit_date() {
		return submit_date;
	}

	public void setSubmit_date(LocalDate submit_date) {
		this.submit_date = submit_date;
	}

	public String getInterview_date() {
		return interview_date;
	}

	public void setInterview_date(String interview_date) {
		this.interview_date = interview_date;
	}

	public String getGender_text() {
		return gender_text;
	}

	public void setGender_text(String gender_text) {
		this.gender_text = gender_text;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTech_skill() {
		return tech_skill;
	}

	public void setTech_skill(String tech_skill) {
		this.tech_skill = tech_skill;
	}

	public String getLanguage_skill() {
		return language_skill;
	}

	public void setLanguage_skill(String language_skill) {
		this.language_skill = language_skill;
	}

	public String getMain_tech() {
		return main_tech;
	}

	public void setMain_tech(String main_tech) {
		this.main_tech = main_tech;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getExpected_salary() {
		return expected_salary;
	}

	public void setExpected_salary(String expected_salary) {
		this.expected_salary = expected_salary;
	}

	public String getCombine_status() {
		return combine_status;
	}

	public void setCombine_status(String combine_status) {
		this.combine_status = combine_status;
	}

	public String getInterview_stage() {
		return interview_stage;
	}

	public void setInterview_stage(String interview_stage) {
		this.interview_stage = interview_stage;
	}

	public String getCandidate_special_status() {
		return candidate_special_status;
	}

	public void setCandidate_special_status(String candidate_special_status) {
		this.candidate_special_status = candidate_special_status;
	}

}
