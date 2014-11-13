package de.outstare.kinosim.cinema.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.cinema.WorkSpace;
import de.outstare.kinosim.util.NumberRange;
import de.outstare.kinosim.util.Position;
import de.outstare.kinosim.util.Randomness;

public class WorkSpacePainter extends RoomPainter {

	private static final long serialVersionUID = 7465300551424675824L;

	private static final Logger LOG = LoggerFactory.getLogger(WorkSpacePainter.class);

	private static final double WORKPLACE_AREA_HEIGHT = 4;
	private static final double WORKPLACE_AREA_WIDTH = 2;
	private int rows;

	private final Corner startCorner;

	private int workplacesPerRow;

	private final double workspacesAreaWidth;

	private final double workspacesAreaHeight;

	private final List<Area> areas;

	private final int workplaceCount;

	public WorkSpacePainter(final Room room, final int pixelsPerMeter) {
		super(room, pixelsPerMeter);
		setBackgroundColor(new Color(200, 150, 26));
		final WorkSpace workspace = (WorkSpace) room;
		LOG.debug("The workplace to be painted: " + workspace.toString());
		rows = 1;
		final int maxWorkplacesPerRow = (int) new NumberRange(7, 10).getRandomValue();
		workplaceCount = workspace.getWorkplaceCount();
		workplacesPerRow = (int) Math.ceil(workplaceCount / (double) rows);
		if (workplaceCount > 2 && Randomness.getRandom().nextDouble() >= 0.5d) {
			rows = 2;
			workplacesPerRow = (int) Math.ceil(workplaceCount / (double) rows);
		}
		while (workplacesPerRow > maxWorkplacesPerRow) {
			rows++;
			workplacesPerRow = (int) Math.ceil(workplaceCount / (double) rows);
		}
		while (rows * WORKPLACE_AREA_HEIGHT * workplacesPerRow * WORKPLACE_AREA_WIDTH > workspace.getAllocatedSpace()) {
			rows--;
			workplacesPerRow = (int) Math.ceil(workplaceCount / (double) rows);
		}
		startCorner = Corner.getRandom();
		workspacesAreaWidth = WORKPLACE_AREA_WIDTH * workplacesPerRow;
		workspacesAreaHeight = WORKPLACE_AREA_HEIGHT * rows;
		backgroundHeight = Math.sqrt((workspace.getAllocatedSpace() * workspacesAreaHeight) / workspacesAreaWidth);
		backgroundWidth = workspacesAreaWidth * (backgroundHeight / workspacesAreaHeight);
		areas = generateAreas();
		LOG.debug("Will paint " + workplaceCount + " tables in " + rows + " rows starting in corner " + startCorner);
		LOG.debug("The area of the office is " + backgroundWidth + " x " + backgroundHeight + "m, in pixels: " + mToPixels(backgroundWidth) + " x "
				+ mToPixels(backgroundHeight));
		LOG.debug("workplacesPerRow: " + workplacesPerRow);
		LOG.debug("workspacesAreaWidth: " + workspacesAreaWidth);
		LOG.debug("workspacesAreaHeight: " + workspacesAreaHeight);
	}

	private List<Area> generateAreas() {
		final List<Area> areas = new ArrayList<>();
		// Generate areas for the Workplaces
		areas.addAll(generateWorkplaceAreas());

		// TODO: Calculate and fill the free areas left
		areas.addAll(generateFreeAreas(areas));

		return areas;
	}

