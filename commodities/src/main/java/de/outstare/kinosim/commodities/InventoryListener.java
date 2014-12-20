package de.outstare.kinosim.commodities;

/**
 * A InventoryListener is notified, when inventory changes.
 */
public interface InventoryListener {
	/**
	 * @see Inventory#getFillRatio()
	 */
	void fillRatioChanged(double newRatio);
}
