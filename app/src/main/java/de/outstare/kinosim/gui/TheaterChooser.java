package de.outstare.kinosim.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.outstare.kinosim.TheaterChooserListener;
import de.outstare.kinosim.TheaterList;
import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.expenses.Leasehold;
import de.outstare.kinosim.housegenerator.gui.TheatreMap;
import de.outstare.kinosim.util.Randomness;

/**
 * A TheaterChooser allows to choose one of multiple {@link MovieTheater}.
 */
public class TheaterChooser {
	private static final int		AVAILABLE	= 6;							// must be even!

	private final TheaterList		list		= new TheaterList(AVAILABLE);
	private TheaterChooserListener	listener;

	public JComponent createUi() {
		final int columns = 3;
		final JPanel panel = new JPanel(new GridLayout(AVAILABLE / columns, columns, 10, 20));
		for (final MovieTheater theater : list.getTheaters()) {
			panel.add(new ChooserButton(theater));
		}
		return panel;
	}

	public void setListener(final TheaterChooserListener listener) {
		this.listener = listener;
	}

	private class ChooserButton extends JPanel {
		private static final long	serialVersionUID	= 2727185316379274737L;

		ChooserButton(final MovieTheater theater) {
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

			addLabel(theater);
			addMap(theater);
			addButton(theater);
		}

		private void addLabel(final MovieTheater theater) {
			final String description = String.format("%d seats on %d halls (%.0f m² estate)", theater.getNumberOfSeats(), theater.getHalls().size(),
					theater.getEstateSpace());
			add(new JLabel(description));
		}

		private void addMap(final MovieTheater theater) {
			final TheatreMap theaterMap = new TheatreMap(theater);
			add(theaterMap.createUi());
		}

		private void addButton(final MovieTheater theater) {
			final Leasehold lease = new Leasehold(theater, Cents.of(Randomness.getGaussianAround(600)));
			final long perMonth = lease.getMonthlyRate().amount.getValue() / 100;
			final JButton button = new JButton();
			button.setAction(new AbstractAction(String.format("select for %d $/month", perMonth)) {
				private static final long	serialVersionUID	= -5485389946391952277L;

				@Override
				public void actionPerformed(final ActionEvent e) {
					if (listener != null) {
						listener.theaterChoosen(theater, lease);
					}
				}
			});
			add(button);
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void showGUI(final JComponent newContentPane) {
		// Create and set up the window.
		final JFrame frame = new JFrame("TheaterChooserDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(final String[] args) {
		final TheaterChooser chooser = new TheaterChooser();
		chooser.setListener((theater, lease) -> System.exit(0));
		// Schedule a job for the event-dispatching thread:
		SwingUtilities.invokeLater(() -> showGUI(chooser.createUi()));
	}
}
