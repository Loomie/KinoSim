package de.outstare.kinosim.movie.generator;

import java.util.List;

import de.outstare.kinosim.util.FileReaderUtil;
import de.outstare.kinosim.util.Randomness;

/**
 * A FakeTitleGenerator generates a title based on defined word lists.
 */
public class FakeTitleGenerator {
	private final List<String> adjectives;
	private final List<String> adverbs;
	private final List<String> nouns;

	public FakeTitleGenerator() {
		adjectives = FileReaderUtil.readFileAsList("adjectives.txt");
		adverbs = FileReaderUtil.readFileAsList("adverbs.txt");
		nouns = FileReaderUtil.readFileAsList("nouns.txt");
	}

	public String generateTitle() {
		final boolean useAdverb = Randomness.nextDouble() >= 0.3;
		final boolean useAdjective = Randomness.nextDouble() >= 0.7;
		final StringBuilder title = new StringBuilder();
		if (useAdverb) {
			title.append(random(adverbs)).append(' ');
		}
		if (useAdjective) {
			title.append(random(adjectives)).append(' ');
		}
		title.append(random(nouns));
		return title.toString();
	}

	private String random(final List<String> words) {
		return words.get(Randomness.nextInt(words.size()));
	}

	// Test
	public static void main(final String[] args) {
		final FakeTitleGenerator g = new FakeTitleGenerator();
		for (int i = 0; i < 100; i++) {
			System.out.println(g.generateTitle());
		}
	}
}
