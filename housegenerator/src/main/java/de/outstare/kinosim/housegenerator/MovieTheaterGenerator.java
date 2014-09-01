package de.outstare.kinosim.housegenerator;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.MovieTheater;

/**
 * A MovieTheaterGenerator creates a full cinema house with mutliple {@link CinemaHall}s.
 *
 */
public interface MovieTheaterGenerator {
	MovieTheater createTheater();
}
