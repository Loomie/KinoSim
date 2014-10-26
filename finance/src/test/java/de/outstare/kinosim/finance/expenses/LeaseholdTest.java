package de.outstare.kinosim.finance.expenses;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.finance.Cents;

public class LeaseholdTest {

	private static class TheaterMock implements MovieTheater {
		private final double	squareMeters;

		TheaterMock(final double squareMeters) {
			this.squareMeters = squareMeters;
		}

		@Override
		public Collection<Room> getRooms() {
			return null;
		}

		@Override
		public int getNumberOfSeats() {
			return 0;
		}

		@Override
		public double getEstateSpace() {
			return squareMeters;
		}
	}

	@Test
	public void testLeasehold() {
		final double squareMeters = 1234.56;
		final MovieTheater theater = new TheaterMock(squareMeters);
		final Cents rent = Cents.of(678);

		final Leasehold result = new Leasehold(theater, rent);

		final Cents expected = Cents.of(Math.round(rent.getValue() * squareMeters));
		assertEquals(expected, result.getMonthlyRate().amount);
	}

}
