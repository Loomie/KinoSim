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
	 * Gives the ratio of the difference of the given value to the expected value in relation to the half range of values (half range, because values
	 * of the other side are too far away to be of any value). The ratio is weighted towards the expected value by a cosine function. Therefore values
	 * near the expected value will have a high ratio.
	 *
	 * Example1: We have values ranging from 0 to 100. We expect a 75 but got a 50. The range of accepted values is 25 to 100 (75 +- 100/2 capped by
	 * range). In this range of accepted values the 50 is exactly in the middle of the left half. Because the ratio is based on a cosine the ratio is
	 * cos(-0.5) instead of 0.5.
	 *
	 * <pre>
	 *  1.0  _
	 *  0.5 / \
	 *  0.0|   |
	 *    -1 0 1
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
		final double x = relativeDiff * Math.PI / 2; // +/- half pi = full pi
		final double y = Math.cos(x);
		assert 0 <= y && y <= 1 : "expect value between 0-1 for x between -pi/2 and +pi/2. Was " + y + " for cos(" + x + ")";
		return y;
	}
}
