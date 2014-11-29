package de.outstare.kinosim.housegenerator.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.outstare.kinosim.cinema.CinemaHall;
import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.cinema.RoomType;
import de.outstare.kinosim.cinema.WorkSpace;
import de.outstare.kinosim.guituil.WindowUtil;
import de.outstare.kinosim.housegenerator.AreaMovieTheaterCreator;
import de.outstare.kinosim.util.Randomness;

public class TheaterView {
	private final MovieTheater theater;

	public TheaterView(final MovieTheater theater) {
		this.theater = theater;
	}

	public JComponent createUi() {
		final JPanel theater = new JPanel(new GridBagLayout());
		// Grid:
		// Hall, Storage, Office
		// Hall, Counter, CashDesk
		final GridBagConstraints hallLayout = new GridBagConstraints();
		hallLayout.fill = GridBagConstraints.BOTH;
		hallLayout.weightx = 2;
		hallLayout.weighty = 2;
		hallLayout.gridheight = 2;
		theater.add(createHallArea(), hallLayout);

		final GridBagConstraints cellLayout = new GridBagConstraints();
		cellLayout.fill = GridBagConstraints.BOTH;
		cellLayout.weightx = 1;
		cellLayout.weighty = 1;

		cellLayout.gridy = 0;
		theater.add(createStorageArea(), cellLayout);
		theater.add(createOfficeArea(), cellLayout);
		cellLayout.gridy = 1;
		theater.add(createCounterArea(), cellLayout);
		theater.add(createCashDeskArea(), cellLayout);
		return theater;
	}

	private JPanel createGrid(final int roomCount) {
		final JPanel area = new JPanel();
		final int squareRoot = (int) Math.ceil(Math.sqrt(roomCount));
		System.out.println(squareRoot + "x" + squareRoot + " for " + roomCount + " rooms");
		area.setLayout(new GridLayout(squareRoot, squareRoot));
		return area;
	}

	private JComponent createHallArea() {
		final List<CinemaHall> halls = theater.getHalls();
		final JPanel hallsArea = createGrid(halls.size());
		for (final CinemaHall hall : halls) {
			hallsArea.add(new JLabel(hall.getName()));
		}
		return hallsArea;
	}

	private JComponent createStorageArea() {
		final Set<Room> storages = theater.getRoomsByType(RoomType.Storage);
		final JPanel storageArea = createGrid(storages.size());
		for (final Room storage : storages) {
			storageArea.add(new JLabel(storage.getAllocatedSpace() + " m² storage"));
		}
		return storageArea;
	}

	private JComponent createWorkplace(final RoomType type, final String workPlaceName) {
		final Set<Room> offices = theater.getRoomsByType(type);
		assert offices.size() == 1;
		final WorkSpace office = (WorkSpace) offices.iterator().next();
		final int placeCount = office.getWorkplaceCount();
		final JPanel officeArea = createGrid(placeCount);
		officeArea.setBorder(BorderFactory.createTitledBorder(type.toString()));
		for (int i = 0; i < placeCount; i++) {
			officeArea.add(new JLabel(workPlaceName + " " + (i + 1)));
		}
		return officeArea;
	}

	private JComponent createOfficeArea() {
		return createWorkplace(RoomType.Office, "seat");
	}

	private JComponent createCounterArea() {
		return createWorkplace(RoomType.Counter, "counter");
	}

	private JComponent createCashDeskArea() {
		return createWorkplace(RoomType.CashDesk, "pay desk");
	}

	public static void main(final String[] args) {
		final MovieTheater t = new AreaMovieTheaterCreator(1000 + Randomness.getGaussianAround(3000)).createTheater();
		final TheaterView view = new TheaterView(t);
		WindowUtil.showAndClose(view.createUi(), "Theater View Demo", null);
	}
}
