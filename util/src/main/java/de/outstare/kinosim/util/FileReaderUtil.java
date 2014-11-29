package de.outstare.kinosim.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class FileReaderUtil {
	public static List<String> readFileAsList(final String filename) {
		final List<String> words = new ArrayList<>();
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(FileReaderUtil.class.getClassLoader().getResourceAsStream(
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
}
