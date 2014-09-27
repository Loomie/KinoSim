package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * A StartDragMoveMouseListener starts a drag {@link TransferHandler#MOVE move operation} when the mouse is pressed.
 */
public class StartDragMoveMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(final MouseEvent event) {
		if (event.getSource() instanceof JComponent) {
			final JComponent component = (JComponent) event.getSource();
			final TransferHandler handler = component.getTransferHandler();
			if (handler != null) {
				handler.exportAsDrag(component, event, TransferHandler.MOVE);
			}
		}
	}
}