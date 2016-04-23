package org.dis.sheet02.dal.factories;

import java.util.Arrays;

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
	
	public String buildDropTableStatement(Class<?> entityType) {
		TableFactory<?> factory = new TableFactory<>(entityType);
		return factory.buildDropTableStatement();
	}
	
	/**
	 * Creates the database schema for all {@link EntitySetImpl}s in the database
	 * context.
	 * 
	 * @param context A database context.
	 */
	public String[] buildCreateTableStatements(Class<?>... entityTypes) {
		String[] createTableScripts = new String[entityTypes.length];
		Arrays.sort(entityTypes, new EntityTypeComparator());
		for (int i = 0; i < entityTypes.length; i++) {
			TableFactory<?> factory = new TableFactory<>(entityTypes[i]);
			createTableScripts[i] = factory.buildCreateTableStatement();
		}
		return createTableScripts;
	}
	
}
