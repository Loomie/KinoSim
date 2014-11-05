package de.outstare.kinosim.finance;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class CentsTest {

	@Test
	public void testOf() {
		final long[] testValues = new long[] { 0L, Long.MAX_VALUE, Long.MIN_VALUE };

		for (final long value : testValues) {
			final Cents result = Cents.of(value);
			assertEquals(value, result.getValue());
		}
	}

	@Test
	public void testAdd() {
		Cents first = Cents.of(1);
		Cents second = Cents.of(2);
		Cents result = first.add(second);
		assertEquals(3, result.getValue());

		first = Cents.of(Integer.MAX_VALUE);
		second = Cents.of(Integer.MAX_VALUE);
		result = first.add(second);
		assertEquals(2L * Integer.MAX_VALUE, result.getValue());

		first = Cents.of(Long.MAX_VALUE);
		second = Cents.of(1);
		try {
			result = first.add(second);
			fail("addition overflowed long to " + result.getValue());
		} catch (final ArithmeticException e) {
			// expected
		}
	}

	@Test
	public void testSubtract() {
		Cents first = Cents.of(2);
		Cents second = Cents.of(1);
		Cents result = first.subtract(second);
		assertEquals(1, result.getValue());

		first = Cents.of(Integer.MIN_VALUE);
		second = Cents.of(Integer.MAX_VALUE);
		result = first.subtract(second);
		assertEquals(2L * Integer.MIN_VALUE + 1, result.getValue());

		first = Cents.of(Long.MIN_VALUE);
		second = Cents.of(1);
		try {
			result = first.subtract(second);
			fail("subtract underflowed long to " + result.getValue());
		} catch (final ArithmeticException e) {
			// expected
		}
	}

	@Test
	public void testCompareTo() {
		Cents first = Cents.of(1);
		Cents second = Cents.of(2);
		assertEquals(-1, first.compareTo(second));
		assertEquals(1, second.compareTo(first));

		first = Cents.of(Integer.MAX_VALUE);
		second = Cents.of(Integer.MAX_VALUE + 1L);
		assertEquals(-1, first.compareTo(second));
		assertEquals(1, second.compareTo(first));

		first = Cents.of(Long.MIN_VALUE);
		second = Cents.of(Long.MAX_VALUE);
		assertEquals(-1, first.compareTo(second));
		assertEquals(1, second.compareTo(first));

		first = Cents.of(Long.MAX_VALUE);
		second = Cents.of(Long.MAX_VALUE);
		assertEquals(0, first.compareTo(second));
		assertEquals(0, second.compareTo(first));

		first = Cents.of(Long.MIN_VALUE);
		second = Cents.of(Long.MIN_VALUE);
		assertEquals(0, first.compareTo(second));
		assertEquals(0, second.compareTo(first));
	}

	@Test
	public void testEqualsAndHashCode() {
		final Cents first = Cents.of(1);

		// x.equals(null) should return false
		assertFalse(first.equals(null));
		// must have same class
		assertFalse(first.equals("1"));
		assertFalse(first.equals(Long.valueOf(1)));
		// reflexive
		assertTrue(first.equals(first));

		Cents second = Cents.of(2);

		assertFalse(first.equals(second));
		assertFalse(second.equals(first));
		assertNotEquals(first.hashCode(), second.hashCode());

		second = second.subtract(first);

		// symmetric
		assertTrue(first.equals(second));
		assertTrue(second.equals(first));
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	public void testFormatted() {
		final Cents cents = Cents.of(1234567890);
		final Locale defaultLocale = Locale.getDefault();
		// force locale
		Locale.setDefault(Locale.US);
		assertEquals("12,345,678.90 $", cents.formatted());
		Locale.setDefault(Locale.GERMANY);
		assertEquals("12.345.678,90 €", cents.formatted());
		// restore default
		Locale.setDefault(defaultLocale);
	}
}
