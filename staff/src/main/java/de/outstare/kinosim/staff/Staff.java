package de.outstare.kinosim.staff;

import de.outstare.kinosim.util.Randomness;

public class Staff {
	private String firstname, lastname;
	private SkillSet skills;

	/**
	 * @param firstname
	 * @param lastname
	 * @param skills
	 */
	public Staff(final String firstname, final String lastname, final SkillSet skills) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.skills = skills;
	}

	public static Staff generateRandomStaff() {
		final NameGenerator generator = new NameGenerator();
		final SkillSet skills = new SkillSet(Randomness.nextDouble(), Randomness.nextDouble(), Randomness.nextDouble(), Randomness.nextDouble(),
				Randomness.nextDouble(), Randomness.nextDouble());
		return new Staff(generator.getRandomFirstname(), generator.getRandomLastname(), skills);
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @return the skills
	 */
	public SkillSet getSkills() {
		return skills;
	}

	/**
	 * Gives the full name of this person.
	 *
	 * @return the firstname and lastname concatenated, separated by a space
	 */
	public String getFullName() {
		return getFirstname() + " " + getLastname();
	}

}
