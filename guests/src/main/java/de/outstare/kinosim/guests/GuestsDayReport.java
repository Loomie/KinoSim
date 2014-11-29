package de.outstare.kinosim.guests;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Preconditions;

import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.ScheduleImpl;
import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.util.Randomness;

/**
 * A GuestsDayReport holds all {@link GuestsShowReport}s of a day.
 */
public class GuestsDayReport implements Iterable<GuestsShowReport> {
	private final SortedSet<GuestsShowReport> reports = new TreeSet<>();
	private final LocalDate date;

	public GuestsDayReport(final GuestCalculator calculator, final Schedule schedule, final LocalDate date) {
		this.date = date;
		// group by movie to distribute people across multiple shows
		final Set<Movie> movies = new HashSet<>();
		schedule.forEach(show -> movies.add(show.getFilm()));
		for (final Movie movie : movies) {
			final int showsWithMovie = schedule.filterForMovie(movie).size();
			for (final Show show : schedule.filterForMovie(movie)) {
				final Map<Audience, Integer> guests = new HashMap<>();
				for (final Audience audience : Audience.values()) {
					// ideal guests (deterministic)
					final int normalGuestCount = (int) Math.round(calculator.calculateAudienceGuests(show, date, audience) / (double) showsWithMovie);
					// real guests (random)
					final int guestCount = Randomness.getGaussianAround(normalGuestCount);
					guests.put(audience, guestCount);
				}

				final GuestsShowReport report = new GuestsShowReport(show, date, guests);

				final int totalGuests = report.getTotalGuests();
				final int maxGuests = show.getHall().getCapacity();
				if (totalGuests > maxGuests) {
					// if capacity of hall is reached reduce guests equally for each audience
					final double ratio = 1 - (totalGuests - maxGuests) / (double) totalGuests;
					for (final Entry<Audience, Integer> entry : guests.entrySet()) {
						final int cappedGuests = (int) (entry.getValue() * ratio);
						guests.put(entry.getKey(), cappedGuests);
					}
					// because ratios are floored, some people may be missing to reach the full capacity
					assert report.getTotalGuests() <= maxGuests : "capped to " + report.getTotalGuests() + " is still over limit of " + maxGuests;
					final int missing = maxGuests - report.getTotalGuests();
					guests.put(Audience.ADULTS, guests.get(Audience.ADULTS) + missing);
				}

				reports.add(report);
			}
		}
	}

	public LocalDate getDay() {
		return date;
	}

	@Override
	public Iterator<GuestsShowReport> iterator() {
		return reports.iterator();
	}

	public int getShowCount() {
		return reports.size();
	}

	public GuestsShowReport getShowReport(final int index) {
		Preconditions.checkElementIndex(index, reports.size());

		final Iterator<GuestsShowReport> it = iterator();
		for (int i = 0; i < index; i++) {
			it.next();
		}
		return it.next();
	}

	public int getTotalGuests() {
		return reports.stream().mapToInt(e -> e.getTotalGuests()).sum();
	}

	public static GuestsDayReport createRandom() {
		return new GuestsDayReport(GuestCalculator.createRandom(), ScheduleImpl.createRandom(), LocalDate.now());
	}
}
