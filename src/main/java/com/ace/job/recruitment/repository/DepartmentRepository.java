package com.ace.job.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ace.job.recruitment.dto.ChartDTO;
import com.ace.job.recruitment.entity.Department;

public interface DepartmentRepository
		extends JpaRepository<Department, Integer>, DataTablesRepository<Department, Integer> {
	Department findByName(String name);

	Department findByNameAndIdNot(String name, int id);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(d.name, COUNT(v.id)) FROM Department d LEFT JOIN d.vacancies v GROUP BY d.name")
	List<ChartDTO> getDepartmentWithVacancyCount();

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(d.name, COALESCE(COUNT(v.id), 0)) " + "FROM Department d "
			+ "LEFT JOIN d.vacancies v ON v.position.id = :positionId " + "GROUP BY d.id, d.name")
	List<ChartDTO> getDepartmentVacancyCountsByPosition(int positionId);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(d.name, COALESCE(COUNT(v.id), 0)) " + "FROM Department d "
			+ "LEFT JOIN d.vacancies v ON YEAR(v.createdDate) = :year " + "GROUP BY d.id, d.name")
	List<ChartDTO> getDepartmentVacancyCountsByYear(int year);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(d.name, COALESCE(COUNT(v.id), 0)) " + "FROM Department d "
			+ "LEFT JOIN d.vacancies v ON YEAR(v.createdDate) = :year AND MONTH(v.createdDate) = :month "
			+ "GROUP BY d.id, d.name")
	List<ChartDTO> getDepartmentVacancyCountsByYearMonth(int year, int month);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(d.name, COALESCE(COUNT(v.id), 0)) " + "FROM Department d "
			+ "LEFT JOIN d.vacancies v ON v.position.id = :positionId " + "AND YEAR(v.createdDate) = :year "
			+ "GROUP BY d.id, d.name")
	List<ChartDTO> getDepartmentVacancyCountsByPositionAndYear(int positionId, int year);

	@Query("SELECT new com.ace.job.recruitment.dto.ChartDTO(d.name, COALESCE(COUNT(v.id), 0)) " + "FROM Department d "
			+ "LEFT JOIN d.vacancies v ON v.position.id = :positionId "
			+ "AND YEAR(v.createdDate) = :year AND MONTH(v.createdDate) = :month GROUP BY d.id, d.name")
	List<ChartDTO> getDepartmentVacancyCountsByPositionYearAndMonth(int positionId, int year, int month);

}