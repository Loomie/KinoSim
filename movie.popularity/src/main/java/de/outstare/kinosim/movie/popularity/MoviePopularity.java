package de.outstare.kinosim.movie.popularity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;

import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.util.Distributions;

/**
 * A MoviePopularity holds all factors and the resulting popularity of a movie (deterministic).
 */
public abstract class MoviePopularity {
	private static final Logger	LOG	= LoggerFactory.getLogger(MoviePopularity.class);

	private MoviePopularity() {
		// no instances
	}

	/**
	 * Get the popularity for the given audience.
	 *
	 * @param audience
	 * @param rating
	 * @param weeksSinceRelease
	 * @return the ratio of people who want to watch the movie (0.0 - 1.0)
	 */
	public static double getPopularity(final Audience audience, final Movie movie) {
		final Rating rating = movie.getRating();
		final AudienceMoviePrefs audiencePrefs = AudienceMoviePrefs.forAudience(audience);
		double total = 0;
		final RatingCategory[] categories = RatingCategory.values();
		for (final RatingCategory category : categories) {
			total += getCategoryPopularity(audiencePrefs, category, rating);
		}
		final int weeksSinceRelease = movie.getWeeksSinceRelease();
		if (weeksSinceRelease > 0) {
			// older movie are not that popular (1 by square root of weeks running) TODO move function to audience, so seniors like historic movies
			// also (or even more?)
			final double timeFactor = Math.pow(weeksSinceRelease, -0.5);
			total *= timeFactor;
		}
		LOG.info("popularity for {} of {} after {} weeks is {}", audience, rating, weeksSinceRelease, total);
		return total;
	}

	private static double getCategoryPopularity(final AudienceMoviePrefs audience, final RatingCategory category, final Rating rating) {
		final int expectedValue = audience.getPreferredValue(category);
		final int currentValue = rating.getValue(category);
		// more than half range away is worth nothing
		final int usedRangeMin = expectedValue - Rating.MAX_VALUE / 2;
		final int usedRangeMax = expectedValue + Rating.MAX_VALUE / 2;
		final double ratio = Distributions.getDifferenceRatio(expectedValue, currentValue, Range.closed(usedRangeMin, usedRangeMax));
		LOG.debug("{} of {} {} is a ratio of {} for {}", currentValue, expectedValue, category, ratio, audience);
		return ratio * audience.getPriorityRatio(category);
	}
}
