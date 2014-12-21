package de.outstare.kinosim.commodities;

import de.outstare.kinosim.finance.Cents;

/**
 * A Good is a type of article that is sold or consumed in a movie theater.
 *
 * @see <a href="https://shop.pco-group.com/index.php">PCO Group Online Shop</a>
 */
public enum Good {
	// Food
	Popcorn(300, .056, 3500), // 22 kg corn, 10 kg sugar -> 300 small portions
	TortillaChips(12, .06, 1200), // 3x 800 g -> 12x 200 g
	Icecream(20, .009, 1500),
	GummiCandy(24, .015, 1200),
	ChocolateBar(32, .005, 1400),
	// Beverages
	SoftDrinks(130, .034, 6500), // 20 l sirup -> 130l Cola
	Coffee(1000, .02, 10000), // 6x 1 kg beans, 6 g per cup
	Beer(20, .036, 1500), // 0,5 l
	FruitJuice(24, 0.18, 1200), // 0,2 l
	// Non-Food
	// Consumed
	Tickets(10000, .0075, 25000),
	Cups(1000, .14, 9600), // price: 70 cup + 20 lid + 6 straw
	PopcornBags(750, .03, 6000), // 1000 small, 500 big -> 750 average
	ToiletPaper(6, .054, 1200), // big rolls
	Detergent(12, .03, 1500), // 12x 1 l
	BinBags(250, .042, 2500);

	private final int packageSize;
	private final double volume;
	private final Cents basePrice;

	private Good(final int packageSize, final double volume, final long basePrice) {
		this.packageSize = packageSize;
		this.volume = volume;
		this.basePrice = Cents.of(basePrice);
	}

	/**
	 * @return how many items are in one box?
	 */
	int getPackageSize() {
		return packageSize;
	}

	/**
	 * @return area used of a single box in cubic meters (m³)
	 */
	double getBoxVolume() {
		return volume;
	}

	double getVolumeOfBoxes(final int boxCount) {
		return volume * boxCount;
	}

	Cents getBasePrice() {
		return basePrice;
	}
}
