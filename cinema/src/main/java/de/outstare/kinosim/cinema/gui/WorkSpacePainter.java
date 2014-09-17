package de.outstare.kinosim.cinema.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.WorkSpace;
import de.outstare.kinosim.util.NumberRange;
import de.outstare.kinosim.util.Randomness;

public class WorkSpacePainter extends JPanel {

	private static final long	serialVersionUID		= 7465300551424675824L;

	private static final Logger	LOG						= LoggerFactory.getLogger(WorkSpacePainter.class);

	private static final double	WORKPLACE_AREA_HEIGHT	= 4;
	private static final double	WORKPLACE_AREA_WIDTH	= 2;
	private static final Color	COLOR_BACKGROUND		= new Color(200, 150, 26);
	private final WorkSpace		workspace;
	private final int			pixelsPerMeter;

	private int					rows;

	private final Random		rand;

	private final Corner		startCorner;

	private double				workplacesPerRow;

	private final double		workspacesAreaWidth;

	private final double		workspacesAreaHeight;

	private final double		backgroundHeight;

	private final double		backgroundWidth;

	public WorkSpacePainter(final WorkSpace workspace, final int pixelsPerMeter) {
		LOG.debug("The workplace to be painted: " + workspace.toString());
		this.workspace = workspace;
		this.pixelsPerMeter = pixelsPerMeter;
		rand = Randomness.getRandom();
		rows = 1;
		final int maxWorkplacesPerRow = (int) new NumberRange(7, 10).getRandomValue();
		workplacesPerRow = Math.ceil(workspace.getWorkplaceCount() / (double) rows);
		if (workspace.getWorkplaceCount() > 2 && rand.nextDouble() >= 0.5d) {
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
		setSize(mToPixels(backgroundWidth), mToPixels(backgroundHeight));
		setPreferredSize(getSize());
		LOG.debug("Will paint " + workspace.getWorkplaceCount() + " tables in " + rows + " rows starting in corner " + startCorner);
		LOG.debug("The area of the office is " + backgroundWidth + " x " + backgroundHeight + "m, in pixels: " + mToPixels(backgroundWidth) + " x "
				+ mToPixels(backgroundHeight));
		LOG.debug("workplacesPerRow: " + workplacesPerRow);
		LOG.debug("workspacesAreaWidth: " + workspacesAreaWidth);
		LOG.debug("workspacesAreaHeight: " + workspacesAreaHeight);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
	@Override
	protected void paintComponent(final Graphics g) {
		paintBackground(mToPixels(backgroundWidth), mToPixels(backgroundHeight), g);
		int row = 0;
		for (int w = 0; w < workspace.getWorkplaceCount(); w++) {
			int x = 0;
			int y = 0;
			final GraphicalArea newArea;
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
				x = mToPixels(col * WORKPLACE_AREA_WIDTH);
			}
			if (startCorner.isLeft) {
				x = mToPixels(col * WORKPLACE_AREA_WIDTH);
			} else {
				x = mToPixels(backgroundWidth - WORKPLACE_AREA_WIDTH * (col + 1));
			}
			if (startCorner.isTop) {
				y = mToPixels(row * WORKPLACE_AREA_HEIGHT);
			} else {
				y = mToPixels(backgroundHeight - (row + 1) * WORKPLACE_AREA_HEIGHT);
			}
			final Point areaCorner = new Point(x, y);
			newArea = new GraphicalArea(areaCorner, mToPixels(WORKPLACE_AREA_WIDTH), mToPixels(WORKPLACE_AREA_HEIGHT), false, walls);
			paintWorkplace(g, newArea);
			if ((w + 1) % workplacesPerRow == 0) {
				row++;
			}
		}
		// Calculate and fill the free areas left

		// TEST/DEBUG
		final String debug = workspace.getWorkplaceCount() + ", " + rows;
		g.setColor(Color.YELLOW);
		g.drawString(debug, 5, 10);
	}

	private void paintWorkplace(final Graphics g, final GraphicalArea workplaceArea) {
		final int tableWidth = mToPixels(0.75);
		final int tableHeight = mToPixels(2);
		final int chairWidth = mToPixels(0.5);
		final int chairHeight = mToPixels(0.75);

		final Point offset = workplaceArea.getPosition();
		final int tablePosY = offset.y + (workplaceArea.getHeight() / 2 - tableHeight / 2);
		final int tablePosX = offset.x + 0;

		g.setColor(Color.BLACK);
		g.fillRect(tablePosX, tablePosY, tableWidth, tableHeight);

		final int chairPosY = offset.y + (workplaceArea.getHeight() / 2 - chairHeight / 2);
		final int chairPosX = offset.x + (tableWidth + mToPixels(0.25));

		g.fillRect(chairPosX, chairPosY, chairWidth, chairHeight);
		g.drawRect(offset.x, offset.y, workplaceArea.getLength(), workplaceArea.getHeight());
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
}
