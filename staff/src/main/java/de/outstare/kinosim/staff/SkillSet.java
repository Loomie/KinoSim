package de.outstare.kinosim.staff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A SkillSet is a set of all skills each mapped to a value of 0-100.
 *
 * @author Baret
 *
 */
public class SkillSet {
	private HashMap<Skill, Double> skills;

	public SkillSet(final double customerService, final double organisation, final double dexterity, final double technology, final double hygiene,
			final double teamwork) {
		skills = new HashMap<>(Skill.values().length);
		skills.put(Skill.CustomerService, customerService);
		skills.put(Skill.Dexterity, dexterity);
		skills.put(Skill.Hygiene, hygiene);
		skills.put(Skill.Organisation, organisation);
		skills.put(Skill.Teamwork, teamwork);
		skills.put(Skill.Technology, technology);
	}

	public SkillSet() {
		this(0, 0, 0, 0, 0, 0);
	}

	public double getValueOf(final Skill skill) {
		return skills.get(skill).doubleValue();
	}

	public Iterator<Entry<Skill, Double>> getIterator() {
		return skills.entrySet().iterator();
	}
}
