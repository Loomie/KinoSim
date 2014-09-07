package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.Show;

/**
 * A ScheduleGui displays all scheduled {@link Show}s in a single row (shows may overlap!).
 */
class ScheduleGui {
	private final Schedule				schedule;
	private final Collection<ShowGui>	showGuis	= new ArrayList<>();
	private Rectangle					dropPreview	= null;

	ScheduleGui(final Schedule schedule) {
		this.schedule = schedule;
	}

	JComponent createUi() {
		final JPanel hallRow = new JPanel(null) {
			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				if (dropPreview != null) {
					g.setColor(Color.DARK_GRAY);
					g.drawRect(dropPreview.x, dropPreview.y, dropPreview.width, dropPreview.height);
				}
			}
		};
		hallRow.setBackground(Color.LIGHT_GRAY);

		for (final Show show : schedule) {
			final ShowGui showGui = new ShowGui(show);
			hallRow.add(showGui.createUi());
			showGui.updateBounds();
			showGuis.add(showGui);
		}

		hallRow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				for (final ShowGui showGui : showGuis) {
					showGui.updateBounds();
				}
			}
		});
		hallRow.addPropertyChangeListener("dropRectangle", evt -> {
			if (evt.getNewValue() instanceof Rectangle) {
				final int oldX = dropPreview == null ? -1 : dropPreview.x;
				final int dropX = ((Rectangle) evt.getNewValue()).x;
				final long dropWithInMinutes = ((Rectangle) evt.getNewValue()).width;
				final int width = ShowGui.minutesToPixels(dropWithInMinutes, hallRow);
				dropPreview = new Rectangle(dropX, 0, width, hallRow.getHeight());
				if (oldX != dropX) {
					hallRow.repaint();
				}
			}
		});

		return hallRow;
	}
}
