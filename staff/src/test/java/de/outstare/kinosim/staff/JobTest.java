package de.outstare.kinosim.staff;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class JobTest {

	Job job;

	@Before
	public void setUp() throws Exception {
		job = Job.Counter;
	}

	@Test
	public void testOverqualified() {
		final SkillSet overQualifiedSkillSet = new SkillSet(90, 60, 90, 80, 100, 100);
		assertEquals(100., job.calculateQualification(overQualifiedSkillSet), 0.);
	}

	@Test
	public void testUnerqualified() {
		final SkillSet underQualifiedSkillSet = new SkillSet(0, 0, 0, 0, 0, 0);
		assertEquals(0., job.calculateQualification(underQualifiedSkillSet), 0);
	}

	@Test
	public void testExactlyMinimalQualified() {
		final SkillSet underQualifiedSkillSet = new SkillSet(30, 20, 40, 10, 40, 10);
		assertEquals(0., job.calculateQualification(underQualifiedSkillSet), 0);
	}

	@Test
	public void testPerfectlyQualified() {
		final SkillSet underQualifiedSkillSet = new SkillSet(80, 50, 80, 60, 70, 70);
		assertEquals(100., job.calculateQualification(underQualifiedSkillSet), 0);
	}

	@Test
	public void test50PercentQualified() {
		final SkillSet underQualifiedSkillSet = new SkillSet(55, 35, 60, 35, 55, 40);
		assertEquals(50., job.calculateQualification(underQualifiedSkillSet), 0);
	}
}
