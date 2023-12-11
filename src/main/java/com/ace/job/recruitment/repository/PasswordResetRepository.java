package com.ace.job.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
	List<PasswordReset> findByUser(User user);

	PasswordReset findByResetToken(String resetToken);

	@Query("SELECT ps FROM PasswordReset ps WHERE resetToken IS NOT NULL")
	List<PasswordReset> findNotNullTokens();
}