package de.outstare.kinosim.staff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A SkillSet is a set of all skills each mapped to a value of 0-1.
 *
 * @author Baret
 *
 */
public class SkillSet {
	private HashMap<Skill, Double> skills;

	public SkillSet(final double customerService, final double organisation, final double dexterity, final double technology, final double hygiene,
			final double teamwork) {
		skills = new HashMap<>(Skill.values().length);
		setSkillValue(Skill.CustomerService, customerService);
		setSkillValue(Skill.Dexterity, dexterity);
		setSkillValue(Skill.Hygiene, hygiene);
		setSkillValue(Skill.Organisation, organisation);
		setSkillValue(Skill.Teamwork, teamwork);
		setSkillValue(Skill.Technology, technology);
	}

	/**
	 * Sets the value for the given skill and ensures that the value is between 0 and 1 (inclusive)
	 *
	 * @param skill
	 * @param value
	 */
	private void setSkillValue(final Skill skill, double value) {
		if (value < 0) {
			value = 0.;
		}
		if (value > 1) {
			value = 1.;
		}
		skills.put(skill, value);
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
