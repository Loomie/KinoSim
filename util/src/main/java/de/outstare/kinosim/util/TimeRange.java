package de.outstare.kinosim.util;

import java.time.Duration;
import java.time.LocalTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A TimeRange is the range between two times.
 */
public class TimeRange {
	private final LocalTime	start;
	private final LocalTime	end;

	public TimeRange(final LocalTime start, final Duration length) {
		this(start, start.plus(length));
	}

	public TimeRange(final LocalTime start, final LocalTime end) {
		this.start = start;
		this.end = end;
	}

	public boolean overlaps(final TimeRange other) {
		return !start.isAfter(other.end) && !end.isBefore(other.start);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		final TimeRange other = (TimeRange) obj;
		return new EqualsBuilder()
				.append(start, other.start)
				.append(end, other.end)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(75527, 21559)
				.append(start)
				.append(end)
				.toHashCode();
	}
}
