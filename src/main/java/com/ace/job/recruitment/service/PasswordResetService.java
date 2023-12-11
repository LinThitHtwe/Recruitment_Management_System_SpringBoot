package com.ace.job.recruitment.service;

import java.util.List;

import com.ace.job.recruitment.entity.PasswordReset;
import com.ace.job.recruitment.entity.User;

public interface PasswordResetService {
	public List<PasswordReset> getByUser(User user);

	public PasswordReset storePasswordReset(PasswordReset passwordReset);

	public PasswordReset updatePasswordReset(PasswordReset passwordReset);

	public PasswordReset getByResetToken(String resetToken);

	public PasswordReset getById(long id);

	public List<PasswordReset> getPasswordResetByNotNullTokens();

}