package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import de.outstare.kinosim.guituil.WindowUtil;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.generator.RandomMovieGenerator;
import de.outstare.kinosim.movie.gui.RatingGui;
import de.outstare.kinosim.schedule.editor.gui.dnd.MovieDragFromListTransferHandler;

/**
 * A MovieListGui is a graphical list of {@link Movie}s. It shows a summary of each movie. Movies can be dragged from this list.
 */
public class MovieListGui implements ListCellRenderer<Movie> {
	private final List<Movie> movies = new ArrayList<>();

	public MovieListGui(final Collection<? extends Movie> movies) {
		this.movies.addAll(movies);
	}

	public JComponent createUi() {
		final ListModel<Movie> model = new AbstractListModel<Movie>() {
			private static final long serialVersionUID = 1L;

			@Override
			public int getSize() {
				return movies.size();
			}

			@Override
			public Movie getElementAt(final int index) {
				return movies.get(index);
			}
		};
		final JList<Movie> list = new JList<>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setDragEnabled(true);
		list.setTransferHandler(new MovieDragFromListTransferHandler());
		list.setCellRenderer(this);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					final Movie selectedMovie = list.getSelectedValue();
					if (selectedMovie != null) {
						final RatingGui ratingUi = new RatingGui(selectedMovie.getRating());
						WindowUtil.show(ratingUi.createUi(), "Rating of " + selectedMovie.getTitle(), null);
					}
				}
			}
		});

		return new JScrollPane(list);
	}

	@Override
	public Component getListCellRendererComponent(final JList<? extends Movie> list, final Movie value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		final JLabel title = new JLabel();
		title.setText(value.getTitle());
		title.setHorizontalAlignment(SwingConstants.LEADING);
		final Font defaultFont = title.getFont();
		// title.setFont(defaultFont.deriveFont(defaultFont.getSize() * 1.2f));

		final JLabel description = new JLabel();
		description.setText(value.getDuration().toMinutes() + " min., " + value.getAgeRating() + " yrs");
		description.setHorizontalAlignment(SwingConstants.LEADING);
		description.setFont(defaultFont.deriveFont(defaultFont.getSize() * 0.8f));

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBackground(isSelected ? Color.CYAN : Color.WHITE);
		// padding
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		panel.add(title);
		panel.add(description);
		return panel;
	}

	/** Test **/
	public static void main(final String[] args) {
		final RandomMovieGenerator generator = new RandomMovieGenerator();
		final List<Movie> someMovies = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			someMovies.add(generator.generate());
		}
		final MovieListGui movieList = new MovieListGui(someMovies);
		// creating and showing this application's GUI.
		WindowUtil.showAndClose(movieList.createUi(), "MovieListGuiDemo", null);
	}
}
