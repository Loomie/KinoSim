package de.outstare.kinosim.guests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.outstare.kinosim.cinema.FixedSizeCinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.movie.SimpleMovie;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.population.PopulationPyramid;
import de.outstare.kinosim.schedule.AdBlock;
import de.outstare.kinosim.schedule.Show;

public class GuestCalculatorTest {
	private final LocalDate	normal_saturday	= LocalDate.of(2014, 03, 01);
	private final LocalDate	good_saturday	= LocalDate.of(2013, 12, 28);
	private final LocalDate	poor_saturday	= LocalDate.of(2014, 06, 28);
	private final LocalDate	normal_monday	= LocalDate.of(2014, 03, 03);
	private final LocalDate	good_monday		= LocalDate.of(2013, 12, 30);
	private final LocalDate	poor_monday		= LocalDate.of(2014, 06, 30);
	private GuestCalculator	objectUnderTest;

	@Before
	public void setUp() throws Exception {
		final PopulationPyramid population = new PopulationPyramid(1500, 100);
		objectUnderTest = new GuestCalculator(population);
	}

	@Test
	public void testCalculateGuestsMin() {
		final Movie movie = createRatedMovie(0, 0, 0, 0, 0);
		final Show show = new Show(LocalTime.of(3, 0), movie, new FixedSizeCinemaHall(1, 500), AdBlock.NONE, 0);
		assertEquals(108, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	@Test
	public void testCalculateGuestsMax() {
		final int maxRating = Rating.MAX_VALUE;
		final Movie movie = createRatedMovie(maxRating, maxRating, maxRating, maxRating, maxRating);
		final Show show = new Show(LocalTime.of(20, 0), movie, new FixedSizeCinemaHall(1, 500), AdBlock.NONE, 0);
		assertEquals(182, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	@Test
	public void testCalculateGuestsMed() {
		final int halfRating = Rating.MAX_VALUE / 2;
		final Movie movie = createRatedMovie(halfRating, halfRating, halfRating, halfRating, halfRating);
		final Show show = new Show(LocalTime.of(18, 0), movie, new FixedSizeCinemaHall(1, 500), AdBlock.NONE, 0);
		assertEquals(405, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	@Test
	public void testCalculateGuestsLow() {
		final int halfRating = Rating.MAX_VALUE / 5;
		final Movie movie = createRatedMovie(halfRating, halfRating, halfRating, halfRating, halfRating);
		final Show show = new Show(LocalTime.of(14, 0), movie, new FixedSizeCinemaHall(1, 500), AdBlock.NONE, 0);
		assertEquals(257, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	/** an almost perfect film must fill a large hall on a Saturday **/
	@Test
	public void testCalculateGuestsGoodDates() {
		final Show show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(), new FixedSizeCinemaHall(1, 500), AdBlock.NONE, 0);
		assertEquals(408, objectUnderTest.calculateGuests(show, normal_saturday));
		assertEquals(500, objectUnderTest.calculateGuests(show, good_saturday));
		assertEquals(242, objectUnderTest.calculateGuests(show, poor_saturday));
		assertEquals(365, objectUnderTest.calculateGuests(show, normal_monday));
		assertEquals(500, objectUnderTest.calculateGuests(show, good_monday));
		assertEquals(199, objectUnderTest.calculateGuests(show, poor_monday));
	}

	@Test
	public void testCalculateGuestsGoodTimes() {
		final Movie movie = createVeryGoodMovie();
		final FixedSizeCinemaHall hall = new FixedSizeCinemaHall(1, 500);
		Show show;
		show = new Show(LocalTime.of(8, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(395, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(11, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(408, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(14, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(418, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(17, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(420, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(20, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(408, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(23, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(363, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	private Movie createVeryGoodMovie() {
		final int avgSerious = (10 + 20 + 50 + 60 + 70) / Audience.values().length;
		final int avgReal = (60 + 40 + 50 + 67 + 80) / Audience.values().length;
		final int avgEmo = (80 + 40 + 60 + 70 + 50) / Audience.values().length;
		final int avgLen = (10 + 50 + 80 + 60 + 30) / Audience.values().length;
		final int avgPro = (20 + 40 + 70 + 60 + 70) / Audience.values().length;
		return createRatedMovie(avgSerious, avgReal, avgEmo, avgLen, avgPro);
	}

	private Movie createRatedMovie(final int serious, final int real, final int emotion, final int length, final int pro) {
		final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<RatingCategory, Integer>();
		ratingPerCategory.put(RatingCategory.DURATION, length);
		ratingPerCategory.put(RatingCategory.EMOTION, emotion);
		ratingPerCategory.put(RatingCategory.PROFESSIONALITY, pro);
		ratingPerCategory.put(RatingCategory.REALITY, real);
		ratingPerCategory.put(RatingCategory.SERIOUSITY, serious);
		final Rating rating = Rating.create(ratingPerCategory);
		return new SimpleMovie(null, null, 0, null, null, rating);
	}
}
