package de.outstare.kinosim.finance.revenue;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.NamedAmount;

/**
 * An Revenue is an amount of money we got.
 */
public class Revenue extends NamedAmount {

	public Revenue(final Cents amount, final String name) {
		super(amount, name);
	}
}
