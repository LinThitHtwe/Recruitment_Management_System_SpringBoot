package com.ace.job.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ace.job.recruitment.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
	Status findByName(String name);
}
