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

	public static TimeRange of(final int startHour, final int endHour) {
		final LocalTime startTime = LocalTime.of(startHour % 24, 0);
		final LocalTime endTime = LocalTime.of(endHour % 24, 0);
		return new TimeRange(startTime, endTime);
	}

	public TimeRange(final LocalTime start, final Duration length) {
		this(start, start.plus(length));
	}

	public TimeRange(final LocalTime start, final LocalTime end) {
		this.start = start;
		this.end = end;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public boolean overlaps(final TimeRange other) {
		if (endsNextDay()) {
			return overlapsDayEnd(other);
		}
		if (other.endsNextDay()) {
			return other.overlapsDayEnd(this);
		}
		assert start.isBefore(end) : "start must be less than end!";
		assert other.start.isBefore(other.end) : "other start must be less than other end!";
		return !start.isAfter(other.end) && !end.isBefore(other.start);
	}

	private boolean overlapsDayEnd(final TimeRange other) {
		// check rest of this day and start of next day separately
		final TimeRange firstPart = new TimeRange(start, LocalTime.MAX);
		final TimeRange secondPart = new TimeRange(LocalTime.MIN, end);
		return firstPart.overlaps(other) || secondPart.overlaps(other);
	}

	/**
	 * @return <code>true</code> if the end of this time range is an the next day.
	 */
	public boolean endsNextDay() {
		return start.isAfter(end);
	}

	public Duration getDuration() {
		final Duration duration = Duration.between(start, end);
		if (duration.isNegative()) {
			return duration.plusDays(1);
		}
		return duration;
	}

	public int toHours() {
		return (int) getDuration().toHours();
	}

	public int toMinutes() {
		return (int) getDuration().toMinutes();
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
