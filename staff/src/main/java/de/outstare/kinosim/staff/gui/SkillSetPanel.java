package de.outstare.kinosim.staff.gui;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import de.outstare.kinosim.guituil.PercentageBar;
import de.outstare.kinosim.guituil.PercentageBarWithMarker;
import de.outstare.kinosim.guituil.TrafficLightsPercentageBar;
import de.outstare.kinosim.staff.Skill;
import de.outstare.kinosim.staff.SkillSet;

public class SkillSetPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -1502423084448746733L;

	public SkillSetPanel(final SkillSet skillset) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final Iterator<Entry<Skill, Double>> i = skillset.getIterator();
		while (i.hasNext()) {
			final Entry<Skill, Double> next = i.next();
			final PercentageBar bar = new TrafficLightsPercentageBar(next.getValue().doubleValue());
			bar.setBorder(new TitledBorder(next.getKey().name()));
			add(bar);
		}
	}

	public SkillSetPanel(final SkillSet neededSkills, final SkillSet minimumSkills) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final Iterator<Entry<Skill, Double>> i = neededSkills.getIterator();
		while (i.hasNext()) {
			final Entry<Skill, Double> next = i.next();
			final PercentageBarWithMarker bar = new PercentageBarWithMarker(next.getValue().doubleValue(), Color.BLUE, minimumSkills.getValueOf(next
					.getKey()),
					Color.RED);
			bar.setBorder(new TitledBorder(next.getKey().name()));
			add(bar);
		}
	}
}
