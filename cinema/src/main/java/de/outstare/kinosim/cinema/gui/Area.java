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
public class Area {
	private Point position = new Point();
	private int length, height;
	/**
	 * indicates if this area has to be filled with stuff (or else has a table)
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
	 * @param pixelsPerMeter
	 */
	public Area(final Point position, final int length, final int height, final boolean freeArea, final List<Direction> walls, final int pixelsPerMeter) {
		super();
		setPosition(position);
		this.length = length;
		this.height = height;
		this.freeArea = freeArea;
		this.walls = walls;
		objects = new HashMap<>();
		if (!freeArea) {
			final Random r = new Random();
			if (r.nextDouble() > 0.7) {
				final PaintableObject o = PaintableObject.PLANT;
				final int x = (int) Math.rint(calculatePositionOnAxisPartCenter(length, o.getWidthInMeters() * pixelsPerMeter, 2, 2));
				final int y = (int) Math.rint(calculatePositionOnAxisPartCenter(height, o.getHeightInMeters() * pixelsPerMeter, 4, 1));
				objects.put(new Point(x, y), o);
			}
		}
	}

	/**
	 * Calculates the point of the top/left edge of an object that you want to place on the part of an axis.<br />
	 * When you want to center i.e. a rectangle on the x axis you have to shift the x-coordinate of the edge by half of the width. That is what this method calculates. <br />
	 * An example: Your x-axis is 100 units long and you want to split it in 2 parts and center your 10 units long object in the first half.<br />
	 * Each part is 50 units long.<br />
	 * The center of the first half is at 25 (100 / 2 / 2). That is where the center of the object should be.<br />
	 * So it has to be shifted "left" by half its size: by 5 (10 / 2).<br />
	 * The position of the left edge of the object is 20 (25 - 5)).<br />
	 * <br />
	 * With this method you can split your axes in "grids" and get the position of objects relative to the axis it is aligned to.
	 *
	 * @param axisSize
	 * @param objectSize
	 * @param axisParts
	 * @param partToBePositionedIn
	 *            The part in that the object will be placed, starting with 1
	 * @return
	 */
	private double calculatePositionOnAxisPartCenter(final double axisSize, final double objectSize, final int axisParts, int partToBePositionedIn) {
		if (partToBePositionedIn < 1) {
			partToBePositionedIn = 1;
		} else if (partToBePositionedIn > axisParts) {
			partToBePositionedIn = axisParts;
		}
		final double partSize = axisSize / axisParts;
		return ((partSize * partToBePositionedIn) - partSize / 2) - objectSize / 2;
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
