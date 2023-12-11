package com.ace.job.recruitment.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, DataTablesRepository<Candidate, Long> {
	// For reporting

	@Query(value = "CALL interview_process_date_filter(:startDate, :endDate,:departmentId,:positionId,:active,:reopen,:urgent);", nativeQuery = true)
	List<Object[]> callInterviewSummary(LocalDate startDate, LocalDate endDate, int departmentId, int positionId,
			Integer active, Integer reopen, Integer urgent);

	@Query(value = "CALL candidate_summary_combined_filter(:vacancyId, :positionId,:departmentId, :interviewStage, :selectionStatus, :interviewStatus, :startDate, :endDate, :isEmploy, :isRecall);", nativeQuery = true)
	List<Object[]> allCandidateViewSummary(long vacancyId, int positionId, int departmentId, int interviewStage,
			String selectionStatus, String interviewStatus, LocalDate startDate, LocalDate endDate, Integer isEmploy,
			Integer isRecall);

	// For reporting

	// for dashboard counts

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c")
	long getAllCandidateCount();

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE isEmploy=true")
	long getAllEmployedCandidateCount();

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE YEAR(c.vacancy.createdDate) = :year")
	long getAllCandidateCountByYear(int year);

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE isEmploy=true AND YEAR(c.vacancy.createdDate) = :year")
	long getAllEmployedCandidateCountByYear(int year);

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE c.vacancy.department.id= :departmentId")
	long getAllCandidateCountForDepartment(int departmentId);

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE isEmploy=true AND c.vacancy.department.id= :departmentId")
	long getAllEmployedCandidateCountForDepartment(int departmentId);

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE YEAR(c.vacancy.createdDate) = :year AND c.vacancy.department.id= :departmentId")
	long getAllCandidateCountByYearForDepartment(int year, int departmentId);

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE isEmploy=true AND YEAR(c.vacancy.createdDate) = :year AND c.vacancy.department.id= :departmentId")
	long getAllEmployedCandidateCountByYearForDepartment(int year, int departmentId);

	@Query("SELECT COALESCE(COUNT(c.id), 0) FROM Candidate c WHERE isEmploy=true AND c.vacancy.id = :vacancyId")
	long getEmployedCandidateCountByVacancy(long vacancyId);

	// for dashboard

	@Query("SELECT c FROM Candidate c WHERE c.email = :email AND c.vacancy.id=:vacancyId")
	List<Candidate> getCandidateForDuplication(String email, long vacancyId);
}