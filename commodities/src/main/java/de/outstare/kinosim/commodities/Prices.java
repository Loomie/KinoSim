package de.outstare.kinosim.commodities;

import java.util.HashMap;
import java.util.Map;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.util.Randomness;

/**
 * Prices contains the current price for all Goods.
 */
public class Prices {
	private final Map<Goods, Cents> currentPrice = new HashMap<>();

	public Prices() {
		// only get random prices once instead of every call
		for (final Goods good : Goods.values()) {
			final Cents price = Cents.of(Randomness.getGaussianAround((int) good.getBasePrice().getValue()));
			currentPrice.put(good, price);
		}
	}

	public Cents getPrice(final Goods good) {
		assert currentPrice.containsKey(good);
		return currentPrice.get(good);
	}
}
