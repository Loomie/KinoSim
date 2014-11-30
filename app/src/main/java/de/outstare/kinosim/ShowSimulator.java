package de.outstare.kinosim;

import java.time.LocalDate;
import java.util.Map;

import de.outstare.kinosim.finance.BankAccount;
import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.IncomeStatement;
import de.outstare.kinosim.finance.IncomeStatement.ExpenseCategory;
import de.outstare.kinosim.finance.IncomeStatement.RevenueCategory;
import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.expenses.Leasehold;
import de.outstare.kinosim.finance.expenses.MovieRental;
import de.outstare.kinosim.finance.expenses.Taxes;
import de.outstare.kinosim.finance.revenue.Revenue;
import de.outstare.kinosim.finance.revenue.TicketPriceCategory;
import de.outstare.kinosim.finance.revenue.TicketSales;
import de.outstare.kinosim.guests.GuestCalculator;
import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.guests.GuestsShowReport;
import de.outstare.kinosim.housegenerator.AreaMovieTheaterCreator;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.util.Randomness;

/**
 * A ShowSimulator simulates running shows. It mimics people going into cinema halls and how much they enjoy it.
 */
public class ShowSimulator {
	private final Schedule schedule;
	private final GuestCalculator calculator;
	private LocalDate day;
	private GuestsDayReport report;

	private final TicketPriceCategory prices = TicketPriceCategory.createRandom();
	private final IncomeStatement balance = new IncomeStatement(new Taxes(0.1 + 0.1 * Randomness.nextDouble()));
	private final BankAccount bankAccount = new BankAccount();
	// TODO move out of ShowSimulator to a more global place where the MovieTheater is known
	private final Leasehold leasehold = new Leasehold(new AreaMovieTheaterCreator(3000).createTheater(), Cents.of(Randomness
			.getGaussianAround(1600)));

	public ShowSimulator(final Schedule schedule, final GuestCalculator calculator, final LocalDate day) {
		super();
		this.schedule = schedule;
		this.calculator = calculator;
		this.day = day.minusDays(1);
		nextDay();
	}

	public LocalDate getDay() {
		return day;
	}

	public GuestsDayReport getReport() {
		return report;
	}

	public IncomeStatement getBalance() {
		return balance;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void nextDay() {
		day = day.plusDays(1);
		createReport();
		updateBalance();
	}

	private void createReport() {
		report = new GuestsDayReport(calculator, schedule, day);
	}

	private void updateBalance() {
		final Cents oldBalance = balance.getTotalBalance();
		for (final GuestsShowReport showReport : report) {
			Revenue sales = new TicketSales(showReport, prices).getRevenue();
			// replace full description with short description, because day is known and guests are already displayed
			// TODO map show to revenue and use own painting
			final String shortName = String.format("%s %s", showReport.getShow().getStart(), showReport.getShow().getFilm().getTitle());
			sales = new Revenue(sales.getAmount(), shortName);
			balance.addRevenue(RevenueCategory.Revenues, sales);
		}
		final Map<String, Expense> movieCosts = new MovieRental(report, prices).getDistributorExpense();
		for (final Expense distributor : movieCosts.values()) {
			balance.addExpense(ExpenseCategory.CostOfProduction, distributor);
		}
		if (day.getDayOfMonth() == 1) {
			balance.addExpense(ExpenseCategory.OtherOperativeExpenses, leasehold.getMonthlyRate());
		}
		final Cents newBalance = balance.getTotalBalance();
		final Cents difference = newBalance.subtract(oldBalance);
		bankAccount.deposit(difference);
	}
}
