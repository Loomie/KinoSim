package de.outstare.kinosim.cinema;

/**
 * An SimpleRoom implements {@link Room} and can be extended to specialized rooms.
 */
public class SimpleRoom implements Room
{
	private final RoomType	type;
	private final double	allocatedSpace;

	public SimpleRoom(final RoomType type, final double allocatedSpace) {
		this.type = type;
		this.allocatedSpace = allocatedSpace;
	}

	/**
	 * @see de.outstare.kinosim.cinema.Room#getType()
	 */
	@Override
	public RoomType getType() {
		return type;
	}

	/**
	 * @see de.outstare.kinosim.cinema.Room#getAllocatedSpace()
	 */
	@Override
	public double getAllocatedSpace() {
		return allocatedSpace;
	}

	@Override
	public String toString() {
		return getType() + " " + getAllocatedSpace() + " m²";
	}
}
