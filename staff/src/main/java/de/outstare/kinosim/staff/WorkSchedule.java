package de.outstare.kinosim.staff;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.outstare.kinosim.util.TimeRange;

/**
 * The workschedule is used to schedule the jobassignments for one job.
 *
 * @author Baret
 *
 */
public class WorkSchedule {
	private Job job;
	private int slotsPerDay;
	private Map<DayOfWeek, List<JobAssignment>> assignments;

	/**
	 * @param job
	 *            The job that is scheduled with this workschedule
	 * @param slotsPerDay
	 *            The numer of max. parallel working staff each day
	 */
	public WorkSchedule(final Job job, final int slotsPerDay) {
		super();
		this.job = job;
		this.slotsPerDay = slotsPerDay;
		assignments = new HashMap<DayOfWeek, List<JobAssignment>>();
		for (final DayOfWeek d : DayOfWeek.values()) {
			assignments.put(d, new ArrayList<JobAssignment>());
		}
	}

	/**
	 * Puts the given staff on the given timeslot, when there is a free slot.
	 *
	 * @param time
	 * @param staff
	 * @return true if the timeslot is free and the staff has been put there
	 */
	public boolean putStaff(final DayOfWeek day, final TimeRange time, final Staff staff) {
		final boolean slotFree = isSlotFree(day, time);
		if (slotFree) {
			assignments.get(day).add(new JobAssignment(time, staff, job));
		}
		return slotFree;
	}

	public void removeStaff(final DayOfWeek day, final TimeRange time, final Staff staff) {
		assignments.get(day).remove(new JobAssignment(time, staff, job));
	}

	/**
	 * @param day
	 * @param time
	 * @return true if the given timerange has a free slot for an emlpoyee
	 */
	public boolean isSlotFree(final DayOfWeek day, final TimeRange time) {
		int freeSlots = slotsPerDay;
		for (final JobAssignment assignment : assignments.get(day)) {
			if (assignment.getTime().overlaps(time)) {
				freeSlots--;
			}
		}
		return freeSlots > 0;
	}
}
