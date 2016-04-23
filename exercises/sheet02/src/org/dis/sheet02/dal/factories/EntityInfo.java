package org.dis.sheet02.dal.factories;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Class with basic information about a database entity.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 * @param <TEntity> The type of the represented entity.
 */
public class EntityInfo<TEntity> {

	/** The type of the entity for which to create statements. */
	private final Class<TEntity> entityType;

	/** The columns of the entity's corresponding table. */
	private ColumnDef[] columns;

	public EntityInfo(Class<TEntity> entityType) {
		this.entityType = entityType;
	}

	/**
	 * Retrieves column column definitions for the entity.
	 * @return An array of all column definitions.
	 */
	protected ColumnDef[] getColumns() {
		if (columns == null) {
			Field[] fields = getFields();
			columns = new ColumnDef[fields.length];
			int i = 0;
			for (; i < fields.length; i++)
				columns[i] = new ColumnDef(fields[i]);
			columns = Arrays.copyOf(columns, i);
		}
		return columns;
	}

	/**
	 * Creates an array of all fields in the the entity type with 
	 * {@link Column} annotations.
	 * @return	A list of all column fields.
	 */
	private Field[] getFields() {
		List<Field> fields = new ArrayList<>();
		addFieldsRecursive(entityType, fields);
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * Recursively add all fields with {@link Column} annotations to the list.
	 * @param type
	 * 			The type of which to add the fields.
	 * @param fields
	 * 			The list in which to aggregate the fields.
	 */
	private void addFieldsRecursive(Class<?> type,
									List<Field> fields) {
		// add fields of super class:
		Class<?> superclass = type.getSuperclass();
		if (superclass != null && superclass != Object.class)
			addFieldsRecursive(superclass, fields);
		// add fields of current class:
		for (Field field : type.getDeclaredFields())
			if (field.getAnnotation(Column.class) != null)
				fields.add(field);
	}

	protected ColumnDef getIdColumn() {
		ColumnDef[] columnDefs = getColumns();
		for (ColumnDef def : columnDefs) {
			if (def.isId())
				return def;
		}
		throw new RuntimeException("No idenity column defined!");
	}

	/**
	 * Gets the type of the represented entity.
	 * 
	 * @return The type of the represented entity.
	 */
	public Class<TEntity> getEntityType() {
		return entityType;
	}

	protected String getTableName() {
		Table annotation = entityType.getAnnotation(Table.class);
		return annotation != null ? annotation.name()
				: entityType.getSimpleName().toUpperCase();
	}

	public List<Class<?>> getForeinKeysTypes() {
		List<Class<?>> keys = new ArrayList<>();
		for (ColumnDef column : getColumns())
			if (column.isForeignkey())
				keys.add(column.getForeignKeyDefinition().getTargetType());
		return keys;
	}

}