package de.outstare.kinosim.finance.revenue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.util.Randomness;

/**
 * A TicketPriceCategory contains the price for each audience for a specific category (maybe type of {@link Show}, place or equipment in
 * {@link CinemaHall}).
 */
public class TicketPriceCategory {
	private final Map<Audience, Cents>	prices	= new HashMap<>();

	/**
	 * @param prices
	 *            in cents
	 */
	public TicketPriceCategory(final Map<Audience, Cents> prices) {
		this.prices.putAll(prices);
	}

	/**
	 * @param audience
	 * @return cents
	 */
	public Cents getPrice(final Audience audience) {
		if (!prices.containsKey(audience)) {
			return Cents.of(0);
		}
		return prices.get(audience);
	}

	public static TicketPriceCategory createRandom() {
		final Map<Audience, Cents> randomPrices = new HashMap<>();
		for (final Audience audience : Audience.values()) {
			randomPrices.put(audience, Cents.of(Randomness.getGaussianAround(800)));
		}
		return new TicketPriceCategory(randomPrices);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
