package org.dis.sheet02.dal.factories;

import java.util.Comparator;
import java.util.List;

public class EntityTypeComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> a, Class<?> b) {
		if (a == null || b == null)
			throw new IllegalArgumentException("Arguments must not be null.");
		List<Class<?>> keysOfA = new EntityInfo<>(a).getForeinKeysTypes();
		List<Class<?>> keysOfB = new EntityInfo<>(b).getForeinKeysTypes();
		boolean aDependsOnB = keysOfA.contains(b);
		boolean bDependsOnA = keysOfB.contains(a);
		if (aDependsOnB && bDependsOnA)
			throw new IllegalStateException("Circular key reference!");
		if (!(aDependsOnB || bDependsOnA))
			return 0;
		if (aDependsOnB)
			return 1;
		return -1;
	}


}
