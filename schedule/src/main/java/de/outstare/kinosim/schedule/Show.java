package de.outstare.kinosim.schedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.FixedSizeCinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.SimpleMovie;

/**
 * A Show is a single scheduled presentation. It defines at which time (when) a movie (what) runs in which hall (where). Additionally advertisements
 * and breaks can be planned.
 */
public class Show {
	private LocalTime			start;
	private final Movie			film;
	private final CinemaHall	hall;
	private final AdBlock		ads;
	private final int			breakDurationInMinutes;

	public Show(final LocalTime start, final Movie film, final CinemaHall hall, final AdBlock ads, final int breakDurationInMinutes) {
		super();
		this.start = start;
		this.film = film;
		this.hall = hall;
		this.ads = ads;
		this.breakDurationInMinutes = breakDurationInMinutes;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(final LocalTime newStart) {
		start = newStart;
	}

	public Duration getDuration() {
		final Duration totalDuration = Duration.ZERO
				.plus(film.getDuration())
				.plus(ads.getDuration())
				.plus(Duration.ofMinutes(breakDurationInMinutes));
		return totalDuration;
	}

	public Movie getFilm() {
		return film;
	}

	public CinemaHall getHall() {
		return hall;
	}

	public AdBlock getAds() {
		return ads;
	}

	public int getBreakDurationInMinutes() {
		return breakDurationInMinutes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Show other = (Show) obj;
		return new EqualsBuilder()
				.append(start, other.start)
				.append(film, other.film)
				.append(hall, other.hall)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(93761, 5651)
				.append(start)
				.append(film)
				.append(hall)
				.toHashCode();
	}

	// clone methods

	public Show copyWithOtherMovie(final Movie newFilm) {
		return new Show(start, newFilm, hall, ads, breakDurationInMinutes);
	}

	public Show copyWithOtherHall(final CinemaHall newHall) {
		return new Show(start, film, newHall, ads, breakDurationInMinutes);
	}

	public Show copyWithOtherAds(final AdBlock newAds) {
		return new Show(start, film, hall, newAds, breakDurationInMinutes);
	}

	public Show copyWithOtherBreak(final int newBreakDurationInMinutes) {
		return new Show(start, film, hall, ads, newBreakDurationInMinutes);
	}

	public static Show createRandom() {
		final Random r = new Random();
		final LocalTime aStart = LocalTime.of(12 + r.nextInt(12), 15 * r.nextInt(4));
		final Movie aFilm = SimpleMovie.createRandom();
		final CinemaHall aHall = new FixedSizeCinemaHall(50 + r.nextInt(450), 100 + r.nextInt(400));
		final AdBlock adblock = new AdBlock(Duration.ofMinutes(r.nextInt(20)));
		final int breakTime = r.nextDouble() > 0.8 ? 5 + r.nextInt(25) : 0;
		return new Show(aStart, aFilm, aHall, adblock, breakTime);
	}
}
