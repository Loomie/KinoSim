package de.outstare.kinosim.finance.revenue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import de.outstare.kinosim.finance.Cents;

/**
 * An Revenue is an amount of money we got.
 */
public class Revenue {
	public final Cents	amount;
	public final String	name;

	public Revenue(final Cents amount, final String name) {
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
		final Revenue other = (Revenue) obj;
		return new EqualsBuilder().append(amount, other.amount).append(name, other.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(41911, 6343).append(amount).append(name).toHashCode();
	}
}
