package de.outstare.kinosim.housegenerator;

import de.outstare.kinosim.cinema.MovieTheater;

/**
 * A AreaMovieTheaterCreator generates a {@link MovieTheater} for a given area of land (an estate).
 */
public class AreaMovieTheaterCreator implements MovieTheaterGenerator {
    private final int area; // in square meters

    public AreaMovieTheaterCreator(final int areaInSquareMeters) {
	area = areaInSquareMeters;
    }

    /**
     * @see de.outstare.kinosim.housegenerator.MovieTheaterGenerator#createTheater()
     */
    @Override
    public MovieTheater createTheater() {
	final double usableArea = area * 1.2; // +20 % because between the large halls offices and so on can be build above other areas
	final double cinemaAreaPerSeat = RoomType.CinemaHall.getSquareMeters(1); // use fixed value instead of a new random value for each call
	// 1. estimate seats by using a fixed part of space for a single hall
	final int calculatedSeats = estimateSeats(usableArea, cinemaAreaPerSeat);
	// 2. calculate space for all rooms except cinema halls (becaues each type will calculated only once, but we want multiple cinemas)
	double usedSpace = calculateSpaceForNonCinemaHalls(calculatedSeats);
	// 3. For the remaining space create cinema halls
	// Idea: Create one large cinema which holds most people and multiple smaller ones which do not differ that much in size.
	// Implementation: Use a decreasing curve of x^(-1/3)
	int hallNo = 1;
	int actualSeats = 0;
	// final List<CinemaHall> cinemas = new LinkedList<CinemaHall>();
	System.out.println("Berechnete Sitze: " + calculatedSeats);
	while (usedSpace < usableArea) {
	    final int seatsForHall = (int) (calculatedSeats / 3.0 * Math.pow(hallNo, -0.5));
	    final double spaceForHall = cinemaAreaPerSeat * seatsForHall;
	    // cinemas.add(new FixedSizeCinemaHall(seatsForHall));
	    System.out.println("Kino " + hallNo + " hat " + seatsForHall + " Sitze und eine Fläche von " + spaceForHall + " m²");
	    usedSpace += spaceForHall;
	    actualSeats += seatsForHall;
	    hallNo++;
	}
	System.out.println("Gesamtsitze: " + actualSeats + " Verwendete Nettofläche: " + usedSpace / 1.2 + " m²");
	final int totalCapacity = actualSeats;
	return new MovieTheater() {
	    @Override
	    public int getNumberOfSeats() {
		return totalCapacity;
	    }
	};
    }

    private double calculateSpaceForNonCinemaHalls(final int calculatedSeats) {
	double usedSpace2 = 0;
	for (final RoomType type : RoomType.values()) {
	    if (type == RoomType.CinemaHall) {
		continue;
	    }
	    final double spaceForRoom = type.getSquareMeters(calculatedSeats);
	    System.out.println(type + " " + spaceForRoom + " m²");
	    usedSpace2 += spaceForRoom;
	}
	return usedSpace2;
    }

    private int estimateSeats(final double usableArea, final double cinemaAreaPerSeat) {
	final double areaForHalls = usableArea * 0.56;
	return (int) (areaForHalls / cinemaAreaPerSeat);
    }

}
