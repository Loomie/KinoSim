package de.outstare.kinosim.housegenerator.hall;

import static de.outstare.kinosim.housegenerator.hall.RandomCinemaHallGenerator.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RandomCinemaHallGeneratorTest
{
	private static final boolean	SHOW_VALUES	= false;

	@Test
	public void testRandomCapacity() {
		for (int i = 0; i < 2000000; i++) {
			final int result = randomCapacity();
			if (SHOW_VALUES) {
				System.out.print(result + " ");
				if (i % 100 == 0) {
					System.out.println();
				}
			}
			assertTrue(String.valueOf(result), result >= MINIMUM + 2); // at least 1 base and 1 raise
			assertTrue(String.valueOf(result), result <= MINIMUM + MAX_BASE + MAX_RAISE);
		}
	}

}
