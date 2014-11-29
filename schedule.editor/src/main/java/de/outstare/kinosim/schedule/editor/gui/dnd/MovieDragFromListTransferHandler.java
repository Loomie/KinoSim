package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import de.outstare.kinosim.movie.Movie;

/**
 * A MovieDragFromListTransferHandler allows dragging of {@link Movie}s from a {@link JList}. The movies will be copied.
 */
public class MovieDragFromListTransferHandler extends TransferHandler {
	private static final long serialVersionUID = -1383105057463258054L;

	@Override
	public int getSourceActions(final JComponent c) {
		return COPY;
	}

	@Override
	protected Transferable createTransferable(final JComponent c) {
		if (c instanceof JList) {
			@SuppressWarnings("unchecked")
			final Movie movie = ((JList<Movie>) c).getSelectedValue();
			return new MovieTransferable(movie);
		}
		return super.createTransferable(c);
	}

}