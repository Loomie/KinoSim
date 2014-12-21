package de.outstare.kinosim.finance;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Cents is a value object. It represents an amount of money.
 */
public class Cents implements Serializable, Comparable<Cents> {
	private static final long serialVersionUID = 1L;

	private final long value;

	public static Cents of(final long value) {
		return new Cents(value);
	}

	private Cents(final long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public Cents add(final Cents other) {
		return new Cents(Math.addExact(value, other.value));
	}

	public Cents subtract(final Cents other) {
		return new Cents(Math.subtractExact(value, other.value));
	}

	public Cents multiply(final double factor) {
		return new Cents(Math.round(value * factor));
	}

	public String formatted() {
		return String.format("%,.2f %s", value / 100.0, Currency.getInstance(Locale.getDefault()).getSymbol());
	}

	@Override
	public int compareTo(final Cents o) {
		return Long.compare(value, o.value);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		final Cents other = (Cents) obj;
		return value == other.value;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(value);
	}
}
