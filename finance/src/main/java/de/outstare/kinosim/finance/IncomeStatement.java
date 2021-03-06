package de.outstare.kinosim.finance;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.expenses.Taxes;
import de.outstare.kinosim.finance.revenue.Revenue;
import de.outstare.kinosim.util.Randomness;

/**
 * An IncomeStatement (or profit and loss account; German: "Gewinn- und Verlustrechnung") lists all revenues and all expenses to determine a net
 * income.
 */
public class IncomeStatement {
	/**
	 * Grouping according to German trading law (HGB)
	 */
	public enum RevenueCategory implements IncomeStatementCategory {
		Revenues,
		StockChange,
		OtherOperativeRevenues,
	}

	/**
	 * Grouping according to German trading law (HGB)
	 */
	public enum ExpenseCategory implements IncomeStatementCategory {
		CostOfProduction,
		StaffCosts,
		OtherOperativeExpenses,
	}

	private static final Logger LOG = LoggerFactory.getLogger(IncomeStatement.class);

	private final Multimap<RevenueCategory, Revenue> revenues = HashMultimap.create();
	private final Multimap<ExpenseCategory, Expense> expenses = HashMultimap.create();
	private final Set<IncomeStatementListener> listeners = new HashSet<>();

	private final Taxes taxes;

	public IncomeStatement(final Taxes taxes) {
		this.taxes = taxes;
	}

	public void addRevenue(final RevenueCategory category, Revenue newRevenue) {
		payTax(newRevenue);

		if (revenues.containsKey(category)) {
			for (final Revenue existingRevenue : revenues.get(category)) {
				if (existingRevenue.getName().equals(newRevenue.getName())) {
					revenues.remove(category, existingRevenue);
					newRevenue = new Revenue(existingRevenue.getAmount().add(newRevenue.getAmount()), existingRevenue.getName());
					break;
				}
			}
		}
		revenues.put(category, newRevenue);
		fireBalanceChange();
	}

	private void payTax(final Revenue newRevenue) {
		if (taxes != null) {
			final Expense tax = taxes.getExpense(newRevenue);
			LOG.info("paying {} taxes for {}", tax.getAmount().formatted(), newRevenue.getName());
			addExpense(ExpenseCategory.OtherOperativeExpenses, tax);
		}
	}

	public void addExpense(final ExpenseCategory category, Expense newExpense) {
		if (expenses.containsKey(category)) {
			for (final Expense existingExpense : expenses.get(category)) {
				if (existingExpense.getName().equals(newExpense.getName())) {
					expenses.remove(category, existingExpense);
					newExpense = new Expense(existingExpense.getAmount().add(newExpense.getAmount()), existingExpense.getName());
					break;
				}
			}
		}
		expenses.put(category, newExpense);
		fireBalanceChange();
	}

	public Multimap<RevenueCategory, Revenue> listRevenues() {
		return ImmutableMultimap.copyOf(revenues);
	}

	public Multimap<ExpenseCategory, Expense> listExpenses() {
		return ImmutableMultimap.copyOf(expenses);
	}

	public Cents sumOfRevenues() {
		return Cents.of(revenues.values().stream().mapToLong(revenue -> revenue.getAmount().getValue()).sum());
	}

	public Cents sumOfExpenses() {
		return Cents.of(expenses.values().stream().mapToLong(expense -> expense.getAmount().getValue()).sum());
	}

	/**
	 * @return The total balance of the statement: profit - expenses (so it can be negative)
	 */
	public Cents getTotalBalance() {
		return Cents.of(sumOfRevenues().getValue() - sumOfExpenses().getValue());
	}

	public Revenue getProfit() {
		final long profit = Math.max(sumOfRevenues().getValue() - sumOfExpenses().getValue(), 0);
		return new Revenue(Cents.of(profit), "Profit");
	}

	public Expense getDeficit() {
		final long deficit = Math.max(sumOfExpenses().getValue() - sumOfRevenues().getValue(), 0);
		return new Expense(Cents.of(deficit), "Deficit");
	}

	@Override
	public String toString() {
		final StringBuilder text = new StringBuilder(100);
		text.append(super.toString());
		text.append('\n');

		prettyPrint(text);
		return text.toString();
	}

	private static final int textWidth = 30;
	private static final int numberWidth = 14;

	public String prettyPrint() {
		final StringBuilder text = new StringBuilder(200);
		prettyPrint(text);
		return text.toString();
	}

	private void prettyPrint(final StringBuilder text) {
		addTableRow(text, new Revenue(sumOfRevenues(), "Revenues"), new Expense(sumOfExpenses(), "Expenses"));
		for (int i = 0; i < 2 * (textWidth + numberWidth) + 3; i++) {
			text.append('-');
		}
		text.append('\n');

		final int rows = Math.max(revenues.size(), expenses.size());
		final Iterator<Revenue> revs = revenues.values().iterator();
		final Iterator<Expense> exps = expenses.values().iterator();
		for (int i = 0; i < rows; i++) {
			addTableRow(text, revs.hasNext() ? revs.next() : null, exps.hasNext() ? exps.next() : null);
		}
		addTableRow(text, getProfit(), getDeficit());
	}

	private void addTableRow(final StringBuilder text, final Revenue revenue, final Expense expense) {
		if (revenue != null) {
			addLine(text, revenue.getName(), revenue.getAmount());
		} else {
			addEmptyRow(text);
		}
		addSeparator(text);
		if (expense != null) {
			addLine(text, expense.getName(), expense.getAmount());
		} else {
			addEmptyRow(text);
		}
		text.append('\n');
	}

	private void addLine(final StringBuilder text, String name, final Cents amount) {
		if (name.length() > textWidth) {
			name = StringUtils.abbreviate(name, textWidth);
		}
		text.append(name);
		for (int i = name.length(); i < textWidth; i++) {
			text.append(' ');
		}
		final String number = amount.formatted();
		for (int i = 0; i < numberWidth - number.length(); i++) {
			text.append(' ');
		}
		text.append(number);
	}

	private void addEmptyRow(final StringBuilder text) {
		for (int i = 0; i < (textWidth + numberWidth); i++) {
			text.append(' ');
		}
	}

	private void addSeparator(final StringBuilder text) {
		text.append(" | ");
	}

	private void fireBalanceChange() {
		final Cents totalBalance = getTotalBalance();
		for (final IncomeStatementListener listener : listeners) {
			listener.balanceChanged(totalBalance);
		}
	}

	public void addListener(final IncomeStatementListener listener) {
		listeners.add(listener);
	}

	public void removeListener(final IncomeStatementListener listener) {
		listeners.remove(listener);
	}

	public static IncomeStatement createRandom() {
		final Taxes taxes = new Taxes(0.01 + 0.2 * Randomness.nextDouble());
		final IncomeStatement result = new IncomeStatement(taxes);
		for (final RevenueCategory cat : RevenueCategory.values()) {
			final int items = 1 + Randomness.nextInt(3);
			for (int i = 0; i < items; i++) {
				result.addRevenue(cat, new Revenue(Cents.of(Randomness.nextInt(10000)), cat.toString() + " item " + i));
			}
		}
		for (final ExpenseCategory cat : ExpenseCategory.values()) {
			final int items = 1 + Randomness.nextInt(3);
			for (int i = 0; i < items; i++) {
				result.addExpense(cat, new Expense(Cents.of(Randomness.nextInt(10000)), cat.toString() + " item " + i));
			}
		}
		return result;
	}
}
