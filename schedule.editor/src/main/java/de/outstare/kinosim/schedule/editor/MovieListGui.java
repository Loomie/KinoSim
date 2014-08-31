package de.outstare.kinosim.schedule.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.generator.RandomMovieGenerator;

/**
 * A MovieListGui is a graphical list of {@link Movie}s. It shows a summary of each movie. Movies can be dragged from this list.
 */
public class MovieListGui implements ListCellRenderer<Movie> {
	private final List<Movie>	movies	= new ArrayList<>();

	public MovieListGui(final Collection<? extends Movie> movies) {
		this.movies.addAll(movies);
	}

	public JComponent createUi() {
		final ListModel<Movie> model = new AbstractListModel<Movie>() {
			private static final long	serialVersionUID	= 1L;

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
		list.setCellRenderer(this);

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
		description.setText(value.getDuration() + " min., " + value.getAgeRating() + " yrs");
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

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI(final MovieListGui movieList) {
		// Create and set up the window.
		final JFrame frame = new JFrame("ListDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		final JComponent newContentPane = movieList.createUi();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/** Test **/
	public static void main(final String[] args) {
		final RandomMovieGenerator generator = new RandomMovieGenerator();
		final List<Movie> someMovies = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			someMovies.add(generator.generate());
		}
		final MovieListGui movieList = new MovieListGui(someMovies);
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(movieList));
	}
}
