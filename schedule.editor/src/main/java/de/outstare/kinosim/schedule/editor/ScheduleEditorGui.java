package de.outstare.kinosim.schedule.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.housegenerator.hall.CinemaHallGenerator;
import de.outstare.kinosim.housegenerator.hall.RandomCinemaHallGenerator;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.movie.generator.MovieGenerator;
import de.outstare.kinosim.movie.generator.RandomMovieGenerator;
import de.outstare.kinosim.schedule.AdBlock;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.schedule.ScheduleImpl;
import de.outstare.kinosim.schedule.Show;

/**
 * A ScheduleEditorGui is the graphical user interface for a {@link ScheduleEditor}. It lets the user manipulate a schedule by moving {@link Movie}s
 * between {@link CinemaHall}s on a timeline.
 *
 * It is basically a table with hours of the day on the x-axis and {@link CinemaHall}s on the y-axis. Additionally a pool of movies is available.
 */
public class ScheduleEditorGui {
	private final ScheduleEditor	editor;

	public ScheduleEditorGui(final ScheduleEditor editor) {
		this.editor = editor;

	}

	public JComponent createUi() {
		final JPanel timeline = new JPanel();
		timeline.setLayout(new GridLayout(1, 24));
		for (int i = 0; i < 24; i++) {
			timeline.add(new JLabel(String.valueOf(i)));
		}

		// we use a list for the rows and paint the columns inside
		final SortedSet<CinemaHall> halls = editor.getAvailableHalls();
		final JPanel rows = new JPanel();
		rows.setLayout(new GridLayout(halls.size(), 1, 0, 4));
		for (final CinemaHall hall : halls) {
			final ScheduleGui cinemaGui = new ScheduleGui(editor.getHallSchedule(hall));
			rows.add(cinemaGui.createUi());
		}

		final JPanel editor = new JPanel(new BorderLayout());
		editor.add(timeline, BorderLayout.NORTH);
		editor.add(rows, BorderLayout.CENTER);
		return editor;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI(final ScheduleEditorGui editorGui) {
		// Create and set up the window.
		final JFrame frame = new JFrame("ScheduleEditorGuiDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		final JComponent newContentPane = editorGui.createUi();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/** Test **/
	public static void main(final String[] args) {
		final Schedule schedule = new ScheduleImpl();
		final CinemaHallGenerator hallGenerator = new RandomCinemaHallGenerator();
		final List<CinemaHall> halls = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			halls.add(hallGenerator.createHall());
		}
		final MovieGenerator movieGenerator = new RandomMovieGenerator();
		final List<Movie> movies = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			movies.add(movieGenerator.generate());
		}
		final Random r = new Random();
		for (int i = 0; i < 10; i++) {
			schedule.add(new Show(LocalTime.of(r.nextInt(24), 0), movies.get(r.nextInt(movies.size())), halls.get(r.nextInt(halls.size())),
					AdBlock.NONE, 0));
		}
		final ScheduleEditor testEditor = new ScheduleEditor(schedule, halls, movies);
		final ScheduleEditorGui editorGui = new ScheduleEditorGui(testEditor);
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(() -> createAndShowGUI(editorGui));
	}
}
