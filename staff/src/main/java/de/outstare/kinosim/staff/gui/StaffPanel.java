package de.outstare.kinosim.staff.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXCollapsiblePane;

import de.outstare.kinosim.staff.Staff;

public class StaffPanel extends JPanel {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 3859328180950107378L;

	/**
	 * @param staff
	 */
	public StaffPanel(final Staff staff) {
		super();
		setLayout(new BorderLayout(1, 5));
		add(new JLabel(staff.getFullName()), BorderLayout.NORTH);
		// label.setIcon(UIManager.getIcon("Tree.expandedIcon"));
		// label.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(final MouseEvent e) {
		// final boolean isCollapsed = collapsible.isCollapsed();
		// collapsible.setCollapsed(!isCollapsed);
		// final String iconKey = isCollapsed ? "Tree.expandedIcon" : "Tree.collapsedIcon";
		// label.setIcon(UIManager.getIcon(iconKey));
		// }
		// });
		final JXCollapsiblePane collapsible = new JXCollapsiblePane();
		final SkillSetPanel skillsPanel = new SkillSetPanel(staff.getSkills());
		skillsPanel.setBorder(new LineBorder(Color.BLACK));
		collapsible.add(skillsPanel);
		final JButton toggle = new JButton(collapsible.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		toggle.setText("Show/Hide skills");
		add(toggle, BorderLayout.CENTER);
		add(collapsible, BorderLayout.SOUTH);
	}
}
