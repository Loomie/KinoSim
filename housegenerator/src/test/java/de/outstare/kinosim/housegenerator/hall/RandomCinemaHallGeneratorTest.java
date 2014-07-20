package de.outstare.kinosim.housegenerator.hall;

import static de.outstare.kinosim.housegenerator.hall.RandomCinemaHallGenerator.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RandomCinemaHallGeneratorTest {

    @Test
    public void testRandomCapacity() {
	for (int i = 0; i < 200000; i++) {
	    final int result = randomCapacity();
	    System.out.print(result + " ");
	    if (i % 200 == 0) {
		System.out.println();
	    }
	    assertTrue(String.valueOf(result), result >= MINIMUM);
	    assertTrue(String.valueOf(result), result <= 99 + 2999 + MINIMUM);
	}
    }

}
