package de.outstare.kinosim.commodities;

import de.outstare.kinosim.finance.Cents;

/**
 * A SellingPrices contains a price for every item sold.
 */
public class SellingPrices extends Prices {
	/**
	 * Creates new selling prices based on a fixed margin
	 *
	 * @param fixedMargin
	 *            the percentage (0 - 0.999999999999999) of the selling price which is above the base price (not the actual buying price!)
	 */
	public SellingPrices(final double fixedMargin) {
		// only calculate prices once instead of every call
		final double divisor = 1 / (1 - fixedMargin);
		for (final Good good : Good.values()) {
			final Cents price = good.getBasePrice().multiply(divisor);
			currentPrice.put(good, price);
		}
	}

	/**
	 * @param good
	 * @return the price for a single item of the given good (normally prices are for boxes)
	 */
	public Cents getItemPrice(final Good good) {
		return getPrice(good).multiply(1.0 / good.getPackageSize());
	}

	public static void main(final String[] args) {
		final SellingPrices p = new SellingPrices(.8);
		for (final Good good : Good.values()) {
			System.out.println(good + ":\tbox price = " + p.getPrice(good).formatted() + ", per item:\t" + p.getItemPrice(good).formatted());
		}
	}
}
