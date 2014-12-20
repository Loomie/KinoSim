package de.outstare.kinosim.finance.gui;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.IncomeStatement;

/**
 * A BalanceLabel shows the total result of an {@link IncomeStatement}.
 */
public class BalanceLabel {
	private final IncomeStatement statement;
	private final Color positiveColor;

	public BalanceLabel(final IncomeStatement statement, final boolean highlightPositive) {
		this.statement = statement;
		positiveColor = (highlightPositive) ? new Color(0, 175, 0) : null;
	}

	public JComponent createUi() {
		final JLabel label = new JLabel();
		updateLabel(label, statement.getTotalBalance());
		statement.addListener(balance -> updateLabel(label, balance));
		return label;
	}

	private void updateLabel(final JLabel label, final Cents balance) {
		label.setText(balance.formatted());
		if (balance.getValue() >= 0) {
			label.setForeground(positiveColor);
		} else {
			label.setForeground(Color.RED);
		}
	}
}
