package de.outstare.kinosim.housegenerator.hall;

import de.outstare.kinosim.cinema.CinemaHall;

public class FixedSizeCinemaHall implements CinemaHall {
    private final int capacity;

    public FixedSizeCinemaHall(final int capacity) {
	this.capacity = capacity;

    }

    @Override
    public int getCapacity() {
	return capacity;
    }
}