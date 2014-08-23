package de.outstare.kinosim.movie.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import de.outstare.kinosim.movie.Genre;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;

public class RandomMovieGenerator implements MovieGenerator {

    public static void main(final String[] args) {
	final MovieGenerator gen = new RandomMovieGenerator();
	for (int i = 0; i < 10; i++) {
	    System.out.println(gen.generate());
	}
    }

    @Override
    public Movie generate() {
	return new Movie() {
	    final String title = createTitle();
	    final Rating rating = createRating();
	    final Set<Genre> genres = createGenres();
	    final int duration = createDuration();
	    final String distributor = createDistributor();
	    final int ageRating = createAgeRating();

	    @Override
	    public String getTitle() {
		return title;
	    }

	    @Override
	    public Rating getRating() {
		return rating;
	    }

	    @Override
	    public Set<Genre> getGenres() {
		return genres;
	    }

	    @Override
	    public int getDuration() {
		return duration;
	    }

	    @Override
	    public String getDistributor() {
		return distributor;
	    }

	    @Override
	    public int getAgeRating() {
		return ageRating;
	    }

	    @Override
	    public String toString() {
		return ToStringBuilder.reflectionToString(this);
	    }
	};
    }

    private String createTitle() {
	final int length = (int) (4 + Math.random() * 8);
	return RandomStringUtils.randomAlphabetic(length);
    }

    private Rating createRating() {
	final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<>();
	for (final RatingCategory category : RatingCategory.values()) {
	    ratingPerCategory.put(category, (int) (Math.random() * Rating.MAX_VALUE));
	}
	return Rating.create(ratingPerCategory);
    }

    private Set<Genre> createGenres() {
	final Set<Genre> genres = new HashSet<>();
	final List<Genre> available = new ArrayList<>(Arrays.asList(Genre.values()));
	final int amount = getRandomIndex(available) / 2 + 1;
	while (genres.size() < amount) {
	    final int index = getRandomIndex(available);
	    final Genre genre = available.get(index);
	    genres.add(genre);
	    available.remove(genre);
	}
	return genres;
    }

    private int getRandomIndex(final List<?> available) {
	return (int) (Math.random() * available.size());
    }

    private int createDuration() {
	return (int) (60 + Math.random() * 90);
    }

    private String createDistributor() {
	final double random = Math.random();
	if (random > 0.8) {
	    return "Foo Production";
	} else if (random > 0.5) {
	    return "Great Pictures";
	} else if (random > 0.3) {
	    return "Silly Studios";
	}
	return "Bar";
    }

    private int createAgeRating() {
	return (int) Math.round(18 * Math.random());
    }

}
