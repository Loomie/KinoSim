package de.outstare.kinosim.finance.expenses;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An Expense is an amount of money we spent.
 */
public class Expense {
	public final int	amount;
	public final String	name;

	public Expense(final int amount, final String name) {
		this.amount = amount;
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		final Expense other = (Expense) obj;
		return new EqualsBuilder().append(amount, other.amount).append(name, other.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(41777, 6257).append(amount).append(name).toHashCode();
	}
}
