package de.outstare.kinosim.guests;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;

import de.outstare.kinosim.movie.popularity.MoviePopularity;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.population.PopulationPyramid;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.util.Distributions;

public class GuestCalculator {
	private static final Logger		LOG						= LoggerFactory.getLogger(GuestCalculator.class);
	private static final double		MAX_SEASON_MULTIPLIER	= 0.5;

	private final PopulationPyramid	population;

	/**
	 * @param population
	 *            the available population in age groups
	 */
	public GuestCalculator(final PopulationPyramid population) {
		super();
		this.population = population;
	}

	int calculateTotalGuests(final Schedule schedule, final LocalDate date) {
		int total = 0;
		for (final Show show : schedule) {
			total += calculateGuests(show, date);
		}
		return total;
	}

	int calculateGuests(final Show show, final LocalDate date) {
		int total = 0;
		for (final Audience audience : Audience.values()) {
			total += calculateAudienceGuests(show, date, audience);
		}
		final int guests = Math.min(total, show.getHall().getCapacity());
		LOG.info("{} guests on {} for {}", guests, date, show);
		return guests;
	}

	int calculateAudienceGuests(final Show show, final LocalDate date, final Audience audience) {
		final double populationPart = population.getPopulationOfAudience(audience) * 0.012; // 12 seats per 1000 inhabitants
		final double movieFactor = MoviePopularity.getPopularity(audience, show.getFilm());
		final double startTimeFactor = startTimeFactor(show, audience);
		final double averageWeightedFactor = (5.0 * movieFactor + 1.0 * startTimeFactor) / 6.0;
		final double releaseDateFactor = getReleaseFactor(show, date);
		final int guests = (int) (populationPart * averageWeightedFactor * releaseDateFactor * dateFactor(date));
		LOG.debug("{} {} on {} for {}", guests, audience, date, show);
		return guests;
	}

	private double getReleaseFactor(final Show show, final LocalDate date) {
		final double timeFactor;
		final int runningWeeks = show.getFilm().getWeeksSinceRelease(date) + 1;
		if (runningWeeks < 1) {
			timeFactor = 1.3; // previews are very popular
		} else {
			// older movie are not that popular (1 by square root of weeks running) TODO move function to audience, so seniors like historic movies
			// also (or even more?)
			timeFactor = Math.pow(runningWeeks, -0.5);
		}
		return timeFactor;
	}

	private double startTimeFactor(final Show show, final Audience audience) {
		final int preferredTime = audience.preferredStartTime.toSecondOfDay();
		int startTime = show.getStart().toSecondOfDay();
		final int secondsOfDay = (int) ChronoUnit.DAYS.getDuration().getSeconds();
		final Range<Integer> range = Range.closed(preferredTime - secondsOfDay / 2, preferredTime + secondsOfDay / 2);
		if (startTime < range.lowerEndpoint()) {
			// start at next day will be treated for example as 26:00 instead of 2:00 because it must be within a 12 hour interval
			startTime += secondsOfDay;
		}
		return Distributions.getDifferenceRatio(preferredTime, startTime, range);
	}

	/**
	 * @return factor around 1.0
	 */
	private double dateFactor(final LocalDate date) {
		// cosinus over the year with maximum in december and minimum in june
		final double yearRatio = date.getMonthValue() / 12.0;
		final double seasonMultiplier = Math.cos(yearRatio * 2 * Math.PI);
		assert -1.0 <= seasonMultiplier && seasonMultiplier <= 1.0;
		final double seasonFactor = 1 + MAX_SEASON_MULTIPLIER * seasonMultiplier;
		assert 1 - MAX_SEASON_MULTIPLIER <= seasonFactor && seasonFactor <= 1 + MAX_SEASON_MULTIPLIER;
		// week
		final double weekdayFactor = getWeekdayFactor(date);
		final double dateFactor = (seasonFactor + weekdayFactor) / 2;
		return dateFactor;
	}

	private double getWeekdayFactor(final LocalDate date) {
		final double weekdayFactor;
		switch (date.getDayOfWeek()) {
		case THURSDAY:
			weekdayFactor = .11;
			break;
		case FRIDAY:
			weekdayFactor = .15;
			break;
		case SATURDAY:
			weekdayFactor = .23;
			break;
		case SUNDAY:
			weekdayFactor = .15;
			break;
		case MONDAY:
			weekdayFactor = .10;
			break;
		case TUESDAY:
			weekdayFactor = .15;
			break;
		case WEDNESDAY:
		default:
			weekdayFactor = .11;
			break;
		}
		return weekdayFactor;
	}

	public static GuestCalculator createRandom() {
		return new GuestCalculator(PopulationPyramid.createRandom());
	}
}
