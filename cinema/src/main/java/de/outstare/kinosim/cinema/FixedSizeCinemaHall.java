package de.outstare.kinosim.cinema;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class FixedSizeCinemaHall extends SimpleRoom implements CinemaHall {
	private final String name;
	private final int capacity;

	public FixedSizeCinemaHall(final String name, final double allocatedSpace, final int capacity) {
		super(RoomType.CinemaHall, allocatedSpace);
		this.name = name;
		this.capacity = capacity;

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int compareTo(final CinemaHall o) {
		// larger capacity first
		return (int) Math.signum(o.getCapacity() - getCapacity());
	}

	@Override
	public String toString() {
		return getName() + " " + super.toString() + " " + getCapacity() + " seats";
	}
}