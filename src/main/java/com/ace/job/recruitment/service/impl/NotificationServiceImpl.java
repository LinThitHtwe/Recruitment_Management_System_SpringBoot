package com.ace.job.recruitment.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ace.job.recruitment.entity.Notification;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.repository.NotificationRepository;
import com.ace.job.recruitment.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public void createNotification(String message, LocalDateTime createdAt, Vacancy vacancy) {
		Notification notification = new Notification();
		notification.setMessage(message);
		notification.setCreatedAt(createdAt);
		notification.setStatus(false);
		notification.setVacancy(vacancy);
		notificationRepository.save(notification);
		messagingTemplate.convertAndSend("/topic/notification", message);
	}

	@Override
	public List<Notification> getAllNotifications() {
		List<Notification> list = notificationRepository.findAll();
		return list;
	}

	@Override
	public void updateNotificationStatus(Notification notification, List<User> user) {
		List<User> users = notification.getUser();
		users.addAll(user);
		notificationRepository.save(notification);
	}

	@Override
	public Notification getNotificationById(long id) {
		return notificationRepository.findById(id);
	}

	@Override
	public List<Notification> getAllByUserId(int id) {
		return notificationRepository.findAllByUserId(id);
	}

}