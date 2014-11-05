package de.outstare.kinosim.population;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PopulationPyramidTest {
	private PopulationPyramid	objectUnderTest;

	@Before
	public void setUp() throws Exception {
		objectUnderTest = new PopulationPyramid(1000, 100);
	}

	@Test
	public void testGetPopulationOfAudience() {
		assertEquals(8325, objectUnderTest.getPopulationOfAudience(Audience.KIDS, 0));
		assertEquals(6720, objectUnderTest.getPopulationOfAudience(Audience.TEENS, 0));
		assertEquals(7500, objectUnderTest.getPopulationOfAudience(Audience.TWENS, 0));
		assertEquals(12000, objectUnderTest.getPopulationOfAudience(Audience.ADULTS, 0));
		assertEquals(17500, objectUnderTest.getPopulationOfAudience(Audience.SENIORS, 0));
	}

	@Test
	public void testGetPopulationOfAudienceRestricted() {
		assertEquals(4525, objectUnderTest.getPopulationOfAudience(Audience.KIDS, Audience.KIDS.maxAge - 5));
		assertEquals(4125, objectUnderTest.getPopulationOfAudience(Audience.TEENS, Audience.TEENS.maxAge - 5));
		assertEquals(3625, objectUnderTest.getPopulationOfAudience(Audience.TWENS, Audience.TWENS.maxAge - 5));
		assertEquals(2625, objectUnderTest.getPopulationOfAudience(Audience.ADULTS, Audience.ADULTS.maxAge - 5));
		assertEquals(12000, objectUnderTest.getPopulationOfAudience(Audience.SENIORS, Audience.SENIORS.minAge + 10));
	}
}
