package org.dis.sheet02.dal.factories;

import java.lang.reflect.Field;
import java.util.Arrays;

public class EntityInfo<TEntity> {

	/** The type of the entity for which to create statements. */
	protected final Class<TEntity> entityType;
	
	/** The columns of the entity's corresponding table. */
	private ColumnDef[] columns;

	public EntityInfo(Class<TEntity> entityType) {
		this.entityType = entityType;
	}

	protected ColumnDef[] getColumns() {
		if (columns == null) {
			Field[] fields = entityType.getDeclaredFields();
			columns = new ColumnDef[fields.length];
			int i = 0;
			for (; i < fields.length; i++)
				columns[i] = new ColumnDef(fields[i]);
			columns = Arrays.copyOf(columns, i);
		}
		return columns;
	}

	protected ColumnDef getIdColumn() {
		ColumnDef[] columnDefs = getColumns();
		for (ColumnDef def : columnDefs) {
			if (def.isId())
				return def;
		}
		throw new RuntimeException("No idenity column defined!");
	}

}