package de.outstare.kinosim.schedule.editor.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import de.outstare.kinosim.schedule.editor.ScheduleEditor;
import de.outstare.kinosim.util.Randomness;
import de.outstare.kinosim.util.TimeRange;

/**
 * A ScheduleEditorGui is the graphical user interface for a {@link ScheduleEditor}. It lets the user manipulate a schedule by moving {@link Movie}s
 * between {@link CinemaHall}s on a timeline.
 *
 * It is basically a table with hours of the day on the x-axis and {@link CinemaHall}s on the y-axis. Additionally a pool of movies is available.
 */
public class ScheduleEditorGui {
	private final ScheduleEditor	editor;
	private final TimeRange			editableTime;
	private JPanel					rows;

	public ScheduleEditorGui(final ScheduleEditor editor, final TimeRange editableTime) {
		this.editor = editor;
		this.editableTime = editableTime;
		editor.addChangeListener(() -> updateHallSchedules());
	}

	public JComponent createUi() {
		final JPanel timeline = new JPanel();
		final int hours = editableTime.toHours();
		final int startHour = editableTime.getStart().getHour();
		timeline.setLayout(new GridLayout(1, hours));
		for (int i = 0; i < hours; i++) {
			timeline.add(new JLabel(String.valueOf((startHour + i) % 24)));
		}

		// we use a list for the rows and paint the columns inside
		rows = new JPanel();
		updateHallSchedules();

		final SortedSet<CinemaHall> halls = editor.getAvailableHalls();
		final JPanel cinemaLabels = new JPanel(new GridLayout(halls.size(), 1));
		for (final CinemaHall hall : halls) {
			cinemaLabels.add(new JLabel("" + hall.getCapacity()));
		}

		final JPanel editor = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridy = 0;
		editor.add(new JLabel(), constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		editor.add(timeline, constraints);

		constraints.gridy++;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.weighty = 1;
		editor.add(cinemaLabels, constraints);

		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		editor.add(rows, constraints);

		return editor;
	}

	private void updateHallSchedules() {
		rows.removeAll();

		final SortedSet<CinemaHall> halls = editor.getAvailableHalls();
		rows.setLayout(new GridLayout(halls.size(), 1, 0, 4));

		for (final CinemaHall hall : halls) {
			final ScheduleGui cinemaGui = new ScheduleGui(editor.getHallSchedule(hall), editableTime);
			final JComponent editorPanel = cinemaGui.createUi();
			editorPanel.setTransferHandler(new MovieToScheduleTransferHandler(editor, hall, editableTime));
			rows.add(editorPanel);
		}

		final Container parent = rows.getParent();
		if (parent != null) {
			parent.validate();
		}
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
		final Random r = Randomness.getRandom();
		final int minStartHour = 13;
		final int maxStartHour = 24;
		for (int i = 0; i < 10; i++) {
			schedule.add(new Show(LocalTime.of(minStartHour + r.nextInt(maxStartHour - minStartHour), 0), movies.get(r.nextInt(movies.size())), halls
					.get(r.nextInt(halls.size())), AdBlock.NONE, 0));
		}
		final ScheduleEditor testEditor = new ScheduleEditor(schedule, halls, movies);
		final ScheduleEditorGui editorGui = new ScheduleEditorGui(testEditor, TimeRange.of(minStartHour, maxStartHour + 2));
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(() -> createAndShowGUI(editorGui));
	}
}
