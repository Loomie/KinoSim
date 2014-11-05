package de.outstare.kinosim.movie.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.outstare.kinosim.util.Randomness;

/**
 * A FakeTitleGenerator generates a title based on defined word lists.
 */
public class FakeTitleGenerator {
	private final List<String>	adverbs;
	private final List<String>	nouns;

	public FakeTitleGenerator() {
		adverbs = readWordList("adverbs.txt");
		nouns = readWordList("nouns.txt");
	}

	private static List<String> readWordList(final String filename) {
		final List<String> words = new ArrayList<>();
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(FakeTitleGenerator.class.getClassLoader().getResourceAsStream(
				filename)))) {
			String word;
			while ((word = reader.readLine()) != null) {
				words.add(StringUtils.capitalize(word));
			}
		} catch (final IOException e) {
			words.add(e.getMessage());
			e.printStackTrace();
		}
		return words;
	}

	public String generateTitle() {
		return adverbs.get(Randomness.nextInt(adverbs.size())) + " " + nouns.get(Randomness.nextInt(nouns.size()));
	}

	// Test
	public static void main(final String[] args) {
		final FakeTitleGenerator g = new FakeTitleGenerator();
		for (int i = 0; i < 100; i++) {
			System.out.println(g.generateTitle());
		}
	}
}
