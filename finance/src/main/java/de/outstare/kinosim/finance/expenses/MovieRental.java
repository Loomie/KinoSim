package de.outstare.kinosim.finance.expenses;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.revenue.TicketPriceCategory;
import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.guests.GuestsShowReport;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.popularity.MoviePopularity;
import de.outstare.kinosim.population.Audience;

/**
 * A MovieRental describes the {@link Expense}s for {@link Movie}s.
 */
public class MovieRental {
	private static final Logger			LOG			= LoggerFactory.getLogger(MovieRental.class);
	private static final double			MIN_RATIO	= 0.35;
	private static final double			MAX_RATIO	= 0.65;

	private final GuestsDayReport		guestReport;
	private final TicketPriceCategory	price;

	public MovieRental(final GuestsDayReport guestReport, final TicketPriceCategory price) {
		this.guestReport = guestReport;
		this.price = price;
	}

	public Map<String, Expense> getDistributorExpense() {

		final Map<String, Expense> distributors = new HashMap<>();
		for (final GuestsShowReport showReport : guestReport) {
			final Movie movie = showReport.getShow().getFilm();
			final double increase = MoviePopularity.getPopularity(movie);
			final double ratio = MIN_RATIO + (MAX_RATIO - MIN_RATIO) * increase;

			long sales = 0;
			for (final Audience audience : Audience.values()) {
				sales += showReport.getGuests(audience) * price.getPrice(audience).getValue();
			}
			final Cents costs = Cents.of(Math.round(sales * ratio));
			LOG.debug("movie rental is {} at {} for {} with {} guests", costs, ratio, showReport.getShow(), showReport.getTotalGuests());
			final String distributor = movie.getDistributor();
			Expense old;
			if (!distributors.containsKey(distributor)) {
				old = new Expense(Cents.of(0), distributor);
			} else {
				old = distributors.get(distributor);
			}
			final Expense newly = new Expense(old.amount.add(costs), distributor);
			distributors.put(distributor, newly);
		}
		return distributors;
	}
}
