package de.outstare.kinosim.cinema.gui;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.RoomType;
import de.outstare.kinosim.cinema.WorkSpace;

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
		final WorkSpacePainter painter = new WorkSpacePainter((WorkSpace) RoomType.Office.createRoom(seats), 20);
		add(painter);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(final String[] args) {
		for (int i = 500; i <= 10000; i += 500) {
			final WorkplacePainterMain w = new WorkplacePainterMain(i);
		}
	}

}
