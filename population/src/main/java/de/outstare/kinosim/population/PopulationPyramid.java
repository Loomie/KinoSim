package de.outstare.kinosim.population;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A PopulationPyramid holds the age distribution of the population. This implementation uses a linear pyramid form.
 *
 * @see <a href="http://de.wikipedia.org/wiki/Altersverteilung#Absch.C3.A4tzung_der_Altersverteilungen">Absch&auml;tzung der Altersverteilungen</a>
 */
public class PopulationPyramid {
	private static final Logger	LOG	= LoggerFactory.getLogger(PopulationPyramid.class);

	private final int			newBorns;
	private final int			maxAge;

	public PopulationPyramid(final int newBorns, final int maxAge) {
		this.newBorns = newBorns;
		this.maxAge = maxAge;
		LOG.info("population with {} new borns and a max age of {}", newBorns, maxAge);
	}

	public int getPopulationOfAudience(final Audience ageGroup) {
		final int allAges = getPeopleByAge(ageGroup.maxAge);
		final int youngsters = getPeopleByAge(ageGroup.minAge);
		final int years = ageGroup.maxAge - ageGroup.minAge;
		// rectangle between min and max of height f(max)
		int total = years * allAges;
		// triangle above it h = f(min) - f(max)
		total += (youngsters - allAges) * years / 2;
		LOG.debug("{} allAges: {}, youngsters: {}, years: {}, total: {}", ageGroup, allAges, youngsters, years, total);
		return total;
	}

	private int getPeopleByAge(final int age) {
		return Math.max(0, newBorns - newBorns / maxAge * age);
	}

	public static PopulationPyramid createRandom() {
		final Random r = new Random();
		return new PopulationPyramid(500 + r.nextInt(1000), 50 + r.nextInt(70));
	}
}
