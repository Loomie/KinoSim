package de.outstare.kinosim.cinema;

/**
 * A CinemaHall is a room for showing a film. It includes seats, a screen, loudspeaker, corridors and so on.
 */
public interface CinemaHall {
    /**
     * @return the number of people who can enjoy a film in this cinema
     */
    int getCapacity();
}
