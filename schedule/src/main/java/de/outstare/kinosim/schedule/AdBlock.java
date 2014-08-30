package de.outstare.kinosim.schedule;

/**
 * A AdBlock contains all advertisements shown prior to a show.
 */
public class AdBlock {
	private final int	lengthInMinutes;

	public AdBlock(final int lengthInMinutes) {
		this.lengthInMinutes = lengthInMinutes;
	}

	public int getLengthInMinutes() {
		return lengthInMinutes;
	}
}
