package de.outstare.kinosim.cinema.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public enum PaintableObject {
	TRASHBIN(0.5, 0.5) {
		@Override
		public void paint(final Graphics g, final Point pos, final int pixelsPerMeter) {
			g.setColor(new Color(10, 10, 10));
			g.fillOval(pos.x, pos.y, (int) (getWidthInMeters() * pixelsPerMeter), (int) (getHeightInMeters() * pixelsPerMeter));
		}
	},
	PLANT(1., 1.) {
		@Override
		public void paint(final Graphics g, final Point pos, final int pixelsPerMeter) {
			g.setColor(new Color(85, 55, 26));
			g.fillRect((int) (0.3 * pixelsPerMeter) + pos.x, (int) (0.3 * pixelsPerMeter) + pos.y, (int) (0.4 * pixelsPerMeter), (int) (0.4 * pixelsPerMeter));
			g.setColor(new Color(0, 90, 0));
			g.fillOval((int) (0.4 * pixelsPerMeter) + pos.x, (int) (0.4 * pixelsPerMeter) + pos.y, (int) (0.2 * pixelsPerMeter), (int) (0.2 * pixelsPerMeter));
			g.setColor(new Color(0, 150, 0));
			g.fillOval((int) (0. * pixelsPerMeter) + pos.x, (int) (0.39 * pixelsPerMeter) + pos.y, (int) (0.4 * pixelsPerMeter), (int) (0.22 * pixelsPerMeter));
			g.fillOval((int) (0.6 * pixelsPerMeter) + pos.x, (int) (0.39 * pixelsPerMeter) + pos.y, (int) (0.4 * pixelsPerMeter), (int) (0.22 * pixelsPerMeter));
			g.fillOval((int) (0.39 * pixelsPerMeter) + pos.x, (int) (0. * pixelsPerMeter) + pos.y, (int) (0.22 * pixelsPerMeter), (int) (0.4 * pixelsPerMeter));
			g.fillOval((int) (0.39 * pixelsPerMeter) + pos.x, (int) (0.6 * pixelsPerMeter) + pos.y, (int) (0.22 * pixelsPerMeter), (int) (0.4 * pixelsPerMeter));
		}
	},
	TABLE(0.75, 2.) {
		@Override
		public void paint(final Graphics g, final Point pos, final int pixelsPerMeter) {
			g.setColor(Color.BLACK);
			g.fillRect(pos.x, pos.y, (int) (getWidthInMeters() * pixelsPerMeter), (int) (getHeightInMeters() * pixelsPerMeter));
		}
	},
	CHAIR(0.5, 0.75) {
		@Override
		public void paint(final Graphics g, final Point pos, final int pixelsPerMeter) {
			g.setColor(Color.BLACK);
			g.fillRect(pos.x, pos.y, (int) (getWidthInMeters() * pixelsPerMeter), (int) (getHeightInMeters() * pixelsPerMeter));
			g.setColor(Color.GRAY);
			g.fillRect(pos.x, (int) (pos.y + getHeightInMeters() * 0.1 * pixelsPerMeter), (int) (getWidthInMeters() * pixelsPerMeter * 0.8),
					(int) (getHeightInMeters() * pixelsPerMeter * 0.9));
		}
	};

	private double widthInMeters;
	private double heightInMeters;

	private PaintableObject(final double width, final double height) {
		widthInMeters = width;
		heightInMeters = height;
	}

	public abstract void paint(Graphics g, Point pos, int pixelsPerMeter);

	/**
	 * @return the widthInMeters
	 */
	public double getWidthInMeters() {
		return widthInMeters;
	}

	/**
	 * @return the heightInMeters
	 */
	public double getHeightInMeters() {
		return heightInMeters;
	}

}
