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
			final double totalArea = result.getRoomSpace();
			assertTrue(totalArea / 1.2 <= 1800);
			System.out.println();
		}
	}

	@Test
	public void testCreateLargeTheater() {
		for (int i = 0; i < 10; i++) {
			final AreaMovieTheaterCreator creator = new AreaMovieTheaterCreator(18000);

			final MovieTheater result = creator.createTheater();

			assertTrue(result.getNumberOfSeats() + " seats", 4600 < result.getNumberOfSeats());
			assertTrue(result.getNumberOfSeats() + " seats", result.getNumberOfSeats() < 12200);
			final double totalArea = result.getRoomSpace();
			assertTrue(totalArea / 1.2 <= 18000);
			System.out.println();
		}
	}
}
