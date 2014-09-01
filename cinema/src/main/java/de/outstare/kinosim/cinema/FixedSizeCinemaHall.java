package de.outstare.kinosim.cinema;

public class FixedSizeCinemaHall extends SimpleRoom implements CinemaHall
{
	private final int	capacity;

	public FixedSizeCinemaHall(final double allocatedSpace, final int capacity) {
		super(RoomType.CinemaHall, allocatedSpace);
		this.capacity = capacity;

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
		return super.toString() + " " + getCapacity() + " seats";
	}
}