package de.outstare.kinosim.schedule;

import java.time.LocalTime;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.util.Randomness;
import de.outstare.kinosim.util.TimeRange;

/**
 * A ScheduleImpl implements {@link Schedule}.
 */
public class ScheduleImpl extends AbstractCollection<Show> implements Schedule {
	private final SortedSet<Show> shows;

	public ScheduleImpl() {
		this(new TreeSet<>(new ShowTimeComparator()));
	}

	private ScheduleImpl(final SortedSet<Show> scheduledShows) {
		shows = scheduledShows;
	}

	@Override
	public boolean add(final Show show) {
		return shows.add(show);
	}

	@Override
	public boolean remove(final Object show) {
		return shows.remove(show);
	}

	@Override
	public int size() {
		return shows.size();
	}

	@Override
	public boolean contains(final Object o) {
		return shows.contains(o);
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

	/**
	 * @see de.outstare.kinosim.schedule.Schedule#isFree(de.outstare.kinosim.cinema.CinemaHall, de.outstare.kinosim.util.TimeRange)
	 */
	@Override
	public boolean isFree(final CinemaHall hall, final TimeRange timeRange) {
		return isFree(hall, timeRange, null);
	}

	private boolean isFree(final CinemaHall hall, final TimeRange timeRange, final Show excludeShow) {
		boolean free = true;
		for (final Show plannedShow : filterForHall(hall)) {
			if (plannedShow.equals(excludeShow)) {
				continue;
			}
			final TimeRange showRange = new TimeRange(plannedShow.getStart(), plannedShow.getDuration());
			if (timeRange.overlaps(showRange)) {
				free = false;
				break;
			}
		}
		return free;
	}

	@Override
	public boolean isFreeFor(final CinemaHall hall, final LocalTime start, final Show show) {
		final TimeRange timeRange = new TimeRange(start, show.getDuration());
		return isFree(hall, timeRange, show);
	}

	public static Schedule createRandom() {
		final Schedule s = new ScheduleImpl();
		final int showCount = Randomness.nextInt(18) + 3;
		for (int i = 0; i < showCount; i++) {
			s.add(Show.createRandom());
		}
		return s;
	}

}
