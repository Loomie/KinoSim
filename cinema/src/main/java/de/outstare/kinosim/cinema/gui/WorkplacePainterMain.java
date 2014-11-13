package de.outstare.kinosim.cinema.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.RoomType;

public class WorkplacePainterMain extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 3567729189772783457L;
	private static final Logger LOG = LoggerFactory.getLogger(WorkplacePainterMain.class);

	public WorkplacePainterMain(final int seats) {
		super("WorkplacePainter Test");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		LOG.debug("Created Office for " + seats + " seats. Painting it...");
		final RoomPanel painterPanel = new RoomPanel(RoomType.Office.createRoom(seats));
		add(painterPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(final String[] args) {
		WorkplacePainterMain last = null;
		int maxY = 0;
		final Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		for (int i = 500; i <= 7000; i += 500) {
			Point pos = new Point(0, 0);
			final WorkplacePainterMain w = new WorkplacePainterMain(i);
			if (last != null) {
				pos = last.getLocation();
				if (pos.x + w.getWidth() > bounds.width) {
					pos.x = 0;
					pos.y += maxY;
					maxY = 0;
				} else {
					pos.x += last.getWidth();
					maxY = Math.max(maxY, pos.y + w.getHeight());
				}
				if (pos.y + w.getHeight() > bounds.height) {
					pos.y = 0;
				}
			}
			w.setLocation(pos);
			last = w;
		}
	}
}
