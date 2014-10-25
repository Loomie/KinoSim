package de.outstare.kinosim.finance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.kinosim.finance.IncomeStatement.ExpenseCategory;
import de.outstare.kinosim.finance.IncomeStatement.RevenueCategory;
import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.revenue.Revenue;

public class IncomeStatementTest {
	private IncomeStatement	testObject;

	@Before
	public void setUp() {
		testObject = new IncomeStatement();

		testObject.getExpenses().put(ExpenseCategory.CostOfProduction, new Expense(1000, "production part1"));
		testObject.getExpenses().put(ExpenseCategory.CostOfProduction, new Expense(200, "production part2"));
		testObject.getExpenses().put(ExpenseCategory.CostOfProduction, new Expense(300, "production part3"));
		testObject.getExpenses().put(ExpenseCategory.StaffCosts, new Expense(50, "staff part1"));
		testObject.getExpenses().put(ExpenseCategory.StaffCosts, new Expense(25, "staff part2"));
		testObject.getExpenses().put(ExpenseCategory.OtherOperativeExpenses, new Expense(4, "other part1"));
		testObject.getExpenses().put(ExpenseCategory.OtherOperativeExpenses, new Expense(8, "other part1"));

		testObject.getRevenues().put(RevenueCategory.Revenues, new Revenue(2000, "revenues part1"));
		testObject.getRevenues().put(RevenueCategory.Revenues, new Revenue(700, "revenues part2"));
		testObject.getRevenues().put(RevenueCategory.StockChange, new Revenue(128, "stock part1"));
		testObject.getRevenues().put(RevenueCategory.StockChange, new Revenue(256, "stock part2"));

		System.out.println(testObject);
	}

	@Test
	public void testSumOfRevenues() {
		assertEquals(3084, testObject.sumOfRevenues());
	}

	@Test
	public void testSumOfExpenses() {
		assertEquals(1587, testObject.sumOfExpenses());
	}

	@Test
	public void testGetProfit() {
		assertEquals(new Revenue(3084 - 1587, "Profit"), testObject.getProfit());

		testObject.getExpenses().put(ExpenseCategory.OtherOperativeExpenses, new Expense(4444, "going into minus"));

		assertEquals(new Revenue(0, "Profit"), testObject.getProfit());
	}

	@Test
	public void testGetDeficit() {
		assertEquals(new Expense(0, "Deficit"), testObject.getDeficit());

		testObject.getExpenses().put(ExpenseCategory.OtherOperativeExpenses, new Expense(4444, "going into minus"));

		assertEquals(new Expense(2947, "Deficit"), testObject.getDeficit());
	}

}
