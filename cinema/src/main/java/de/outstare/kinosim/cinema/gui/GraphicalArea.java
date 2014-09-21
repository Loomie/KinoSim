package de.outstare.kinosim.cinema.gui;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * An aera to be printed. All values are in pixels (pos and size).
 *
 * @author Baret
 *
 */
public class GraphicalArea {
	private Point position = new Point();
	private int length, height;
	/**
	 * indicates if this area has to be filled with stuff
	 */
	private boolean freeArea;
	private List<Direction> walls;
	private Map<Point, PaintableObject> objects;

	/**
	 * @param position
	 * @param length
	 * @param height
	 * @param freeArea
	 *            is this area free and will be filled up?
	 * @param walls
	 */
	public GraphicalArea(final Point position, final int length, final int height, final boolean freeArea, final List<Direction> walls) {
		super();
		setPosition(position);
		this.length = length;
		this.height = height;
		this.freeArea = freeArea;
		this.walls = walls;
		objects = new HashMap<>(0);
		if (!freeArea) {
			final Random r = new Random();
			if (r.nextDouble() > 0.7) {
				final PaintableObject o = PaintableObject.PLANT;
				objects.put(new Point(0, 0), o);
			}
		}
	}

	/**
	 * @return the objects
	 */
	public Map<Point, PaintableObject> getObjects() {
		return objects;
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns true if there is no workplce in this area (in other words: if this area can be filled with stuff).
	 *
	 * @return the freeArea
	 */
	public boolean isFreeArea() {
		return freeArea;
	}

	/**
	 * @return the walls
	 */
	public List<Direction> getWalls() {
		return walls;
	}

	/**
	 * Sets the position
	 *
	 * @param newPosition
	 *            the new position may not be null, else the position does not change
	 */
	public void setPosition(final Point newPosition) {
		if (newPosition != null) {
			position = newPosition;
		}
	}

	/**
	 * @param newLength
	 * @param newHeight
	 */
	public void setSize(final int newLength, final int newHeight) {
		length = newLength;
		height = newHeight;
	}
}
