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

class GuestCalculator {
	private static final Logger		LOG						= LoggerFactory.getLogger(GuestCalculator.class);
	private static final double		MAX_SEASON_MULTIPLIER	= 0.5;

	private final PopulationPyramid	population;

	/**
	 * @param month
	 *            the time of year for what guests should be calculated (summer or winter?)
	 * @param population
	 *            the available population in age groups
	 */
	GuestCalculator(final PopulationPyramid population) {
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
		final double populationPart = population.getPopulationOfAudience(audience) * 0.012;
		final double movieFactor = new MoviePopularity(show.getFilm().getRating()).getPopularity(audience);
		final double startFactor = startTimeFactor(show, audience);
		final double averageWeightedFactor = (5.0 * movieFactor + 1.0 * startFactor) / 6;
		final int guests = (int) (populationPart * averageWeightedFactor * dateFactor(date));
		LOG.debug("{} {} on {} for {}", guests, audience, date, show);
		return guests;
	}

	private double startTimeFactor(final Show show, final Audience audience) {
		final int preferredTime = audience.preferredStartTime.toSecondOfDay();
		final int startTime = show.getStart().toSecondOfDay();
		final int secondsOfDay = (int) ChronoUnit.DAYS.getDuration().toMinutes() * 60;
		return Distributions.getDifferenceRatio(preferredTime, startTime, Range.closed(0, secondsOfDay));
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
