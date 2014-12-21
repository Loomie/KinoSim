package de.outstare.kinosim.commodities.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.outstare.kinosim.commodities.Good;
import de.outstare.kinosim.commodities.Inventory;
import de.outstare.kinosim.commodities.Purchasing;
import de.outstare.kinosim.finance.IncomeStatement;
import de.outstare.kinosim.finance.expenses.Taxes;
import de.outstare.kinosim.finance.gui.BalanceLabel;
import de.outstare.kinosim.guituil.WindowUtil;

/**
 * A PurchasingGui allows to buy stuff.
 */
public class PurchasingGui {
	private final Purchasing purchaser;
	private JLabel balance;

	public PurchasingGui(final Purchasing purchaser) {
		this.purchaser = purchaser;
	}

	public JComponent createUi() {
		final JPanel panel = new JPanel(new GridBagLayout());

		final GridBagConstraints layout = new GridBagConstraints();
		layout.anchor = GridBagConstraints.LINE_START;
		layout.insets = new Insets(2, 3, 2, 3);
		layout.gridy = 0;
		for (final Good good : Good.values()) {
			createRow(good, panel, layout);
			layout.gridy++;
		}

		balance = new JLabel();

		final GridBagConstraints fillLine = new GridBagConstraints();
		fillLine.anchor = GridBagConstraints.CENTER;
		fillLine.fill = GridBagConstraints.HORIZONTAL;
		fillLine.gridwidth = 2;
		panel.add(balance, fillLine);

		return panel;
	}

	private void createRow(final Good good, final JPanel panel, final GridBagConstraints layout) {
		panel.add(new JLabel(good.toString()), layout);
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		final Dimension size = spinner.getPreferredSize();
		size.width = 50;
		spinner.setPreferredSize(size);
		panel.add(spinner, layout);

		spinner.addChangeListener(new ChangeListener() {
			private int previousAmount = 0;

			@Override
			public void stateChanged(final ChangeEvent event) {
				final int currentAmount = ((Number) spinner.getValue()).intValue();
				final int difference = currentAmount - previousAmount;
				if (difference == 0) {
					return;
				}
				if (purchaser.isStorageFree(good, difference)) {
					purchaser.buy(good, difference);
					previousAmount = currentAmount;
				} else {
					spinner.setValue(previousAmount);
				}
			}
		});
	}

	public static void main(final String[] args) {
		final Inventory inv = new Inventory(20);
		final IncomeStatement incomeStatement = new IncomeStatement(Taxes.createRandom());
		final Purchasing buyer = new Purchasing(inv, incomeStatement);
		final PurchasingGui gui = new PurchasingGui(buyer);

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(new BalanceLabel(incomeStatement, false).createUi());
		panel.add(new InventoryBar(inv).createUi());
		panel.add(gui.createUi());
		WindowUtil.showAndClose(panel, "Purchasing Demo", null);
	}
}
