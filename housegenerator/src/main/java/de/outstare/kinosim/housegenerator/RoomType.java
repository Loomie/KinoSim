package de.outstare.kinosim.housegenerator;

/**
 * A RoomType contains data about space usage in a movie theater.
 */
enum RoomType {
	CinemaHall(0.9, 1.5, SpaceUnit.PerSeat), Office(9, 25, SpaceUnit.PerWorkplace, new NumberRange(1, 4)), CashDesk(5, 10, SpaceUnit.PerWorkplace,
			new NumberRange(2, 5)), Counter(5, 16, SpaceUnit.PerWorkplace, new NumberRange(3, 6)), Foyer(0.5, 1, SpaceUnit.PerSeat), Storage(0.05,
			0.2, SpaceUnit.PerSeat), StaffRoom(2, 6, SpaceUnit.PerWorkplace, new NumberRange(6, 20)), Toilet(.04, .15, SpaceUnit.PerSeat);

	private enum SpaceUnit {
		PerSeat, PerWorkplace
	}

	private final NumberRange			spacePerUnit;
	private final RoomType.SpaceUnit	spaceUnit;
	private final NumberRange			workspacesPerThousandSeats;

	private RoomType(final double minimumSpacePerUnit, final double maximumSpacePerUnit, final RoomType.SpaceUnit spaceUnit) {
		this(minimumSpacePerUnit, maximumSpacePerUnit, spaceUnit, null);
	}

	private RoomType(final double minimumSpacePerUnit, final double maximumSpacePerUnit, final RoomType.SpaceUnit spaceUnit,
			final NumberRange workspacesPerThousandSeats) {
		spacePerUnit = new NumberRange(minimumSpacePerUnit, maximumSpacePerUnit);
		this.spaceUnit = spaceUnit;
		this.workspacesPerThousandSeats = workspacesPerThousandSeats;
	}

	double getSquareMeters(final int seats) {
		final double space = spacePerUnit.getRandomValue();
		if (spaceUnit == SpaceUnit.PerSeat) {
			return space * seats;
		} else if (spaceUnit == SpaceUnit.PerWorkplace) {
			return space * getRandomWorkSpaces(seats);
		}
		throw new IllegalStateException("Unknown space unit " + spaceUnit);
	}

	int getRandomWorkSpaces(final int seats) {
		final int workspaces = (int) Math.round(workspacesPerThousandSeats.getRandomValue() * seats / 1000.0);
		return Math.max(workspaces, 1);
	}
}