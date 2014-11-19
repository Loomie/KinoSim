package de.outstare.kinosim.staff.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.outstare.kinosim.staff.Job;

public class JobPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -8778850391590389590L;

	public JobPanel(final Job job) {
		setLayout(new BorderLayout(1, 5));
		add(new JLabel(job.name()), BorderLayout.NORTH);
		add(new SkillSetPanel(job.getNeededSkills(), job.getMinimumSkills()), BorderLayout.CENTER);
	}
}
