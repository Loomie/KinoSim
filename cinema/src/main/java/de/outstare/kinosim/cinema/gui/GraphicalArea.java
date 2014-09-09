package de.outstare.kinosim.cinema.gui;

import java.awt.Point;
import java.util.List;

/**
 * An aera to be printed. All values are in pixels (pos and size).
 *
 * @author Baret
 *
 */
public class GraphicalArea {
	private Point position;
	private int length, height;
	/**
	 * indicates if this area has to be filled with stuff
	 */
	private boolean freeArea;
	private List<Direction> walls;

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
		this.position = position;
		this.length = length;
		this.height = height;
		this.freeArea = freeArea;
		this.walls = walls;
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

}
