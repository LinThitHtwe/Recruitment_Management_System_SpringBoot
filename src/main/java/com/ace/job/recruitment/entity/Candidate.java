package com.ace.job.recruitment.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "candidate")
public class Candidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String dob;

	@Column(columnDefinition = "BOOLEAN", nullable = false)
	private boolean gender;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String education;

	@Column(nullable = false)
	private String email;

	@Column(name = "tech_skill", nullable = false)
	private String techSkill;

	@Column(name = "language_skill", nullable = false)
	private String languageSkill;

	@Column(name = "main_tech", nullable = false)
	private String mainTech;

	@Column(nullable = false)
	private String exp;

	@Column(nullable = false)
	private String level;

	@Column(name = "expected_salary", nullable = false)
	private String expectedSalary;

	@Column(name = "submit_date", nullable = false)
	private LocalDate submitDate;

	@Column(name = "submit_time", nullable = false)
	private LocalTime submitTime;

	@Column(columnDefinition = "longblob")
	private byte[] file;

	@Column(nullable = false)
	private String filetype;

	@Column(name = "is_employ", nullable = false)
	private boolean isEmploy;

	@Column(name = "employed_user_id")
	private int employedUserId;

	public int getEmployedUserId() {
		return employedUserId;
	}

	public void setEmployedUserId(int employedUserId) {
		this.employedUserId = employedUserId;
	}

	@Column(name = "is_mail_sent", nullable = false)
	private boolean isMailSent;

	@Column(name = "is_recall")
	private boolean isRecall;

	public boolean isRecall() {
		return isRecall;
	}

	public void isRecall(boolean isRecall) {
		this.isRecall = isRecall;
	}

	public boolean isMailSent() {
		return isMailSent;
	}

	public void setMailSent(boolean isMailSent) {
		this.isMailSent = isMailSent;
	}

	public boolean isEmploy() {
		return isEmploy;
	}

	public void setEmploy(boolean isEmploy) {
		this.isEmploy = isEmploy;
	}

	public List<MailHistory> getMailHistories() {
		return mailHistories;
	}

	public void setMailHistories(List<MailHistory> mailHistories) {
		this.mailHistories = mailHistories;
	}

	@ManyToOne
	@JoinColumn(name = "vacancy_id", nullable = false)
	@JsonManagedReference
	private Vacancy vacancy;

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<CandidateStatus> candidateStatusList;

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<CandidateInterview> candidateInterviews;

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<MailHistory> mailHistories = new ArrayList<>();

	public boolean isIsEmploy() {
		return isEmploy;
	}

	public boolean isIsMailSent() {
		return isMailSent;
	}

	public List<CandidateInterview> getCandidateInterviews() {
		return candidateInterviews;
	}

	public List<CandidateStatus> getCandidateStatusList() {
		return candidateStatusList;
	}

	public void setCandidateStatusList(List<CandidateStatus> candidateStatusList) {
		this.candidateStatusList = candidateStatusList;
	}

	public void setCandidateInterviews(List<CandidateInterview> candidateInterviews) {
		this.candidateInterviews = candidateInterviews;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
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

	public String getTechSkill() {
		return techSkill;
	}

	public void setTechSkill(String techSkill) {
		this.techSkill = techSkill;
	}

	public String getLanguageSkill() {
		return languageSkill;
	}

	public void setLanguageSkill(String languageSkill) {
		this.languageSkill = languageSkill;
	}

	public String getMainTech() {
		return mainTech;
	}

	public void setMainTech(String mainTech) {
		this.mainTech = mainTech;
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

	public String getExpectedSalary() {
		return expectedSalary;
	}

	public void setExpectedSalary(String expectedSalary) {
		this.expectedSalary = expectedSalary;
	}

	public LocalDate getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(LocalDate submitDate) {
		this.submitDate = submitDate;
	}

	public LocalTime getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(LocalTime submitTime) {
		this.submitTime = submitTime;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public Vacancy getVacancy() {
		return vacancy;
	}

	public void setVacancy(Vacancy vacancy) {
		this.vacancy = vacancy;
	}

	public Candidate(String name, String dob, boolean gender, String phone, String education, String email,
			String techSkill, String languageSkill, String mainTech, String exp, String level, String expectedSalary,
			LocalDate submitDate, LocalTime submitTime, byte[] file, String filetype, boolean isEmploy,
			int employedUserId, boolean isMailSent) {
		super();
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.phone = phone;
		this.education = education;
		this.email = email;
		this.techSkill = techSkill;
		this.languageSkill = languageSkill;
		this.mainTech = mainTech;
		this.exp = exp;
		this.level = level;
		this.expectedSalary = expectedSalary;
		this.submitDate = submitDate;
		this.submitTime = submitTime;
		this.file = file;
		this.filetype = filetype;
		this.isEmploy = isEmploy;
		this.employedUserId = employedUserId;
		this.isMailSent = isMailSent;
	}

	public Candidate() {
		super();
	}

}