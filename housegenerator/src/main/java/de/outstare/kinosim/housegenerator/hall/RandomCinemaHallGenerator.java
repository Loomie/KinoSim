package de.outstare.kinosim.housegenerator.hall;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.RoomType;
import de.outstare.kinosim.util.Randomness;

/**
 * A HouseGenerator creates a {@link CinemaHall}.
 */
public class RandomCinemaHallGenerator implements CinemaHallGenerator {
	static final int MINIMUM = 20;
	static final int MAX_BASE = 100;
	static final int MAX_RAISE = 3000;

	static int randomCapacity() {
		final int base = Randomness.nextInt(MAX_BASE) + 1;
		final int raise = Randomness.nextInt(MAX_RAISE) + 1;
		return MINIMUM + base + raise;
	}

	@Override
	public CinemaHall createHall() {
		return (CinemaHall) RoomType.CinemaHall.createRoom(randomCapacity());
	}

}
