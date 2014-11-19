package de.outstare.kinosim.staff.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PercentageBar extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4539272986206234560L;
	private double percent;
	private Color color;

	/**
	 * @param percent
	 *            a value between 0 and 1
	 * @param color
	 *            the color of the bar
	 */
	public PercentageBar(final double percent, final Color color) {
		if (percent > 1.) {
			this.percent = 1.;
		} else if (percent < 0) {
			this.percent = 0.;
		} else {
			this.percent = percent;
		}
		this.color = color;
		setPreferredSize(new Dimension(200, 40));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		g.setColor(Color.BLACK);
		final int bottom = getPaintAreaBottom();
		final int right = getPaintAreaRight();
		final int paintHeight = getPaintAreaHeight();
		final int paintWidth = getPaintAreaWidth();
		g.drawLine(getPaintAreaLeft(), getPaintAreaTop(), getPaintAreaLeft(), bottom);
		g.drawLine(right, getPaintAreaTop(), right, bottom);
		final int step = Math.floorDiv(paintWidth, 10);
		for (int i = step; i < paintWidth; i += step) {
			g.drawLine(i, bottom - paintHeight / 2, i, bottom);
		}

		if (percent > 0) {
			g.setColor(color);
			final int x = (int) (paintWidth * percent) + 1;
			g.fillRect(getPaintAreaLeft(), getPaintAreaTop(), x, paintHeight + 1);
		}
	}

	/**
	 * @return
	 */
	protected int getPaintAreaTop() {
		return getInsets().top;
	}

	/**
	 * @return
	 */
	protected int getPaintAreaLeft() {
		return getInsets().left;
	}

	/**
	 * @return
	 */
	protected int getPaintAreaWidth() {
		return getWidth() - getPaintAreaLeft() - getInsets().right;
	}

	/**
	 * @return
	 */
	protected int getPaintAreaHeight() {
		return getHeight() - getPaintAreaTop() - getInsets().bottom;
	}

	/**
	 * @return
	 */
	protected int getPaintAreaRight() {
		return getWidth() - getInsets().right;
	}

	/**
	 * @return
	 */
	protected int getPaintAreaBottom() {
		return getHeight() - getInsets().bottom;
	}
}
