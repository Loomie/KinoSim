package de.outstare.kinosim.finance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.kinosim.finance.IncomeStatement.ExpenseCategory;
import de.outstare.kinosim.finance.IncomeStatement.RevenueCategory;
import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.expenses.Taxes;
import de.outstare.kinosim.finance.revenue.Revenue;

public class IncomeStatementTest {
	private IncomeStatement	testObject;

	@Before
	public void setUp() {
		testObject = new IncomeStatement(null);

		testObject.addExpense(ExpenseCategory.CostOfProduction, new Expense(Cents.of(1000), "production part1"));
		testObject.addExpense(ExpenseCategory.CostOfProduction, new Expense(Cents.of(200), "production part2"));
		testObject.addExpense(ExpenseCategory.CostOfProduction, new Expense(Cents.of(300), "production part3"));
		testObject.addExpense(ExpenseCategory.StaffCosts, new Expense(Cents.of(50), "staff part1"));
		testObject.addExpense(ExpenseCategory.StaffCosts, new Expense(Cents.of(25), "staff part2"));
		testObject.addExpense(ExpenseCategory.OtherOperativeExpenses, new Expense(Cents.of(4), "other part1"));
		testObject.addExpense(ExpenseCategory.OtherOperativeExpenses, new Expense(Cents.of(8), "other part1"));

		testObject.addRevenue(RevenueCategory.Revenues, new Revenue(Cents.of(2000), "revenues part1"));
		testObject.addRevenue(RevenueCategory.Revenues, new Revenue(Cents.of(700), "revenues part2"));
		testObject.addRevenue(RevenueCategory.StockChange, new Revenue(Cents.of(128), "stock part1"));
		testObject.addRevenue(RevenueCategory.StockChange, new Revenue(Cents.of(256), "stock part2"));

		System.out.println(testObject);
	}

	@Test
	public void testSumOfRevenues() {
		assertEquals(Cents.of(3084), testObject.sumOfRevenues());
	}

	@Test
	public void testSumOfExpenses() {
		assertEquals(Cents.of(1587), testObject.sumOfExpenses());
	}

	@Test
	public void testGetProfit() {
		assertEquals(new Revenue(Cents.of(3084 - 1587), "Profit"), testObject.getProfit());

		testObject.addExpense(ExpenseCategory.OtherOperativeExpenses, new Expense(Cents.of(4444), "going into minus"));

		assertEquals(new Revenue(Cents.of(0), "Profit"), testObject.getProfit());
	}

	@Test
	public void testGetDeficit() {
		assertEquals(new Expense(Cents.of(0), "Deficit"), testObject.getDeficit());

		testObject.addExpense(ExpenseCategory.OtherOperativeExpenses, new Expense(Cents.of(4444), "going into minus"));

		assertEquals(new Expense(Cents.of(2947), "Deficit"), testObject.getDeficit());
	}

	@Test
	public void testAddRevenue() {
		final RevenueCategory category = RevenueCategory.Revenues;
		final Revenue newRevenue = new Revenue(Cents.of(300), "revenues part3");
		final Revenue additionalRevenue = new Revenue(Cents.of(123), "revenues part1");
		assertEquals(Cents.of(3084), testObject.sumOfRevenues());

		testObject.addRevenue(category, newRevenue);

		assertTrue(testObject.listRevenues().get(category).contains(newRevenue));
		assertEquals(Cents.of(3384), testObject.sumOfRevenues());

		testObject.addRevenue(category, additionalRevenue);

		assertFalse(testObject.listRevenues().get(category).contains(additionalRevenue));
		assertEquals(Cents.of(3507), testObject.sumOfRevenues());
	}

	@Test
	public void testAddExpense() {
		final ExpenseCategory category = ExpenseCategory.CostOfProduction;
		final Expense newExpense = new Expense(Cents.of(300), "production part4");
		final Expense additionalExpense = new Expense(Cents.of(123), "production part1");
		assertEquals(Cents.of(1587), testObject.sumOfExpenses());

		testObject.addExpense(category, newExpense);

		assertTrue(testObject.listExpenses().get(category).contains(newExpense));
		assertEquals(Cents.of(1887), testObject.sumOfExpenses());

		testObject.addExpense(category, additionalExpense);

		assertFalse(testObject.listExpenses().get(category).contains(additionalExpense));
		assertEquals(Cents.of(2010), testObject.sumOfExpenses());
	}

	@Test
	public void testTaxes() {
		final Taxes taxes = new Taxes(0.2);
		testObject = new IncomeStatement(taxes);

		final RevenueCategory category = RevenueCategory.Revenues;
		final Revenue newRevenue = new Revenue(Cents.of(30000), "revenues part1");
		final Revenue additionalRevenue = new Revenue(Cents.of(12347), "revenues part1");
		assertEquals(Cents.of(0), testObject.sumOfExpenses());
		assertTrue(testObject.listExpenses().get(ExpenseCategory.OtherOperativeExpenses).isEmpty());

		testObject.addRevenue(category, newRevenue);

		assertEquals(Cents.of(6000), testObject.sumOfExpenses());
		assertFalse(testObject.listExpenses().get(ExpenseCategory.OtherOperativeExpenses).isEmpty());

		testObject.addRevenue(category, additionalRevenue);

		assertEquals(Cents.of(8469), testObject.sumOfExpenses());
	}
}
