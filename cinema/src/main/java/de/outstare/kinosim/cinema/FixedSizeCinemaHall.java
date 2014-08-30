package de.outstare.kinosim.cinema;

public class FixedSizeCinemaHall implements CinemaHall {
	private final int	capacity;

	public FixedSizeCinemaHall(final int capacity) {
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
}