
package com.ace.job.recruitment.repository; /*import static
											* org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.when;
											* 
											* import java.time.LocalDateTime; import java.util.Arrays; import
											* java.util.List;
											* 
											* import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
											* import org.mockito.InjectMocks; import org.mockito.Mock; import
											* org.mockito.MockitoAnnotations; import
											* org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest; import
											* org.springframework.boot.test.context.SpringBootTest;
											* 
											* import com.ace.job.recruitment.entity.Notification; import
											* com.ace.job.recruitment.repository.NotificationRepository;
											* 
											* @SpringBootTest public class NotificationRepositoryTest {
											* 
											* @Mock private NotificationRepository notificationRepository;
											* 
											* @BeforeEach public void setup() { MockitoAnnotations.openMocks(this); }
											* 
											* @Test public void testFindAllByStatus() { // Mock data Notification
											* notification1 = new Notification(); notification1.setStatus(true);
											* 
											* Notification notification2 = new Notification();
											* notification2.setStatus(false);
											* 
											* List<Notification> notifications = Arrays.asList(notification1,
											* notification2);
											* 
											* // Mock repository method
											* when(notificationRepository.findAllByStatus(true)).thenReturn(notifications);
											* 
											* // Test repository method List<Notification> result =
											* notificationRepository.findAllByStatus(true);
											* 
											* assertEquals(1, result.size()); assertTrue(result.get(0).isStatus()); }
											* 
											* @Test public void testFindAllByUserId() { // Mock data int userId = 1;
											* Notification notification1 = new Notification();
											* notification1.setUser(Arrays.asList()); // Set user mock data here
											* notification1.setId(userId);
											* 
											* Notification notification2 = new Notification();
											* notification2.setUser(Arrays.asList()); // Set user mock data here
											* notification2.setId(userId + 1);
											* 
											* List<Notification> notifications = Arrays.asList(notification1,
											* notification2);
											* 
											* // Mock repository method
											* when(notificationRepository.findAllByUserId(userId)).thenReturn(notifications
											* );
											* 
											* // Test repository method List<Notification> result =
											* notificationRepository.findAllByUserId(userId);
											* 
											* assertEquals(1, result.size()); assertEquals(userId,
											* result.get(0).getUser().get(0).getId()); }
											* 
											* @Test public void testFindById() { // Mock data long notificationId = 1L;
											* Notification notification = new Notification();
											* notification.setId(notificationId); notification.setStatus(true);
											* notification.setCreatedAt(LocalDateTime.now());
											* 
											* // Mock repository method
											* when(notificationRepository.findById(notificationId)).thenReturn(notification
											* );
											* 
											* // Test repository method Notification result =
											* notificationRepository.findById(notificationId);
											* 
											* assertNotNull(result); assertEquals(notificationId, result.getId()); } }
											*/

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.ace.job.recruitment.entity.Notification;
import com.ace.job.recruitment.entity.User;

@SpringBootTest
@Transactional
@DirtiesContext
public class NotificationRepositoryTest {

	@Autowired
	private NotificationRepository notificationRepository;

	@Test
	public void testFindAllByStatus() {
		// Save sample notifications
		Notification notification1 = new Notification("Message 1", LocalDateTime.now(), true);
		Notification notification2 = new Notification("Message 2", LocalDateTime.now(), false);
		notificationRepository.saveAll(List.of(notification1, notification2));

		// Test repository method
		List<Notification> result = notificationRepository.findAllByStatus(true);

		assertEquals(1, result.size());
		assertTrue(result.get(0).isStatus());
	}

	@Test
	public void testFindAllByUserId() {
		User user1 = new User();
		user1.setId(1);
		User user2 = new User();
		user2.setId(2);
		// Save sample notifications
		Notification notification1 = new Notification("Message 1", LocalDateTime.now(), true);
		Notification notification2 = new Notification("Message 2", LocalDateTime.now(), true);
		notification1.setUser(List.of(user1));
		notification2.setUser(List.of(user2));
		notificationRepository.saveAll(List.of(notification1, notification2));

		// Test repository method
		List<Notification> result = notificationRepository.findAllByUserId(1);

	}

	@Test
	public void testFindById() {
		// Save a sample notification
		Notification notification = new Notification("Message", LocalDateTime.now(), true);
		notificationRepository.save(notification);

		// Test repository method
		Notification result = notificationRepository.findById(notification.getId());

		assertNotNull(result);
		assertEquals(notification.getId(), result.getId());
	}
}
