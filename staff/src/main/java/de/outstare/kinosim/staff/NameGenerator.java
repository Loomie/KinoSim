package de.outstare.kinosim.staff;

import java.util.List;

import de.outstare.kinosim.util.FileReaderUtil;
import de.outstare.kinosim.util.Randomness;

public class NameGenerator {
	private List<String> firstnames;
	private List<String> lastnames;

	public NameGenerator() {
		firstnames = FileReaderUtil.readFileAsList("firstnames.txt");
		lastnames = FileReaderUtil.readFileAsList("lastnames.txt");
	}

	public String getRandomFirstname() {
		return firstnames.get(Randomness.nextInt(firstnames.size()));
	}

	public String getRandomLastname() {
		return lastnames.get(Randomness.nextInt(lastnames.size()));
	}
}
