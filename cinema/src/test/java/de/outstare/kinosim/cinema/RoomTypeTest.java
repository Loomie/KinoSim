package de.outstare.kinosim.cinema;

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
		Room result;
		for (int i = 0; i < 10000; i++) {
			result = roomType.createRoom(seats);
			assertTrue("square meters: " + result, expectedMinimum <= result.getAllocatedSpace());
			assertTrue("square meters: " + result, result.getAllocatedSpace() <= expectedMaximum);
		}
	}

	@Test
	public void testGetWorkPlacesOffice() {
		testWorkplaces(RoomType.Office, 1000, 1, 4);
		testWorkplaces(RoomType.Office, 2000, 2, 8);
		testWorkplaces(RoomType.Office, 500, 1, 2);
		testWorkplaces(RoomType.Office, 100, 1, 1);
	}

	private void testWorkplaces(final RoomType roomType, final int seats, final int expectedMinimum, final int expectedMaximum) {
		int result;
		for (int i = 0; i < 10000; i++) {
			result = roomType.getRandomWorkPlaces(seats);
			assertTrue("workplaces: " + result, expectedMinimum <= result);
			assertTrue("workplaces: " + result, result <= expectedMaximum);
		}
	}

}
