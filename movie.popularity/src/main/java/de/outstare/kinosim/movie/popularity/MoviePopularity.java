package de.outstare.kinosim.movie.popularity;

import com.google.common.collect.Range;

import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.util.Distributions;

/**
 * A MoviePopularity holds all factors and the resulting popularity of a movie.
 */
public class MoviePopularity {
	private final Rating	rating;

	// private final Set<Genre> genres;
	// private final int guestsPerWeek; // estimated if the movie is not yet running, else last full week
	// private final int guestsPerWeekend; // estimated if the movie is not yet running, else last full weekend

	public MoviePopularity(final Rating rating) {
		this.rating = rating;
	}

	/**
	 * Get the popularity for the given audience.
	 *
	 * @param audience
	 * @return the ratio of people who want to watch the movie (0.0 - 1.0)
	 */
	public double getPopularity(final Audience audience) {
		final AudienceMoviePrefs audiencePrefs = AudienceMoviePrefs.forAudience(audience);
		double total = 0;
		final RatingCategory[] categories = RatingCategory.values();
		for (final RatingCategory category : categories) {
			total += getCategoryPopularity(audiencePrefs, category);
		}
		return total;
	}

	private double getCategoryPopularity(final AudienceMoviePrefs audience, final RatingCategory category) {
		final int expectedValue = audience.getPreferredValue(category);
		final int currentValue = rating.getValue(category);
		// more than half range away is worth nothing
		final int usedRangeMin = expectedValue - Rating.MAX_VALUE / 2;
		final int usedRangeMax = expectedValue + Rating.MAX_VALUE / 2;
		final double ratio = Distributions.getDifferenceRatio(expectedValue, currentValue, Range.closed(usedRangeMin, usedRangeMax));
		// and weight it with the priority
		return ratio * audience.getPriorityRatio(category);
	}
}
