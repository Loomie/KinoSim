package de.outstare.kinosim.schedule;

import java.time.LocalTime;
import java.util.Collection;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.util.TimeRange;

/**
 * A Schedule is a plan for a day. It defines when movies will run in what hall. It holds all {@link Show}s in a chronological order.
 */
public interface Schedule extends Collection<Show> {
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

	/**
	 * @return <code>null</code> if no shows is scheduled
	 */
	Show getEarliest();

	/**
	 * @return <code>null</code> if no shows is scheduled
	 */
	Show getLatest();

	/**
	 * @param hall
	 * @param timeRange
	 * @return <code>false</code> if another show already occupies the given hall in the given time.
	 */
	boolean isFree(final CinemaHall hall, final TimeRange timeRange);

	/**
	 * @param hall
	 * @param start
	 * @param show
	 * @return <code>true</code> if the given time is {@link #isFree(CinemaHall, TimeRange)} and not blocked by the given show
	 */
	boolean isFreeFor(final CinemaHall hall, LocalTime start, Show show);
}
