package de.outstare.kinosim.commodities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.population.Audience;

/**
 * A Need defines for each {@link Audience} the amount of {@link Good}s wanted.
 */
public class Need {
	private static final Table<Audience, Good, Double> NEEDS = HashBasedTable.create(Audience.values().length, Good.values().length);
	static {
		NEEDS.put(Audience.KIDS, Good.Beer, .0);
		NEEDS.put(Audience.KIDS, Good.ChocolateBar, .10);
		NEEDS.put(Audience.KIDS, Good.Coffee, .0);
		NEEDS.put(Audience.KIDS, Good.FruitJuice, .15);
		NEEDS.put(Audience.KIDS, Good.GummiCandy, .10);
		NEEDS.put(Audience.KIDS, Good.Icecream, .30);
		NEEDS.put(Audience.KIDS, Good.Popcorn, .80);
		NEEDS.put(Audience.KIDS, Good.SoftDrinks, .50);
		NEEDS.put(Audience.KIDS, Good.TortillaChips, .10);

		NEEDS.put(Audience.TEENS, Good.Beer, .03);
		NEEDS.put(Audience.TEENS, Good.ChocolateBar, .12);
		NEEDS.put(Audience.TEENS, Good.Coffee, .03);
		NEEDS.put(Audience.TEENS, Good.FruitJuice, .10);
		NEEDS.put(Audience.TEENS, Good.GummiCandy, .15);
		NEEDS.put(Audience.TEENS, Good.Icecream, .40);
		NEEDS.put(Audience.TEENS, Good.Popcorn, .60);
		NEEDS.put(Audience.TEENS, Good.SoftDrinks, .60);
		NEEDS.put(Audience.TEENS, Good.TortillaChips, .20);

		NEEDS.put(Audience.TWENS, Good.Beer, .20);
		NEEDS.put(Audience.TWENS, Good.ChocolateBar, .05);
		NEEDS.put(Audience.TWENS, Good.Coffee, .10);
		NEEDS.put(Audience.TWENS, Good.FruitJuice, .08);
		NEEDS.put(Audience.TWENS, Good.GummiCandy, .06);
		NEEDS.put(Audience.TWENS, Good.Icecream, .20);
		NEEDS.put(Audience.TWENS, Good.Popcorn, .50);
		NEEDS.put(Audience.TWENS, Good.SoftDrinks, .40);
		NEEDS.put(Audience.TWENS, Good.TortillaChips, .25);

		NEEDS.put(Audience.ADULTS, Good.Beer, .20);
		NEEDS.put(Audience.ADULTS, Good.ChocolateBar, .04);
		NEEDS.put(Audience.ADULTS, Good.Coffee, .12);
		NEEDS.put(Audience.ADULTS, Good.FruitJuice, .05);
		NEEDS.put(Audience.ADULTS, Good.GummiCandy, .03);
		NEEDS.put(Audience.ADULTS, Good.Icecream, .10);
		NEEDS.put(Audience.ADULTS, Good.Popcorn, .40);
		NEEDS.put(Audience.ADULTS, Good.SoftDrinks, .30);
		NEEDS.put(Audience.ADULTS, Good.TortillaChips, .15);

		NEEDS.put(Audience.SENIORS, Good.Beer, .15);
		NEEDS.put(Audience.SENIORS, Good.ChocolateBar, .02);
		NEEDS.put(Audience.SENIORS, Good.Coffee, .08);
		NEEDS.put(Audience.SENIORS, Good.FruitJuice, .07);
		NEEDS.put(Audience.SENIORS, Good.GummiCandy, .02);
		NEEDS.put(Audience.SENIORS, Good.Icecream, .06);
		NEEDS.put(Audience.SENIORS, Good.Popcorn, .30);
		NEEDS.put(Audience.SENIORS, Good.SoftDrinks, .20);
		NEEDS.put(Audience.SENIORS, Good.TortillaChips, .10);
	}

	private final Table<Audience, Good, Integer> need = HashBasedTable.create(Audience.values().length, Good.values().length);

	public Need(final GuestsDayReport report) {
		for (final Audience audience : Audience.values()) {
			final int guests = report.getGuests(audience);
			for (final Good good : Good.values()) {
				final double ratio = NEEDS.contains(audience, good) ? NEEDS.get(audience, good) : 0;
				final int desideratum = (int) Math.round(guests * ratio);
				need.put(audience, good, desideratum);
			}
		}
	}

	public int getItemCount(final Good good) {
		int sum = 0;
		for (final Audience audience : Audience.values()) {
			sum += need.get(audience, good);
		}
		return sum;
	}
}
