package de.outstare.kinosim.population;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.population.PopulationPyramid;

public class PopulationPyramidTest {
	private PopulationPyramid	objectUnderTest;

	@Before
	public void setUp() throws Exception {
		objectUnderTest = new PopulationPyramid(1000, 100);
	}

	@Test
	public void testGetPopulationOfAudience() {
		assertEquals(8325, objectUnderTest.getPopulationOfAudience(Audience.KIDS));
		assertEquals(6720, objectUnderTest.getPopulationOfAudience(Audience.TEENS));
		assertEquals(7500, objectUnderTest.getPopulationOfAudience(Audience.TWENS));
		assertEquals(12000, objectUnderTest.getPopulationOfAudience(Audience.ADULTS));
		assertEquals(10500, objectUnderTest.getPopulationOfAudience(Audience.SENIORS));
	}

}
