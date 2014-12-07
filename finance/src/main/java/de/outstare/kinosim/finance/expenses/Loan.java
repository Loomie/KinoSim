package de.outstare.kinosim.finance.expenses;

import java.time.YearMonth;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.staff.Personnel;
import de.outstare.kinosim.staff.Staff;

/**
 * A Loan defines what is payed to your {@link Personnel}
 */
public class Loan {
	private final Personnel employees;
	private final Cents nettoPerHour;

	public Loan(final Personnel employees, final Cents perHour) {
		this.employees = employees;
		nettoPerHour = perHour;
	}

	public Expense getTotalLoan(final YearMonth month) {
		Cents total = Cents.of(0);

		final double bruttoPerHour = nettoPerHour.getValue() * 1.4; // health insurance and so on payed by employer
		final double weekly = 40 * bruttoPerHour;
		final long monthly = (long) (weekly * month.lengthOfMonth() / 7.0);
		System.out.println("Loan.getTotalLoan() per Month: " + monthly);

		for (final Staff employee : employees) {
			total = total.add(Cents.of(monthly));
		}

		return new Expense(total, "Loans");
	}
}
