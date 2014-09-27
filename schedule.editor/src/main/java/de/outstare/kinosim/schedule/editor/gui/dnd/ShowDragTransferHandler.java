package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import de.outstare.kinosim.schedule.Show;

/**
 * A ShowDragTransferHandler allows dragging of {@link Show}s.
 */
public class ShowDragTransferHandler extends TransferHandler {
	private static final long	serialVersionUID	= 2029051768749350087L;

	private final ShowProvider	provider;

	public ShowDragTransferHandler(final ShowProvider provider) {
		this.provider = provider;
	}

	@Override
	public int getSourceActions(final JComponent c) {
		return COPY_OR_MOVE;
	}

	@Override
	protected Transferable createTransferable(final JComponent c) {
		return new ShowTransferable(provider.getShow());
	}

	@Override
	protected void exportDone(final JComponent source, final Transferable data, final int action) {
		if (action == MOVE) {
			source.getParent().remove(source);
		}
	}
}