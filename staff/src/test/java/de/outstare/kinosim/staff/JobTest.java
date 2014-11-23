package de.outstare.kinosim.staff;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JobTest {

	Job	job;

	@Before
	public void setUp() throws Exception {
		job = Job.Counter;
	}

	@Test
	public void testOverqualified() {
		final SkillSet overQualifiedSkillSet = new SkillSet(.9, .6, .9, .8, 1., 1.);
		assertEquals(1., job.calculateQualification(overQualifiedSkillSet), 0.);
	}

	@Test
	public void testUnerqualified() {
		final SkillSet underQualifiedSkillSet = new SkillSet(0, 0, 0, 0, 0, 0);
		assertEquals(0., job.calculateQualification(underQualifiedSkillSet), 0);
	}

	@Test
	public void testExactlyMinimalQualified() {
		final SkillSet minimalQualifiedSkillSet = new SkillSet(.3, .2, .4, .1, .4, .1);
		assertEquals(0., job.calculateQualification(minimalQualifiedSkillSet), 0);
	}

	@Test
	public void testPerfectlyQualified() {
		final SkillSet perfectlyQualifiedSkillSet = new SkillSet(.8, .5, .8, .6, .7, .7);
		assertEquals(1., job.calculateQualification(perfectlyQualifiedSkillSet), 0);
	}

	@Test
	public void test50PercentQualified() {
		final SkillSet halfQualifiedSkillSet = new SkillSet(.55, .35, .6, .35, .55, .4);
		assertEquals(.5, job.calculateQualification(halfQualifiedSkillSet), 0.0000000000000002);
	}

	@Test
	public void testPerfectQualifiedWithMissingSkill() {
		final SkillSet perfectlyQualifiedWithMissingSkill = new SkillSet(.8, .199999, .8, .6, .7, .7);
		assertEquals(.83333277777, job.calculateQualification(perfectlyQualifiedWithMissingSkill), 0.00000000001);
	}

	@Test
	public void testPartlyNotMinimalQualified() {
		final SkillSet halfQualifiedWithMissingSkills = new SkillSet(.2, .1, .6, .35, .55, .4);
		assertEquals(.244444, job.calculateQualification(halfQualifiedWithMissingSkills), 0.000001);
	}
}
