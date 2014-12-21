package de.outstare.kinosim.finance.revenue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.guests.GuestsShowReport;
import de.outstare.kinosim.population.Audience;
import de.outstare.kinosim.schedule.Show;

/**
 * A TicketSales describes the income for {@link Show}s by sold tickets.
 */
public class TicketSales {
	private static final Logger LOG = LoggerFactory.getLogger(TicketSales.class);

	private final GuestsShowReport showReport;
	private final TicketPriceCategory price;

	public TicketSales(final GuestsShowReport showReport, final TicketPriceCategory price) {
		this.showReport = showReport;
		this.price = price;
	}

	public Revenue getRevenue() {
		Cents sales = Cents.of(0);
		for (final Audience audience : Audience.values()) {
			Cents audienceSales = price.getPrice(audience).multiply(showReport.getGuests(audience));
			sales = sales.add(audienceSales);
		}
		final String description = String.format("%d guests on %s %s for %s", showReport.getTotalGuests(), showReport.getDay(), showReport.getShow()
				.getStart(), showReport.getShow().getFilm().getTitle());
		LOG.debug("got {} for {}", sales.formatted(), description);
		return new Revenue(sales, description);
	}

}
