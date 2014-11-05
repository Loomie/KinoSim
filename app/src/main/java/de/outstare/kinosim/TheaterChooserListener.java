package de.outstare.kinosim;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.finance.expenses.Leasehold;

/**
 * A TheaterChooserListener is notified after a {@link MovieTheater} is choosen.
 */
public interface TheaterChooserListener {
	void theaterChoosen(MovieTheater theater, Leasehold lease);
}
