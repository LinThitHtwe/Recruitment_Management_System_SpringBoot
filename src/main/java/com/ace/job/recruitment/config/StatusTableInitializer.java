package com.ace.job.recruitment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ace.job.recruitment.entity.Status;
import com.ace.job.recruitment.service.StatusService;

@Component
public class StatusTableInitializer implements CommandLineRunner {
	@Autowired
	StatusService statusService;

	@Override
	public void run(String... args) throws Exception {
		if (statusService.getStatusByName("Received") == null) {
			Status status1 = new Status();
			status1.setId(1);
			status1.setName("Received");
			statusService.addStatus(status1);
		}

		if (statusService.getStatusByName("Viewed") == null) {
			Status status2 = new Status();
			status2.setId(2);
			status2.setName("Viewed");
			statusService.addStatus(status2);
		}

		if (statusService.getStatusByName("Considering") == null) {
			Status status3 = new Status();
			status3.setId(3);
			status3.setName("Considering");
			statusService.addStatus(status3);
		}

	}

}
