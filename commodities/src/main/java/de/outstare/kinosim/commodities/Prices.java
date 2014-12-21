package de.outstare.kinosim.commodities;

import java.util.HashMap;
import java.util.Map;

import de.outstare.kinosim.finance.Cents;

/**
 * A Prices holds a price for every {@link Good}
 */
abstract class Prices {

	protected final Map<Good, Cents> currentPrice = new HashMap<>();

	public Cents getPrice(final Good good) {
		assert currentPrice.containsKey(good);
		return currentPrice.get(good);
	}

}