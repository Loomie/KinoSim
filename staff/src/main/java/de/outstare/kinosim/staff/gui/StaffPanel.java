package de.outstare.kinosim.staff.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import de.outstare.kinosim.staff.Staff;

public class StaffPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 3859328180950107378L;

	/**
	 * @param staff
	 */
	public StaffPanel(final Staff staff) {
		super();
		setLayout(new BorderLayout(1, 5));
		add(new JLabel(staff.getFullName()), BorderLayout.NORTH);
		final SkillSetPanel skillsPanel = new SkillSetPanel(staff.getSkills());
		skillsPanel.setBorder(new LineBorder(Color.BLACK));
		add(skillsPanel, BorderLayout.CENTER);
	}
}
