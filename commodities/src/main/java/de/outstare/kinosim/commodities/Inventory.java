package de.outstare.kinosim.commodities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;

/**
 * An Inventory holds all wares that are stored in a movie theater.
 */
public class Inventory {
	private final Map<Good, AtomicInteger> goodCounts = new HashMap<>();
	private final double storageVolume;

	/**
	 * @param storageVolume
	 *            in cubic meters (m³) (> 0)
	 */
	public Inventory(final double storageVolume) {
		Preconditions.checkArgument(storageVolume > 0, "storage volume must be greater than zero");
		this.storageVolume = storageVolume;
	}

	public int getAmount(final Good good) {
		if (!goodCounts.containsKey(good)) {
			return 0;
		}
		return goodCounts.get(good).intValue();
	}

	public void add(final Good good, final int amount) throws InventoryFullException {
		if (!isFree(good, amount)) {
			throw new InventoryFullException();
		}
		if (!goodCounts.containsKey(good)) {
			goodCounts.put(good, new AtomicInteger());
		}
		goodCounts.get(good).addAndGet(amount);
	}

	public void remove(final Good good, final int amount) {
		if (goodCounts.containsKey(good)) {
			if (amount >= goodCounts.get(good).intValue()) {
				goodCounts.remove(good);
			} else {
				goodCounts.get(good).addAndGet(-1 * amount);
			}
		}
	}

	public double getFillRatio() {
		return usedStorageVolume() / storageVolume;
	}

	double usedStorageVolume() {
		return goodCounts.entrySet().parallelStream().mapToDouble(e -> e.getKey().getVolumeOfBoxes(e.getValue().intValue())).sum();
	}

	public boolean isFree(final Good good, final int amount) {
		final double used = usedStorageVolume();
		final double required = good.getVolumeOfBoxes(amount);
		return used + required <= storageVolume;
	}
}
