package de.outstare.kinosim.cinema.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.WorkSpace;
import de.outstare.kinosim.util.NumberRange;
import de.outstare.kinosim.util.Position;
import de.outstare.kinosim.util.Randomness;

public class WorkSpacePainter extends JPanel implements ComponentListener {

	private static final long serialVersionUID = 7465300551424675824L;

	private static final Logger LOG = LoggerFactory.getLogger(WorkSpacePainter.class);

	private static final double WORKPLACE_AREA_HEIGHT = 4;
	private static final double WORKPLACE_AREA_WIDTH = 2;
	private static final Color COLOR_BACKGROUND = new Color(200, 150, 26);
	private final WorkSpace workspace;
	private int pixelsPerMeter;

	private int rows;

	private final Corner startCorner;

	private double workplacesPerRow;

	private final double workspacesAreaWidth;

	private final double workspacesAreaHeight;

	/**
	 * Height of the background in meters
	 */
	private final double backgroundHeight;

	/**
	 * Width of the background in meters
	 */
	private final double backgroundWidth;

	private final List<Area> areas;

	private int oldPixelsPerMeter;

	public WorkSpacePainter(final WorkSpace workspace, final int pixelsPerMeter) {
		LOG.debug("The workplace to be painted: " + workspace.toString());
		this.workspace = workspace;
		this.pixelsPerMeter = pixelsPerMeter;
		rows = 1;
		final int maxWorkplacesPerRow = (int) new NumberRange(7, 10).getRandomValue();
		workplacesPerRow = Math.ceil(workspace.getWorkplaceCount() / (double) rows);
		if (workspace.getWorkplaceCount() > 2 && Randomness.getRandom().nextDouble() >= 0.5d) {
			rows = 2;
			workplacesPerRow = Math.ceil(workspace.getWorkplaceCount() / (double) rows);
		}
		while (workplacesPerRow > maxWorkplacesPerRow) {
			rows++;
			workplacesPerRow = Math.ceil(workspace.getWorkplaceCount() / (double) rows);
		}
		while (rows * WORKPLACE_AREA_HEIGHT * workplacesPerRow * WORKPLACE_AREA_WIDTH > workspace.getAllocatedSpace()) {
			rows--;
			workplacesPerRow = Math.ceil(workspace.getWorkplaceCount() / (double) rows);
		}
		startCorner = Corner.getRandom();
		workspacesAreaWidth = WORKPLACE_AREA_WIDTH * workplacesPerRow;
		workspacesAreaHeight = WORKPLACE_AREA_HEIGHT * rows;
		backgroundHeight = Math.sqrt((workspace.getAllocatedSpace() * workspacesAreaHeight) / workspacesAreaWidth);
		backgroundWidth = workspacesAreaWidth * (backgroundHeight / workspacesAreaHeight);
		areas = generateAreas();
		setSize(mToPixels(backgroundWidth), mToPixels(backgroundHeight));
		setPreferredSize(getSize());
		setMinimumSize(new Dimension((int) backgroundWidth, (int) backgroundHeight));
		oldPixelsPerMeter = pixelsPerMeter;
		LOG.debug("Will paint " + workspace.getWorkplaceCount() + " tables in " + rows + " rows starting in corner " + startCorner);
		LOG.debug("The area of the office is " + backgroundWidth + " x " + backgroundHeight + "m, in pixels: " + mToPixels(backgroundWidth) + " x "
				+ mToPixels(backgroundHeight));
		LOG.debug("workplacesPerRow: " + workplacesPerRow);
		LOG.debug("workspacesAreaWidth: " + workspacesAreaWidth);
		LOG.debug("workspacesAreaHeight: " + workspacesAreaHeight);

		addComponentListener(this);
	}

	private List<Area> generateAreas() {
		final ArrayList<Area> areas = new ArrayList<>();
		// Generate areas for the Workplaces
		int row = 0;
		for (int w = 0; w < workspace.getWorkplaceCount(); w++) {
			double x = 0;
			double y = 0;
			final ArrayList<Direction> walls = new ArrayList<>();
			final int col = (int) (w % workplacesPerRow);
			switch (startCorner) {
			case TOP_LEFT:
				addDirecionsToWalls(workplacesPerRow, w, row, walls, Direction.N, Direction.W);
				break;
			case TOP_RIGHT:
				addDirecionsToWalls(workplacesPerRow, w, row, walls, Direction.N, Direction.E);
				break;
			case BOTTOM_RIGHT:
				addDirecionsToWalls(workplacesPerRow, w, row, walls, Direction.S, Direction.E);
				break;
			case BOTTOM_LEFT:
				addDirecionsToWalls(workplacesPerRow, w, row, walls, Direction.S, Direction.W);
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

		// TODO: Calculate and fill the free areas left

		return areas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		paintBackground(mToPixels(backgroundWidth), mToPixels(backgroundHeight), g);
		for (final Area area : areas) {
			if (!area.isFreeArea()) {
				paintWorkplace(g, area);
			}
		}
	}

	private void paintWorkplace(final Graphics g, final Area workplaceArea) {
		final Position offset = workplaceArea.getPosition();
		// Draw a border just for testing purposes
		g.setColor(Color.BLACK);
		g.drawRect(mToPixels(offset.getX()), mToPixels(offset.getY()), mToPixels(workplaceArea.getLength()), mToPixels(workplaceArea.getHeight()));

		for (final Entry<Position, PaintableObject> i : workplaceArea.getObjects().entrySet()) {
			final Position relativePos = i.getKey();
			i.getValue().paint(g, new Point(mToPixels(relativePos.getX() + offset.getX()), mToPixels(relativePos.getY() + offset.getY())), pixelsPerMeter);
		}

	}

	private void paintBackground(final int backgroundWidth, final int backgroundHeight, final Graphics g) {
		g.setColor(COLOR_BACKGROUND);
		g.fillRect(0, 0, backgroundWidth, backgroundHeight);
	}

	private void addDirecionsToWalls(final double workplacesPerRow, final int w, final int r, final ArrayList<Direction> walls,
			final Direction firstRowDirection, final Direction firstColDirection) {
		if (r == 0) {
			walls.add(firstRowDirection);
		} else if ((w + 1) % workplacesPerRow == 0) {
			walls.add(firstColDirection);
		}
	}

	private int mToPixels(final double meters) {
		return (int) (meters * pixelsPerMeter);
	}

	@Override
	public void componentResized(final ComponentEvent e) {
		oldPixelsPerMeter = pixelsPerMeter;
		pixelsPerMeter = (int) (getWidth() / (double) getHeight() < backgroundWidth / backgroundHeight ? getWidth() / backgroundWidth : getHeight()
				/ backgroundHeight);
		if (pixelsPerMeter < backgroundHeight || pixelsPerMeter < backgroundWidth) {
			pixelsPerMeter = oldPixelsPerMeter;
		}
	}

	@Override
	public void componentMoved(final ComponentEvent e) {

	}

	@Override
	public void componentShown(final ComponentEvent e) {

	}

	@Override
	public void componentHidden(final ComponentEvent e) {

	}
}
