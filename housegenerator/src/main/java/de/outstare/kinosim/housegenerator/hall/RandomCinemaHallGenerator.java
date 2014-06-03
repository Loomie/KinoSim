package de.outstare.kinosim.housegenerator.hall;

import java.util.Random;

import de.outstare.kinosim.cinema.CinemaHall;

/**
 * A HouseGenerator creates a {@link CinemaHall}.
 */
public class RandomCinemaHallGenerator implements CinemHallGenerator {
    private static final Random random = new Random();
    static final int MINIMUM = 20;

    static int randomCapacity() {
	final int base = random.nextInt(100);
	final int raise = random.nextInt(1000);
	return MINIMUM + base + raise;
    }

    public CinemaHall createHall() {
	return new FixedSizeCinemaHall(randomCapacity());
    }

}
