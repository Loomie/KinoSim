package de.outstare.kinosim.cinema;

import java.util.Collection;

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
	 * FIXME use operations instead of getter, like getWorplaces(RoomType) or {@link #getNumberOfSeats()}
	 * 
	 * @return
	 */
	Collection<Room> getRooms();
}
