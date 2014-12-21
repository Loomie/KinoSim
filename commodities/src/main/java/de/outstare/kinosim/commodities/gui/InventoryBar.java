package de.outstare.kinosim.commodities.gui;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import de.outstare.kinosim.commodities.Good;
import de.outstare.kinosim.commodities.Inventory;

/**
 * An InventoryBar shows a bar with the fill level of an {@link Inventory}.
 */
public class InventoryBar {
	private final Inventory inventory;

	public InventoryBar(final Inventory inventory) {
		this.inventory = inventory;
	}

	public JComponent createUi() {
		final JProgressBar inventoryBar = new JProgressBar(0, 100);
		updateStorageBar(inventoryBar, inventory.getFillRatio());
		inventory.addListener(ratio -> updateStorageBar(inventoryBar, ratio));
		return inventoryBar;
	}

	private void updateStorageBar(final JProgressBar bar, final double ratio) {
		final int percent = (int) Math.round(ratio * 100);
		bar.setValue(percent);

		final StringBuilder tooltip = new StringBuilder();
		tooltip.append("<html>");
		for (final Good good : Good.values()) {
			tooltip.append(good.toString()).append(": ").append(inventory.getAmount(good)).append("<br>");
		}
		tooltip.append("</html>");
		bar.setToolTipText(tooltip.toString());
	}
}
