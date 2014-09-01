package de.outstare.kinosim.housegenerator.hall;

import de.outstare.kinosim.cinema.CinemaHall;

/**
 * A CinemHallGenerator creates a {@link CinemaHall}. Implementation decide based on what information the generation will occur.
 */
public interface CinemaHallGenerator {
	CinemaHall createHall();
}
