package de.outstare.kinosim.movie;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Period;

import org.junit.Test;

public class SimpleMovieTest {

	@Test
	public void testGetTimeSinceRelease() {
		final LocalDate release = LocalDate.now().minusDays(7);
		assertEquals(Period.ofDays(7), new SimpleMovie(null, null, 0, null, null, null, release).getTimeSinceRelease());
	}

}
