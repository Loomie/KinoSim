package de.outstare.kinosim.cinema.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.outstare.kinosim.util.Position;

/**
 * An aera to be printed. All values are in meters (pos and size).
 *
 * @author Baret
 *
 */
public class Area {
	private Position position;
	private double length, height;
	/**
	 * indicates if this area has to be filled with stuff (or else has a table)
	 */
	private boolean freeArea;
	private List<Direction> walls;
	private Map<Position, PaintableObject> objects;

	/**
	 * @param position
	 * @param length
	 * @param height
	 * @param freeArea
	 *            is this area free and will be filled up?
	 * @param walls
	 */
	public Area(final Position position, final double length, final double height, final boolean freeArea, final List<Direction> walls) {
		super();
		this.position = position;
		this.length = length;
		this.height = height;
		this.freeArea = freeArea;
		this.walls = walls;
		objects = new HashMap<>();
		final List<Position> positions = new ArrayList<Position>();
		if (!freeArea) {
			final PaintableObject table = PaintableObject.TABLE;
			objects.put(new Position(0, calculatePositionOnAxisPartCenter(getHeight(), table.getHeightInMeters(), 1, 1)), table);
			final PaintableObject chair = PaintableObject.CHAIR;
			objects.put(new Position(table.getWidthInMeters() + 0.25, calculatePositionOnAxisPartCenter(getHeight(), chair.getHeightInMeters(), 1, 1)), chair);
			final Random r = new Random();
			if (r.nextDouble() > 0.7) {
				final PaintableObject o = PaintableObject.PLANT;
				final double x = calculatePositionOnAxisPartCenter(length, o.getWidthInMeters(), 2, 2);
				final double y = calculatePositionOnAxisPartCenter(height, o.getHeightInMeters(), 4, 1);
				objects.put(new Position(x, y), o);
			}
		} else {
			final PaintableObject o = PaintableObject.PLANT;
			// objects.put(
			// new Position(calculatePositionOnAxisPartCenter(length, o.getWidthInMeters(), 1, 1), calculatePositionOnAxisPartCenter(height,
			// o.getHeightInMeters(), 1, 1)), o);
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
	public Map<Position, PaintableObject> getObjects() {
		return objects;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (freeArea ? "" : "Non-") + "free area at " + position + ", width = " + length + ", height = " + height;
	}
}
