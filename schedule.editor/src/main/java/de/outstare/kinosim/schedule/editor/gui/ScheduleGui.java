package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TooManyListenersException;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.schedule.editor.gui.dnd.ScheduleDropTransferHandler;
import de.outstare.kinosim.util.TimeRange;

/**
 * A ScheduleGui displays all scheduled {@link Show}s in a single row (shows may overlap!).
 */
class ScheduleGui {
	private final Schedule schedule;
	private final Collection<ShowGui> showGuis = new ArrayList<>();
	private Rectangle dropPreview = null;

	protected DropTargetListener dropListener = new DropTargetAdapter() {
		@Override
		public void drop(final DropTargetDropEvent dtde) {
			dropPreview = null;
			dtde.getDropTargetContext().getComponent().repaint();
		}

		@Override
		public void dragExit(final java.awt.dnd.DropTargetEvent dte) {
			dropPreview = null;
			dte.getDropTargetContext().getComponent().repaint();
		};
	};

	private final TimeRange visibleTime;

	ScheduleGui(final Schedule schedule, final TimeRange visibleTime) {
		this.schedule = schedule;
		this.visibleTime = visibleTime;
	}

	JComponent createUi() {
		final JPanel hallRow = new JPanel(null) {
			private static final long serialVersionUID = -6965802305925904974L;

			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				if (dropPreview != null) {
					g.setColor(Color.DARK_GRAY);
					g.drawRect(dropPreview.x, dropPreview.y, dropPreview.width, dropPreview.height);
				}
			}

			@Override
			public synchronized void setDropTarget(final DropTarget dt) {
				final DropTarget old = getDropTarget();
				if (old != null) {
					old.removeDropTargetListener(dropListener);
				}
				super.setDropTarget(dt);
				if (dt != null) {
					try {
						dt.addDropTargetListener(dropListener);
					} catch (final TooManyListenersException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public Dimension getMinimumSize() {
				final int minWidth = visibleTime.toHours() * 10;
				int minHeight = 0;
				for (final Component child : getComponents()) {
					final Dimension childMinSize = child.getMinimumSize();
					minHeight = Math.max(minHeight, childMinSize.height);
				}
				return new Dimension(minWidth, minHeight);
			}

			@Override
			public Dimension getPreferredSize() {
				int minWidth = 0;
				int minHeight = 0;
				for (final Component child : getComponents()) {
					final Dimension childMinSize = child.getPreferredSize();
					minWidth += childMinSize.width;
					minHeight = Math.max(minHeight, childMinSize.height);
				}
				return new Dimension(minWidth, minHeight);
			}
		};
		hallRow.setBackground(Color.LIGHT_GRAY);

		for (final Show show : schedule) {
			final ShowGui showGui = new ShowGui(show);
			hallRow.add(showGui.createUi());
			showGui.updateBounds(visibleTime);
			showGuis.add(showGui);
		}

		hallRow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				for (final ShowGui showGui : showGuis) {
					showGui.updateBounds(visibleTime);
				}
			}
		});
		hallRow.addPropertyChangeListener(ScheduleDropTransferHandler.DROP_AREA_PROPERTY, evt -> {
			if (evt.getNewValue() instanceof Rectangle) {
				final int oldX = dropPreview == null ? -1 : dropPreview.x;
				final int dropX = ((Rectangle) evt.getNewValue()).x;
				final long dropWithInMinutes = ((Rectangle) evt.getNewValue()).width;
				final int width = ShowGui.minutesToPixels(dropWithInMinutes, hallRow, visibleTime.getDuration());
				dropPreview = new Rectangle(dropX, 0, width, hallRow.getHeight());
				if (oldX != dropX) {
					hallRow.repaint();
				}
			} else {
				dropPreview = null;
			}
		});

		return hallRow;
	}
}
