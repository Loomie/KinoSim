package de.outstare.kinosim.schedule;

import java.time.Duration;

/**
 * An AdBlock contains all advertisements shown prior to a show.
 */
public class AdBlock {
	public static final AdBlock	NONE	= new AdBlock(Duration.ZERO);

	private final Duration		duration;

	public AdBlock(final Duration duration) {
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}
}
