package de.outstare.kinosim;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.finance.expenses.Leasehold;
import de.outstare.kinosim.population.PopulationPyramid;

/**
 * A TheaterChooserListener is notified after a {@link MovieTheater} is choosen.
 */
public interface TheaterChooserListener {
	void theaterChoosen(MovieTheater theater, Leasehold lease, PopulationPyramid population);
}
