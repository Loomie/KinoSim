package de.outstare.kinosim.population;

/**
 * A PopulationPyramid holds the age distribution of the population. This implementation uses a linear pyramid form.
 *
 * @see http://de.wikipedia.org/wiki/Altersverteilung#Absch.C3.A4tzung_der_Altersverteilungen
 */
public class PopulationPyramid {
	private final int	newBorns;
	private final int	maxAge;

	public PopulationPyramid(final int newBorns, final int maxAge) {
		this.newBorns = newBorns;
		this.maxAge = maxAge;
	}

	public int getPopulationOfAudience(final Audience ageGroup) {
		final int allAges = getPeopleByAge(ageGroup.maxAge);
		final int youngsters = getPeopleByAge(ageGroup.minAge);
		final int years = ageGroup.maxAge - ageGroup.minAge;
		// rectangle between min and max of height f(max)
		int total = years * allAges;
		// triangle above it h = f(min) - f(max)
		total += (youngsters - allAges) * years / 2;
		return total;
	}

	private int getPeopleByAge(final int age) {
		return newBorns - newBorns / maxAge * age;
	}
}
