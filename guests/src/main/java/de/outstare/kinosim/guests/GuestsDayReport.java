package de.outstare.kinosim.guests;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Preconditions;

import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.ScheduleImpl;
import de.outstare.kinosim.schedule.Show;

/**
 * A GuestsDayReport holds all {@link GuestsShowReport}s of a day.
 */
public class GuestsDayReport implements Iterable<GuestsShowReport> {
	private final SortedSet<GuestsShowReport>	reports	= new TreeSet<>();
	private final LocalDate						date;

	public GuestsDayReport(final GuestCalculator calculator, final Schedule schedule, final LocalDate date) {
		this.date = date;
		for (final Show show : schedule) {
			final Map<Audience, Integer> guests = new HashMap<>();
			for (final Audience audience : Audience.values()) {
				final int guestCount = calculator.calculateAudienceGuests(show, date, audience);
				guests.put(audience, guestCount);
			}

			final GuestsShowReport report = new GuestsShowReport(show, date, guests);
			reports.add(report);
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