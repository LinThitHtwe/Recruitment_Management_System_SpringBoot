package com.ace.job.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ace.job.recruitment.dto.VacancyDto;
import com.ace.job.recruitment.entity.Vacancy;
import com.ace.job.recruitment.repository.VacancyRepository;
import com.ace.job.recruitment.service.impl.VacancyServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class VacancyServiceTest {

	@Mock
	private VacancyRepository vacancyRepository;

	@Mock
	private NotificationService notificationService;

	@InjectMocks
	private VacancyServiceImpl vacancyService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEditVacancyAndNotify() {
		VacancyDto vacancyDto = new VacancyDto();
		String message = "Test Message";

	}

	@Test
    public void testGetAllVacancy() {
        // Mock the repository to return a list of vacancies
        when(vacancyRepository.findAll()).thenReturn(Collections.singletonList(new Vacancy()));

        List<Vacancy> vacancies = vacancyService.getAllVacancy();

        // Verify that the findAll method was called and returned the expected vacancies
        verify(vacancyRepository, times(1)).findAll();
    }

	@Test
    public void testGetUrgentVacancy() {
        // Mock the repository to return a list of urgent vacancies
        when(vacancyRepository.findByUrgentTrue()).thenReturn(Collections.singletonList(new Vacancy()));

        List<Vacancy> urgentVacancies = vacancyService.getUrgentVacancy();

        // Verify that the findByUrgentTrue method was called and returned the expected urgent vacancies
        verify(vacancyRepository, times(1)).findByUrgentTrue();
    }

	@Test
	public void testGetVacancyById() {
		long vacancyId = 1L;
		Vacancy mockVacancy = new Vacancy();
		mockVacancy.setId(vacancyId);
		// Set up mock behavior for the findById method
		when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(mockVacancy));

		VacancyDto dto = vacancyService.getVacancyById(vacancyId);

		assertEquals(vacancyId, dto.getId());
	}

	@Test
	public void testGetCurrentDateTime() {
		LocalDateTime expectedDateTime = LocalDateTime.now();
		// Since getCurrentDateTime uses LocalDateTime.now(), you can directly compare
		// the values
		LocalDateTime actualDateTime = vacancyService.getCurrentDateTime();

		assertEquals(expectedDateTime.getDayOfMonth(), actualDateTime.getDayOfMonth());
		assertEquals(expectedDateTime.getMonthValue(), actualDateTime.getMonthValue());
		assertEquals(expectedDateTime.getYear(), actualDateTime.getYear());
		assertEquals(expectedDateTime.getHour(), actualDateTime.getHour());
		assertEquals(expectedDateTime.getMinute(), actualDateTime.getMinute());
	}

	@Test
	public void testGetCurrentDate() {
		LocalDate expectedDate = LocalDate.now();
		// Since getCurrentDate uses LocalDate.now(), you can directly compare the
		// values
		LocalDate actualDate = vacancyService.getCurrentDate();

		assertEquals(expectedDate.getDayOfMonth(), actualDate.getDayOfMonth());
		assertEquals(expectedDate.getMonthValue(), actualDate.getMonthValue());
		assertEquals(expectedDate.getYear(), actualDate.getYear());
	}

	@Test
	public void testGetCurrentTime() {
		LocalTime expectedTime = LocalTime.now();
		// Since getCurrentTime uses LocalTime.now(), you can directly compare the
		// values
		LocalTime actualTime = vacancyService.getCurrentTime();

		assertEquals(expectedTime.getHour(), actualTime.getHour());
		assertEquals(expectedTime.getMinute(), actualTime.getMinute());
	}

	@Test
	public void testConvertToFormattedTime() {
		String timeString = "14:30";
		String expectedFormattedTime = "02:30 PM";

		String actualFormattedTime = vacancyService.convertToFormattedTime(timeString);

		assertEquals(expectedFormattedTime, actualFormattedTime);
	}

	@Test
	public void testMarkVacanciesAsInactiveAfter30Days() {
		LocalDate currentDate = LocalDate.now();
		Vacancy activeVacancy1 = new Vacancy();
		activeVacancy1.setActive(true);
		activeVacancy1.setCreatedDate(currentDate.minusDays(31)); // Created more than 30 days ago
		Vacancy activeVacancy2 = new Vacancy();
		activeVacancy2.setActive(true);
		activeVacancy2.setCreatedDate(currentDate.minusDays(29)); // Created less than 30 days ago
		List<Vacancy> activeVacancies = new ArrayList<>();
		activeVacancies.add(activeVacancy1);
		activeVacancies.add(activeVacancy2);

		when(vacancyRepository.findByActiveTrue()).thenReturn(activeVacancies);

		vacancyService.markVacanciesAsInactiveAfter30Days();

		assertFalse(activeVacancy1.isActive());

	}

	@Test
	public void testCalculateDaysLeft() {
		LocalDate createdDate = LocalDate.now().minusDays(15);
		int expectedDaysLeft = 15;

		int actualDaysLeft = vacancyService.calculateDaysLeft(createdDate);

		assertEquals(expectedDaysLeft, actualDaysLeft);
	}

	@Test
	public void testTrimArray() {
		String[] array = { "value1", "", "value2", "", "value3" };
		List<String> expectedTrimmedArray = new ArrayList<>();
		expectedTrimmedArray.add("value1");
		expectedTrimmedArray.add("value2");
		expectedTrimmedArray.add("value3");

		List<String> actualTrimmedArray = vacancyService.trimArray(array);

		assertEquals(expectedTrimmedArray, actualTrimmedArray);
	}

}
