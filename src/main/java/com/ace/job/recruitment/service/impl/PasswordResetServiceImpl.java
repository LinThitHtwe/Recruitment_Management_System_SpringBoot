package com.ace.job.recruitment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.repository.PasswordResetRepository;
import com.ace.job.recruitment.service.PasswordResetService;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
	@Autowired
	PasswordResetRepository passwordResetRepository;

	@Override
	public List<PasswordReset> getByUser(User user) {
		return passwordResetRepository.findByUser(user);
	}

	@Override
	public PasswordReset storePasswordReset(PasswordReset passwordReset) {
		return passwordResetRepository.save(passwordReset);
	}

	@Override
	public PasswordReset getByResetToken(String resetToken) {
		return passwordResetRepository.findByResetToken(resetToken);
	}

	@Override
	public PasswordReset getById(long id) {
		return passwordResetRepository.findById(id).get();
	}

	@Override
	public PasswordReset updatePasswordReset(PasswordReset passwordReset) {
		return passwordResetRepository.save(passwordReset);
	}

	@Override
	public List<PasswordReset> getPasswordResetByNotNullTokens() {
		return passwordResetRepository.findNotNullTokens();
	}

}