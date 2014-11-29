package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import de.outstare.kinosim.movie.Movie;

public class MovieTransferable implements Transferable {
	public static final DataFlavor MOVIE_DATA_FLAVOR = new DataFlavor(Movie.class, Movie.class.getSimpleName());

	private final Movie movie;

	public MovieTransferable(final Movie movie) {
		this.movie = movie;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { MOVIE_DATA_FLAVOR };
	}

	@Override
	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return MOVIE_DATA_FLAVOR.equals(flavor);
	}

	@Override
	public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (!isDataFlavorSupported(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		return movie;
	}

}