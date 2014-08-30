package de.outstare.kinosim.housegenerator;

import static org.junit.Assert.*;

import org.junit.Test;

import de.outstare.kinosim.cinema.MovieTheater;

public class AreaMovieTheaterCreatorTest {

	@Test
	public void testCreateTheater() {
		for (int i = 0; i < 10; i++) {
			final AreaMovieTheaterCreator creator = new AreaMovieTheaterCreator(1800);

			final MovieTheater result = creator.createTheater();

			assertTrue(result.getNumberOfSeats() + " seats", 100 < result.getNumberOfSeats());
			assertTrue(result.getNumberOfSeats() + " seats", result.getNumberOfSeats() < 1100);
			System.out.println();
		}
	}

}
