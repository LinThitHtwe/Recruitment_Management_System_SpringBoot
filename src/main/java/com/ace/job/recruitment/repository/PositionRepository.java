package com.ace.job.recruitment.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>, DataTablesRepository<Position, Integer> {
	List<Position> findByName(String name);

	@Query("SELECT p FROM Position p " + "WHERE LOWER(p.name) LIKE %:query% OR "
			+ "LOWER(p.createdDateTime) LIKE %:query% OR " + "LOWER(p.createdUserId) LIKE %:query% OR "
			+ "p.createdUserId IN (SELECT u.id FROM User u WHERE LOWER(u.name) LIKE %:query%) ")
	List<Position> findByAnyFieldContaining(@Param("query") String query);

	// dashboard chart start
	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COUNT(v.id)) FROM Position p LEFT JOIN p.vacancies v GROUP BY p.name")
	List<ChartDTO> getPositionWithVacancyCount();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(v.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId " + "GROUP BY p.id, p.name")
	List<ChartDTO> getPositionVacancyCountsByDepartment(int departmentId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(v.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON YEAR(v.createdDate) = :year " + "GROUP BY p.id, p.name")
	List<ChartDTO> getPositionVacancyCountsByYear(int year);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(v.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON YEAR(v.createdDate) = :year AND MONTH(v.createdDate) = :month "
			+ "GROUP BY p.id, p.name")
	List<ChartDTO> getPositionVacancyCountsByYearMonth(int year, int month);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(v.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId " + "AND YEAR(v.createdDate) = :year "
			+ "GROUP BY p.id, p.name")
	List<ChartDTO> getPositionVacancyCountsByDepartmentAndYear(int departmentId, int year);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(v.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId "
			+ "AND YEAR(v.createdDate) = :year AND MONTH(v.createdDate) = :month GROUP BY p.id, p.name")
	List<ChartDTO> getPositionVacancyCountsByDepartmentYearAndMonth(int departmentId, int year, int month);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, "
			+ "   SUM(v.count) AS count FROM Position p JOIN p.vacancies v ON p.id = v.position.id "
			+ "   GROUP BY p.name, YEAR(v.createdDate)" + "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getPositionDemand();

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, "
			+ "   SUM(v.count) AS count FROM Position p JOIN p.vacancies v ON p.id = v.position.id "
			+ "WHERE YEAR(v.createdDate) = :startYear " + "   GROUP BY p.name, YEAR(v.createdDate)" + "ORDER BY p.name")
	List<Map<String, Object>> getPositionDemandByStartYear(int startYear);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, "
			+ "   SUM(v.count) AS count FROM Position p JOIN p.vacancies v ON p.id = v.position.id "
			+ "WHERE YEAR(v.createdDate) BETWEEN :startYear AND :endYear GROUP BY p.name, YEAR(v.createdDate)"
			+ "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getPositionDemandByYears(int startYear, int endYear);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, " + "COALESCE(COUNT(c.id), 0) AS count "
			+ "FROM Position p " + "JOIN p.vacancies v "
			+ "LEFT JOIN v.candidates c GROUP BY p.name, YEAR(v.createdDate) " + "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getCandidatePositionDemand();

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, " + "COALESCE(COUNT(c.id), 0) AS count "
			+ "FROM Position p " + "JOIN p.vacancies v " + "LEFT JOIN v.candidates c "
			+ "WHERE YEAR(v.createdDate) =:startYear " + "GROUP BY p.name, YEAR(v.createdDate) " + "ORDER BY p.name")
	List<Map<String, Object>> getCandidatePositionDemandByStartYear(int startYear);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, " + "COALESCE(COUNT(c.id), 0) AS count "
			+ "FROM Position p " + "JOIN p.vacancies v " + "LEFT JOIN v.candidates c "
			+ "WHERE YEAR(v.createdDate) BETWEEN :startYear AND :endYear " + "GROUP BY p.name, YEAR(v.createdDate) "
			+ "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getCandidatePositionDemandByYears(int startYear, int endYear);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COUNT(c.id)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v LEFT JOIN v.candidates c " + "GROUP BY p.name")
	List<ChartDTO> getCandidateCountByPosition();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId LEFT JOIN v.candidates c "
			+ "GROUP BY p.name")
	List<ChartDTO> getCandidateCountByPositionAndDepartment(int departmentId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON YEAR(v.createdDate) = :year LEFT JOIN v.candidates c " + "GROUP BY p.name")
	List<ChartDTO> getCandidateCountByPositionAndYear(int year);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON YEAR(v.createdDate) = :year AND MONTH(v.createdDate) = :month LEFT JOIN v.candidates c "
			+ "GROUP BY p.name")
	List<ChartDTO> getCandidateCountByPositionAndTime(int year, int month);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId " + "AND YEAR(v.createdDate) = :year "
			+ " LEFT JOIN v.candidates c GROUP BY p.name")
	List<ChartDTO> getCandidateCountByPositionAndDepartmentAndYear(int departmentId, int year);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId "
			+ "AND YEAR(v.createdDate) = :year AND MONTH(v.createdDate) = :month  LEFT JOIN v.candidates c GROUP BY p.name")
	List<ChartDTO> getCandidateCountByPositionAndDepartmentAndTime(int departmentId, int year, int month);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.active = true LEFT JOIN v.candidates c " + "GROUP BY p.name")
	List<ChartDTO> getCandidateCountByActiveVacancies();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) " + "FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.active = true AND v.urgent=true LEFT JOIN v.candidates c "
			+ "GROUP BY p.name")
	List<ChartDTO> getCandidateCountByUrgentVacancies();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId AND v.active = true LEFT JOIN v.candidates c "
			+ " GROUP BY p.name")
	List<ChartDTO> getCandidateCountByActiveVacanciesAndDepartment(int departmentId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(p.name, COALESCE(COUNT(c.id), 0)) FROM Position p "
			+ "LEFT JOIN p.vacancies v ON v.department.id = :departmentId AND v.active = true AND v.urgent = true LEFT JOIN v.candidates c "
			+ " GROUP BY p.name")
	List<ChartDTO> getCandidateCountByUrgentVacanciesAndDepartment(int departmentId);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, "
			+ "   SUM(v.count) AS count FROM Position p JOIN p.vacancies v ON p.id = v.position.id AND v.department.id=:departmentId "
			+ "   GROUP BY p.name, YEAR(v.createdDate)" + "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getPositionDemandForInterviewer(int departmentId);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, "
			+ "   SUM(v.count) AS count FROM Position p JOIN p.vacancies v ON p.id = v.position.id AND v.department.id=:departmentId "
			+ "WHERE YEAR(v.createdDate) = :startYear " + "   GROUP BY p.name, YEAR(v.createdDate)" + "ORDER BY p.name")
	List<Map<String, Object>> getPositionDemandByStartYearForInterviewer(int departmentId, int startYear);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, "
			+ "   SUM(v.count) AS count FROM Position p JOIN p.vacancies v ON p.id = v.position.id AND v.department.id=:departmentId "
			+ "WHERE YEAR(v.createdDate) BETWEEN :startYear AND :endYear GROUP BY p.name, YEAR(v.createdDate)"
			+ "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getPositionDemandByYearsForInterviewer(int departmentId, int startYear, int endYear);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, " + "COALESCE(COUNT(c.id), 0) AS count "
			+ "FROM Position p " + "JOIN p.vacancies v On v.department.id=:departmentId "
			+ "LEFT JOIN v.candidates c GROUP BY p.name, YEAR(v.createdDate) " + "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getCandidatePositionDemandForInterviewer(int departmentId);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, " + "COALESCE(COUNT(c.id), 0) AS count "
			+ "FROM Position p " + "JOIN p.vacancies v " + "LEFT JOIN v.candidates c On v.department.id=:departmentId "
			+ "WHERE YEAR(v.createdDate) =:startYear " + "GROUP BY p.name, YEAR(v.createdDate) " + "ORDER BY p.name")
	List<Map<String, Object>> getCandidateTrendByStartYearForInterviewer(int departmentId, int startYear);

	@Query("SELECT p.name AS positionName, YEAR(v.createdDate) AS year, " + "COALESCE(COUNT(c.id), 0) AS count "
			+ "FROM Position p " + "JOIN p.vacancies v On v.department.id=:departmentId " + "LEFT JOIN v.candidates c "
			+ "WHERE YEAR(v.createdDate) BETWEEN :startYear AND :endYear " + "GROUP BY p.name, YEAR(v.createdDate) "
			+ "ORDER BY p.name, YEAR(v.createdDate)")
	List<Map<String, Object>> getCandidateTrendByYearsForInterviewer(int departmentId, int startYear, int endYear);

	// dashboard chart end

}
