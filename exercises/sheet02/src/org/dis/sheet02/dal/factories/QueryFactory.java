package org.dis.sheet02.dal.factories;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.persistence.Table;

public class QueryFactory<TEntity> extends BaseSqlFactory<TEntity> {

	public QueryFactory(Class<TEntity> entityType) {
		super(entityType);
	}

	public String buildUpdateStatement(TEntity entity) {
		ColumnDef[] columns = getInsertableColumns();
		String[] values = formatColumnValues(entity, columns);
		String[] setFragments = new String[columns.length];
		for (int i = 0; i < columns.length; i++)
			setFragments[i] = String.format("%s = %s",
					columns[i].getColumnName(), values[i]);
		String query = String.format("UPDATE %s SET \n\t%s",
				getTableName(getEntityType()), String.join(",\n\t", setFragments));
		return query;
	}

	public String buildInsertStatement(TEntity entity) {
		ColumnDef[] columns = getInsertableColumns();
		String[] values = formatColumnValues(entity, columns);
		String query = String.format("INSERT INTO %s (\n\t%s)\nVALUES (%s)",
				getTableName(getEntityType()),
				String.join(",\n\t", getColumnNames(columns)),
				String.join(",\n\t", values));
		return query;
	}

	public String buildDeleteStatement(TEntity entity) {
		ColumnDef idColumn = getIdColumn();
		idColumn.getField().setAccessible(true);
		Object idValue = null;
		try {
			idValue = idColumn.getField().get(entity);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return String.format("DELETE FROM %s WHERE %s = %s",
				getTableName(getEntityType()), idColumn.getColumnName(),
				formatValue(idValue));
	}

	/**
	 * Creates a SELECT statement to retrieve all rows from the entitie's table.
	 * 
	 * @return The SELECT statement.
	 */
	public String buildSelectAllStatement() {
		ColumnDef[] columns = getColumns();
		String[] colnames = getColumnNames(columns);
		String tablename = getTableName(getEntityType());
		return String.format("SELECT %s\n  FROM %s", String.join(",\n       ", colnames),
				tablename);
	}

	public String buildSelectAllStatement(Object id) {
		String query = buildSelectAllStatement();
		query = query + String.format("\n WHERE %s = %s",
				getIdColumn().getColumnName(), formatValue(id));
		return query;
	}

	private String[] formatColumnValues(TEntity entity, ColumnDef[] columns) {
		String[] values = new String[columns.length];
		try {
			for (int i = 0; i < columns.length; i++) {
				Field field = columns[i].getField();
				field.setAccessible(true);
				Object value = field.get(entity);
				values[i] = formatValue(value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return values;
	}

	private ColumnDef[] getInsertableColumns() {
		ColumnDef[] columnDefs = getColumns();
		ColumnDef[] insertable = new ColumnDef[columnDefs.length];
		int i = 0;
		for (int j = 0; j < columnDefs.length; j++)
			if (!columnDefs[j].isId()) {
				insertable[i++] = columnDefs[j];
			}
		return Arrays.copyOf(insertable, i);
	}

	protected String getTableName(Class<TEntity> type) {
		Table annotation = type.getAnnotation(Table.class);
		return annotation != null ? annotation.name()
				: type.getSimpleName().toUpperCase();
	}

}
