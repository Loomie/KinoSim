package de.outstare.kinosim.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeRangeTest {

	@Test
	public void testNotOverlaps() {
		final TimeRange oneTwo = TimeRange.of(1, 2);
		final TimeRange twoFour = TimeRange.of(2, 4);
		final TimeRange threeFive = TimeRange.of(3, 5);
		final TimeRange oneSix = TimeRange.of(1, 6);
		final TimeRange wrapping1 = TimeRange.of(23, 2);
		final TimeRange wrapping2 = TimeRange.of(22, 1);

		assertNotOverlaps(oneTwo, threeFive);
		assertOverlaps(oneTwo, twoFour);
		assertOverlaps(twoFour, threeFive);
		assertOverlaps(oneSix, twoFour);
		assertOverlaps(oneTwo, wrapping1);
		assertOverlaps(wrapping1, wrapping2);
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

	@Test
	public void testToHours() {
		final TimeRange oneSix = TimeRange.of(1, 6);
		final TimeRange wrapping = TimeRange.of(23, 2);

		assertEquals(5, oneSix.toHours());
		assertEquals(3, wrapping.toHours());
	}
}
