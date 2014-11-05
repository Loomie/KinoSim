package de.outstare.kinosim.finance;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.revenue.Revenue;

/**
 * An IncomeStatement (or profit and loss account; German: "Gewinn- und Verlustrechnung") lists all revenues and all expenses to determine a net
 * income.
 */
public class IncomeStatement {
	/**
	 * Grouping according to German trading law (HGB)
	 */
	public enum RevenueCategory {
		Revenues,
		StockChange,
		OtherOperativeRevenues,
	}

	/**
	 * Grouping according to German trading law (HGB)
	 */
	public enum ExpenseCategory {
		CostOfProduction,
		StaffCosts,
		OtherOperativeExpenses,
	}

	private final Multimap<RevenueCategory, Revenue>	revenues	= HashMultimap.create();
	private final Multimap<ExpenseCategory, Expense>	expenses	= HashMultimap.create();

	public void addRevenue(final RevenueCategory category, Revenue newRevenue) {
		if (revenues.containsKey(category)) {
			for (final Revenue existingRevenue : revenues.get(category)) {
				if (existingRevenue.name.equals(newRevenue.name)) {
					revenues.remove(category, existingRevenue);
					newRevenue = new Revenue(existingRevenue.amount.add(newRevenue.amount), existingRevenue.name);
					break;
				}
			}
		}
		revenues.put(category, newRevenue);
	}

	public void addExpense(final ExpenseCategory category, Expense newExpense) {
		if (expenses.containsKey(category)) {
			for (final Expense existingExpense : expenses.get(category)) {
				if (existingExpense.name.equals(newExpense.name)) {
					expenses.remove(category, existingExpense);
					newExpense = new Expense(existingExpense.amount.add(newExpense.amount), existingExpense.name);
					break;
				}
			}
		}
		expenses.put(category, newExpense);
	}

	public Multimap<RevenueCategory, Revenue> listRevenues() {
		return ImmutableMultimap.copyOf(revenues);
	}

	public Multimap<ExpenseCategory, Expense> listExpenses() {
		return ImmutableMultimap.copyOf(expenses);
	}

	public Cents sumOfRevenues() {
		return Cents.of(revenues.values().stream().mapToLong(revenue -> revenue.amount.getValue()).sum());
	}

	public Cents sumOfExpenses() {
		return Cents.of(expenses.values().stream().mapToLong(expense -> expense.amount.getValue()).sum());
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

	private static final int	textWidth	= 30;
	private static final int	numberWidth	= 14;

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
			addLine(text, revenue.name, revenue.amount);
		} else {
			addEmptyRow(text);
		}
		addSeparator(text);
		if (expense != null) {
			addLine(text, expense.name, expense.amount);
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
}
