package de.outstare.kinosim.finance.expenses;

import java.util.Collection;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.revenue.Revenue;

/**
 * A Taxes is a cumulation of all taxes that arise on {@link Revenue}s.
 */
public class Taxes {
	private final double	rate;

	public Taxes(final double rate) {
		this.rate = rate;
	}

	public Expense getExpense(final Revenue taxableRevenue) {
		final long taxes = getTax(taxableRevenue);
		return createExpense(taxes);
	}

	private long getTax(final Revenue taxableRevenue) {
		return (long) (taxableRevenue.getAmount().getValue() * rate);
	}

	private Expense createExpense(final long taxes) {
		return new Expense(Cents.of(taxes), "Taxes");
	}
}
