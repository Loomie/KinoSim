package de.outstare.kinosim.finance.expenses;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.NamedAmount;

/**
 * An Expense is an amount of money we spent.
 */
public class Expense extends NamedAmount {

	public Expense(final Cents amount, final String name) {
		super(amount, name);
	}
}
