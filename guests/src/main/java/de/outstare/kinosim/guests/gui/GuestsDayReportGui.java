package de.outstare.kinosim.guests.gui;

import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.StringUtils;

import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.guests.GuestsShowReport;
import de.outstare.kinosim.population.Audience;

/**
 * A GuestsDayReportGui displays guest numbers of the day in a table.
 */
public class GuestsDayReportGui {
	private final GuestsDayReport	report;

	public GuestsDayReportGui(final GuestsDayReport report) {
		this.report = report;
	}

	public JComponent createUi() {
		final JTable table = new JTable(createModel()) {
			private static final long	serialVersionUID	= 1954725414916757784L;

			@Override
			public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
				final Component renderComponent = super.prepareRenderer(renderer, row, column);
				if (row == 0 && renderComponent instanceof JLabel) {
					final JLabel label = (JLabel) renderComponent;
					label.setFont(label.getFont().deriveFont(Font.BOLD));
				}
				return renderComponent;
			}
		};
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.setAutoCreateRowSorter(true);

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		final String dayLabel = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(report.getDay());
		panel.add(new JLabel(dayLabel));
		panel.add(table.getTableHeader());
		panel.add(table);
		return panel;
	}

	private TableModel createModel() {
		return new AbstractTableModel() {
			private static final long	serialVersionUID	= 5778358533378897039L;

			@Override
			public int getRowCount() {
				return report.getShowCount() + 1;
			}

			@Override
			public int getColumnCount() {
				return 9;
			}

			@Override
			public String getColumnName(final int column) {
				switch (column) {
				case 0:
					return "Movie";
				case 1:
					return "Time";
				case 2:
					return "Hall";
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					return Audience.values()[column - 3].toString();
				case 8:
					return "Total";
				}
				return super.getColumnName(column);
			}

			@Override
			public Object getValueAt(final int rowIndex, final int columnIndex) {
				if (rowIndex == 0) {
					// first row is sum
					switch (columnIndex) {
					case 0:
						return "Total";
					case 8:
						return report.getTotalGuests();
					default:
						return StringUtils.EMPTY;
					}
				}
				final GuestsShowReport showReport = report.getShowReport(rowIndex - 1);

				Object value = StringUtils.EMPTY;
				switch (columnIndex) {
				case 0:
					value = showReport.getShow().getFilm().getTitle();
					break;
				case 1:
					value = showReport.getShow().getStart();
					break;
				case 2:
					value = showReport.getShow().getHall();
					break;
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					value = showReport.getGuests(Audience.values()[columnIndex - 3]);
					break;
				case 8:
					value = showReport.getTotalGuests();
					break;
				}
				return value;
			}

			@Override
			public Class<?> getColumnClass(final int columnIndex) {
				if (columnIndex == 1) {
					return LocalDate.class;
				}
				if (3 <= columnIndex && columnIndex <= 8) {
					return Integer.class;
				}
				return super.getColumnClass(columnIndex);
			}
		};
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI(final GuestsDayReportGui movieList) {
		// Create and set up the window.
		final JFrame frame = new JFrame("GuestsDayReportGuiDemo");
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
		final GuestsDayReport aReport = GuestsDayReport.createRandom();
		final GuestsDayReportGui movieList = new GuestsDayReportGui(aReport);
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(movieList));
	}
}
