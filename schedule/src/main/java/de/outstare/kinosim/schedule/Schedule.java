package de.outstare.kinosim.schedule;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.movie.Movie;

/**
 * A Schedule is a plan for a day. It defines when movies will run in what hall. It holds all {@link Show}s in a chronological order.
 */
public interface Schedule extends Iterable<Show> {
	/**
	 * @param hall
	 * @return a view that only lists the {@link Show}s for the given hall.
	 */
	Schedule filterForHall(CinemaHall hall);

	/**
	 * @param hall
	 * @return a view that only lists the {@link Show}s for the given movie.
	 */
	Schedule filterForMovie(Movie hall);
}
