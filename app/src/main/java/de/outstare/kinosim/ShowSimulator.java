package de.outstare.kinosim;

import java.time.LocalDate;

import de.outstare.kinosim.guests.GuestCalculator;
import de.outstare.kinosim.guests.GuestsDayReport;
import de.outstare.kinosim.schedule.Schedule;

/**
 * A ShowSimulator simulates running shows. It mimics people going into cinema halls and how much they enjoy it.
 */
public class ShowSimulator {
	private final Schedule			schedule;
	private final GuestCalculator	calculator;
	private LocalDate				day;
	private GuestsDayReport			report;

	public ShowSimulator(final Schedule schedule, final GuestCalculator calculator, final LocalDate day) {
		super();
		this.schedule = schedule;
		this.calculator = calculator;
		this.day = day;
		createReport();
	}

	public LocalDate getDay() {
		return day;
	}

	public GuestsDayReport getReport() {
		return report;
	}

	public void nextDay() {
		day = day.plusDays(1);
		createReport();
	}

	private void createReport() {
		report = new GuestsDayReport(calculator, schedule, day);
	}
}
