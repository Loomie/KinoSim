package de.outstare.kinosim.guituil;

import java.awt.Color;
import java.awt.Graphics;

public class PercentageBarWithMarker extends PercentageBar {
	private double markerPercentage;
	private Color markerColor;

	/**
	 * @param percent
	 * @param color
	 * @param markerPercentage
	 * @param markerColor
	 */
	public PercentageBarWithMarker(final double percent, final Color color, final double markerPercentage, final Color markerColor) {
		super(percent, color);
		this.markerPercentage = markerPercentage;
		this.markerColor = markerColor;
	}

	/**
	 * @param percent
	 * @param color
	 */
	public PercentageBarWithMarker(final double percent, final Color color) {
		this(percent, color, -1., null);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -1569756128586994030L;

	/* (non-Javadoc)
	 * @see de.outstare.kinosim.staff.gui.PercentageBar#paintComponent(java.awt.Graphics) */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (markerPercentage >= 0. && markerPercentage <= 1.) {
			if (markerColor != null) {
				g.setColor(markerColor);
			}
			final int x = getPaintAreaLeft() + (int) (getPaintAreaWidth() * markerPercentage);
			g.drawLine(x, getPaintAreaTop(), x, getPaintAreaBottom());
		}
	}

}
