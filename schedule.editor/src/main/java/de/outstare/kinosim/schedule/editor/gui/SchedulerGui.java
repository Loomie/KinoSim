package de.outstare.kinosim.schedule.editor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.guituil.WindowUtil;
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
 * A SchedulerGui allows the creation and edition of a {@link Schedule}. Beside a {@link ScheduleEditor} a list of {@link Movie}s is available.
 */
public class SchedulerGui {
	private final ScheduleEditor	editor;
	private final TimeRange			editableTime;

	public SchedulerGui(final ScheduleEditor editor, final TimeRange editableTime) {
		this.editor = editor;
		this.editableTime = editableTime;
	}

	public JComponent createUi() {
		final ScheduleEditorGui editorGui = new ScheduleEditorGui(editor, editableTime);
		final MovieListGui movies = new MovieListGui(editor.getAvailableMovies());

		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(movies.createUi(), BorderLayout.WEST);
		panel.add(editorGui.createUi(), BorderLayout.CENTER);
		return panel;
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
		final SchedulerGui editorGui = new SchedulerGui(testEditor, TimeRange.of(minStartHour, maxStartHour + 2));
		// creating and showing this application's GUI.
		WindowUtil.showAndClose(editorGui.createUi(), "ScheduleEditorGuiDemo", new Dimension(1000, 500));
	}
}
