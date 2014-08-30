package de.outstare.kinosim.housegenerator;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberRangeTest {

	@Test
	public void testGetRandomValue() {
		final double minimum = 40;
		final double maximum = 60;
		final NumberRange range = new NumberRange(minimum, maximum);
		double result;
		for (int i = 0; i < 10000; i++) {
			result = range.getRandomValue();
			assertTrue("Expected " + minimum + " <= " + result, minimum <= result);
			assertTrue("Expected " + result + " <= " + maximum, result <= maximum);
		}
	}

}
