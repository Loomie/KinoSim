package de.outstare.kinosim.staff;

import de.outstare.kinosim.util.TimeRange;

/**
 * A jobassignment is the definition when a specific employee does a specific job. I.e. Mark works at the counter from 6 to 9 pm.
 *
 * @author Baret
 *
 */
public class JobAssignment implements Comparable<JobAssignment> {
	private TimeRange time;
	private Staff staff;
	private Job job;

	/**
	 * @param time
	 * @param staff
	 * @param job
	 */
	public JobAssignment(final TimeRange time, final Staff staff, final Job job) {
		super();
		this.time = time;
		this.staff = staff;
		this.job = job;
	}

	@Override
	public int compareTo(final JobAssignment o) {
		return time.getStart().compareTo(o.time.getStart());
	}

	/**
	 * @return the time
	 */
	public TimeRange getTime() {
		return time;
	}

	/**
	 * @return the staff
	 */
	public Staff getStaff() {
		return staff;
	}

	/**
	 * @return the job
	 */
	public Job getJob() {
		return job;
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
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + ((staff == null) ? 0 : staff.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		final JobAssignment other = (JobAssignment) obj;
		if (job != other.job) {
			return false;
		}
		if (staff == null) {
			if (other.staff != null) {
				return false;
			}
		} else if (!staff.equals(other.staff)) {
			return false;
		}
		if (time == null) {
			if (other.time != null) {
				return false;
			}
		} else if (!time.equals(other.time)) {
			return false;
		}
		return true;
	}

}
