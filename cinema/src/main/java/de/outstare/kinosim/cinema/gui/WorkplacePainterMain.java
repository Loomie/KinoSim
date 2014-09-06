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

	public WorkplacePainterMain() {
		super("WorkplacePainter Test");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		final int seats = 1000;
		LOG.debug("Created Office for " + seats + " seats. Painting it...");
		final WorkSpacePainter painter = new WorkSpacePainter((WorkSpace) RoomType.Office.createRoom(seats), 40);
		add(painter);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(final String[] args) {
		final WorkplacePainterMain last = null;
		for (int i = 0; i <= 10; i++) {
			final WorkplacePainterMain w = new WorkplacePainterMain();
			// if (last != null) {
			// w.setLocation(last.getLocation().x, last.getLocation().y + last.getSize().height);
			// }
			// last = w;
		}
	}

}
