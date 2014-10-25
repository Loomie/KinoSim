package de.outstare.kinosim.finance.expenses;

import static de.outstare.kinosim.population.Audience.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.revenue.TicketPriceCategory;
import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.guests.GuestsShowReport;
import de.outstare.kinosim.population.Audience;

public class MovieRentalTest {
	private final String			distributorName1	= "Test Distributor One";
	private final String			distributorName2	= "Test Distributor Two";
	private final String			distributorName3	= "Test Distributor Three";
	private MovieRental				objectUnderTest;
	private Map<Audience, Cents>	prices;

	@Before
	public void setUp() {
		final GuestsShowReport report1 = mock(GuestsShowReport.class, RETURNS_DEEP_STUBS);
		when(report1.getShow().getFilm().getDistributor()).thenReturn(distributorName1);
		when(report1.getGuests(ADULTS)).thenReturn(0);
		when(report1.getGuests(KIDS)).thenReturn(5);
		when(report1.getGuests(SENIORS)).thenReturn(0);
		when(report1.getGuests(TEENS)).thenReturn(5);
		when(report1.getGuests(TWENS)).thenReturn(5);

		final GuestsShowReport report2 = mock(GuestsShowReport.class, RETURNS_DEEP_STUBS);
		when(report2.getShow().getFilm().getDistributor()).thenReturn(distributorName1);
		when(report2.getGuests(ADULTS)).thenReturn(5);
		when(report2.getGuests(KIDS)).thenReturn(0);
		when(report2.getGuests(SENIORS)).thenReturn(5);
		when(report2.getGuests(TEENS)).thenReturn(0);
		when(report2.getGuests(TWENS)).thenReturn(0);

		final GuestsShowReport report3 = mock(GuestsShowReport.class, RETURNS_DEEP_STUBS);
		when(report3.getShow().getFilm().getDistributor()).thenReturn(distributorName2);
		when(report3.getGuests(ADULTS)).thenReturn(1000);
		when(report3.getGuests(KIDS)).thenReturn(2000);
		when(report3.getGuests(SENIORS)).thenReturn(3000);
		when(report3.getGuests(TEENS)).thenReturn(4000);
		when(report3.getGuests(TWENS)).thenReturn(5000);

		final GuestsShowReport report4 = mock(GuestsShowReport.class, RETURNS_DEEP_STUBS);
		when(report4.getShow().getFilm().getDistributor()).thenReturn(distributorName2);
		when(report4.getGuests(ADULTS)).thenReturn(5000);
		when(report4.getGuests(KIDS)).thenReturn(4000);
		when(report4.getGuests(SENIORS)).thenReturn(3000);
		when(report4.getGuests(TEENS)).thenReturn(2000);
		when(report4.getGuests(TWENS)).thenReturn(1000);

		final GuestsShowReport report5 = mock(GuestsShowReport.class, RETURNS_DEEP_STUBS);
		when(report5.getShow().getFilm().getDistributor()).thenReturn(distributorName3);
		when(report5.getGuests(ADULTS)).thenReturn(1);
		when(report5.getGuests(KIDS)).thenReturn(1);
		when(report5.getGuests(SENIORS)).thenReturn(1);
		when(report5.getGuests(TEENS)).thenReturn(1);
		when(report5.getGuests(TWENS)).thenReturn(1);

		final List<GuestsShowReport> showReports = Lists.newArrayList();
		showReports.add(report1);
		showReports.add(report2);
		showReports.add(report3);
		showReports.add(report4);
		showReports.add(report5);
		final GuestsDayReport report = mock(GuestsDayReport.class);
		when(report.iterator()).thenReturn(showReports.iterator());

		prices = new HashMap<>();
		// using prime numbers to differentiate the audiences
		prices.put(ADULTS, Cents.of(200));
		prices.put(KIDS, Cents.of(300));
		prices.put(SENIORS, Cents.of(500));
		prices.put(TEENS, Cents.of(700));
		prices.put(TWENS, Cents.of(1100));
		final TicketPriceCategory priceCategory = new TicketPriceCategory(prices);

		objectUnderTest = new MovieRental(report, priceCategory);
	}

	@Test
	public void testGetDistributorExpense() {
		final long sum = prices.values().stream().mapToLong(i -> i.getValue()).sum();
		final double ticketRatio = 0.4069582; // TODO mock popularity instead of relaying on MoviePopularity
		final double ticketsPart = sum * ticketRatio;

		final Map<String, Expense> result = objectUnderTest.getDistributorExpense();

		assertEquals(Cents.of(Math.round(5 * ticketsPart)), result.get(distributorName1).amount);
		assertEquals(Cents.of(Math.round(6000 * ticketsPart)), result.get(distributorName2).amount);
		assertEquals(Cents.of(Math.round(1 * ticketsPart)), result.get(distributorName3).amount);
	}

}
