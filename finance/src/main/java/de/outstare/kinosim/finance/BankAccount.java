package de.outstare.kinosim.finance;

public class BankAccount {
	private Cents balance;

	public BankAccount() {
		balance = Cents.of(0);
	}

	public void withdraw(final Cents amount) {
		balance = balance.subtract(amount);
	}

	public void deposit(final Cents amount) {
		balance = balance.add(amount);
	}

	/**
	 * @return the current balance of the account
	 */
	public Cents getBalance() {
		return balance;
	}

	/**
	 * @return The string "Bank account : x€"
	 */
	public String prettyPrint() {
		return "Bank account: " + balance.formatted();
	}
}
