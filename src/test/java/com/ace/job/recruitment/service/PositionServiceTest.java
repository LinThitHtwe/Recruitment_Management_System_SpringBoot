package com.ace.job.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ace.job.recruitment.entity.Position;
import com.ace.job.recruitment.repository.PositionRepository;
import com.ace.job.recruitment.service.PositionService;
import com.ace.job.recruitment.service.impl.PositionServiceImpl;

public class PositionServiceTest {

	@InjectMocks
	private PositionService positionService = new PositionServiceImpl();

	@Mock
	private PositionRepository positionRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddPosition() {
		// Create a sample Position
		Position position = new Position();
		position.setId(1);
		position.setName("Software Engineer");

		// Mock the repository's save method
		when(positionRepository.save(any(Position.class))).thenReturn(position);

		// Call the service method
		Position savedPosition = positionService.addPosition(position);

		// Verify the save method was called with the correct argument
		verify(positionRepository, times(1)).save(position);

		// Assert that the returned position matches the expected position
		assertThat(savedPosition).isEqualTo(position);
	}

	@Test
	public void testGetPositionById() {
		// Create a sample Position
		Position position = new Position();
		position.setId(1);
		position.setName("Software Engineer");

		// Mock the repository's findById method
		when(positionRepository.findById(1)).thenReturn(Optional.of(position));

		// Call the service method
		Position foundPosition = positionService.getPositionById(1);

		// Verify the findById method was called with the correct argument
		verify(positionRepository, times(1)).findById(1);

		// Assert that the returned position matches the expected position
		assertThat(foundPosition).isEqualTo(position);
	}

	@Test
	public void testGetPositionByName() {
		// Create a sample Position
		Position position = new Position();
		position.setId(1);
		position.setName("Software Engineer");

		// Mock the repository's findByName method
		when(positionRepository.findByName("Software Engineer")).thenReturn(List.of(position));

		// Call the service method
		List<Position> foundPositions = positionService.getPositionByName("Software Engineer");

		// Verify the findByName method was called with the correct argument
		verify(positionRepository, times(1)).findByName("Software Engineer");

		// Assert that the returned list contains the expected position
		assertThat(foundPositions).containsExactly(position);
	}

	@Test
    public void testGetPositionByName_NotFound() {
        // Mock the repository's findByName method to return an empty list
        when(positionRepository.findByName("Software Engineer")).thenReturn(new ArrayList<>());

        // Call the service method
        List<Position> foundPositions = positionService.getPositionByName("Software Engineer");

        // Verify the findByName method was called with the correct argument
        verify(positionRepository, times(1)).findByName("Software Engineer");

        // Assert that the returned list is empty
        assertThat(foundPositions).isEmpty();
    }

	@Test
	public void testUpdatePosition() {
		// Create a sample Position
		Position position = new Position();
		position.setId(1);
		position.setName("Software Engineer");

		// Mock the repository's save method
		when(positionRepository.save(any(Position.class))).thenReturn(position);

		// Call the service method to update the position
		Position updatedPosition = positionService.updatePosition(position);

		// Verify the save method was called with the correct argument
		verify(positionRepository, times(1)).save(position);

		// Assert that the returned position matches the expected position
		assertThat(updatedPosition).isEqualTo(position);
	}

	@Test
	public void testGetAllPositions() {
		// Create some sample Positions
		Position position1 = new Position();
		position1.setId(1);
		position1.setName("Software Engineer");

		Position position2 = new Position();
		position2.setId(2);
		position2.setName("Product Manager");

		// Mock the repository's findAll method
		when(positionRepository.findAll()).thenReturn(List.of(position1, position2));

		// Call the service method
		List<Position> allPositions = positionService.getAllPositions();

		// Verify the findAll method was called
		verify(positionRepository, times(1)).findAll();

		// Assert that the returned list contains all the expected positions
		assertThat(allPositions).containsExactlyInAnyOrder(position1, position2);
	}

}
