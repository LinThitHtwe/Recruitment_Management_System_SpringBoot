package com.ace.job.recruitment.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.dto.VacancyForInterviewDTO;
import com.ace.job.recruitment.entity.Department;
import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.entity.Vacancy;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, DataTablesRepository<Vacancy, Long> {
	List<Vacancy> findAllByActiveTrue();

	List<Vacancy> findAllByActiveTrueAndUrgentFalse();

	Page<Vacancy> findByActiveTrue(Pageable pageable);

	@Query("SELECT v FROM Vacancy v JOIN FETCH v.position JOIN FETCH v.department")
	List<Vacancy> findAllWithPositionAndDepartment(Pageable pageable);

	Page<Vacancy> findAll(Specification<Vacancy> specification, Pageable pageable);

	Page<Vacancy> findByUrgentTrueAndActiveTrue(Pageable pageable);

	Page<Vacancy> findByPositionNameAndActiveTrue(String position, Pageable pageable);

	Page<Vacancy> findByPositionNameAndUrgentAndActiveTrue(String position, Boolean urgent, Pageable pageable);

	Page<Vacancy> findByTypeAndActiveTrue(String type, Pageable pageable);

	Page<Vacancy> findByTypeAndUrgentAndActiveTrue(String type, Boolean urgent, Pageable pageable);

	Page<Vacancy> findByDepartmentNameAndActiveTrue(String department, Pageable pageable);

	Page<Vacancy> findByDepartmentNameAndUrgentAndActiveTrue(String department, Boolean urgent, Pageable pageable);

	long count(Specification<Vacancy> specification);

	// to get vacancyList for interview add
	@Query("SELECT new com.ace.job.recruitment.dto.VacancyForInterviewDTO(v.id, v.position, v.department,v.createdDate,v.dueDate) FROM Vacancy v")
	List<VacancyForInterviewDTO> getVacanciesForInterview();

	List<Vacancy> findByUrgentTrue();

	List<Vacancy> findByActiveTrue();

	// get min year of vacancy
	@Query("SELECT COALESCE(MIN(YEAR(v.createdDate)), 0) FROM Vacancy v")
	int getMinYear();

	// for dashboard counts
	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v")
	long getAllVacancyCount();

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true")
	int getAllActiveVacancyCount();

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND v.urgent=true")
	int getAllUrgentVacancyCount();

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE YEAR(v.createdDate) = :year")
	long getAllVacancyCountByYear(int year);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND YEAR(v.createdDate) = :year")
	int getAllActiveVacancyCountByYear(int year);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND v.urgent=true AND YEAR(v.createdDate) = :year")
	int getAllUrgentVacancyCountByYear(int year);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.department.id = :departmentId")
	long getAllVacancyCountForDepartment(int departmentId);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND v.department.id = :departmentId")
	int getAllActiveVacancyCountForDepartment(int departmentId);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND v.urgent=true AND v.department.id = :departmentId")
	int getAllUrgentVacancyCountForDepartment(int departmentId);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE YEAR(v.createdDate) = :year AND v.department.id = :departmentId")
	long getAllVacancyCountByYearForDepartment(int year, int departmentId);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND YEAR(v.createdDate) = :year AND v.department.id = :departmentId")
	int getAllActiveVacancyCountByYearForDepartment(int year, int departmentId);

	@Query("SELECT COALESCE(COUNT(v.id), 0) FROM Vacancy v WHERE v.active=true AND v.urgent=true AND YEAR(v.createdDate) = :year AND v.department.id = :departmentId")
	int getAllUrgentVacancyCountByYearForDepartment(int year, int departmentId);

	// dashboard chart start
	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(s.name,COUNT(c.id)) " + "FROM Vacancy v "
			+ "JOIN Candidate c ON v.id = c.vacancy.id " + "JOIN CandidateStatus cs ON c.id = cs.candidate.id "
			+ "JOIN Status s ON cs.status.id = s.id GROUP BY s.name")
	List<ChartDTO> getCandidateCountsForSelection();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(CONCAT('Interview',' stage ',CAST(i.stage AS string),' ', ci.status), COUNT(c.id)) FROM Vacancy v "
			+ "JOIN Interview i ON v.id = i.vacancy.id " + "JOIN CandidateInterview ci ON i.id = ci.interview.id "
			+ "JOIN Candidate c ON ci.candidate.id = c.id " + "GROUP BY i.stage, ci.status")
	List<ChartDTO> getCandidateCountsForInterviews();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(s.name,COUNT(c.id)) " + "FROM Vacancy v "
			+ "JOIN Candidate c ON v.id = c.vacancy.id " + "JOIN CandidateStatus cs ON c.id = cs.candidate.id "
			+ "JOIN Status s ON cs.status.id = s.id " + "WHERE v.id = :vacancyId " + "GROUP BY s.name")
	List<ChartDTO> getCandidateCountsByVacancyIdForSelection(Long vacancyId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(CONCAT('Interview',' stage ',CAST(i.stage AS string),' ', ci.status), COUNT(c.id)) FROM Vacancy v "
			+ "JOIN Interview i ON v.id = i.vacancy.id " + "JOIN CandidateInterview ci ON i.id = ci.interview.id "
			+ "JOIN Candidate c ON ci.candidate.id = c.id " + "WHERE v.id = :vacancyId "
			+ "GROUP BY v.id, i.stage, ci.status")
	List<ChartDTO> getCandidateCountsByVacancyIdForInterviews(Long vacancyId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(s.name,COUNT(c.id)) " + "FROM Vacancy v "
			+ "JOIN Candidate c ON v.id = c.vacancy.id AND v.department.id=:departmentId "
			+ "JOIN CandidateStatus cs ON c.id = cs.candidate.id "
			+ "JOIN Status s ON cs.status.id = s.id GROUP BY s.name")
	List<ChartDTO> getCandidateCountsForSelectionForInterviewer(int departmentId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(CONCAT('Interview',' stage ',CAST(i.stage AS string),' ', ci.status), COUNT(c.id)) FROM Vacancy v "
			+ "JOIN Interview i ON v.id = i.vacancy.id AND v.department.id=:departmentId "
			+ "JOIN CandidateInterview ci ON i.id = ci.interview.id " + "JOIN Candidate c ON ci.candidate.id = c.id "
			+ "GROUP BY i.stage, ci.status")
	List<ChartDTO> getCandidateCountsForInterviewsForInterviewer(int departmentId);

	List<Vacancy> findByDepartmentAndPosition(Department department, Position position);

	// dashboard chart end

	// to get vacancyList for interview add
	@Query("SELECT new com.ace.job.recruitment.dto.VacancyForInterviewDTO(v.id, v.position, v.department,v.createdDate,v.dueDate) FROM Vacancy v WHERE v.createdDate >= :threeMonthsAgo")
	List<VacancyForInterviewDTO> getVacanciesForInterview(LocalDate threeMonthsAgo);

	// to get vacancyList for interview add
	@Query("SELECT new com.ace.job.recruitment.dto.VacancyForInterviewDTO(v.id, v.position, v.department,v.createdDate,v.dueDate) FROM Vacancy v WHERE v.createdDate >= :threeMonthsAgo OR v.id = :vacancyId")
	List<VacancyForInterviewDTO> getVacanciesForInterviewUpdate(LocalDate threeMonthsAgo, long vacancyId);

}