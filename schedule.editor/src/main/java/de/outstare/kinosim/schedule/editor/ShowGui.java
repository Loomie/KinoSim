package de.outstare.kinosim.schedule.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.time.temporal.ChronoUnit;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.outstare.kinosim.schedule.Show;

/**
 * A ShowGui displays a {@link Show} graphically.
 */
class ShowGui {
	private final Show	show;
	private JComponent	ui;

	ShowGui(final Show show) {
		this.show = show;
	}

	JComponent createUi() {
		// TODO
		final JLabel label = new JLabel(show.getStart() + " " + show.getFilm().getTitle());
		label.setOpaque(true);
		label.setBackground(Color.YELLOW);

		ui = label;
		return label;
	}

	/**
	 * Sets position and width according to start and length of show relative to parent.
	 */
	void updateBounds() {
		final long totalSecs = ChronoUnit.DAYS.getDuration().toMinutes() * 60;
		final long startInSec = show.getStart().toSecondOfDay();
		final long lengthInSec = show.getDuration().getSeconds();
		System.out.println("len: " + lengthInSec);
		final double relativeStart = startInSec / (double) totalSecs;
		final double relativeLength = lengthInSec / (double) totalSecs;

		final Dimension parentSize = (ui.getParent() == null) ? new Dimension(100, 10) : ui.getParent().getSize();
		System.out.println(parentSize);
		final int x = (int) (parentSize.width * relativeStart);
		final int y = 0;
		final int width = (int) (parentSize.width * relativeLength);
		final int height = parentSize.height;
		ui.setBounds(x, y, width, height);
		System.out.println("ShowGui.updateBounds() " + ui.getBounds());
	}
}
