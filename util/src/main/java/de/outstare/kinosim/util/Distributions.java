package de.outstare.kinosim.util;

import com.google.common.collect.Range;

/**
 * Distributions holds methods for calculation numbers based on distributions.
 */
public class Distributions {
	private Distributions() {
		// no instances
	}

	public static double defaultNormalDistribution(final double x) {
		return normalDistribution(x, 0, 1);
	}

	/**
	 * @param x
	 * @param mu
	 *            expected value
	 * @param sigma
	 *            standard deviation
	 * @return normal distributed value of x
	 */
	public static double normalDistribution(final double x, final double mu, final double sigma) {
		return 1 / (sigma * Math.sqrt(2 * Math.PI)) * Math.pow(Math.E, -0.5 * Math.pow((x - mu) / sigma, 2));
	}

	/**
	 * Gives the ratio of the difference of the given value to the expected value in relation to the given range of values. The ratio is weighted
	 * towards the expected value by a cosine function. Therefore values near the expected value will have a high ratio.
	 *
	 * Example1: We have values ranging from 0 to 100. We expect the middle which is 50, but got a 17. In the range of values the 17 is almost the one
	 * third of the left half, so it's two thrid away from the expected value. Because the ratio is based on a cosine the ratio is cos(-0.66 * pi)
	 * instead of 0.34. And because cosine returns values from -1 to +1, but we want a ratio from 0 to 1 the value is scaled accordingly. In this case
	 * the cosine returns -0.48 which is scaled to 0.26 (that's 8 percent off the linear value).
	 *
	 * <pre>
	 *   1.0    -
	 *   0.5   / \
	 *   0.0  |   |
	 *  -0.5 /     \
	 *  -1.0-       -
	 *     -1   0   1 <- relative distance from expected value
	 * </pre>
	 *
	 * Example2: We have values ranging from 0 to 100. We expect a 75 but got a 10. Because it is more than half-range away the ratio is 0;
	 *
	 * @param expectedValue
	 *            the best value which currentValue can have
	 * @param currentValue
	 *            an actual value in the given range which should be weighted depending on it's distance from the expectedValue
	 * @param valueRange
	 *            to range of values which are possible
	 * @return 1.0 if the expected value is hit, and 0.0 if the currentValue is more than half the range away from it.
	 */
	public static double getDifferenceRatio(final int expectedValue, final int currentValue, final Range<Integer> valueRange) {
		if (!valueRange.contains(currentValue)) {
			return 0;
		}
		final int diff = currentValue - expectedValue;
		// shortcut to eliminate special case
		if (diff == 0) {
			return 1.0;
		}
		final double valueCount = (diff < 0) ? expectedValue - valueRange.lowerEndpoint() : valueRange.upperEndpoint() - expectedValue;
		final double relativeDiff = diff / valueCount;
		assert -1.0 <= relativeDiff && relativeDiff <= 1.0 : "difference ration must be max. +/-1.0, but was " + relativeDiff;
		// we use a cosinus curve for weighting the difference (preferred value is at zero)
		final double x = relativeDiff * Math.PI; // +/- pi = full curve
		final double y = Math.cos(x); // -1.0 to +1.0
		final double ratio = (y + 1) / 2.0;
		assert 0 <= ratio && ratio <= 1 : "expect value between 0-1 for x between -pi and +pi. Was " + ratio + " for cos(" + x + ")";
		return ratio;
	}
}
