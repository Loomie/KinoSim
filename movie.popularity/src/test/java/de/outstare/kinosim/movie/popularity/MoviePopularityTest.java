package de.outstare.kinosim.movie.popularity;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.movie.SimpleMovie;
import de.outstare.kinosim.population.Audience;

public class MoviePopularityTest {

	@Test
	public void testGetPopularityMin() {
		final Rating rating = createRating(0, 0, 0, 0, 0);
		final LocalDate release = LocalDate.now();
		final Movie movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		double result;
		// for (final Audience a : Audience.values()) {
		// result = MoviePopularity.getPopularity(a, movie);
		// System.out.println(a + " " + result);
		// }
		result = MoviePopularity.getPopularity(Audience.ADULTS, movie);
		assertEquals(0, result, 0);
		result = MoviePopularity.getPopularity(Audience.KIDS, movie);
		assertEquals(0.6533813728906053, result, 0);
		result = MoviePopularity.getPopularity(Audience.SENIORS, movie);
		assertEquals(0.05182372542187895, result, 0);
		result = MoviePopularity.getPopularity(Audience.TEENS, movie);
		assertEquals(0.24409830056250528, result, 0);
		result = MoviePopularity.getPopularity(Audience.TWENS, movie);
		assertEquals(0, result, 0);
	}

	@Test
	public void testGetPopularityMax() {
		final int max = Rating.MAX_VALUE;
		final Rating rating = createRating(max, max, max, max, max);
		final LocalDate release = LocalDate.now();
		final Movie movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		double result;
		result = MoviePopularity.getPopularity(Audience.ADULTS, movie);
		assertEquals(0.16571783483984953, result, 0);
		result = MoviePopularity.getPopularity(Audience.KIDS, movie);
		assertEquals(0.1077254248593737, result, 0);
		result = MoviePopularity.getPopularity(Audience.SENIORS, movie);
		assertEquals(0.2209220259843842, result, 0);
		result = MoviePopularity.getPopularity(Audience.TEENS, movie);
		assertEquals(0, result, 0);
		result = MoviePopularity.getPopularity(Audience.TWENS, movie);
		assertEquals(0.25364745084375795, result, 0);
	}

	@Test
	public void testGetPopularityMedium() {
		final int medium = Rating.MAX_VALUE / 2;
		final Rating rating = createRating(medium, medium, medium, medium, medium);
		final LocalDate release = LocalDate.now();
		final Movie movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		double result;
		result = MoviePopularity.getPopularity(Audience.ADULTS, movie);
		assertEquals(0.8342821651601505, result, 0);
		result = MoviePopularity.getPopularity(Audience.KIDS, movie);
		assertEquals(0.23889320225002106, result, 0);
		result = MoviePopularity.getPopularity(Audience.SENIORS, movie);
		assertEquals(0.7272542485937369, result, 0);
		result = MoviePopularity.getPopularity(Audience.TEENS, movie);
		assertEquals(0.7559016994374947, result, 0);
		result = MoviePopularity.getPopularity(Audience.TWENS, movie);
		assertEquals(0.7463525491562422, result, 0);
	}

	@Test
	public void testGetPopularityPerfect() {
		double result;
		Rating rating;
		Movie movie;
		final LocalDate release = LocalDate.now();

		rating = createRating(60, 67, 70, 60, 60);
		movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		result = MoviePopularity.getPopularity(Audience.ADULTS, movie);
		assertEquals(1.0, result, 0);

		rating = createRating(10, 60, 80, 10, 20);
		movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		result = MoviePopularity.getPopularity(Audience.KIDS, movie);
		assertEquals(1.0, result, 0);

		rating = createRating(70, 80, 50, 30, 70);
		movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		result = MoviePopularity.getPopularity(Audience.SENIORS, movie);
		assertEquals(1.0, result, 0);

		rating = createRating(20, 40, 40, 50, 40);
		movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		result = MoviePopularity.getPopularity(Audience.TEENS, movie);
		assertEquals(1.0, result, 0);

		rating = createRating(50, 50, 60, 80, 70);
		movie = new SimpleMovie(null, null, 0, null, null, rating, release);
		result = MoviePopularity.getPopularity(Audience.TWENS, movie);
		assertEquals(1.0, result, 0);
	}

	private Rating createRating(final int serious, final int real, final int emotion, final int length, final int pro) {
		final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<RatingCategory, Integer>();
		ratingPerCategory.put(RatingCategory.DURATION, length);
		ratingPerCategory.put(RatingCategory.EMOTION, emotion);
		ratingPerCategory.put(RatingCategory.PROFESSIONALITY, pro);
		ratingPerCategory.put(RatingCategory.REALITY, real);
		ratingPerCategory.put(RatingCategory.SERIOUSITY, serious);
		return Rating.create(ratingPerCategory);
	}
}
