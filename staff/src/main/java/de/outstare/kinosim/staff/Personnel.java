package de.outstare.kinosim.staff;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Personnel is the total list of all your members
 */
public class Personnel implements Iterable<Staff> {
	private final Set<Staff> employees = new HashSet<>();

	public void hire(final Staff employee) {
		employees.add(employee);
	}

	public void fire(final Staff employee) {
		employees.remove(employee);
	}

	@Override
	public Iterator<Staff> iterator() {
		return employees.iterator();
	}
}
