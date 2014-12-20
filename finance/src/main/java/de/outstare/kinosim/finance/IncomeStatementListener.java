package de.outstare.kinosim.finance;

/**
 * An IncomeStatementListener is notified if an {@link IncomeStatement} changes.
 */
public interface IncomeStatementListener {
	void balanceChanged(Cents newBalance);
}
