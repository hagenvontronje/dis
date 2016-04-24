package org.dis.sheet02.dal.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dis.sheet02.dal.dbcontext.EntitySet;

public class SchemaFactory {

	public static final String QUIET_DROP_PROCEDURE = 
			"CREATE PROCEDURE db2perf_quiet_drop( IN statement VARCHAR(1000) )\n" +
					"LANGUAGE SQL\n" +
					"BEGIN\n" +
					"   DECLARE SQLSTATE CHAR(5);\n" +
					"   DECLARE NotThere    CONDITION FOR SQLSTATE '42704';\n" +
					"   DECLARE NotThereSig CONDITION FOR SQLSTATE '42883';\n" +
					"   DECLARE EXIT HANDLER FOR NotThere, NotThereSig\n" +
					"      SET SQLSTATE = '     ';\n" +
					"   SET statement = 'DROP ' || statement;\n" +
					"   EXECUTE IMMEDIATE statement;\n" +
					"END";
	/**
	 * Drop all {@link EntitySet}s in the database context.
	 * 
	 * @param entityTypes The entities to drop.
	 */
	public String[] buildDropTableStatements(Class<?>... entityTypes) {
		String[] dropTableScripts = new String[entityTypes.length];
		entityTypes = sort(entityTypes);
		for (int i = entityTypes.length - 1; i >= 0; i--) {
			TableFactory<?> factory = new TableFactory<>(entityTypes[i]);
			dropTableScripts[i] = factory.buildDropTableStatement();
		}
		return dropTableScripts;
	}
	
	private Class<?>[] sort(Class<?>[] entityTypes) {
		List<List<Class<?>>> partitions = partitionTypes(entityTypes);
		partitions.sort(new EntityTypePartitionComparator());
		List<Class<?>> sortedEntities = new ArrayList<>(entityTypes.length);
		for (List<Class<?>> partition : partitions) {
			sortedEntities.addAll(partition);
		}
		return sortedEntities.toArray(entityTypes);
	}

	private List<List<Class<?>>> partitionTypes(Class<?>[] entityTypes) {
		List<List<Class<?>>> equisets = new ArrayList<List<Class<?>>>(entityTypes.length);
		EntityTypeComparator comparator = new EntityTypeComparator();
		for (Class<?> type : entityTypes) {
			if (equisets.isEmpty()) {
				ArrayList<Class<?>> list = new ArrayList<>();
				list.add(type);
				equisets.add(list);
			}
			else {
				for (List<Class<?>> set : equisets) {
					boolean isEqual = true;
					Iterator<Class<?>> iterator = set.iterator();
					while(iterator.hasNext() && isEqual) {
						isEqual = comparator.compare(type, iterator.next()) == 0;
					}
					if (isEqual) {
						set.add(type);
						type = null;
						break;
					}
				}
				if (type != null){
					ArrayList<Class<?>> list = new ArrayList<>();
					list.add(type);
					equisets.add(list);
				}
			}
		}
		return equisets;
	}

	/**
	 * Creates the database schema for all {@link EntitySet}s in the database
	 * context.
	 * 
	 * @param entityTypes The entities to create.
	 */
	public String[] buildCreateTableStatements(Class<?>... entityTypes) {
		String[] createTableScripts = new String[entityTypes.length];
		entityTypes = sort(entityTypes);
		for (int i = 0; i < entityTypes.length; i++) {
			TableFactory<?> factory = new TableFactory<>(entityTypes[i]);
			createTableScripts[i] = factory.buildCreateTableStatement();
		}
		return createTableScripts;
	}
	
}
