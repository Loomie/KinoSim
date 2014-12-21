package de.outstare.kinosim.commodities;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.util.Randomness;

/**
 * BuyingPrices contains the current price for buying any {@link Good}.
 */
public class BuyingPrices extends Prices {
	/**
	 * Creates new random prices for buying
	 */
	public BuyingPrices() {
		// only get random prices once instead of every call
		for (final Good good : Good.values()) {
			final Cents price = Cents.of(Randomness.getGaussianAround((int) good.getBasePrice().getValue()));
			currentPrice.put(good, price);
		}
	}
}
