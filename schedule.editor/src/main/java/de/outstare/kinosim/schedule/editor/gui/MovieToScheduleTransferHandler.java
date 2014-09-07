package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.schedule.editor.ScheduleEditor;

/**
 * A MovieToScheduleTransferHandler creates a new show for movies dropped on a {@link ScheduleGui}.
 */
class MovieToScheduleTransferHandler extends TransferHandler {
	private static class MovieTransferable implements Transferable {
		private final Movie	movie;

		MovieTransferable(final Movie movie) {
			this.movie = movie;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { MOVIE_DATA_FLAVOR };
		}

		@Override
		public boolean isDataFlavorSupported(final DataFlavor flavor) {
			return MOVIE_DATA_FLAVOR.equals(flavor);
		}

		@Override
		public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return movie;
		}

	}

	private static final Logger		LOG					= LoggerFactory.getLogger(MovieToScheduleTransferHandler.class);
	private static final long		serialVersionUID	= -7023136469907812544L;
	static final DataFlavor			MOVIE_DATA_FLAVOR	= new DataFlavor(Movie.class, Movie.class.getSimpleName());

	private final ScheduleEditor	editor;
	private final CinemaHall		hallForNewMovies;

	/**
	 * Create a drag only transfer handler
	 */
	MovieToScheduleTransferHandler() {
		this(null, null);
	}

	/**
	 * Create a drag and drop handler for movies
	 */
	MovieToScheduleTransferHandler(final ScheduleEditor editor, final CinemaHall hallForNewMovies) {
		this.editor = editor;
		this.hallForNewMovies = hallForNewMovies;
	}

	@Override
	public int getSourceActions(final JComponent c) {
		return COPY;
	}

	@Override
	protected Transferable createTransferable(final JComponent c) {
		if (c instanceof JList) {
			@SuppressWarnings("unchecked")
			final Movie movie = ((JList<Movie>) c).getSelectedValue();
			return new MovieTransferable(movie);
		}
		return super.createTransferable(c);
	}

	@Override
	public boolean canImport(final TransferSupport support) {
		final boolean isSupported = support.isDataFlavorSupported(MOVIE_DATA_FLAVOR);

		Rectangle dropLocation;
		if (isSupported) {
			dropLocation = new Rectangle(support.getDropLocation().getDropPoint());
			final Transferable transferable = support.getTransferable();
			if (transferable != null) {
				try {
					final Movie movie = (Movie) transferable.getTransferData(MOVIE_DATA_FLAVOR);
					dropLocation.width = (int) movie.getDuration().toMinutes();
				} catch (UnsupportedFlavorException | IOException e) {
					LOG.error("could not get movie out of transferable: {}", e.toString());
				}
			}
		} else {
			dropLocation = null;
		}
		((JComponent) support.getComponent()).putClientProperty("dropRectangle", dropLocation);
		return isSupported;
	}

	@Override
	public boolean importData(final TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}

		final Transferable t = support.getTransferable();
		Movie movie;
		try {
			movie = (Movie) t.getTransferData(MOVIE_DATA_FLAVOR);
		} catch (UnsupportedFlavorException | IOException e) {
			LOG.error("failed to get dropped movie", e);
			return false;
		}

		final LocalTime startTime = getStartTime(support);
		editor.add(hallForNewMovies, movie, startTime);

		return true;
	}

	private LocalTime getStartTime(final TransferSupport support) {
		final int width = support.getComponent().getWidth();
		final int dropX = support.getDropLocation().getDropPoint().x;
		final double dropRatio = dropX / (double) width;

		final long totalWithSeconds = ChronoUnit.DAYS.getDuration().toMinutes() * 60;
		final long daySeconds = (long) (dropRatio * totalWithSeconds);
		return LocalTime.ofSecondOfDay(daySeconds);
	}
}
