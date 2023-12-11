//package com.ace.job.recruitment.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.context.SecurityContextImpl;
//
//import com.ace.job.recruitment.entity.Position;
//import com.ace.job.recruitment.entity.User;
//import com.ace.job.recruitment.model.AppUserDetails;
//import com.ace.job.recruitment.service.PositionService;
//import com.ace.job.recruitment.service.UserService;
//
//class PositionControllerTest {
//
//	private PositionService positionService = mock(PositionService.class);
//	private UserService userService = mock(UserService.class);
//	private PositionController positionController = new PositionController();
//
//	@Test
//	void addPosition_shouldReturnBadRequestIfPositionExists() {
//		// Arrange
//		Position position = new Position();
//		position.setName("Test Position");
//		when(positionService.getPositionByName(position.getName())).thenReturn(List.of(new Position()));
//
//		// Act
//		ResponseEntity<String> response = positionController.addPosition(position);
//
//		// Assert
//		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//		assertEquals("Test Position already exists", response.getBody());
//	}
//
//	@Test
//	void addPosition_shouldAddNewPositionAndReturnOk() {
//		// Arrange
//		Position position = new Position();
//		position.setName("Test Position");
//		when(positionService.getPositionByName(position.getName())).thenReturn(Collections.emptyList());
//		when(positionService.addPosition(any(Position.class))).thenReturn(true);
//
//		// Act
//		ResponseEntity<String> response = positionController.addPosition(position);
//
//		// Assert
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertNull(response.getBody());
//	}
//
//	@Test
//	void duplicatePosition_shouldReturnTrueIfPositionExists() {
//		// Arrange
//		String positionName = "Test Position";
//		when(positionService.getPositionByName(positionName)).thenReturn(List.of(new Position()));
//
//		// Act
//		boolean result = positionController.duplicatePosition(positionName);
//
//		// Assert
//		assertTrue(result);
//	}
//
//	@Test
//	void duplicatePosition_shouldReturnFalseIfPositionDoesNotExist() {
//		// Arrange
//		String positionName = "Test Position";
//		when(positionService.getPositionByName(positionName)).thenReturn(Collections.emptyList());
//
//		// Act
//		boolean result = positionController.duplicatePosition(positionName);
//
//		// Assert
//		assertFalse(result);
//	}
//
//	@Test
//	void updatePosition_shouldReturnOkIfPositionUpdatedSuccessfully() {
//		// Arrange
//		int positionId = 1;
//		String positionName = "Updated Test Position";
//		Position existingPosition = new Position();
//		existingPosition.setId(positionId);
//		when(positionService.getPositionById(positionId)).thenReturn(existingPosition);
//		when(positionService.updatePosition(any(Position.class))).thenReturn(true);
//
//		// Act
//		ResponseEntity<String> response = positionController.updatePosition(positionId, positionName);
//
//		// Assert
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertNull(response.getBody());
//		assertEquals(positionName, existingPosition.getName());
//	}
//
//	@Test
//	void getPositionTableValues_shouldReturnPositionTableValues() {
//		// Arrange
//		int page = 1;
//		int rowCount = 5;
//		int currentPage = 0;
//		int actualRowCount = 5;
//		Page<Position> positionPage = mock(Page.class);
//		List<Position> positions = List.of(new Position());
//		when(positionService.getPosition(currentPage, actualRowCount)).thenReturn(positionPage);
//		when(positionPage.getTotalPages()).thenReturn(1);
//		when(positionPage.getContent()).thenReturn(positions);
//
//		// Act
//		ResponseEntity<?> responseEntity = positionController.getPositionTableValues(null, Optional.of(page), rowCount);
//		Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
//
//		// Assert
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertNotNull(response);
//		assertEquals(positionPage, response.get("positionPage"));
//		assertEquals(List.of(1), response.get("pageNumbers"));
//		assertEquals(actualRowCount, response.get("rowCount"));
//	}
//
//	@Test
//	void searchPositions_shouldReturnEmptyListIfNoMatchesFound() {
//		// Arrange
//		String query = "Non-Existent Position";
//		when(positionService.search(query)).thenReturn(Collections.emptyList());
//
//		// Act
//		ResponseEntity<List<Position>> responseEntity = positionController.searchPositions(query);
//
//		// Assert
//		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
//		assertTrue(responseEntity.getBody().isEmpty());
//	}
//
//	@Test
//	void searchPositions_shouldReturnListOfPositionsIfMatchesFound() {
//		// Arrange
//		String query = "Test";
//		Position position = new Position();
//		position.setName("Test Position");
//		when(positionService.search(query)).thenReturn(List.of(position));
//
//		// Act
//		ResponseEntity<List<Position>> responseEntity = positionController.searchPositions(query);
//		List<Position> positions = responseEntity.getBody();
//
//		// Assert
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertNotNull(positions);
//		assertEquals(1, positions.size());
//		assertEquals(position.getName(), positions.get(0).getName());
//	}
//
//	@Test
//	void positionDetail_shouldReturnNotFoundIfPositionNotFound() {
//		// Arrange
//		int positionId = 1;
//		when(positionService.getPositionById(positionId)).thenReturn(null);
//
//		// Act
//		ResponseEntity<Position> responseEntity = positionController.positionDetail(positionId);
//
//		// Assert
//		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//		assertNull(responseEntity.getBody());
//	}
//
//	@Test
//	void positionDetail_shouldReturnPositionDetailIfPositionFound() {
//		// Arrange
//		int positionId = 1;
//		Position position = new Position();
//		position.setId(positionId);
//		when(positionService.getPositionById(positionId)).thenReturn(position);
//		User createdUser = new User();
//		createdUser.setName("Test User");
//		when(userService.getUserById(position.getCreatedUserId())).thenReturn(createdUser);
//		when(userService.getUserById(position.getUpdatedUserId())).thenReturn(null);
//
//		// Act
//		ResponseEntity<Position> responseEntity = positionController.positionDetail(positionId);
//		Position positionDetail = responseEntity.getBody();
//
//		// Assert
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertNotNull(positionDetail);
//		assertEquals(createdUser.getName(), positionDetail.getCreatedUsername());
//		assertEquals("-", positionDetail.getUpdatedUsername());
//	}
//
//	// ... (Same as previous test cases)
//
//	@Test
//	void getPositionDetail_shouldReturnNotFoundIfPositionNotFound() {
//		// Arrange
//		int positionId = 1;
//		when(positionService.getPositionById(positionId)).thenReturn(null);
//
//		// Act
//		ResponseEntity<Position> responseEntity = positionController.positionDetail(positionId);
//
//		// Assert
//		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//		assertNull(responseEntity.getBody());
//	}
//
//	@Test
//	void getPositionDetail_shouldReturnPositionDetailIfPositionFound() {
//		// Arrange
//		int positionId = 1;
//		Position position = new Position();
//		position.setId(positionId);
//		when(positionService.getPositionById(positionId)).thenReturn(position);
//		User createdUser = new User();
//		createdUser.setName("Test User");
//		when(userService.getUserById(position.getCreatedUserId())).thenReturn(createdUser);
//		when(userService.getUserById(position.getUpdatedUserId())).thenReturn(null);
//
//		// Act
//		ResponseEntity<Position> responseEntity = positionController.positionDetail(positionId);
//		Position positionDetail = responseEntity.getBody();
//
//		// Assert
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertNotNull(positionDetail);
//		assertEquals(createdUser.getName(), positionDetail.getCreatedUsername());
//		assertEquals("-", positionDetail.getUpdatedUsername());
//	}
//
//	@Test
//	void getCurrentUserId_shouldReturnUserIdIfAuthenticated() {
//		// Arrange
//		AppUserDetails userDetails = new AppUserDetails(123, "testuser", "password", Collections.emptyList());
//		Authentication authentication = mock(Authentication.class);
//		when(authentication.isAuthenticated()).thenReturn(true);
//		when(authentication.getPrincipal()).thenReturn(userDetails);
//		SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
//
//		// Act
//		int userId = positionController.getCurrentUserId();
//
//		// Assert
//		assertEquals(123, userId);
//	}
//
//	@Test
//	void getCurrentUserId_shouldReturnNegativeOneIfNotAuthenticated() {
//		// Arrange
//		Authentication authentication = mock(Authentication.class);
//		when(authentication.isAuthenticated()).thenReturn(false);
//		SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
//
//		// Act
//		int userId = positionController.getCurrentUserId();
//
//		// Assert
//		assertEquals(-1, userId);
//	}
//
//	@Test
//	void getCurrentUserId_shouldReturnNegativeOneIfPrincipalIsNotAppUserDetails() {
//		// Arrange
//		Object principal = new Object();
//		Authentication authentication = mock(Authentication.class);
//		when(authentication.isAuthenticated()).thenReturn(true);
//		when(authentication.getPrincipal()).thenReturn(principal);
//		SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
//
//		// Act
//		int userId = positionController.getCurrentUserId();
//
//		// Assert
//		assertEquals(-1, userId);
//	}
//}
