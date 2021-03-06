package de.outstare.kinosim;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import de.outstare.kinosim.cinema.MovieTheater;
import de.outstare.kinosim.cinema.Room;
import de.outstare.kinosim.cinema.RoomType;
import de.outstare.kinosim.cinema.WorkSpace;
import de.outstare.kinosim.commodities.Inventory;
import de.outstare.kinosim.commodities.Purchasing;
import de.outstare.kinosim.commodities.Selling;
import de.outstare.kinosim.commodities.SellingPrices;
import de.outstare.kinosim.finance.BankAccount;
import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.finance.IncomeStatement;
import de.outstare.kinosim.finance.IncomeStatement.ExpenseCategory;
import de.outstare.kinosim.finance.IncomeStatement.RevenueCategory;
import de.outstare.kinosim.finance.expenses.Expense;
import de.outstare.kinosim.finance.expenses.Leasehold;
import de.outstare.kinosim.finance.expenses.MovieRental;
import de.outstare.kinosim.finance.expenses.Taxes;
import de.outstare.kinosim.finance.expenses.Wages;
import de.outstare.kinosim.finance.revenue.Revenue;
import de.outstare.kinosim.finance.revenue.TicketPriceCategory;
import de.outstare.kinosim.finance.revenue.TicketSales;
import de.outstare.kinosim.guests.GuestCalculator;
import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.guests.GuestsShowReport;
import de.outstare.kinosim.schedule.Schedule;
import de.outstare.kinosim.staff.Personnel;
import de.outstare.kinosim.staff.Staff;
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
	private final Leasehold leasehold;
	private final Wages wages;
	private final Inventory inv;
	private final Selling selling;

	/**
	 * @param theater
	 *            FIXME the theater does not belong to the show simulator!
	 */
	public ShowSimulator(final Schedule schedule, final GuestCalculator calculator, final LocalDate day, final MovieTheater theater) {
		super();
		this.schedule = schedule;
		this.calculator = calculator;
		// TODO move out of ShowSimulator to a more global place where the MovieTheater is known
		leasehold = new Leasehold(theater, Cents.of(Randomness.getGaussianAround(1600)));
		wages = new Wages(hireAllWorkers(theater), Cents.of(Randomness.getGaussianAround(800)));
		final Room storage = theater.getRoomsByType(RoomType.Storage).iterator().next();
		inv = new Inventory(storage.getAllocatedSpace() * 1.8 * 0.2); // only 20 % are fully used for placing goods (remaining space is for walk or
		// wasted)
		selling = new Selling(inv, balance, new SellingPrices(0.5));

		this.day = day.minusDays(1);
		nextDay();
	}

	/**
	 * hires an employee for every work place FIXME move out of ShowSimulator
	 */
	private static Personnel hireAllWorkers(final MovieTheater theater) {
		final Personnel employees = new Personnel();
		for (final Room room : theater.getRooms()) {
			if (!room.getType().isWorkSpace() || room.getType() == RoomType.StaffRoom) { // we don't hire people for idling
				System.out.println("ShowSimulator.hireAllWorkers() skipping " + room);
				continue;
			}
			final WorkSpace workSpace = (WorkSpace) room;
			final int workers = workSpace.getWorkplaceCount();
			System.out.println("ShowSimulator.hireAllWorkers() adding " + workers + " workers for " + room);
			for (char i = 0; i < workers; i++) {
				final Staff employee = Staff.generateRandomStaff();
				employees.hire(employee);
			}
		}
		return employees;
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

	public Inventory getInventory() {
		return inv;
	}

	public Purchasing getPurchasing() {
		return new Purchasing(inv, balance);
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

			selling.sellToGuests(report);
		}
		final Map<String, Expense> movieCosts = new MovieRental(report, prices).getDistributorExpense();
		for (final Expense distributor : movieCosts.values()) {
			balance.addExpense(ExpenseCategory.CostOfProduction, distributor);
		}
		if (day.getDayOfMonth() == 1) {
			balance.addExpense(ExpenseCategory.OtherOperativeExpenses, leasehold.getMonthlyRate());
			final YearMonth lastMonth = YearMonth.from(day.minusMonths(1));
			balance.addExpense(ExpenseCategory.StaffCosts, wages.getTotalWages(lastMonth));
		}
		final Cents newBalance = balance.getTotalBalance();
		final Cents difference = newBalance.subtract(oldBalance);
		bankAccount.deposit(difference);
	}
}
