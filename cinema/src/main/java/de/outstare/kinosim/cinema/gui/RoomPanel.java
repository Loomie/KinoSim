package de.outstare.kinosim.cinema.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import de.outstare.kinosim.cinema.Room;

public class RoomPanel extends JPanel implements ComponentListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 2279094571989111889L;
	private RoomPainter painter;

	public RoomPanel(final Room room) {
		switch (room.getType()) {
		case CashDesk:
			break;
		case CinemaHall:
			break;
		case Counter:
			break;
		case Foyer:
			break;
		case Office:
			painter = new WorkSpacePainter(room, 20);
			break;
		case StaffRoom:
			break;
		case Storage:
			break;
		case Toilet:
			break;
		default:
			break;
		}
		addComponentListener(this);
		setSize(painter.mToPixels(painter.backgroundWidth), painter.mToPixels(painter.backgroundHeight));
		setPreferredSize(getSize());
		setMinimumSize(new Dimension((int) painter.backgroundWidth, (int) painter.backgroundHeight));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
	@Override
	protected void paintComponent(final Graphics g) {
		painter.paintRoom(g);
	}

	@Override
	public void componentResized(final ComponentEvent e) {
		final int oldPixelsPerMeter = painter.pixelsPerMeter;
		painter.setPixelsPerMeter((int) (getWidth() / (double) getHeight() < painter.backgroundWidth / painter.backgroundHeight ? getWidth()
				/ painter.backgroundWidth : getHeight() / painter.backgroundHeight));
		if (painter.pixelsPerMeter < painter.backgroundHeight || painter.pixelsPerMeter < painter.backgroundWidth) {
			painter.pixelsPerMeter = oldPixelsPerMeter;
		}
	}

	@Override
	public void componentMoved(final ComponentEvent e) {

	}

	@Override
	public void componentShown(final ComponentEvent e) {

	}

	@Override
	public void componentHidden(final ComponentEvent e) {

	}

}
