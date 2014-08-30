package de.outstare.kinosim.schedule;

import java.util.Comparator;

/**
 * A ShowTimeComparator orders {@link Show}s by their start time. Same times will be sorted by halls, then by movie title.
 */
public class ShowTimeComparator implements Comparator<Show> {

	@Override
	public int compare(final Show o1, final Show o2) {
		int result = o1.getStart().compareTo(o2.getStart());
		if (result == 0) {
			result = o1.getHall().compareTo(o2.getHall());
			if (result == 0) {
				result = o1.getFilm().compareTo(o2.getFilm());
			}
		}
		return result;
	}

}
