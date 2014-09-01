package de.outstare.kinosim.cinema;

/**
 * A WorkSpace is an area of a {@link MovieTheater} where one type of work is done on multiple workplaces.
 */
public class WorkSpace extends SimpleRoom
{
	private final int	workplaceCount;

	WorkSpace(final RoomType type, final double allocatedSpace, final int workplaceCount) {
		super(type, allocatedSpace);
		this.workplaceCount = workplaceCount;
	}

	/**
	 * @return the number of workplaces this room offers
	 */
	public int getWorkplaceCount() {
		return workplaceCount;
	}

	@Override
	public String toString() {
		return super.toString() + " " + getWorkplaceCount() + " workplaces";
	}
}
