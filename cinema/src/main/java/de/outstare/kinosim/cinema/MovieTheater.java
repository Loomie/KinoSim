package de.outstare.kinosim.cinema;

import java.util.Collection;
import java.util.List;

/**
 * A MovieTheater is a multiplex cinema with multiple screens, a foyer with counters, offices and storage rooms.
 *
 */
public interface MovieTheater {
	/**
	 * @return the total number of people who can simultaneously see movies in this theater
	 */
	int getNumberOfSeats();

	/**
	 * @return all {@link CinemaHall}s of this theater ordered by size (largest first)
	 */
	List<CinemaHall> getHalls();

	/**
	 * @return every {@link Room} of this theater.
	 */
	Collection<Room> getRooms();

	/**
	 * @return number of square meters (m²) all rooms offer.
	 */
	double getRoomSpace();

	/**
	 * @return number of square meters (m²) the estate covers.
	 */
	double getEstateSpace();
}
