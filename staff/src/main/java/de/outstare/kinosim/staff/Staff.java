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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((skills == null) ? 0 : skills.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Staff other = (Staff) obj;
		if (firstname == null) {
			if (other.firstname != null) {
				return false;
			}
		} else if (!firstname.equals(other.firstname)) {
			return false;
		}
		if (lastname == null) {
			if (other.lastname != null) {
				return false;
			}
		} else if (!lastname.equals(other.lastname)) {
			return false;
		}
		if (skills == null) {
			if (other.skills != null) {
				return false;
			}
		} else if (!skills.equals(other.skills)) {
			return false;
		}
		return true;
	}

}
