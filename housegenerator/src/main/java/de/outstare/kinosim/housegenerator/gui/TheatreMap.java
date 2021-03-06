package de.outstare.kinosim.housegenerator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.cinema.RoomType;
import de.outstare.kinosim.cinema.WorkSpace;
import de.outstare.kinosim.housegenerator.AreaMovieTheaterCreator;
import de.outstare.kinosim.util.Randomness;

/**
 * A TheatreMap visualizes the outline of a {@link MovieTheater}.
 */
public class TheatreMap {
	private static final boolean DEBUG = false;

	private final MovieTheater theater;

	public TheatreMap(final MovieTheater theater) {
		super();
		this.theater = theater;
	}

	public JComponent createUi() {
		// a square which is filled with squares for each room according to its area
		final double totalArea = theater.getRoomSpace();
		final double totalEdgeLengthInMeters = Math.sqrt(totalArea);
		final Map<Room, Rectangle2D.Double> roomSquares = getRooms(totalEdgeLengthInMeters);

		final JPanel map = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getMinimumSize() {
				return new Dimension(100, 100);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(400, 400);
			};

			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth(), getHeight());
				final Graphics2D g2 = (Graphics2D) g;
				for (final Entry<Room, Double> roomSquares : roomSquares.entrySet()) {
					final Room room = roomSquares.getKey();
					final Rectangle2D.Double scaled = scale(roomSquares.getValue());
					g2.draw(scaled);
					g2.setColor(getColor(room.getType()));
					scaled.x++;
					scaled.y++;
					scaled.width -= 1;
					scaled.height -= 1;
					g2.fill(scaled);
					g2.setColor(Color.BLACK);
					String label;
					String size;
					if (room.getType() == RoomType.CinemaHall) {
						label = ((CinemaHall) room).getName();
						size = ((CinemaHall) room).getCapacity() + " seats";
					} else {
						label = room.getType().toString();
						if (room.getType().isWorkSpace()) {
							size = ((WorkSpace) room).getWorkplaceCount() + " worker";
						} else {
							size = "";
						}
					}
					g2.drawString(label, (float) (scaled.x + 5), (float) (scaled.y + 15));
					g2.drawString(size, (float) (scaled.x + 5), (float) (scaled.y + 15 + g2.getFontMetrics().getHeight()));
					if (DEBUG) {
						g2.drawString(String.valueOf((int) room.getAllocatedSpace()) + " m²", (float) (scaled.x + 5), (float) (scaled.y + 35));
					}
				}
			}

			private Double scale(final Double room) {
				final double ratioX = getWidth() / totalEdgeLengthInMeters;
				final double ratioY = getHeight() / totalEdgeLengthInMeters;
				return new Double(room.x * ratioX, room.y * ratioY, room.width * ratioX, room.height * ratioY);
			}
		};
		map.setOpaque(true);
		map.setBackground(Color.WHITE);
		return map;
	}

	private Map<Room, Rectangle2D.Double> getRooms(final double totalEdgeLengthInMeters) {
		final Map<Room, Rectangle2D.Double> roomOutlines = new HashMap<>();
		final Queue<Room> roomsBySize = new PriorityQueue<>((r1, r2) -> java.lang.Double.compare(r2.getAllocatedSpace(), r1.getAllocatedSpace()));
		roomsBySize.addAll(theater.getRooms());
		Point2D.Double rowBounds = new Point2D.Double(0.0, totalEdgeLengthInMeters);
		int row = 0;
		while (!roomsBySize.isEmpty()) {
			rowBounds = createRoomsForRow(totalEdgeLengthInMeters, roomsBySize, roomOutlines, rowBounds.x, rowBounds.y, row % 2 == 0);
			row++;
		}
		assert roomsBySize.isEmpty() : "all rooms must have their space!";
		return roomOutlines;
	}

	/** @return remaining space between minY and maxY which was not used **/
	private Point2D.Double createRoomsForRow(final double totalWidthInMeters, final Queue<Room> roomsBySize,
			final Map<Room, Rectangle2D.Double> roomOutlines, final double minY, final double maxY, final boolean publicTopLeft) {
		double leftXInMeters = 0;
		double rightXInMeters = totalWidthInMeters;
		double rowMinY = totalWidthInMeters;
		double rowMaxY = 0;
		while (!roomsBySize.isEmpty()) {
			final Room room = roomsBySize.remove();
			if (RoomType.Foyer == room.getType()) {
				// foyer uses the free space between the other rooms
				continue;
			}

			final double squareEdgeLength = Math.sqrt(room.getAllocatedSpace());
			final double width = Randomness.getGaussianAround(squareEdgeLength);
			final double height = room.getAllocatedSpace() / width;

			final double x = (room.getType().isPublicAccessible() ^ publicTopLeft) ? rightXInMeters - width : leftXInMeters;
			if (x < leftXInMeters || x + width > rightXInMeters + 0.01) { // allow some double incorrectness
				roomsBySize.add(room);
				break;
			}

			final double y;
			if (publicTopLeft) {
				y = minY;
			} else {
				y = maxY - height;
			}

			final Rectangle2D.Double square = new Double(x, y, width, height);
			roomOutlines.put(room, square);
			rowMinY = Math.min(square.y, rowMinY);
			rowMaxY = Math.max(square.y + square.height, rowMaxY);

			if (room.getType().isPublicAccessible() ^ publicTopLeft) {
				rightXInMeters -= width;
			} else {
				leftXInMeters += width;
			}
		}
		if (publicTopLeft) {
			// used some space on top (decreasing upper bound)
			return new Point2D.Double(rowMaxY, maxY);
		} else {
			// used some space on bottom (raising lower bound)
			return new Point2D.Double(minY, rowMinY);
		}
	}

	private static Color getColor(final RoomType roomType) {
		switch (roomType) {
		case CinemaHall:
			return new Color(0xffd5a8);// 0xffca8e
		case CashDesk:
			return new Color(0xa8eaba);
		case Counter:
			return new Color(0xcdefab);
		case Foyer:
			return Color.WHITE;
		case Office:
			return new Color(0xabcdef);
		case StaffRoom:
			return new Color(0x9fd0ea);
		case Storage:
			return new Color(0xcccce0);
		case Toilet:
			return new Color(0xeaccd8);
		default:
			return Color.MAGENTA;
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI(final TheatreMap movieList, final int x, final int y) {
		// Create and set up the window.
		final JFrame frame = new JFrame("TheatreMapDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		final JComponent newContentPane = movieList.createUi();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.setSize(400, 400);
		frame.setLocation(x, y);
		frame.setVisible(true);
	}

	/** Test **/
	public static void main(final String[] args) {
		for (int i = 0; i < 15; i++) {
			final MovieTheater movieTheater = new AreaMovieTheaterCreator(1000 + Randomness.nextInt(9000)).createTheater();
			final TheatreMap map = new TheatreMap(movieTheater);
			// Schedule a job for the event-dispatching thread:
			// creating and showing this application's GUI.
			final int x = (i % 5) * 400;
			final int y = (i / 5) * 400;
			javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(map, x, y));
		}
	}
}
