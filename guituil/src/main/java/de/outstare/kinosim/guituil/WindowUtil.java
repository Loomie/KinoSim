package de.outstare.kinosim.guituil;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class WindowUtil {
	/**
	 * Opens an application window to display the given component. When the window is closed the application will stop.
	 *
	 * @param component
	 * @param windowTitle
	 * @param size
	 *            <code>null</code> for minimal size (packed)
	 */
	public static void showAndClose(final JComponent component, final String windowTitle, final Dimension size) {
		// Schedule a job for the event-dispatching thread:
		SwingUtilities.invokeLater(() -> {
			final JFrame frame = showGUI(component, windowTitle, size);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
	}

	/**
	 * Show the given component in it's own window. After closing the window the application keeps running. The window is opened asynchronously.
	 * 
	 * @param component
	 * @param windowTitle
	 * @param size
	 */
	public static void show(final JComponent component, final String windowTitle, final Dimension size) {
		// Schedule a job for the event-dispatching thread:
		SwingUtilities.invokeLater(() -> showGUI(component, windowTitle, size));
	}

	private static JFrame showGUI(final JComponent newContentPane, final String windowTitle, final Dimension size) {
		// Create and set up the window.
		final JFrame frame = new JFrame(windowTitle);

		// Create and set up the content pane.
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		if (size == null) {
			frame.pack();
		} else {
			frame.setSize(size);
		}
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		return frame;
	}
}
