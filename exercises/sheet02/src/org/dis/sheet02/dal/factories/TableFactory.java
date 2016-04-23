package org.dis.sheet02.dal.factories;

import java.util.Date;

public class TableFactory<TEntity> extends BaseSqlFactory<TEntity> {
	private static final String DROP_IF_EXISTS_FORMAT = 
//			"IF EXISTS(SELECT 1 FROM SYSIBM.SYSTABLES "
//			+ "WHERE NAME='%s' AND TYPE='T' AND creator = '%s')\n"
//			+ "THEN \n\tcustomStoredproc('DROP TABLE %s');\nEND IF;";
//			"CALL db2perf_quiet_drop( 'TABLE %s' );";
			"DROP TABLE %s";
	
	private static final String ID_GENERATION_DEFINITION = 
			" PRIMARY KEY GENERATED "
			+ "ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1, NO CACHE)";

	public TableFactory(Class<TEntity> entityType) {
		super(entityType);
	}

	public String buildCreateTableStatement() {
		return getCreateTableStatement();
	}

	private String getCreateTableStatement() {
		String table = getTableName();
		String[] columns = getColumnDefinitions(); 
		return String.format(
				"CREATE TABLE %s(\n\t%s\n)",
				table,
				String.join(",\n\t", columns));
	}

	private String[] getColumnDefinitions() {
		ColumnDef[] columns = getColumns();
		String[] defs = new String[columns.length];
		for (int i = 0; i < defs.length; i++)
			defs[i] = getColumnDefinition(columns[i]);
		return defs;
	}

	private String getColumnDefinition(ColumnDef column) {
		String columnType = getColumnTypeDef(column);
		if (column.isForeignkey())
			return String.format("%s %s %s", 
					column.getColumnName(),
					columnType,
					getForeignKeyDef(column.getForeignKeyDefinition()));
		else 
			return String.format("%s %s", 
					column.getColumnName(),
					columnType);
	}

	private String getForeignKeyDef(ForeignKeyDef fkDef) {
		return String.format(
				"REFERENCES %s(%s)",
				fkDef.getTargetTable(),
				fkDef.getTargetColumn());
	}

	private String getColumnTypeDef(ColumnDef column) {
		StringBuilder typeDef = new StringBuilder();
		Class<?> type = column.getField().getType();
		
		if (type == int.class)
			typeDef.append("INT");
		else if (type == double.class) {
			typeDef.append("NUMERIC");
			
			int scale = column.getScale();
			int precision = column.getPrecision();
			if (scale > 0)
				typeDef.append(String.format("(%d, %d)", scale, precision));
			else 
				throw new IllegalStateException(
						"Missing precision annotaion: " +
						String.format("%s.%s", 
								column.getField().getDeclaringClass().getName(),
								column.getField().getName()));
		}
		else if (type == String.class) {
			typeDef.append("VARCHAR");
			int length = column.getLength();
			if (length > 0)
				typeDef.append(String.format("(%d)", length));
			else
				throw new IllegalStateException(
						"Missing length annotaion: " +
						String.format("%s.%s", 
								column.getField().getDeclaringClass().getName(),
								column.getField().getName()));
		}
		else if (type == Date.class) {
			typeDef.append("DATE");
		}
		
		else 
			throw new IllegalArgumentException(
					"Unknown column type: " + column.getField().getType().getName());
		
		if (!column.isNullable())
			typeDef.append(" NOT NULL");

		if (column.isId()) {
			if (type != int.class)
				throw new IllegalArgumentException(
						"Primary key column must be of type int");
			typeDef.append(ID_GENERATION_DEFINITION);
		}
		return typeDef.toString();
	}

	public String buildDropTableStatement() {
		String table = getTableName();
		return String.format(DROP_IF_EXISTS_FORMAT, table);
	}

}
