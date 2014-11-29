package de.outstare.kinosim.population;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.util.Randomness;

/**
 * A PopulationPyramid holds the age distribution of the population. This implementation uses a linear pyramid form.
 *
 * @see <a href="http://de.wikipedia.org/wiki/Altersverteilung#Absch.C3.A4tzung_der_Altersverteilungen">Absch&auml;tzung der Altersverteilungen</a>
 */
public class PopulationPyramid {
	private static final Logger LOG = LoggerFactory.getLogger(PopulationPyramid.class);

	private final int newBorns;
	private final int maxAge;

	public PopulationPyramid(final int newBorns, final int maxAge) {
		this.newBorns = newBorns;
		this.maxAge = maxAge;
		LOG.info("population with {} new borns and a max age of {}", newBorns, maxAge);
	}

	/**
	 * @param ageGroup
	 *            for which the number of people in this population is to be determined
	 * @param minimumAge
	 *            which further restricts which part of the given audience will be considered
	 * @return the number of people in this population with the given age
	 */
	public int getPopulationOfAudience(final Audience ageGroup, final int minimumAge) {
		final int partMinAge = Math.max(ageGroup.minAge, minimumAge);
		final int partMaxAge = Math.max(ageGroup.maxAge, minimumAge);
		final int allAges = getPeopleByAge(partMaxAge);
		final int youngsters = getPeopleByAge(partMinAge);
		final int years = partMaxAge - partMinAge;
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
		return new PopulationPyramid(500 + Randomness.nextInt(1000), 50 + Randomness.nextInt(70));
	}

	public int getTotal() {
		int sum = 0;
		for (final Audience audience : Audience.values()) {
			sum += getPopulationOfAudience(audience, 0);
		}
		return sum;
	}
}
