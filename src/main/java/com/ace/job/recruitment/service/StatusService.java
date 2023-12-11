package com.ace.job.recruitment.service;

import com.ace.job.recruitment.entity.Status;

public interface StatusService {
	Status getStatusById(int id);

	Status addStatus(Status status);

	Status getStatusByName(String name);
}
