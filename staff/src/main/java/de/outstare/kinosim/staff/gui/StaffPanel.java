package de.outstare.kinosim.staff.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.VerticalLayout;

import de.outstare.kinosim.staff.Staff;

public class StaffPanel extends JXTitledPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 3859328180950107378L;

	/**
	 * @param staff
	 */
	public StaffPanel(final Staff staff) {
		super(staff.getFullName());
		final JXCollapsiblePane collapsible = new JXCollapsiblePane();
		final SkillSetPanel skillsPanel = new SkillSetPanel(staff.getSkills());
		collapsible.add(skillsPanel);
		final JButton toggle = new JButton(collapsible.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		toggle.setText("Show/Hide skills");
		final JPanel togglePanel = new JPanel(new VerticalLayout());
		togglePanel.add(toggle);
		togglePanel.add(collapsible);
		add(togglePanel, BorderLayout.CENTER);
	}
}
