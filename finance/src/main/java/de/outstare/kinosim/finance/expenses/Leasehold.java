package de.outstare.kinosim.finance.expenses;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.finance.Cents;

/**
 * A Leasehold describes the {@link Expense}s for the estate and building of the movie theater.
 */
public class Leasehold {
	private final long rent;

	public Leasehold(final MovieTheater theater, final Cents rentPerSquareMeter) {
		rent = Math.round(theater.getEstateSpace() * rentPerSquareMeter.getValue());
	}

	public Expense getMonthlyRate() {
		return new Expense(Cents.of(rent), "leasehold");
	}
}
