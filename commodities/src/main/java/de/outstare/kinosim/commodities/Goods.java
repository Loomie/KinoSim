package de.outstare.kinosim.commodities;

/**
 * A Goods is a type of article that is sold or consumed in a movie theater.
 *
 * @see <a href="https://shop.pco-group.com/index.php?&sp=de&hp=pco_group_de&kt=default">PCO Group Online Shop</a>
 */
public enum Goods {
	// Food
	Popcorn(300, .056), // 22 kg corn, 10 kg sugar -> 300 small portions
	TortillaChips(12, .045), // 3x 800 g -> 12x 200 g
	Icecream(200, .009),
	GummiCandy(30, .015),
	ChocolateBar(36, .005),
	// Beverages
	SoftDrinks(130, .02), // 20 l sirup -> 130l Cola
	Coffee(1000, .02), // 6x 1 kg beans, 6 g per cup
	Beer(20, .036), // 0,5 l
	FruitJuice(24, 0.18), // 0,2 l
	// Non-Food
	// Consumed
	Tickets(10000, .0075),
	Cups(1000, .14),
	PopcornBags(750, .03), // 1000 small, 500 big -> 750 average
	ToiletPaper(6, .054), // big rolls
	Detergent(12, .03), // 12x 1 l
	BinBags(500, .03);

	private final int packageSize;
	private final double volume;

	private Goods(final int packageSize, final double volume) {
		this.packageSize = packageSize;
		this.volume = volume;
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
}
