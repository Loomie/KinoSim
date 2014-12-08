package de.outstare.kinosim.finance.expenses;

import java.time.YearMonth;

import de.outstare.kinosim.finance.Cents;
import de.outstare.kinosim.staff.Personnel;
import de.outstare.kinosim.staff.Staff;

/**
 * Wages define what is paid to your {@link Personnel}
 */
public class Wages {
	private final Personnel employees;
	private final Cents nettoPerHour;

	public Wages(final Personnel employees, final Cents perHour) {
		this.employees = employees;
		nettoPerHour = perHour;
	}

	public Expense getTotalWages(final YearMonth month) {
		Cents total = Cents.of(0);

		final double bruttoPerHour = nettoPerHour.getValue() * 1.4; // health insurance and so on payed by employer
		final double weekly = 40 * bruttoPerHour;
		final long monthly = (long) (weekly * month.lengthOfMonth() / 7.0);
		System.out.println("Wages.getTotalWages() per Month: " + monthly);

		for (@SuppressWarnings("unused")
		final Staff employee : employees) {
			total = total.add(Cents.of(monthly));
		}

		return new Expense(total, "Wages");
	}
}
