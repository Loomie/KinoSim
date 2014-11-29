package de.outstare.kinosim.cinema.gui;

import de.outstare.kinosim.util.Randomness;

enum Corner {
	TOP_LEFT(true, true),
	TOP_RIGHT(true, false),
	BOTTOM_RIGHT(false, false),
	BOTTOM_LEFT(false, true);

	boolean isTop;
	boolean isLeft;

	private Corner(final boolean isTop, final boolean isLeft) {
		this.isTop = isTop;
		this.isLeft = isLeft;
	}

	static Corner getRandom() {
		return values()[Randomness.nextInt(values().length)];
	}
}