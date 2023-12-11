package com.ace.job.recruitment.config;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.entity.Interview;
import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.service.InterviewService;
import com.ace.job.recruitment.service.PasswordResetService;
import com.ace.job.recruitment.service.VacancyService;

@Configuration
@EnableScheduling
public class ScheduledTasks {

	@Autowired
	private PasswordResetService passwordResetService;

	@Autowired
	private VacancyService vacancyService;

	@Autowired
	private InterviewService interviewService;

	@Scheduled(cron = "0 0 * * * ?", zone = "Asia/Yangon")
	public void deleteResetTokenAfterOneHour() {
		System.out.println("schedule running");
		List<PasswordReset> passwordResetList = new ArrayList<PasswordReset>();
		passwordResetList = passwordResetService.getPasswordResetByNotNullTokens();

		if (!passwordResetList.isEmpty()) {
			System.out.println("reset not null");
			for (PasswordReset ps : passwordResetList) {
				Long expiredTimeStamp = ps.getExpireTimestamp();
				long currentTimeSeconds = System.currentTimeMillis() / 1000;
				long oneHour = 60 * 60; // 1 hour in seconds
				Long expiredPlusOneHour = expiredTimeStamp + oneHour;

				if (currentTimeSeconds >= expiredPlusOneHour) {
					System.out.println("after one hour");
					ps.setResetToken(null);
					passwordResetService.updatePasswordReset(ps);
				}
			}
		}
	}

	@Scheduled(cron = "0 0 * * * ?", zone = "Asia/Yangon")
	public void inactivateInterviewAfterExpired() {
		List<Interview> interviewList = new ArrayList<Interview>();
		interviewList = interviewService.checkInterviewForExpired();
		if (!interviewList.isEmpty()) {
			for (Interview i : interviewList) {
				i.setStatus(false);
				interviewService.updateInterview(i);
			}
		}
	}

	@Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Yangon")
	public void markVacanciesAsInactive() {
		List<Vacancy> vacancies = vacancyService.getAllVacancy();

		for (Vacancy vacancy : vacancies) {
			VacancyDto dto = new VacancyDto(vacancy.getId(), vacancy.getRequirement(), vacancy.getResponsibility(),
					vacancy.getDescription(), vacancy.getPreference(), vacancy.getStartWorkingDay(),
					vacancy.getEndWorkingDay(), vacancy.getStartWorkingHour(), vacancy.getEndWorkingHour(),
					vacancy.getSalary(), vacancy.getCount(), vacancy.getType(), vacancy.isReopenStatus(),
					vacancy.isReopened(), vacancy.getCreatedDate(), vacancy.getCreatedTime(), vacancy.getUpdatedDate(),
					vacancy.getUpdatedTime(), vacancy.getCreatedUserId(), vacancy.getUpdatedUserId(),
					vacancy.getDueDate(), vacancy.isActive(), vacancy.isUrgent(), vacancy.getReopenDate(),
					vacancy.getReopenTime(), vacancy.getDepartment(), vacancy.getCandidates(), vacancy.getPosition());
			LocalDate creationDate = vacancy.getCreatedDate();
			LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Yangon"));
			Duration duration = Duration.between(creationDate, currentDate);

			if (duration.toDays() >= 30) {
				dto.setActive(false); // Update the vacancy status
				vacancyService.updateVacancy(dto); // Update the vacancy in the database
			}
		}
	}
}