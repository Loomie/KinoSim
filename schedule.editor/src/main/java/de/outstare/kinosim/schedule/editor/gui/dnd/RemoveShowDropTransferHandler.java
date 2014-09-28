package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.commons.lang3.ArrayUtils;

import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.schedule.editor.ScheduleEditor;

/**
 * A RemoveShowDropTransferHandler removes a dropped show from the given editor.
 */
public class RemoveShowDropTransferHandler extends TransferHandler {
	private static final long		serialVersionUID	= 8920909801506422537L;

	private final ScheduleEditor	editor;

	public RemoveShowDropTransferHandler(final ScheduleEditor editor) {
		this.editor = editor;
	}

	@Override
	public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
		return ArrayUtils.contains(transferFlavors, ShowTransferable.SHOW_DATA_FLAVOR);
	}

	@Override
	public boolean importData(final JComponent comp, final Transferable t) {
		try {
			final Show show = (Show) t.getTransferData(ShowTransferable.SHOW_DATA_FLAVOR);
			editor.remove(show);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}