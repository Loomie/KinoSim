package de.outstare.kinosim.staff;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import de.outstare.kinosim.util.TimeRange;

public class WorkScheduleTest {

	private static final DayOfWeek DAY = DayOfWeek.MONDAY;
	private static final TimeRange TIMESLOT = new TimeRange(LocalTime.of(15, 0), LocalTime.of(16, 0));
	WorkSchedule schedule;

	/**
	 * @return
	 */
	private boolean putRandomStaff() {
		return schedule.putStaff(DAY, TIMESLOT, Staff.generateRandomStaff());
	}

	@Before
	public void setUp() throws Exception {
		schedule = new WorkSchedule(Job.Counter, 2);
	}

	@Test
	public void testAdd1Staff() {
		assertTrue(putRandomStaff());
	}

	@Test
	public void testAdd2Staff() {
		putRandomStaff();
		assertTrue(putRandomStaff());
	}

	@Test
	public void testAdd3Staff() {
		putRandomStaff();
		putRandomStaff();
		assertFalse(putRandomStaff());
	}

	@Test
	public void testPutAndRemoveStaff() {
		final Staff staff = Staff.generateRandomStaff();
		final WorkSchedule testSchedule = new WorkSchedule(Job.Entrance, 1);
		assertTrue(testSchedule.putStaff(DAY, TIMESLOT, staff));
		assertFalse(testSchedule.isSlotFree(DAY, TIMESLOT));
		testSchedule.removeStaff(DAY, TIMESLOT, staff);
		assertTrue(testSchedule.isSlotFree(DAY, TIMESLOT));
	}

}
