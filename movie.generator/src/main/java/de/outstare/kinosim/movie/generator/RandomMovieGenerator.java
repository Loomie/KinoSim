package de.outstare.kinosim.movie.generator;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.outstare.kinosim.movie.Genre;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.movie.SimpleMovie;
import de.outstare.kinosim.util.Randomness;

public class RandomMovieGenerator implements MovieGenerator {
	private final FakeTitleGenerator	titleGen	= new FakeTitleGenerator();

	public static void main(final String[] args) {
		final MovieGenerator gen = new RandomMovieGenerator();
		for (int i = 0; i < 10; i++) {
			System.out.println(gen.generate());
		}
	}

	@Override
	public Movie generate() {
		return new SimpleMovie(createTitle(), createDuration(), createAgeRating(), createDistributor(), createGenres(), createRating(),
				createReleaseDate());
	}

	private String createTitle() {
		return titleGen.generateTitle();
		// final int length = (int) (4 + Randomness.nextDouble() * 8);
		// return RandomStringUtils.randomAlphabetic(length);
	}

	private Rating createRating() {
		final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<>();
		for (final RatingCategory category : RatingCategory.values()) {
			ratingPerCategory.put(category, Randomness.nextInt(Rating.MAX_VALUE));
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
		return Randomness.nextInt(available.size());
	}

	private Duration createDuration() {
		return Duration.ofMinutes(60 + Randomness.nextInt(90));
	}

	private String createDistributor() {
		final double random = Randomness.nextDouble();
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
		return 1 + Randomness.nextInt(18);
	}

	private LocalDate createReleaseDate() {
		final Random r = Randomness.getRandom();
		return LocalDate.now().minusWeeks(r.nextInt(8)).with(ChronoField.DAY_OF_WEEK, DayOfWeek.THURSDAY.getLong(ChronoField.DAY_OF_WEEK));
	}
}
