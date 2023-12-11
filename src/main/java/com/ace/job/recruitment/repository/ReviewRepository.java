package com.ace.job.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.entity.Reviews;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

}
