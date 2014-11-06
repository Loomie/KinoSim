package de.outstare.kinosim.finance.gui;

import java.text.Collator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.google.common.collect.Multimap;

import de.outstare.kinosim.finance.IncomeStatement;
import de.outstare.kinosim.finance.IncomeStatementCategory;
import de.outstare.kinosim.finance.NamedAmount;

/**
 * A IncomeStatementGui displays the balance as a 'T' with a tree on each side. The root elements of the tree are the categories and below are the
 * single entries.
 */
public class IncomeStatementGui {
	private static class IncomeStatementModel extends AbstractTableModel {
		private static final long		serialVersionUID	= 1L;

		private final IncomeStatement	balance;

		IncomeStatementModel(final IncomeStatement balance) {
			this.balance = balance;
		}

		@Override
		public int getRowCount() {
			return Math.max(count(balance.listRevenues()), count(balance.listExpenses()));
		}

		private int count(final Multimap<?, ?> multimap) {
			return multimap.size() + multimap.keySet().size();
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			switch (columnIndex) {
			case 0:
				return getName(balance.listRevenues(), rowIndex);
			case 1:
				return getAmount(balance.listRevenues(), rowIndex);
			case 3:
				return getName(balance.listExpenses(), rowIndex);
			case 4:
				return getAmount(balance.listExpenses(), rowIndex);
			case 2:
			default:
				return null;
			}
		}

		private String getName(final Multimap<? extends IncomeStatementCategory, ? extends NamedAmount> categorizedValues, final int rowIndex) {
			final Row row = getRow(categorizedValues, rowIndex);
			if (row == null) {
				return null;
			} else if (row.isCategory()) {
				return row.category.toString();
			}
			return "    " + row.amount.getName();
		}

		private String getAmount(final Multimap<? extends IncomeStatementCategory, ? extends NamedAmount> categorizedValues, final int rowIndex) {
			final Row row = getRow(categorizedValues, rowIndex);
			if (row == null || row.isCategory()) {
				return null;
			}
			return row.amount.getAmount().formatted();
		}

		private <C extends IncomeStatementCategory> Row getRow(final Multimap<C, ? extends NamedAmount> categorizedValues, final int rowIndex) {
			System.out.println("IncomeStatementGui.IncomeStatementModel.getRow() ===================================== " + count(categorizedValues)
					+ " <= " + rowIndex);
			if (count(categorizedValues) <= rowIndex) {
				return null;
			}
			// find key (category) of row
			final LinkedList<C> sortedKeys = new LinkedList<>(categorizedValues.keySet());
			sortedKeys.sort(null);
			for (final C key : sortedKeys) {
				System.out.println(key + " has " + categorizedValues.get(key).size() + " values");
			}
			C key = sortedKeys.poll();
			int categoryIndex = 0;
			while (categoryIndex + categorizedValues.get(key).size() < rowIndex) {
				System.out.println("category " + key + " at " + categoryIndex + " has " + categorizedValues.get(key).size() + " values. rowIndex = "
						+ rowIndex);
				categoryIndex += categorizedValues.get(key).size() + 1; // 1 for key itself
				key = sortedKeys.remove();
			}
			System.out.println("category " + key + " at " + categoryIndex + " has " + categorizedValues.get(key).size() + " values. rowIndex = "
					+ rowIndex);
			// get element
			if (categoryIndex == rowIndex) {
				return new Row(key, null);
			} else {
				final List<NamedAmount> sortedValues = new ArrayList<>(categorizedValues.get(key));
				sortedValues.sort((value1, value2) -> Collator.getInstance().compare(value1.getName(), value2.getName()));
				final NamedAmount value = sortedValues.get(rowIndex - (categoryIndex + 1));
				return new Row(key, value);
			}
		}

		private static class Row {
			final IncomeStatementCategory	category;
			final NamedAmount				amount;

			Row(final IncomeStatementCategory category, final NamedAmount amount) {
				this.category = category;
				this.amount = amount;
			}

			boolean isCategory() {
				return amount == null;
			}
		}
	}

	private final IncomeStatement	balance;

	public IncomeStatementGui(final IncomeStatement balance) {
		this.balance = balance;
	}

	public JComponent createUi() {
		final JTable table = new JTable(new IncomeStatementModel(balance));
		table.getColumnModel().getColumn(2).setMaxWidth(10);
		return table;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
	 */
	private static void showGUI(final JComponent newContentPane) {
		// Create and set up the window.
		final JFrame frame = new JFrame("IncomeStatementDemo");
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
		final IncomeStatement balance = IncomeStatement.createRandom();
		final IncomeStatementGui gui = new IncomeStatementGui(balance);
		// Schedule a job for the event-dispatching thread:
		SwingUtilities.invokeLater(() -> showGUI(gui.createUi()));
	}
}
