package de.outstare.kinosim.util;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Test;

public class TimeRangeTest {

	@Test
	public void testNotOverlaps() {
		final TimeRange oneTwo = createRange(1, 2);
		final TimeRange twoFour = createRange(2, 4);
		final TimeRange threeFive = createRange(3, 5);
		final TimeRange oneSix = createRange(1, 6);

		assertNotOverlaps(oneTwo, threeFive);
		assertOverlaps(oneTwo, twoFour);
		assertOverlaps(twoFour, threeFive);
		assertOverlaps(oneSix, twoFour);
	}

	private void assertOverlaps(final TimeRange first, final TimeRange second) {
		// test commutativity
		assertTrue(first.overlaps(second));
		assertTrue(second.overlaps(first));
	}

	private void assertNotOverlaps(final TimeRange first, final TimeRange second) {
		// test commutativity
		assertFalse(first.overlaps(second));
		assertFalse(second.overlaps(first));
	}

	private TimeRange createRange(final int startHour, final int endHour) {
		return new TimeRange(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0));
	}

}
