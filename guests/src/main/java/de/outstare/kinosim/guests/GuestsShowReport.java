package de.outstare.kinosim.guests;

import java.time.LocalDate;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.schedule.Show;

/**
 * A GuestsShowReport holds detailed for a show how many guests were there.
 */
public class GuestsShowReport implements Comparable<GuestsShowReport> {
	private final Show						show;
	private final LocalDate					day;
	private final Map<Audience, Integer>	guests;

	public GuestsShowReport(final Show show, final LocalDate day, final Map<Audience, Integer> guests) {
		super();
		this.show = show;
		this.day = day;
		this.guests = guests;
	}

	public Show getShow() {
		return show;
	}

	public LocalDate getDay() {
		return day;
	}

	public int getGuests(final Audience audience) {
		if (!guests.containsKey(audience)) {
			return 0;
		}
		return guests.get(audience);
	}

	public int getTotalGuests() {
		return guests.values().stream().mapToInt(e -> e).sum();
	}

	@Override
	public int compareTo(final GuestsShowReport o) {
		// sort by: Movie, Start, Hall
		return new CompareToBuilder()
				.append(show.getFilm(), o.show.getFilm())
				.append(show.getStart(), o.show.getStart())
				.append(show.getHall(), o.show.getHall())
				.toComparison();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		final GuestsShowReport o = (GuestsShowReport) obj;
		return new EqualsBuilder()
				.append(show.getFilm(), o.show.getFilm())
				.append(show.getStart(), o.show.getStart())
				.append(show.getHall(), o.show.getHall())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(93169, 10099)
				.append(show.getFilm())
				.append(show.getStart())
				.append(show.getHall())
				.toHashCode();
	}
}
