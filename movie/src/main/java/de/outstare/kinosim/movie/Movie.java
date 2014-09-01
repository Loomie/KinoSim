package de.outstare.kinosim.movie;

import java.time.Duration;
import java.util.Set;

/**
 * A Movie describes all aspects of a film relevant for a cinema.
 */
public interface Movie extends Comparable<Movie> {
	/**
	 * @return the name of this film
	 */
	String getTitle();

	/**
	 * @return the length in minutes
	 */
	Duration getDuration();

	/**
	 * @return the minimum age for whom this film is appropriate (in Germany "FSK", in US the MPAA rating)
	 */
	int getAgeRating();

	/**
	 * @return the company who distributes this film
	 */
	String getDistributor();

	/**
	 * @return the categories of this film
	 */
	Set<Genre> getGenres();

	/**
	 * @return what this movie means to people
	 */
	Rating getRating();
}
