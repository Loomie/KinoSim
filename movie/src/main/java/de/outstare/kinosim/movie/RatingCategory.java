package de.outstare.kinosim.movie;

/**
 * A RatingCategory is an aspect of a movie that is rated.
 */
public enum RatingCategory {
    /**
     * determines if a movie is slapstick or news (plot)
     */
    SERIOUSITY,
    /**
     * determines if the movie is total fiction or a documentation (narrative form)
     */
    REALITY,
    /**
     * determines if the movie is cold or full of emotions (mood)
     */
    EMOTION,
    /**
     * short movie or never ending story?
     */
    DURATION,
    /**
     * determines if the movie is home made or a million dollar budget production
     */
    PROFESSIONALITY
}
