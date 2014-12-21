package de.outstare.kinosim.commodities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.IncomeStatement;
import de.outstare.kinosim.finance.IncomeStatement.ExpenseCategory;
import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.expenses.Taxes;
import de.outstare.kinosim.util.Randomness;

/**
 * A Purchasing handles the buying of items to fill the stock. Therefore it applies prices to amounts of stuff.
 */
public class Purchasing {
	private static final Logger LOG = LoggerFactory.getLogger(Purchasing.class);

	private final Prices prices = new BuyingPrices();
	private final Inventory storage;
	private final IncomeStatement balance;

	/**
	 * Creates a new Purchasing with random prices.
	 *
	 * @param storage
	 *            into which items will be bought
	 * @param balance
	 *            where the bill will be applied to
	 */
	public Purchasing(final Inventory storage, final IncomeStatement balance) {
		this.storage = storage;
		this.balance = balance;
	}

	public Prices getPrices() {
		return prices;
	}

	/**
	 * Puts the given good in the {@link Inventory} and places the bill on the {@link IncomeStatement}. The inventory must have enough free space for
	 * the given amount!
	 *
	 * @see Inventory#isFree(Good, int)
	 */
	public void buy(final Good good, final int amount) {
		assert storage.isFree(good, amount);
		final Cents totalPrice = Cents.of(prices.getPrice(good).getValue() * amount);
		final Expense costs = new Expense(totalPrice, good.toString());
		balance.addExpense(ExpenseCategory.CostOfProduction, costs);
		storage.add(good, amount);
		LOG.info("bought {} {} for {}", amount, good, totalPrice.formatted());
	}

	public boolean isStorageFree(final Good good, final int amount) {
		return storage.isFree(good, amount);
	}

	// Test
	public static void main(final String[] args) {
		final Inventory inv = new Inventory(100 * .2);
		final IncomeStatement balance = new IncomeStatement(Taxes.createRandom());
		final Purchasing test = new Purchasing(inv, balance);

		for (int i = 0; i < 15; i++) {
			final Good good = Good.values()[Randomness.nextInt(Good.values().length)];
			final int amount = Randomness.nextInt(10) + 1;
			if (inv.isFree(good, amount)) {
				System.out.println("buying " + amount + " boxes " + good);
				test.buy(good, amount);
			} else {
				System.out.println("no room for " + amount + " boxes " + good);
			}
		}

		System.out.println("storage is " + inv.getFillRatio() * 100 + " % full (" + inv.usedStorageVolume() + " m³)");
		System.out.println(balance.prettyPrint());
	}
}
