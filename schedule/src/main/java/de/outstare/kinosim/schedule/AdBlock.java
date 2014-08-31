package de.outstare.kinosim.schedule;

/**
 * An AdBlock contains all advertisements shown prior to a show.
 */
public class AdBlock {
	public static final AdBlock	NONE	= new AdBlock(0);

	private final int			lengthInMinutes;

	public AdBlock(final int lengthInMinutes) {
		this.lengthInMinutes = lengthInMinutes;
	}

	public int getLengthInMinutes() {
		return lengthInMinutes;
	}
}
