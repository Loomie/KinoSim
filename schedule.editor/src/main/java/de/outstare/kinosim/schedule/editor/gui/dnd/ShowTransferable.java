package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import de.outstare.kinosim.schedule.Show;

public class ShowTransferable implements Transferable {
	public static final DataFlavor SHOW_DATA_FLAVOR = new DataFlavor(Show.class, Show.class.getSimpleName());

	private final Show show;

	public ShowTransferable(final Show movie) {
		show = movie;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { SHOW_DATA_FLAVOR };
	}

	@Override
	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return SHOW_DATA_FLAVOR.equals(flavor);
	}

	@Override
	public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (!isDataFlavorSupported(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		return show;
	}

}