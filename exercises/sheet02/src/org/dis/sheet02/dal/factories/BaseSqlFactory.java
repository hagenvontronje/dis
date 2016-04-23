package org.dis.sheet02.dal.factories;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseSqlFactory<TEntity> extends EntityInfo<TEntity> {

	/** The date format to use in DML statements. */
	private static final SimpleDateFormat DATE_FORMAT = 
			new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
	
	/** The null keyword to use in SQL statements.*/
	private static final String NULL_KEYWORD = "null";
	
	public BaseSqlFactory(Class<TEntity> entityType) {
		super(entityType);
	}

	protected String[] getColumnNames(ColumnDef[] columns) {
		String[] colnames = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			colnames[i] = columns[i].getColumnName();
		}
		return colnames;
	}

	public String formatValue(Object value) {
		if (value == null)
			return NULL_KEYWORD;
		if (value instanceof Date)
			return String.format("'%s'", DATE_FORMAT.format((Date) value));
		if (value instanceof Integer || value instanceof Long
				|| value instanceof Short)
			return value.toString();
		else
			return String.format("'%s'", value);
	}

}