package de.outstare.kinosim.staff.gui;

import java.awt.Color;

/**
 * A TrafficLightsPercentageBar colors the bar from red (low) over yellow (medium) to green (full).
 */
public class TrafficLightsPercentageBar extends PercentageBar {
	private static final long	serialVersionUID	= 1L;

	public TrafficLightsPercentageBar(final double percent) {
		super(percent, calculateColor(percent));
	}

	private static Color calculateColor(final double percent) {
		final int minHue = 0; // red
		final int maxHue = 120; // green
		final int hue = (int) (minHue + (maxHue - minHue) * percent);
		return Color.getHSBColor(hue / 360f, 0.8f, 0.8f);
	}
}
