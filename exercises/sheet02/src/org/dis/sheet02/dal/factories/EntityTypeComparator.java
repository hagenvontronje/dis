package org.dis.sheet02.dal.factories;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator for entity classes, that uses foreign key dependencies. Foreign
 * key dependencies only impose a partial ordering, thus this class does not
 * comply with the contract of the {@link Comparator} interface!
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class EntityTypeComparator implements Comparator<Class<?>> {
	
	@Override
	public int compare(Class<?> a, Class<?> b) {
		int val = 0;
		if (a == null || b == null)
			throw new IllegalArgumentException("Arguments must not be null.");
		List<Class<?>> keysOfA = new EntityInfo<>(a).getDependencies();
		List<Class<?>> keysOfB = new EntityInfo<>(b).getDependencies();
		boolean aDependsOnB = keysOfA.contains(b);
		boolean bDependsOnA = keysOfB.contains(a);
		if (aDependsOnB && bDependsOnA)
			throw new IllegalStateException("Circular key reference!");
		if (!(aDependsOnB || bDependsOnA)) {
			val = 0;
		}
		else if (aDependsOnB)
			val = 1;
		else
			val = -1;
		return val;
	}


}
