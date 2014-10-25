package de.outstare.kinosim.finance.expenses;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.finance.Cents;

/**
 * A Leasehold describes the {@link Expense}s for the estate and building of the movie theater.
 */
public class Leasehold {
	public final Expense	monthlyRate;

	public Leasehold(final MovieTheater theater, final Cents rentPerSquareMeter) {
		final long rent = Math.round(theater.getEstateSpace() * rentPerSquareMeter.getValue());
		monthlyRate = new Expense(Cents.of(rent), "leasehold");
	}
}
