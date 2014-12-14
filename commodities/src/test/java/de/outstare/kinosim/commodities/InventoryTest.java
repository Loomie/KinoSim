package de.outstare.kinosim.commodities;

import static de.outstare.kinosim.commodities.Good.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InventoryTest {
	private static final int volume = 100;
	private Inventory testObject;

	@Before
	public void setUp() throws Exception {
		testObject = new Inventory(volume);
	}

	@Test
	public void testAdd() {
		assertEquals(0, testObject.getAmount(Popcorn));

		testObject.add(Popcorn, 3);

		assertEquals(3, testObject.getAmount(Popcorn));
	}

	@Test
	public void testRemove() {
		testObject.add(Popcorn, 3);
		assertEquals(3, testObject.getAmount(Popcorn));

		testObject.remove(Popcorn, 2);

		assertEquals(1, testObject.getAmount(Popcorn));
	}

	@Test
	public void testGetFillRatio() {
		final int addPerStep = 1;
		for (int added = 0; added < 1000; added += addPerStep) {
			final double expected = Popcorn.getVolumeOfBoxes(added) / volume;
			assertEquals(added + " added", expected, testObject.getFillRatio(), 0);
			testObject.add(Popcorn, addPerStep);
		}
	}

	@Test
	public void testIsFree() {
		assertTrue(testObject.isFree(Popcorn, 1));

		testObject.add(Popcorn, (int) (volume / Popcorn.getBoxVolume()));

		assertFalse(testObject.isFree(Popcorn, 1));
	}

}
