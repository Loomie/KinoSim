package de.outstare.kinosim.housegenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.FixedSizeCinemaHall;
import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.cinema.RoomType;

/**
 * A AreaMovieTheaterCreator generates a {@link MovieTheater} for a given area of land (an estate).
 */
public class AreaMovieTheaterCreator implements MovieTheaterGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(AreaMovieTheaterCreator.class);

	private final int area; // in square meters

	public AreaMovieTheaterCreator(final int areaInSquareMeters) {
		area = areaInSquareMeters;
	}

	/**
	 * @see de.outstare.kinosim.housegenerator.MovieTheaterGenerator#createTheater()
	 */
	@Override
	public MovieTheater createTheater() {
		final double usableArea = area * 1.2; // +20 % because between the large halls offices and so on can be build above other areas
		final double cinemaAreaPerSeat = RoomType.CinemaHall.createRoom(1).getAllocatedSpace(); // use fixed value instead of a new random value for
		// each call
		// 1. estimate seats by using a fixed part of space for a single hall
		final int calculatedSeats = estimateSeats(usableArea, cinemaAreaPerSeat);
		// 2. create all rooms except cinema halls (because each type will be calculated only once, but we want multiple halls)
		final Set<Room> rooms = createNonCinemaHalls(calculatedSeats);
		double usedSpace = rooms.stream().mapToDouble(room -> room.getAllocatedSpace()).sum();
		// 3. For the remaining space create cinema halls
		// Idea: Create one large cinema which holds most people and multiple smaller ones which do not differ that much in size.
		// Implementation: Use a decreasing curve of x^(-1/2)
		int hallNo = 1;
		int actualSeats = 0;
		LOG.debug("Calculated seats: " + calculatedSeats);
		while (usedSpace < usableArea) {
			final int seatsForHall = (int) (calculatedSeats / 3.0 * Math.pow(hallNo, -0.5));
			final double spaceForHall = cinemaAreaPerSeat * seatsForHall;
			rooms.add(new FixedSizeCinemaHall(spaceForHall, seatsForHall));
			LOG.debug("Hall " + hallNo + " has " + seatsForHall + " seats and an area of " + spaceForHall + " m²");
			usedSpace += spaceForHall;
			actualSeats += seatsForHall;
			hallNo++;
		}
		LOG.debug("Total seats: " + actualSeats + " used netto area: " + usedSpace / 1.2 + " m²");
		final int totalCapacity = actualSeats;
		return new MovieTheater() {
			@Override
			public Collection<Room> getRooms() {
				return rooms;
			}

			@Override
			public int getNumberOfSeats() {
				return totalCapacity;
			}
		};
	}

	private Set<Room> createNonCinemaHalls(final int calculatedSeats) {
		final Set<Room> rooms = new HashSet<>();
		for (final RoomType type : RoomType.values()) {
			if (type == RoomType.CinemaHall) {
				continue;
			}
			final Room room = type.createRoom(calculatedSeats);
			LOG.debug(room.toString());
			rooms.add(room);
		}
		return rooms;
	}

	private int estimateSeats(final double usableArea, final double cinemaAreaPerSeat) {
		final double areaForHalls = usableArea * 0.56;
		return (int) (areaForHalls / cinemaAreaPerSeat);
	}

}
