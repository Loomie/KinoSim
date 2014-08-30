package de.outstare.kinosim.housegenerator;

import static org.junit.Assert.*;

import org.junit.Test;

public class RoomTypeTest {

	@Test
	public void testGetSquareMetersStorage() {
		testArea(RoomType.Storage, 1000, 50, 200);
		testArea(RoomType.Storage, 2000, 100, 400);
		testArea(RoomType.Storage, 100, 5, 20);
		testArea(RoomType.Storage, 10, 0.5, 2);
	}

	private void testArea(final RoomType roomType, final int seats, final double expectedMinimum, final double expectedMaximum) {
		double result;
		for (int i = 0; i < 10000; i++) {
			result = roomType.getSquareMeters(seats);
			assertTrue("square meters: " + result, expectedMinimum <= result);
			assertTrue("square meters: " + result, result <= expectedMaximum);
		}
	}

	@Test
	public void testGetWorkSpacesOffice() {
		testWorkspaces(RoomType.Office, 1000, 1, 4);
		testWorkspaces(RoomType.Office, 2000, 2, 8);
		testWorkspaces(RoomType.Office, 500, 1, 2);
		testWorkspaces(RoomType.Office, 100, 1, 1);
	}

	private void testWorkspaces(final RoomType roomType, final int seats, final int expectedMinimum, final int expectedMaximum) {
		int result;
		for (int i = 0; i < 10000; i++) {
			result = roomType.getRandomWorkSpaces(seats);
			assertTrue("workspaces: " + result, expectedMinimum <= result);
			assertTrue("workspaces: " + result, result <= expectedMaximum);
		}
	}

}
