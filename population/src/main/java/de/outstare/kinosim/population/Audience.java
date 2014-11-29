package de.outstare.kinosim.population;

import java.time.LocalTime;

import com.google.common.collect.Range;

public enum Audience {
	/**
	 * 3-11
	 */
	KIDS(3, 12, 14),
	/**
	 * 12-19
	 */
	TEENS(12, 20, 18),
	/**
	 * 20-29
	 */
	TWENS(20, 30, 21),
	/**
	 * 30-49
	 */
	ADULTS(30, 50, 19),
	/**
	 * 50-120
	 */
	SENIORS(50, 120, 16);

	/**
	 * inclusive
	 */
	public final int minAge;
	/**
	 * exclusive
	 */
	public final int maxAge;
	public final LocalTime preferredStartTime;

	private Audience(final int minAge, final int maxAge, final int preferredStartHour) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		preferredStartTime = LocalTime.of(preferredStartHour, 0);
	}

	public Range<Integer> getAgeRange() {
		return Range.closedOpen(minAge, maxAge);
	}
}