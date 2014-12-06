package de.outstare.kinosim.housegenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.FixedSizeCinemaHall;
import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.cinema.RoomType;
import de.outstare.kinosim.util.Randomness;

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
		// Basic Implementation: Use a decreasing curve of x^(-1/2)
		// Extended Implementation: Use a decreasing curve of 1.5*x^(maxSeats*-0.00025) (sharper decrease in large theaters for more halls)
		int hallNo = 1;
		int actualSeats = 0;
		final double maxSeats = Math.min(Randomness.getGaussianAround(700), calculatedSeats / 3.0);
		LOG.debug("Calculated seats: " + calculatedSeats + ", max seats per hall: " + maxSeats);
		final List<CinemaHall> halls = new ArrayList<>();
		while (usedSpace < usableArea) {
			final int seatsForHall = (int) (maxSeats * Math.pow(hallNo, -0.5));
			final double spaceForHall = cinemaAreaPerSeat * seatsForHall;
			if (usedSpace + spaceForHall > usableArea) {
				// no more space
				break;
			}
			final String name = "Hall " + hallNo;
			final CinemaHall hall = new FixedSizeCinemaHall(name, spaceForHall, seatsForHall);
			rooms.add(hall);
			halls.add(hall);
			LOG.debug(name + " has " + seatsForHall + " seats and an area of " + spaceForHall + " m²");
			usedSpace += spaceForHall;
			actualSeats += seatsForHall;
			hallNo++;
		}
		LOG.debug("Total seats: " + actualSeats + " used netto area: " + usedSpace / 1.2 + " m²");
		final int totalCapacity = actualSeats;
		final double totalSpace = usedSpace;
		return new MovieTheater() {
			@Override
			public List<CinemaHall> getHalls() {
				return Collections.unmodifiableList(halls);
			}

			@Override
			public Collection<Room> getRooms() {
				return Collections.unmodifiableCollection(rooms);
			}

			@Override
			public Set<Room> getRoomsByType(final RoomType type) {
				return getRooms().stream().filter(r -> r.getType() == type).collect(Collectors.toSet());
			}

			@Override
			public int getNumberOfSeats() {
				return totalCapacity;
			}

			@Override
			public double getRoomSpace() {
				return totalSpace;
			}

			@Override
			public double getEstateSpace() {
				return area;
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
		final double areaForHalls = usableArea * 0.508;
		return (int) (areaForHalls / cinemaAreaPerSeat);
	}

}
