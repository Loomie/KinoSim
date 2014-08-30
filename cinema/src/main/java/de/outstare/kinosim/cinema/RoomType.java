package de.outstare.kinosim.cinema;

import de.outstare.kinosim.util.NumberRange;

/**
 * A RoomType contains data about space usage in a movie theater.
 */
public enum RoomType {
	CinemaHall(0.9, 1.5, SpaceUnit.PerSeat) {
		@Override
		public CinemaHall createRoom(final int seats) {
			final double space = spacePerUnit.getRandomValue();
			final double allocatedSpace = space * seats;
			return new FixedSizeCinemaHall(allocatedSpace, seats);
		}
	},
	Office(9, 25, SpaceUnit.PerWorkplace, new NumberRange(1, 4)),
	CashDesk(5, 10, SpaceUnit.PerWorkplace, new NumberRange(2, 5)),
	Counter(5, 16, SpaceUnit.PerWorkplace, new NumberRange(3, 6)),
	Foyer(0.5, 1, SpaceUnit.PerSeat),
	Storage(0.05, 0.2, SpaceUnit.PerSeat),
	StaffRoom(2, 6, SpaceUnit.PerWorkplace, new NumberRange(6, 20)),
	Toilet(.04, .15, SpaceUnit.PerSeat);

	private enum SpaceUnit {
		PerSeat,
		PerWorkplace
	}

	protected final NumberRange			spacePerUnit;
	private final RoomType.SpaceUnit	spaceUnit;
	private final NumberRange			workplacesPerThousandSeats;

	private RoomType(final double minimumSpacePerUnit, final double maximumSpacePerUnit, final RoomType.SpaceUnit spaceUnit) {
		this(minimumSpacePerUnit, maximumSpacePerUnit, spaceUnit, null);
	}

	private RoomType(final double minimumSpacePerUnit, final double maximumSpacePerUnit, final RoomType.SpaceUnit spaceUnit,
			final NumberRange workplacesPerThousandSeats) {
		spacePerUnit = new NumberRange(minimumSpacePerUnit, maximumSpacePerUnit);
		this.spaceUnit = spaceUnit;
		this.workplacesPerThousandSeats = workplacesPerThousandSeats;
	}

	/**
	 * @param seats
	 *            total number of seats of a {@link MovieTheater}
	 * @return a random value for the given amount of seats
	 */
	public Room createRoom(final int seats) {
		final double space = spacePerUnit.getRandomValue();
		if (spaceUnit == SpaceUnit.PerSeat) {
			final double allocatedSpace = space * seats;
			return new SimpleRoom(this, allocatedSpace);
		} else if (spaceUnit == SpaceUnit.PerWorkplace) {
			final int workPlaces = getRandomWorkPlaces(seats);
			final double allocatedSpace = space * workPlaces;
			return new WorkSpace(this, allocatedSpace, workPlaces);
		}
		throw new IllegalStateException("Unknown space unit " + spaceUnit);
	}

	int getRandomWorkPlaces(final int seats) {
		final int workplaces = (int) Math.round(workplacesPerThousandSeats.getRandomValue() * seats / 1000.0);
		return Math.max(workplaces, 1);
	}
}