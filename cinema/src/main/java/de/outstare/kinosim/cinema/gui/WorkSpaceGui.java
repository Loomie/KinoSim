package de.outstare.kinosim.cinema.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.outstare.kinosim.cinema.WorkSpace;

public class WorkSpaceGui {
	private WorkSpace workSpace;

	public WorkSpaceGui(final WorkSpace workSpace) {
		this.workSpace = workSpace;
	}

	public JComponent createUi() {

		return new JPanel();
	}
}
