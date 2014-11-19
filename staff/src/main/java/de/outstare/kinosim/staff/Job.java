package de.outstare.kinosim.staff;

import java.util.Iterator;
import java.util.Map.Entry;

public enum Job {
	Counter(.8, .5, .8, .6, .7, .7,
			.3, .2, .4, .1, .4, .1),
	Entrance(.9, .8, .8, .1, .5, .8,
			.2, .2, .3, 0., .3, .4),
	Till(1., .5, .5, .8, .3, .4,
			.5, .1, 0., .5, 0., .1),
	Office(.7, .1, .5, .9, .2, .8,
			.4, .6, .1, .5, 0., .4),
	Projection(.1, .7, .8, .8, .1, .5,
			0., 4., .4, .6, 0., .1);

	private SkillSet neededSkills;
	private SkillSet minimumSkills;

	private Job(final double customerService, final double organisation, final double dexterity, final double technology, final double hygiene,
			final double teamwork, final double minCustomerService, final double minOrganisation, final double minDexterity, final double minTechnology,
			final double minHygiene, final double minTeamwork) {
		neededSkills = new SkillSet(customerService, organisation, dexterity, technology, hygiene, teamwork);
		minimumSkills = new SkillSet(minCustomerService, minOrganisation, minDexterity, minTechnology, minHygiene, minTeamwork);
	}

	public double calculateQualification(final SkillSet skillSetToCalculate) {
		double sum = 0;
		final Iterator<Entry<Skill, Double>> i = skillSetToCalculate.getIterator();
		int count = 0;
		while (i.hasNext()) {
			count++;
			final Entry<Skill, Double> entry = i.next();
			final double value = entry.getValue().doubleValue();
			final double needed = neededSkills.getValueOf(entry.getKey());
			final double min = minimumSkills.getValueOf(entry.getKey());
			if (value >= needed) {
				sum += 1.;
			} else if (value > min) {
				sum += ((value - min) / (needed - min));
			}
		}
		return sum / count;
	}

	/**
	 * @return the neededSkills
	 */
	public SkillSet getNeededSkills() {
		return neededSkills;
	}

	/**
	 * @return the minimumSkills
	 */
	public SkillSet getMinimumSkills() {
		return minimumSkills;
	}
}
