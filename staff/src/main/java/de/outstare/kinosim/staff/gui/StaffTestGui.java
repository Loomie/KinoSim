package de.outstare.kinosim.staff.gui;

import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;

import de.outstare.kinosim.staff.Job;
import de.outstare.kinosim.staff.NameGenerator;
import de.outstare.kinosim.staff.Staff;

public class StaffTestGui {
	public static void main(final String[] args) {
		final NameGenerator gen = new NameGenerator();
		final int staffCount = 3;
		final JFrame frame = new JFrame("Staff");
		final Staff[] staff = new Staff[staffCount];
		frame.setLayout(new GridLayout(staffCount + 1, 6));
		for (int y = 0; y <= staffCount; y++) {
			for (int x = 0; x < 6; x++) {
				if (y == 0) {
					if (x == 0) {
						frame.add(new JLabel("Jobs->"));
					} else {
						frame.add(new JobPanel(Job.values()[x - 1]));
					}
				} else {
					if (x == 0) {
						staff[y - 1] = Staff.generateRandomStaff();
						frame.add(new StaffPanel(staff[y - 1]));
					} else {
						final double qualification = Job.values()[x - 1].calculateQualification(staff[y - 1].getSkills());
						final DecimalFormat format = new DecimalFormat("#.##");
						frame.add(new JLabel(format.format(qualification * 100.) + " % qualified"));
					}
				}
			}
		}
		// final Staff staff = new Staff(gen.getRandomFirstname(), gen.getRandomLastname(), new SkillSet(0.08, 0.02, 0.04, 0.06, 0.12, 0.1));
		// frame.add(new StaffPanel(staff));
		// frame.add(new StaffPanel(Staff.generateRandomStaff()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
