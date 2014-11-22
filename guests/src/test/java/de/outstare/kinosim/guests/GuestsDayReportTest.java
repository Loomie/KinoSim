package de.outstare.kinosim.guests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.FixedSizeCinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.movie.SimpleMovie;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.schedule.AdBlock;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.Show;

public class GuestsDayReportTest {

	@Test
	public void testGetTotalGuests() {
		// real objects for ease of data holding and sorting
		final Movie film1 = newTestMovie("Test 1", 20);
		final Movie film2 = newTestMovie("Test 2", 80);
		final CinemaHall hall1 = new FixedSizeCinemaHall("1", 1, 100);
		final CinemaHall hall2 = new FixedSizeCinemaHall("2", 2, 200);
		final Show show1 = new Show(LocalTime.of(1, 0), film1, hall1, AdBlock.NONE, 0);
		final Show show2 = new Show(LocalTime.of(1, 0), film2, hall2, AdBlock.NONE, 0);
		final Show show3 = new Show(LocalTime.of(2, 0), film1, hall1, AdBlock.NONE, 0);
		final List<Show> shows = Arrays.asList(show1, show2, show3);
		// mocks for faked calculations
		final Schedule schedule = mock(Schedule.class);
		when(schedule.iterator()).thenReturn(shows.iterator());
		final LocalDate date = LocalDate.now();
		final GuestCalculator calculator = mock(GuestCalculator.class);
		when(calculator.calculateAudienceGuests(show1, date, Audience.ADULTS)).thenReturn(1);
		when(calculator.calculateAudienceGuests(show1, date, Audience.KIDS)).thenReturn(2);
		when(calculator.calculateAudienceGuests(show1, date, Audience.SENIORS)).thenReturn(3);
		when(calculator.calculateAudienceGuests(show3, date, Audience.ADULTS)).thenReturn(11);
		when(calculator.calculateAudienceGuests(show3, date, Audience.KIDS)).thenReturn(12);
		when(calculator.calculateAudienceGuests(show3, date, Audience.SENIORS)).thenReturn(13);
		when(calculator.calculateAudienceGuests(show2, date, Audience.ADULTS)).thenReturn(21);
		when(calculator.calculateAudienceGuests(show2, date, Audience.KIDS)).thenReturn(22);
		when(calculator.calculateAudienceGuests(show2, date, Audience.SENIORS)).thenReturn(23);

		final GuestsDayReport report = new GuestsDayReport(calculator, schedule, date);

		assertAround(108, report.getTotalGuests());

		int i = 0;
		for (final GuestsShowReport subReport : report) {
			assertAround("show " + i, 6 + i * 30, subReport.getTotalGuests());
			assertAround("show " + i, 1 + i * 10, subReport.getGuests(Audience.ADULTS));
			assertAround("show " + i, 2 + i * 10, subReport.getGuests(Audience.KIDS));
			assertAround("show " + i, 3 + i * 10, subReport.getGuests(Audience.SENIORS));
			i++;
		}
		assertEquals(i, 3);
	}

	@Test
	public void testGetTotalGuestsAboveHallCapacity() {
		// real objects for ease of data holding and sorting
		final Movie film1 = newTestMovie("Test 1", 20);
		final Movie film2 = newTestMovie("Test 2", 80);
		final CinemaHall hall1 = new FixedSizeCinemaHall("1", 1, 20);
		final CinemaHall hall2 = new FixedSizeCinemaHall("2", 2, 10);
		final Show show1 = new Show(LocalTime.of(1, 0), film1, hall1, AdBlock.NONE, 0);
		final Show show2 = new Show(LocalTime.of(1, 0), film2, hall2, AdBlock.NONE, 0);
		final Show show3 = new Show(LocalTime.of(2, 0), film1, hall1, AdBlock.NONE, 0);
		final List<Show> shows = Arrays.asList(show1, show2, show3);
		// mocks for faked calculations
		final Schedule schedule = mock(Schedule.class);
		when(schedule.iterator()).thenReturn(shows.iterator());
		final LocalDate date = LocalDate.now();
		final GuestCalculator calculator = mock(GuestCalculator.class);
		when(calculator.calculateAudienceGuests(show1, date, Audience.ADULTS)).thenReturn(1000);
		when(calculator.calculateAudienceGuests(show1, date, Audience.KIDS)).thenReturn(2000);
		when(calculator.calculateAudienceGuests(show1, date, Audience.SENIORS)).thenReturn(3000);
		when(calculator.calculateAudienceGuests(show3, date, Audience.ADULTS)).thenReturn(111);
		when(calculator.calculateAudienceGuests(show3, date, Audience.KIDS)).thenReturn(222);
		when(calculator.calculateAudienceGuests(show3, date, Audience.SENIORS)).thenReturn(333);
		when(calculator.calculateAudienceGuests(show2, date, Audience.ADULTS)).thenReturn(511);
		when(calculator.calculateAudienceGuests(show2, date, Audience.KIDS)).thenReturn(522);
		when(calculator.calculateAudienceGuests(show2, date, Audience.SENIORS)).thenReturn(533);

		final GuestsDayReport report = new GuestsDayReport(calculator, schedule, date);

		assertEquals(hall1.getCapacity() + hall2.getCapacity() + hall1.getCapacity(), report.getTotalGuests());

		int i = 0;
		for (final GuestsShowReport subReport : report) {
			final int maxGuests = subReport.getShow().getHall().getCapacity();
			assertEquals("show " + i, maxGuests, subReport.getTotalGuests());
			i++;
		}
		assertEquals(i, 3);
	}

	private void assertAround(final int expected, final int actual) {
		assertAround("", expected, actual);
	}

	private void assertAround(String message, final int expected, final int actual) {
		// up to 40% tolerance
		final double min = Math.floor(expected * 0.6);
		final double max = Math.ceil(expected * 1.4);
		message += ": expected " + min + " to " + max + " but was " + actual;
		assertTrue(message, min <= actual && actual <= max);
	}

	private Movie newTestMovie(final String title, final int rating) {
		final Rating aRating = Rating.create(Collections.singletonMap(RatingCategory.SERIOUSITY, rating));
		return new SimpleMovie(title, null, 0, null, null, aRating, null);
	}

}
