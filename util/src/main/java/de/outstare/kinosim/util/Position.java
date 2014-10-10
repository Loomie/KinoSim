package de.outstare.kinosim.util;

public class Position {
	private double x, y;

	/**
	 * @param x
	 * @param y
	 */
	public Position(final double x, final double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + x + "|" + y + ")";
	}
}
