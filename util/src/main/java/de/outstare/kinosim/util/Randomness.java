package de.outstare.kinosim.util;

import java.util.Random;

public abstract class Randomness {
	// one random for all calls
	private static final Random	RANDOM	= new Random();

	private Randomness() {
		// no instances
	}

	public static Random getRandom() {
		return RANDOM;
	}

	public static int nextInt(final int bound) {
		return RANDOM.nextInt(bound);
	}

	public static double nextDouble() {
		return RANDOM.nextDouble();
	}

	public static double nextGaussian() {
		return RANDOM.nextGaussian();
	}
}
