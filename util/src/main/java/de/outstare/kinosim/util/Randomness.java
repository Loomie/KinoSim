package de.outstare.kinosim.util;

import java.util.Random;

/**
 * Randomness wraps an instance of {@link Random} and may add additional helper methods.
 */
public abstract class Randomness {
	// one random for all calls
	private static final Random RANDOM = new Random();

	private Randomness() {
		// no instances
	}

	public static Random getRandom() {
		return RANDOM;
	}

	/**
	 * @param bound
	 * @return A random integer beween 0 (inclusive) and bound (exclusive)
	 */
	public static int nextInt(final int bound) {
		return RANDOM.nextInt(bound);
	}

	/**
	 * @return a random double betwen 0 (inclusive) and 1.0 (exclusive)
	 */
	public static double nextDouble() {
		return RANDOM.nextDouble();
	}

	public static double nextGaussian() {
		return RANDOM.nextGaussian();
	}

	/**
	 * @param number
	 * @return a normally distributed number around the given number (in average +- 8%)
	 */
	public static int getGaussianAround(final int number) {
		return (int) (getGaussianAround((double) number));
	}

	/**
	 * @param number
	 * @return a normally distributed number around the given number (in average +- 8%)
	 */
	public static double getGaussianAround(final double number) {
		final double factor = 1 + RANDOM.nextGaussian() / 10.0; // yields +- 8 % with peaks up to 30-40 %
		return number * factor;
	}

	/**
	 * test for getGaussianAround
	 * 
	 * @param args
	 **/
	public static void main(final String[] args) {
		final int loops = 10000;
		final int center = 1000;
		int min = center, max = center;
		int sum = 0;
		for (int i = 0; i < loops; i++) {
			final int n = getGaussianAround(center);
			System.out.println(n);
			min = Math.min(min, n);
			max = Math.max(max, n);
			sum += Math.abs(n - center);
		}
		final long maxPercent = Math.round((max / (double) center - 1) * 100);
		final long minPercent = Math.round((min / (double) center - 1) * 100);
		final long avgPercent = Math.round(sum / (double) loops / center * 100);
		System.out.println("max: " + maxPercent + " %, min: " + minPercent + " %, avg: +- " + avgPercent + " %");
	}
}
