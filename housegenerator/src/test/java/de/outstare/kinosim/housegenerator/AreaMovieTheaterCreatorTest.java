package de.outstare.kinosim.housegenerator;

import static org.junit.Assert.*;

import org.junit.Test;

import de.outstare.kinosim.cinema.MovieTheater;

public class AreaMovieTheaterCreatorTest {

    @Test
    public void testCreateTheater() {
	final AreaMovieTheaterCreator creator = new AreaMovieTheaterCreator(1800);

	final MovieTheater result = creator.createTheater();

	assertTrue(result.getNumberOfSeats() + " seats", 100 < result.getNumberOfSeats());
	assertTrue(result.getNumberOfSeats() + " seats", result.getNumberOfSeats() < 1000);
    }

}
