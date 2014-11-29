package de.outstare.kinosim;

import java.util.ArrayList;
import java.util.List;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.housegenerator.AreaMovieTheaterCreator;
import de.outstare.kinosim.housegenerator.MovieTheaterGenerator;
import de.outstare.kinosim.util.Randomness;

/**
 * A TheaterList holds multiple {@link MovieTheater}s.
 */
public class TheaterList {
	private final List<MovieTheater> theaters = new ArrayList<>();

	public TheaterList(final int availableCount) {
		for (int i = 0; i < availableCount; i++) {
			final MovieTheaterGenerator generator = new AreaMovieTheaterCreator((int) (800 + 5200 * Randomness.nextDouble()));
			theaters.add(generator.createTheater());
		}
	}

	public List<MovieTheater> getTheaters() {
		return theaters;
	}
}
