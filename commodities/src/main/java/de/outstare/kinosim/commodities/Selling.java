package de.outstare.kinosim.commodities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.IncomeStatement;
import de.outstare.kinosim.finance.IncomeStatement.RevenueCategory;
import de.outstare.kinosim.finance.revenue.Revenue;
import de.outstare.kinosim.guests.GuestsDayReport;

/**
 * A Selling sells products to guests. It calculates the {@link Revenue} and updates the {@link Inventory} accordingly.
 */
public class Selling {
	private static final Logger LOG = LoggerFactory.getLogger(Selling.class);

	private final Inventory storage;
	private final IncomeStatement balance;
	private final SellingPrices prices;

	public Selling(final Inventory inv, final IncomeStatement balance, final SellingPrices prices) {
		storage = inv;
		this.balance = balance;
		this.prices = prices;
	}

	public void sellToGuests(final GuestsDayReport guests) {
		final Need needs = new Need(guests);
		for (final Good good : Good.values()) {
			final int wantedItems = needs.getItemCount(good);
			final int available = Math.min(storage.getAmount(good) * good.getPackageSize(), wantedItems);
			if (available > 0) {
				final int boxes = (int) Math.ceil(available / (double) good.getPackageSize());
				storage.remove(good, boxes);
				final Cents revenue = prices.getItemPrice(good).multiply(available);
				balance.addRevenue(RevenueCategory.Revenues, new Revenue(revenue, good.toString()));
				LOG.info("sold {} {} for {} ({} boxes used)", available, good, revenue.formatted(), boxes);
			}
		}
	}
}
