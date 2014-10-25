package de.outstare.kinosim.finance;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashMultimap;
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

	public Multimap<RevenueCategory, Revenue> getRevenues() {
		return revenues;
	}

	public Multimap<ExpenseCategory, Expense> getExpenses() {
		return expenses;
	}

	public int sumOfRevenues() {
		return revenues.values().stream().mapToInt(revenue -> revenue.amount).sum();
	}

	public int sumOfExpenses() {
		return expenses.values().stream().mapToInt(expense -> expense.amount).sum();
	}

	public Revenue getProfit() {
		final int profit = Math.max(sumOfRevenues() - sumOfExpenses(), 0);
		return new Revenue(profit, "Profit");
	}

	public Expense getDeficit() {
		final int deficit = Math.max(sumOfExpenses() - sumOfRevenues(), 0);
		return new Expense(deficit, "Deficit");
	}

	@Override
	public String toString() {
		final StringBuilder text = new StringBuilder(100);
		text.append(super.toString());
		text.append('\n');

		prettyPrint(text);
		return text.toString();
	}

	public String prettyPrint() {
		final StringBuilder text = new StringBuilder(200);
		prettyPrint(text);
		return text.toString();
	}

	private void prettyPrint(final StringBuilder text) {
		addTableRow(text, new Revenue(sumOfRevenues(), "Revenues"), new Expense(sumOfExpenses(), "Expenses"));
		for (int i = 0; i < 2 * 28 + 3; i++) {
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

	private void addLine(final StringBuilder text, String name, final int amount) {
		final int textWidth = 20;
		final int numberWidth = 8;
		if (name.length() > textWidth) {
			name = StringUtils.abbreviate(name, textWidth);
		}
		text.append(name);
		for (int i = name.length(); i < textWidth; i++) {
			text.append(' ');
		}
		final String number = String.valueOf(amount);
		for (int i = 0; i < numberWidth - number.length(); i++) {
			text.append(' ');
		}
		text.append(number);
	}

	private void addEmptyRow(final StringBuilder text) {
		for (int i = 0; i < 28; i++) {
			text.append(' ');
		}
	}

	private void addSeparator(final StringBuilder text) {
		text.append(" | ");
	}
}