	/**
	 * Calculates the three areas not covered by the workplace areas
	 *
	 * @param usedAreas
	 *            A List of areas used by the workplaces
	 * @return
	 */
	private List<Area> generateFreeAreas(final List<Area> usedAreas) {
		final ArrayList<Area> freeAreas = new ArrayList<>();
		final double w2 = workspacesAreaWidth - (workplacesPerRow * rows - workplaceCount) * WORKPLACE_AREA_WIDTH;
		final double h2 = workplaceCount % rows == 0 ? rows * WORKPLACE_AREA_HEIGHT : (rows - 1) * WORKPLACE_AREA_HEIGHT;

		final double x1 = backgroundWidth - workspacesAreaWidth;
		final double x2 = backgroundWidth - w2;
		final double y1 = backgroundHeight - h2;
		final double y2 = backgroundHeight - workspacesAreaHeight;

		double p1x, p1y, p2x, p2y, p3x, p3y;
		final List<Direction> walls1 = new ArrayList<Direction>();
		final List<Direction> walls2 = new ArrayList<Direction>();
		final List<Direction> walls3 = new ArrayList<Direction>();
		if (startCorner.isLeft) {
			walls1.add(Direction.E);
			walls2.add(Direction.E);
			walls3.add(Direction.W);

			p1x = workspacesAreaWidth;
			p2x = w2;
			p3x = 0;
		} else {
			walls1.add(Direction.W);
			walls2.add(Direction.W);
			walls3.add(Direction.E);

			p1x = 0;
			p2x = 0;
			p3x = x2;
		}
		if (startCorner.isTop) {
			walls1.add(Direction.N);
			walls2.add(Direction.S);
			walls3.add(Direction.S);

			p1y = 0;
			p2y = h2;
			p3y = workspacesAreaHeight;
		} else {
			walls1.add(Direction.S);
			walls2.add(Direction.N);
			walls3.add(Direction.N);

			p1y = y1;
			p2y = 0;
			p3y = 0;
		}
		final Area a1 = new Area(new Position(p1x, p1y), x1, h2, true, walls1);
		LOG.debug(a1.toString());
		freeAreas.add(a1);
		final Area a2 = new Area(new Position(p2x, p2y), x2, y1, true, walls2);
		LOG.debug(a2.toString());
		freeAreas.add(a2);
		final Area a3 = new Area(new Position(p3x, p3y), w2, y2, true, walls3);
		LOG.debug(a3.toString());
		freeAreas.add(a3);
		return freeAreas;
	}

	/**
	 * @param areas
	 */
	private List<Area> generateWorkplaceAreas() {
		final List<Area> areas = new ArrayList<>();
		int row = 0;
		for (int w = 0; w < workplaceCount; w++) {
			double x = 0;
			double y = 0;
			final ArrayList<Direction> walls = new ArrayList<>();
			final int col = w % workplacesPerRow;
			switch (startCorner) {
			case TOP_LEFT:
				addDirecionsToWalls(col, row, walls, Direction.N, Direction.W);
				break;
			case TOP_RIGHT:
				addDirecionsToWalls(col, row, walls, Direction.N, Direction.E);
				break;
			case BOTTOM_RIGHT:
				addDirecionsToWalls(col, row, walls, Direction.S, Direction.E);
				break;
			case BOTTOM_LEFT:
				addDirecionsToWalls(col, row, walls, Direction.S, Direction.W);
				x = col * WORKPLACE_AREA_WIDTH;
			}
			if (startCorner.isLeft) {
				x = col * WORKPLACE_AREA_WIDTH;
			} else {
				x = backgroundWidth - WORKPLACE_AREA_WIDTH * (col + 1);
			}
			if (startCorner.isTop) {
				y = row * WORKPLACE_AREA_HEIGHT;
			} else {
				y = backgroundHeight - (row + 1) * WORKPLACE_AREA_HEIGHT;
			}
			final Position areaCorner = new Position(x, y);
			areas.add(new Area(areaCorner, WORKPLACE_AREA_WIDTH, WORKPLACE_AREA_HEIGHT, false, walls));
			if ((w + 1) % workplacesPerRow == 0) {
				row++;
			}
		}
		return areas;
	}

	private void paintArea(final Graphics g, final Area area) {
		final Position offset = area.getPosition();
		// Draw the objects of the area
		for (final Entry<Position, PaintableObject> i : area.getObjects().entrySet()) {
			final Position relativePos = i.getKey();
			i.getValue().paint(g, new Point(mToPixels(relativePos.getX() + offset.getX()), mToPixels(relativePos.getY() + offset.getY())), pixelsPerMeter);
		}

	}

	private void addDirecionsToWalls(final int col, final int row, final ArrayList<Direction> walls, final Direction firstRowDirection,
			final Direction firstColDirection) {
		if (row == 0) {
			walls.add(firstRowDirection);
		}
		if (col == 0) {
			walls.add(firstColDirection);
		}
	}

	@Override
	protected void paint(final Room r, final Graphics g) {
		for (final Area a : areas) {
			paintArea(g, a);
		}
	}
}
