package de.outstare.kinosim.gui;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.outstare.kinosim.ShowSimulator;
import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.guests.GuestCalculator;
import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.guests.gui.GuestsDayReportGui;
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
import de.outstare.kinosim.schedule.editor.gui.SchedulerGui;
import de.outstare.kinosim.util.TimeRange;

/**
 * A ShowSimulatorGui shows a {@link ScheduleEditor}, a button for simulating and a {@link GuestsDayReport} for the simulation result.
 */
public class ShowSimulatorGui {
	private final ScheduleEditor	editor;
	private final ShowSimulator		simulator;
	private final TimeRange			editableTime;

	/**
	 * @param editor
	 *            with a schedule
	 * @param simulator
	 *            that shares the same schedule as the editor!
	 */
	public ShowSimulatorGui(final ScheduleEditor editor, final ShowSimulator simulator, final TimeRange editableTime) {
		this.editor = editor;
		this.simulator = simulator;
		this.editableTime = editableTime;
	}

	public JComponent createUi() {
		final JPanel panel = new JPanel();
		final SchedulerGui editorUi = new SchedulerGui(editor, editableTime);
		final JButton simulate = new JButton("simulate");
		simulate.setAction(new AbstractAction("simulate") {
			private static final long	serialVersionUID	= -219059695783807057L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				simulator.nextDay();

				panel.remove(2);
				panel.add(createReportUi());
				panel.validate();
			}
		});
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(editorUi.createUi());
		panel.add(simulate);
		panel.add(createReportUi());
		return panel;
	}

	private JComponent createReportUi() {
		return new GuestsDayReportGui(simulator.getReport()).createUi();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI(final ShowSimulatorGui editorGui) {
		// Create and set up the window.
		final JFrame frame = new JFrame("ScheduleEditorGuiDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		final JComponent newContentPane = editorGui.createUi();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.setSize(1000, 700);
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
		final int minStartHour = 13;
		final int maxStartHour = 24;
		for (int i = 0; i < 4; i++) {
			final Movie movie = movies.get(r.nextInt(movies.size()));
			final CinemaHall hall = halls.get(r.nextInt(halls.size()));
			LocalTime showStart;
			int loops = 10;
			do {
				showStart = LocalTime.of(minStartHour + r.nextInt(maxStartHour - minStartHour), 0);
				loops--;
			} while (!schedule.isFree(hall, new TimeRange(showStart, movie.getDuration())) && loops > 0);
			schedule.add(new Show(showStart, movie, hall, AdBlock.NONE, 0));
		}
		final ScheduleEditor testEditor = new ScheduleEditor(schedule, halls, movies);

		final ShowSimulator testSimulator = new ShowSimulator(schedule, GuestCalculator.createRandom(), LocalDate.now());
		final ShowSimulatorGui editorGui = new ShowSimulatorGui(testEditor, testSimulator, TimeRange.of(minStartHour, maxStartHour + 2));
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(() -> createAndShowGUI(editorGui));
	}
}
