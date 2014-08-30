package de.outstare.kinosim.util;

import java.util.Random;

/**
 * A NumberRange is a number between a minimum and a maximum.
 */
public class NumberRange {
	private static final Random	RANDOM	= new Random();

	private final double		minimum;
	private final double		maximum;

	private final double		average;
	private final double		singleRange;

	public NumberRange(final double minimum, final double maximum) {
		super();
		this.minimum = minimum;
		this.maximum = maximum;
		singleRange = (maximum - minimum) / 2.0;
		average = minimum + singleRange;
	}

	public double getRandomValue() {
		final double normalRandom = RANDOM.nextGaussian();
		final double valueInRange = average + Math.max(-1.0, Math.min(normalRandom / 4, 1.0)) * singleRange;
		return Math.min(Math.max(minimum, valueInRange), maximum);
	}
}
