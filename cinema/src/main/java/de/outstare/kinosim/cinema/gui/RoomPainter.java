package de.outstare.kinosim.cinema.gui;

import java.awt.Color;
import java.awt.Graphics;

import de.outstare.kinosim.cinema.Room;

public abstract class RoomPainter {
	protected int pixelsPerMeter;
	protected Room room;
	/**
	 * Height of the background in meters
	 */
	protected double backgroundHeight;
	/**
	 * Width of the background in meters
	 */
	protected double backgroundWidth;
	private Color backgroundColor;

	public RoomPainter(final Room r, final int pixelsPerMeter) {
		this.pixelsPerMeter = pixelsPerMeter;
		room = r;
		backgroundHeight = Math.sqrt(r.getAllocatedSpace());
		backgroundWidth = backgroundHeight;
		setBackgroundColor(Color.WHITE);
	}

	public void paintRoom(final Graphics g) {
		paintBackground(g);
		paint(room, g);
	}

	protected void paintBackground(final Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, mToPixels(backgroundWidth), mToPixels(backgroundHeight));
	}

	protected void setBackgroundColor(final Color newBackgroundColor) {
		backgroundColor = newBackgroundColor;
	}

	protected int mToPixels(final double meters) {
		return (int) (meters * pixelsPerMeter);
	}

	/**
	 * @return the pixelsPerMeter
	 */
	public int getPixelsPerMeter() {
		return pixelsPerMeter;
	}

	/**
	 * @param pixelsPerMeter
	 *            the pixelsPerMeter to set
	 */
	public void setPixelsPerMeter(final int pixelsPerMeter) {
		this.pixelsPerMeter = pixelsPerMeter;
	}

	protected abstract void paint(Room r, Graphics g);
}
