package movie.popularity;

import de.outstare.kinosim.movie.Genre;

/**
 * A MoviePopularity holds all factors and the resulting popularity of a movie.
 */
public class MoviePopularity {
    private final Genre genre;
    private final int guestsPerWeek; // estimated if the movie is not yet running, else last full week
    private final int guestsPerWeekend; // estimated if the movie is not yet running, else last full weekend

    public MoviePopularity(final Genre genre, final int guestsPerWeek, final int guestsPerWeekend) {
	super();
	this.genre = genre;
	this.guestsPerWeek = guestsPerWeek;
	this.guestsPerWeekend = guestsPerWeekend;
    }

    /**
     * Get the popularity for the given audience.
     *
     * @param audience
     * @return the ratio of people who want to watch the movie (0.0 - 1.0)
     */
    public double getPopularity(final Audience audience) {
	// FIXME TODO
	return 1.0;
    }
}
