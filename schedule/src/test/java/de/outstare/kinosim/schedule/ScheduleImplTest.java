package de.outstare.kinosim.schedule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.FixedSizeCinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.SimpleMovie;

public class ScheduleImplTest {
	private final CinemaHall	hall1;
	private final CinemaHall	hall2;
	private final CinemaHall	hall3;
	private final SimpleMovie	movie1;
	private final SimpleMovie	movie2;
	private final SimpleMovie	movie3;
	private final Show			show1;
	private final Show			show2;
	private final Show			show3;
	private final Show			show4;
	private final Show			show5;

	private ScheduleImpl		objectUnderTest;

	public ScheduleImplTest() {
		hall1 = new FixedSizeCinemaHall("1", 1, 100);
		hall2 = new FixedSizeCinemaHall("2", 1, 50);
		hall3 = new FixedSizeCinemaHall("3", 1, 10);
		movie1 = new SimpleMovie("a", null, 0, null, null, null, null);
		movie2 = new SimpleMovie("b", null, 0, null, null, null, null);
		movie3 = new SimpleMovie("x", null, 0, null, null, null, null);
		show1 = mockShow(LocalTime.MIN, hall3, movie3);
		show2 = mockShow(LocalTime.NOON, hall1, movie3);
		show3 = mockShow(LocalTime.NOON, hall2, movie1);
		show4 = mockShow(LocalTime.NOON, hall2, movie2);
		show5 = mockShow(LocalTime.MAX, hall3, movie3);
	}

	@Before
	public void setUp() throws Exception {
		objectUnderTest = new ScheduleImpl();
	}

	@Test
	public void testAdd() {
		final Show show = mockShow(LocalTime.NOON, new FixedSizeCinemaHall("1", 1, 1), movie1);

		objectUnderTest.add(show);

		assertShows(objectUnderTest, show);
	}

	@Test
	public void testRemove() {
		final Show show = mockShow(LocalTime.NOON, new FixedSizeCinemaHall("1", 1, 1), movie1);
		objectUnderTest.add(show);

		objectUnderTest.remove(show);

		assertShows(objectUnderTest);
	}

	@Test
	public void testIterator() {
		addShows(show4, show3, show1, show5, show2);

		assertShows(objectUnderTest, show1, show2, show3, show4, show5);
	}

	@Test
	public void testFilterForHall() {
		addShows(show4, show3, show1, show5, show2);

		Schedule result = objectUnderTest.filterForHall(hall2);
		assertShows(result, show3, show4);

		// filter again does not change anything
		result = result.filterForHall(hall2);
		assertShows(result, show3, show4);

		// filter for a different move returns nothing
		result = result.filterForHall(hall1);
		assertShows(result);
	}

	@Test
	public void testFilterForMovie() {
		addShows(show4, show3, show1, show5, show2);

		Schedule result = objectUnderTest.filterForMovie(movie3);
		assertShows(result, show1, show2, show5);

		// filter again does not change anything
		result = result.filterForMovie(movie3);
		assertShows(result, show1, show2, show5);

		// filter for a different move returns nothing
		result = result.filterForMovie(movie2);
		assertShows(result);
	}

	private void addShows(final Show... shows) {
		for (final Show show : shows) {
			objectUnderTest.add(show);
		}
	}

	private void assertShows(final Schedule schedule, final Show... expected) {
		int i = 0;
		for (final Show actual : schedule) {
			assertTrue(i < expected.length);
			assertEquals(expected[i], actual);
			i++;
		}
		assertEquals(expected.length, i);
	}

	private Show mockShow(final LocalTime start, final CinemaHall hall, final Movie movie) {
		final Show show = mock(Show.class, RETURNS_DEEP_STUBS);
		when(show.getStart()).thenReturn(start);
		when(show.getHall()).thenReturn(hall);
		when(show.getFilm()).thenReturn(movie);
		when(show.toString()).thenReturn(start + ", " + hall.getCapacity() + ", " + movie.getTitle());
		return show;
	}
}
