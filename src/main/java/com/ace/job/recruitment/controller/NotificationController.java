package com.ace.job.recruitment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ace.job.recruitment.entity.Notification;
import com.ace.job.recruitment.entity.User;
import com.ace.job.recruitment.model.AppUserDetails;
import com.ace.job.recruitment.service.NotificationService;
import com.ace.job.recruitment.service.UserService;

@Controller
@RequestMapping("/hr")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	@ModelAttribute("notifications")
	private List<Notification> getAllNotifications() {
		return notificationService.getAllNotifications();
	}

	@GetMapping("/view-all-notifications")
	private String showAllNotifications(@AuthenticationPrincipal AppUserDetails userDetails) {
		markAsRead(userDetails);
		return "notification_list";
	}

	@ResponseBody
	@GetMapping("/mark-as-read")
	private void markAsRead(@AuthenticationPrincipal AppUserDetails userDetails) {
		List<Notification> notifications = notificationService.getAllNotifications();
		User userById = userService.getUserById(userDetails.getId());
		List<User> user = new ArrayList<>();
		user.add(userById);
		for (Notification notification : notifications) {
			notificationService.updateNotificationStatus(notification, user);
		}
	}
}
