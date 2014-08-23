package de.outstare.kinosim.movie.popularity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.outstare.kinosim.movie.RatingCategory;

/**
 * A Audience is the target group of people for a movie.
 */
public enum Audience {
    /**
     * 0-11
     */
    KIDS(40, 10, 15, 25, 10, /**/10, 60, 80, 10, 20),
    /**
     * 12-19
     */
    TEENS(30, 10, 25, 20, 15, /**/20, 40, 40, 50, 40),
    /**
     * 20-29
     */
    TWENS(20, 10, 20, 20, 30, /**/50, 50, 60, 80, 70),
    /**
     * 30-49
     */
    ADULTS(25, 20, 15, 15, 25, /**/60, 67, 70, 60, 60),
    /**
     * 50-1000
     */
    SENIORS(10, 10, 30, 15, 35, /**/70, 80, 50, 30, 70);

    private final Map<RatingCategory, Integer> factorPriorities = new HashMap<RatingCategory, Integer>();
    private final Map<RatingCategory, Integer> factorPreference = new HashMap<RatingCategory, Integer>();

    /**
     * Priority means how important the factor is (sum is 100%). The preference value is which amount of the factor is mostly wanted.
     */
    private Audience(final int seriousPrio, final int realityPrio, final int emotePrio, final int lengthPrio, final int professionalPrio,
	    final int seriousPref, final int realityPref, final int emotePref, final int lengthPref, final int professionalPref) {
	factorPriorities.put(RatingCategory.SERIOUSITY, seriousPrio);
	factorPriorities.put(RatingCategory.REALITY, realityPrio);
	factorPriorities.put(RatingCategory.EMOTION, emotePrio);
	factorPriorities.put(RatingCategory.DURATION, lengthPrio);
	factorPriorities.put(RatingCategory.PROFESSIONALITY, professionalPrio);

	factorPreference.put(RatingCategory.SERIOUSITY, seriousPref);
	factorPreference.put(RatingCategory.REALITY, realityPref);
	factorPreference.put(RatingCategory.EMOTION, emotePref);
	factorPreference.put(RatingCategory.DURATION, lengthPref);
	factorPreference.put(RatingCategory.PROFESSIONALITY, professionalPref);
    }

    double getPriorityRatio(final RatingCategory factor) {
	return factorPriorities.get(factor).intValue() / (double) getSum(factorPriorities.values());
    }

    int getPreferedValue(final RatingCategory factor) {
	assert factorPreference.containsKey(factor);
	return factorPreference.get(factor).intValue();
    }

    double getPreferedRatio(final RatingCategory factor) {
	return factorPriorities.get(factor).intValue() / (double) getSum(factorPriorities.values());
    }

    private int getSum(final Collection<Integer> values) {
	int sum = 0;
	for (final Integer priority : values) {
	    sum += priority.intValue();
	}
	return sum;
    }
}
