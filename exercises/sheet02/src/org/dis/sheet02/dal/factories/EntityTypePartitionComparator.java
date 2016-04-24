package org.dis.sheet02.dal.factories;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator for equality sets of entity classes. The sets are compared by 
 * all their elements. A set A is lesser than set B, if and only if all elements
 * of A are lesser than or equal to all elements of B. 
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class EntityTypePartitionComparator
		implements Comparator<List<Class<?>>> {

	private final EntityTypeComparator entityComparator = new EntityTypeComparator();
	
	@Override
	public int compare(List<Class<?>> o1, List<Class<?>> o2) {
		int preliminary = 0;
		for (Class<?> classA : o1) {
			for (Class<?> classB : o2) {
				int current = entityComparator.compare(classA, classB);
				
				// ordering can change form lesser to greater or vice versa:
				if (Math.abs(current - preliminary) > 1)
					throw new RuntimeException("Paiwise order violation.");
				
				// ordering may change from equal to greater/lesser then:
				if (current != 0)
					preliminary = current;
			}
		}
		return preliminary;
	}

}
