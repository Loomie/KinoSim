package de.outstare.kinosim.schedule.editor.gui.dnd;

import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.schedule.Show;
import de.outstare.kinosim.schedule.editor.ScheduleEditor;
import de.outstare.kinosim.util.TimeRange;

/**
 * A ScheduleDropTransferHandler allows the drop of movies and shows. It creates a new show for dropped movies.
 */
public class ScheduleDropTransferHandler extends TransferHandler {
	private static final Logger		LOG					= LoggerFactory.getLogger(ScheduleDropTransferHandler.class);
	private static final long		serialVersionUID	= -7023136469907812544L;

	private final ScheduleEditor	editor;
	private final CinemaHall		hallForNewMovies;
	private final TimeRange			editableTime;
	/**
	 * A rectangle which holds the point of the drop in pixel coordinates and the width of the show in minutes (height is not used)
	 */
	public static final String		DROP_AREA_PROPERTY	= "dropRectangle";

	/**
	 * Create a drop handler for movies and shows
	 */
	public ScheduleDropTransferHandler(final ScheduleEditor editor, final CinemaHall hallForNewMovies, final TimeRange editableTime) {
		this.editor = Preconditions.checkNotNull(editor);
		this.hallForNewMovies = Preconditions.checkNotNull(hallForNewMovies);
		this.editableTime = Preconditions.checkNotNull(editableTime);
	}

	private boolean isMovie(final TransferSupport support) {
		return support.isDataFlavorSupported(MovieTransferable.MOVIE_DATA_FLAVOR);
	}

	private boolean isShow(final TransferSupport support) {
		return support.isDataFlavorSupported(ShowTransferable.SHOW_DATA_FLAVOR);
	}

	@Override
	public boolean canImport(final TransferSupport support) {
		boolean isAccepted = isMovie(support) || isShow(support);

		Rectangle dropLocation;
		if (isAccepted) {
			dropLocation = new Rectangle(support.getDropLocation().getDropPoint());
			if (support.getTransferable() != null) {
				final Duration length = getDuration(support);
				if (length != null) {
					dropLocation.width = (int) length.toMinutes();
				} else {
					isAccepted = false;
				}
			}
		} else {
			dropLocation = null;
		}
		((JComponent) support.getComponent()).putClientProperty(ScheduleDropTransferHandler.DROP_AREA_PROPERTY, dropLocation);
		return isAccepted;
	}

	private Duration getDuration(final TransferSupport support) {
		final Transferable transferable = support.getTransferable();
		Duration duration = null;
		if (isMovie(support)) {
			try {
				final Movie movie = (Movie) transferable.getTransferData(MovieTransferable.MOVIE_DATA_FLAVOR);
				duration = movie.getDuration();
				if (!editor.isFree(hallForNewMovies, getStartTime(support), duration)) {
					duration = null;
				}
			} catch (UnsupportedFlavorException | IOException e) {
				LOG.error("could not get movie out of transferable: {}", e.toString());
			}
		} else if (isShow(support)) {
			try {
				final Show show = (Show) transferable.getTransferData(ShowTransferable.SHOW_DATA_FLAVOR);
				duration = show.getDuration();
				if (!editor.isFreeFor(hallForNewMovies, getStartTime(support), show)) {
					duration = null;
				}
			} catch (UnsupportedFlavorException | IOException e) {
				LOG.error("could not get show out of transferable: {}", e.toString());
			}
		}
		return duration;
	}

	@Override
	public boolean importData(final TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}

		final Transferable t = support.getTransferable();
		if (t == null) {
			return false;
		}

		final LocalTime startTime = getStartTime(support);
		if (isMovie(support)) {
			Movie movie;
			try {
				movie = (Movie) t.getTransferData(MovieTransferable.MOVIE_DATA_FLAVOR);
			} catch (UnsupportedFlavorException | IOException e) {
				LOG.error("failed to get dropped movie", e);
				return false;
			}

			editor.add(hallForNewMovies, movie, startTime);
			return true;
		} else if (isShow(support)) {
			Show show;
			try {
				show = (Show) t.getTransferData(ShowTransferable.SHOW_DATA_FLAVOR);
			} catch (UnsupportedFlavorException | IOException e) {
				LOG.error("failed to get dropped show", e);
				return false;
			}

			editor.moveToTimeInHall(show, startTime, hallForNewMovies);
			return true;
		}

		LOG.warn("can import drag&drop, but nothing imported!");
		return false;
	}

	private LocalTime getStartTime(final TransferSupport support) {
		final int width = support.getComponent().getWidth();
		final int dropX = support.getDropLocation().getDropPoint().x;
		final double dropRatio = dropX / (double) width;

		final long totalWithMinutes = editableTime.toMinutes();
		final long dayMinutes = (long) (dropRatio * totalWithMinutes);
		return editableTime.getStart().plusMinutes(dayMinutes);
	}
}
