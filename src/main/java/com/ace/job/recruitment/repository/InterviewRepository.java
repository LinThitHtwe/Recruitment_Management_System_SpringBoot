package com.ace.job.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.entity.Interview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long>, DataTablesRepository<Interview, Long> {
	List<Interview> findAllByTypeAndStageAndVacancyIdAndStatus(String type, int stage, long vacancyId, boolean status);

	@Query("SELECT i FROM Interview i WHERE (i.status=true AND i.endDate IS NOT NULL AND i.endDate < CURRENT_DATE) OR (i.status=true AND i.endDate IS NULL AND i.startDate < CURRENT_DATE)")
	List<Interview> getInterviewForExpired();

	List<Interview> findByTypeAndStageAndStatus(String type, int stage, boolean status);

	@Query("SELECT i FROM Interview i WHERE type=:type AND stage=:stage AND i.vacancy.id=:vacancyId AND i.status=true")
	List<Interview> getInterviewForDuplication(String type, int stage, long vacancyId);

	@Query("SELECT i FROM Interview i WHERE type = :type AND stage = :stage AND i.vacancy.id = :vacancyId AND i.id <> :interviewId AND i.status=true")
	List<Interview> getInterviewForDuplicationUpdate(String type, int stage, long vacancyId, long interviewId);

	List<Interview> getInterviewByTypeAndStageAndStatusAndVacancyId(String type, int stage, boolean status,
			long vacancyId);

}