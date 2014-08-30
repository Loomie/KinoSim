package de.outstare.kinosim.cinema;

/**
 * A MovieTheater is a multiplex cinema with multiple screens, a foyer with counters, offices and storage rooms.
 *
 */
public interface MovieTheater {
	/**
	 * @return the total number of people who can simultaneously see movies in this theater
	 */
	int getNumberOfSeats();
}
