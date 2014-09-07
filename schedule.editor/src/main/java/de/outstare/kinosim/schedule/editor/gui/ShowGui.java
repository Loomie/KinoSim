package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.time.temporal.ChronoUnit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

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
		final String labelText = show.getStart() + " " + show.getFilm().getTitle();
		// TODO
		final JLabel label = new JLabel(labelText);
		label.setBorder(new LineBorder(Color.BLACK));
		label.setToolTipText(labelText);
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
		final long lengthInMins = show.getDuration().toMinutes();
		final double relativeStart = startInSec / (double) totalSecs;

		final Dimension parentSize = (ui.getParent() == null) ? new Dimension(100, 10) : ui.getParent().getSize();
		final Insets insets = ui.getParent().getInsets();
		parentSize.setSize(parentSize.width - insets.left - insets.right, parentSize.height - insets.top - insets.bottom);
		final int x = (int) (parentSize.width * relativeStart);
		final int y = insets.top;
		final int width = minutesToPixels(lengthInMins, ui.getParent());
		final int height = parentSize.height;
		ui.setBounds(x, y, width, height);
	}

	static int minutesToPixels(final long minutes, final Container parent) {
		final long totalMins = ChronoUnit.DAYS.getDuration().toMinutes();
		final double relativeLength = minutes / (double) totalMins;

		final int parentWidth = (parent == null) ? 100 : parent.getWidth();
		return (int) (parentWidth * relativeLength);
	}
}
