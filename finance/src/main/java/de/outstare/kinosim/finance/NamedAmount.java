package de.outstare.kinosim.finance;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A NamedAmount is an amount of money with a name for what it is used.
 */
public class NamedAmount {
	private final Cents amount;
	private final String name;

	public NamedAmount(final Cents amount, final String name) {
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
		final NamedAmount other = (NamedAmount) obj;
		return new EqualsBuilder().append(getAmount(), other.getAmount()).append(getName(), other.getName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(41911, 6343).append(getAmount()).append(getName()).toHashCode();
	}

	/**
	 * @return the amount of money this object represents
	 */
	public Cents getAmount() {
		return amount;
	}

	/**
	 * @return the name of this money
	 */
	public String getName() {
		return name;
	}
}
