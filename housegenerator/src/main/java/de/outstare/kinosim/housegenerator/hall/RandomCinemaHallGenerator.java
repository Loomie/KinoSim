package de.outstare.kinosim.housegenerator.hall;

import java.util.Random;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.RoomType;

/**
 * A HouseGenerator creates a {@link CinemaHall}.
 */
public class RandomCinemaHallGenerator implements CinemaHallGenerator
{
	private static final Random	random		= new Random();
	static final int			MINIMUM		= 20;
	static final int			MAX_BASE	= 100;
	static final int			MAX_RAISE	= 3000;

	static int randomCapacity() {
		final int base = random.nextInt(MAX_BASE) + 1;
		final int raise = random.nextInt(MAX_RAISE) + 1;
		return MINIMUM + base + raise;
	}

	@Override
	public CinemaHall createHall() {
		return (CinemaHall) RoomType.CinemaHall.createRoom(randomCapacity());
	}

}
