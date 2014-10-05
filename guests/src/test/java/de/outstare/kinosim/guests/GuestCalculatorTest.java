package de.outstare.kinosim.guests;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
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
		final Movie movie = createRatedMovie(normal_saturday, 0, 0, 0, 0, 0);
		final Show show = new Show(LocalTime.of(3, 0), movie, new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(85, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	@Test
	public void testCalculateGuestsMax() {
		final int maxRating = Rating.MAX_VALUE;
		final Movie movie = createRatedMovie(normal_saturday, maxRating, maxRating, maxRating, maxRating, maxRating);
		final Show show = new Show(LocalTime.of(20, 0), movie, new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(156, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	@Test
	public void testCalculateGuestsMed() {
		final int halfRating = Rating.MAX_VALUE / 2;
		final Movie movie = createRatedMovie(normal_saturday, halfRating, halfRating, halfRating, halfRating, halfRating);
		final Show show = new Show(LocalTime.of(18, 0), movie, new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(412, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	@Test
	public void testCalculateGuestsLow() {
		final int halfRating = Rating.MAX_VALUE / 5;
		final Movie movie = createRatedMovie(normal_saturday, halfRating, halfRating, halfRating, halfRating, halfRating);
		final Show show = new Show(LocalTime.of(14, 0), movie, new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(222, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	/** an almost perfect film must fill a large hall on a Saturday **/
	@Test
	public void testCalculateGuestsGoodDates() {
		Show show;

		show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(normal_saturday), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(417, objectUnderTest.calculateGuests(show, normal_saturday));

		show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(good_saturday), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(500, objectUnderTest.calculateGuests(show, good_saturday));

		show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(poor_saturday), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(247, objectUnderTest.calculateGuests(show, poor_saturday));

		show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(normal_monday), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(373, objectUnderTest.calculateGuests(show, normal_monday));

		show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(good_monday), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(500, objectUnderTest.calculateGuests(show, good_monday));

		show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(poor_monday), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		assertEquals(202, objectUnderTest.calculateGuests(show, poor_monday));
	}

	@Test
	public void testCalculateGuestsGoodTimes() {
		final Movie movie = createVeryGoodMovie(normal_saturday);
		final FixedSizeCinemaHall hall = new FixedSizeCinemaHall("1", 1, 500);
		Show show;
		show = new Show(LocalTime.of(2, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(360, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(5, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(345, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(8, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(356, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(11, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(383, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(14, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(413, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(17, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(426, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(20, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(417, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(23, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(390, objectUnderTest.calculateGuests(show, normal_saturday));
		// both times around midnight must almost be equal!
		show = new Show(LocalTime.of(23, 59), movie, hall, AdBlock.NONE, 0);
		assertEquals(380, objectUnderTest.calculateGuests(show, normal_saturday));
		show = new Show(LocalTime.of(0, 0), movie, hall, AdBlock.NONE, 0);
		assertEquals(380, objectUnderTest.calculateGuests(show, normal_saturday));
	}

	/** first weeks must drop fast, after multiple weeks decrease slowly **/
	@Test
	public void testCalculateGuestsWeeksAfterRelease() {
		final LocalDate release = normal_saturday.with(ChronoField.DAY_OF_WEEK, DayOfWeek.THURSDAY.getValue());
		final Show show = new Show(LocalTime.of(20, 0), createVeryGoodMovie(release), new FixedSizeCinemaHall("1", 1, 500), AdBlock.NONE, 0);
		// preview
		assertEquals(500, objectUnderTest.calculateGuests(show, normal_saturday.minusWeeks(1)));
		// first week
		assertEquals(417, objectUnderTest.calculateGuests(show, normal_saturday));
		assertEquals(294, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(1)));
		assertEquals(240, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(2)));
		assertEquals(207, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(3)));
		assertEquals(185, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(4)));
		assertEquals(135, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(5)));
		assertEquals(125, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(6)));
		assertEquals(116, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(7)));
		assertEquals(110, objectUnderTest.calculateGuests(show, normal_saturday.plusWeeks(8)));
	}

	private Movie createVeryGoodMovie(final LocalDate release) {
		final int avgSerious = (10 + 20 + 50 + 60 + 70) / Audience.values().length;
		final int avgReal = (60 + 40 + 50 + 67 + 80) / Audience.values().length;
		final int avgEmo = (80 + 40 + 60 + 70 + 50) / Audience.values().length;
		final int avgLen = (10 + 50 + 80 + 60 + 30) / Audience.values().length;
		final int avgPro = (20 + 40 + 70 + 60 + 70) / Audience.values().length;
		return createRatedMovie(release, avgSerious, avgReal, avgEmo, avgLen, avgPro);
	}

	private Movie createRatedMovie(final LocalDate release, final int serious, final int real, final int emotion, final int length, final int pro) {
		final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<RatingCategory, Integer>();
		ratingPerCategory.put(RatingCategory.DURATION, length);
		ratingPerCategory.put(RatingCategory.EMOTION, emotion);
		ratingPerCategory.put(RatingCategory.PROFESSIONALITY, pro);
		ratingPerCategory.put(RatingCategory.REALITY, real);
		ratingPerCategory.put(RatingCategory.SERIOUSITY, serious);
		final Rating rating = Rating.create(ratingPerCategory);
		return new SimpleMovie(null, null, 0, null, null, rating, release);
	}
}
