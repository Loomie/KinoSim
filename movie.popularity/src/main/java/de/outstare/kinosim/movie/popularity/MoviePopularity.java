package de.outstare.kinosim.movie.popularity;

import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;

/**
 * A MoviePopularity holds all factors and the resulting popularity of a movie.
 */
public class MoviePopularity {
    private final Rating rating;

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
	double total = 0;
	final RatingCategory[] categories = RatingCategory.values();
	for (final RatingCategory category : categories) {
	    total += getCategoryPopularity(audience, category);
	}
	return total;
    }

    private double getCategoryPopularity(final Audience audience, final RatingCategory category) {
	final int expectedValue = audience.getPreferedValue(category);
	final int currentValue = rating.getValue(category);
	final int diff = currentValue - expectedValue;
	if (diff > Rating.MAX_VALUE / 2) {
	    return 0;
	}
	final double relativeDiff = diff / (Rating.MAX_VALUE / 2.0);
	assert -0.5 <= relativeDiff && relativeDiff <= 0.5 : "difference ration must be max. +/-0.5, but was " + relativeDiff;
	// we use a cosinus curve for weighting the difference (preferred value is at zero)
	final double x = relativeDiff * Math.PI / 2; // +/- half pi = full pi
	final double y = Math.cos(x);
	assert 0 <= y && y <= 1 : "expect value between 0-1 for x between -pi/2 and +pi/2. Was " + y + " for cos(" + x + ")";
	// and weight it with the priority
	return y * audience.getPriorityRatio(category);
    }
}
