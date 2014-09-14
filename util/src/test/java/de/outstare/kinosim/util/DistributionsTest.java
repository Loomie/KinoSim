package de.outstare.kinosim.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Range;

public class DistributionsTest {

	@Test
	public void testDefaultNormalDistribution() {
		assertEquals(0.05, Distributions.defaultNormalDistribution(-2.0), 0.01);
		assertEquals(0.25, Distributions.defaultNormalDistribution(-1.0), 0.01);
		assertEquals(0.4, Distributions.defaultNormalDistribution(0.0), 0.01);
		assertEquals(0.25, Distributions.defaultNormalDistribution(1.0), 0.01);
		assertEquals(0.05, Distributions.defaultNormalDistribution(2.0), 0.01);
	}

	@Test
	public void testNormalDistribution() {
		assertEquals(0.03, Distributions.normalDistribution(46, 50, 2), 0.01);
		assertEquals(0.12, Distributions.normalDistribution(48, 50, 2), 0.01);
		assertEquals(0.2, Distributions.normalDistribution(50, 50, 2), 0.01);
		assertEquals(0.12, Distributions.normalDistribution(52, 50, 2), 0.01);
		assertEquals(0.03, Distributions.normalDistribution(54, 50, 2), 0.01);
	}

	@Test
	public void testGetDifferenceRatio() {
		int expectedValue = 50;
		Range<Integer> range = Range.closed(25, 75);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 24, range), 0.0);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 25, range), 0.00001);
		assertEquals(0.232, Distributions.getDifferenceRatio(expectedValue, 33, range), 0.001);
		assertEquals(0.531, Distributions.getDifferenceRatio(expectedValue, 38, range), 0.001);
		assertEquals(1.0, Distributions.getDifferenceRatio(expectedValue, expectedValue, range), 0.0);
		assertEquals(0.531, Distributions.getDifferenceRatio(expectedValue, 62, range), 0.001);
		assertEquals(0.232, Distributions.getDifferenceRatio(expectedValue, 67, range), 0.001);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 75, range), 0.00001);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 76, range), 0.0);

		// expected is not in middle
		expectedValue = 75;
		range = Range.closed(0, 100);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, -1, range), 0.0);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 0, range), 0.0);
		assertEquals(0.25, Distributions.getDifferenceRatio(expectedValue, 25, range), 0.00001);
		assertEquals(0.75, Distributions.getDifferenceRatio(expectedValue, 50, range), 0.0);
		assertEquals(0.878, Distributions.getDifferenceRatio(expectedValue, 58, range), 0.001);
		assertEquals(0.938, Distributions.getDifferenceRatio(expectedValue, 63, range), 0.001);
		assertEquals(1.0, Distributions.getDifferenceRatio(expectedValue, expectedValue, range), 0.0);
		assertEquals(0.531, Distributions.getDifferenceRatio(expectedValue, 87, range), 0.001);
		assertEquals(0.232, Distributions.getDifferenceRatio(expectedValue, 92, range), 0.001);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 100, range), 0.0);
		assertEquals(0.0, Distributions.getDifferenceRatio(expectedValue, 101, range), 0.0);
		expectedValue = 50;
		assertEquals(0.259, Distributions.getDifferenceRatio(expectedValue, 17, range), 0.001);
	}
}
