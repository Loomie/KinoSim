package de.outstare.kinosim.movie;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * A Rating determines the objective weight of {@link RatingCategory} for a movie. The objectiveness might come from a representative number of
 * critics.
 */
public class Rating {
	/**
	 * The max rating value (currently 100 to be easily used as a percentage)
	 */
	public static final int						MAX_VALUE	= 100;

	private final Map<RatingCategory, Integer>	ratings		= new EnumMap<>(RatingCategory.class);

	private Rating() {
		// factory method must be used
	}

	/**
	 * @param category
	 * @return a value from 0 to #MAX_VALUE (including)
	 */
	public int getValue(final RatingCategory category) {
		return ratings.get(category);
	}

	/**
	 * @param ratingPerCategory
	 *            each value must be from 0 to #MAX_VALUE (including)
	 * @return
	 */
	public static Rating create(final Map<RatingCategory, Integer> ratingPerCategory) {
		final Rating result = new Rating();
		for (final Entry<RatingCategory, Integer> entry : ratingPerCategory.entrySet()) {
			final Integer value = entry.getValue();
			if (value.intValue() < 0 || value.intValue() > MAX_VALUE) {
				throw new IllegalArgumentException("value must be 0-" + MAX_VALUE + "! Was " + value);
			}
			result.ratings.put(entry.getKey(), value);
		}
		return result;
	}

	public static Rating createRandom() {
		final Random r = new Random();
		final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<>();
		for (final RatingCategory cat : RatingCategory.values()) {
			ratingPerCategory.put(cat, r.nextInt(MAX_VALUE + 1));
		}
		return create(ratingPerCategory);
	}
}
