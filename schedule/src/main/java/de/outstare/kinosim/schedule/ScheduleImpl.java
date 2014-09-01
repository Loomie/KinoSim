package de.outstare.kinosim.schedule;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.movie.Movie;

/**
 * A ScheduleImpl implements {@link Schedule}.
 */
public class ScheduleImpl implements Schedule
{
	private final SortedSet<Show>	shows;

	public ScheduleImpl() {
		this(new TreeSet<>(new ShowTimeComparator()));
	}

	private ScheduleImpl(final SortedSet<Show> scheduledShows) {
		shows = scheduledShows;
	}

	@Override
	public void add(final Show show) {
		shows.add(show);
	}

	@Override
	public void remove(final Show show) {
		shows.remove(show);
	}

	@Override
	public Show getEarliest() {
		if (shows.isEmpty()) {
			return null;
		}
		return shows.first();
	}

	@Override
	public Show getLatest() {
		if (shows.isEmpty()) {
			return null;
		}
		return shows.last();
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Show> iterator() {
		return shows.iterator();
	}

	/**
	 * @see de.outstare.kinosim.schedule.Schedule#filterForHall(de.outstare.kinosim.cinema.CinemaHall)
	 */
	@Override
	public Schedule filterForHall(final CinemaHall hall) {
		return new ScheduleImpl(Sets.filter(shows, show -> hall.equals(show.getHall())));
	}

	/**
	 * @see de.outstare.kinosim.schedule.Schedule#filterForMovie(de.outstare.kinosim.movie.Movie)
	 */
	@Override
	public Schedule filterForMovie(final Movie movie) {
		return new ScheduleImpl(Sets.filter(shows, show -> movie.equals(show.getFilm())));
	}

}
