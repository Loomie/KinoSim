package de.outstare.kinosim.staff;

import java.util.Iterator;
import java.util.Map.Entry;

public enum Job {
	Counter(80., 50., 80., 60., 70., 70,
			30., 20., 40., 10., 40., 10.),
	Entrance(90., 80., 80., 10., 50., 80.,
			20., 20., 30., 0., 30., 40.),
	Till(100., 50., 50., 80., 30., 40.,
			50., 10., 0., 50., 0., 10.),
	Office(70., 100., 50., 90., 20., 80.,
			40., 60., 10., 50., 0., 40.),
	Projection(10., 70., 80., 80., 10., 50.,
			0., 40., 40., 60., 0., 10.);

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
				sum += 100;
			} else if (value > min) {
				sum += ((needed - min) / (value - min)) * 100.;
			}
		}
		return sum / count;
	}
}
