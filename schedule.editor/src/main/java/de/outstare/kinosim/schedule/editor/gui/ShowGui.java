package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.schedule.editor.gui.dnd.StartDragMoveMouseListener;
import de.outstare.kinosim.schedule.editor.gui.dnd.ShowDragTransferHandler;
import de.outstare.kinosim.schedule.editor.gui.dnd.ShowProvider;
import de.outstare.kinosim.util.TimeRange;

/**
 * A ShowGui displays a {@link Show} graphically.
 */
class ShowGui implements ShowProvider {
	private final Show	show;
	private JComponent	ui;
	private JLabel		label;

	ShowGui(final Show show) {
		this.show = show;
	}

	@Override
	public Show getShow() {
		return show;
	}

	JComponent createUi() {
		label = new JLabel();
		updateLabel();

		final JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(label);
		panel.add(new JLabel(show.getFilm().getTitle()));
		ui = panel;
		ui.setOpaque(true);
		ui.setBackground(Color.YELLOW);
		ui.setBorder(new LineBorder(Color.BLACK));

		// drag
		ui.setTransferHandler(new ShowDragTransferHandler(this));
		ui.addMouseListener(new StartDragMoveMouseListener());

		return ui;
	}

	private void updateLabel() {
		label.setText(show.getStart().toString());
	}

	/**
	 * Sets position and width according to start and length of show relative to parent.
	 */
	void updateBounds(final TimeRange visibleTime) {
		final long totalSecs = visibleTime.getDuration().getSeconds();
		final long startInSec = visibleTime.getStart().until(show.getStart(), ChronoUnit.SECONDS);
		final long lengthInMins = show.getDuration().toMinutes();
		final double relativeStart = startInSec / (double) totalSecs;

		final Dimension parentSize = (ui.getParent() == null) ? new Dimension(100, 10) : ui.getParent().getSize();
		final Insets insets = ui.getParent().getInsets();
		parentSize.setSize(parentSize.width - insets.left - insets.right, parentSize.height - insets.top - insets.bottom);
		final int x = (int) (parentSize.width * relativeStart);
		final int y = insets.top;
		final int width = minutesToPixels(lengthInMins, ui.getParent(), visibleTime.getDuration());
		final int height = parentSize.height;
		ui.setBounds(x, y, width, height);
		ui.validate();
	}

	static int minutesToPixels(final long minutes, final Container parent, final Duration visibleTime) {
		final long totalMins = visibleTime.toMinutes();
		final double relativeLength = minutes / (double) totalMins;

		final int parentWidth = (parent == null) ? 100 : parent.getWidth();
		return (int) (parentWidth * relativeLength);
	}
}
