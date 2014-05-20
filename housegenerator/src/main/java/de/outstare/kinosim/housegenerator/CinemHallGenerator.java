package de.outstare.kinosim.housegenerator;

import de.outstare.kinosim.cinema.CinemaHall;

/**
 * A CinemHallGenerator creates a {@link CinemaHall}. Implementation decide based on what information the generation will occur.
 */
public interface CinemHallGenerator {
    CinemaHall createHall();
}
