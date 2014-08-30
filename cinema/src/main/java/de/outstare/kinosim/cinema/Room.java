package de.outstare.kinosim.cinema;

/**
 * A Room is part of a {@link MovieTheater}. It uses some space and depending on the {@link RoomType} may hold guests or workplaces.
 */
public interface Room {

	RoomType getType();

	/**
	 * @return the used space in square meters
	 */
	double getAllocatedSpace();
}
