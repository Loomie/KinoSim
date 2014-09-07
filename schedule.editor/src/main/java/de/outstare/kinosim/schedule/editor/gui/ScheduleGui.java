package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Color;
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

	ScheduleGui(final Schedule schedule) {
		this.schedule = schedule;
	}

	JComponent createUi() {
		final JPanel showRow = new JPanel(null);
		showRow.setBackground(Color.LIGHT_GRAY);

		for (final Show show : schedule) {
			final ShowGui showGui = new ShowGui(show);
			showRow.add(showGui.createUi());
			showGui.updateBounds();
			showGuis.add(showGui);
		}

		showRow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				for (final ShowGui showGui : showGuis) {
					showGui.updateBounds();
				}
			}
		});

		return showRow;
	}
}
