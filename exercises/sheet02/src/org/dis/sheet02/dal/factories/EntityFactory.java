package org.dis.sheet02.dal.factories;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory<TEntity> extends EntityInfo<TEntity> {

	public EntityFactory(Class<TEntity> entityType) {
		super(entityType);
	}

	public boolean isNewEntity(TEntity entity) {
		ColumnDef idColumn = getIdColumn();
		try {
			idColumn.getField().setAccessible(true);
			Object idValue = idColumn.getField().get(entity);
			if (idValue instanceof Integer)
				return ((Integer) idValue) <= 0;
			throw new RuntimeException("Unhandled type of identity column: "
					+ idValue.getClass().getSimpleName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setId(TEntity entity, int idValue) {
		try {
			Field idField = getIdColumn().getField();
			idField.setAccessible(true);
			idField.set(entity, idValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates entities with the values of all rows in the result set.
	 * 
	 * @param result
	 *            The {@link ResultSet} to convert.
	 * @return A list of entities.
	 * @throws QueryException
	 *             If an error ocurrs while browsing the result.
	 */
	public List<TEntity> createEntities(ResultSet result)
			throws SQLException {
		List<TEntity> entities = new ArrayList<>();
		try {
			while (result.next()) {
				TEntity entity = createEntnity(result);
				entities.add(entity);
			}
		} catch (SQLException | IllegalArgumentException
				| IllegalAccessException e) {
			throw new SQLException(e);
		}
		return entities;
	}

	/**
	 * Creates a new entity from the current row in the result set.
	 * 
	 * @param result
	 *            The result set to use.
	 * @return A new entity initialized with values from the result set.
	 * @throws SQLException
	 *             When an database error occurs during the execution.
	 * @throws IllegalAccessException
	 *             When initializing the entity fails.
	 */
	public TEntity createEntnity(ResultSet result)
			throws SQLException, IllegalAccessException {
		ColumnDef[] columnDefs = getColumns();
		TEntity entity = createNewEntity();
		for (int i = 0; i < columnDefs.length; i++) {
			ColumnDef columndef = columnDefs[i];
			columndef.getField().setAccessible(true);
			setValue(entity, columndef.getField(), result, i+1);
		}
		return entity;
	}

	/**
	 * Sets a field to a columns value in the result set.
	 * @param object The object on which to set the field.
	 * @param field The field to set.
	 * @param source The result set to use as source.
	 * @param idx The index of the source column.
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void setValue(Object object, Field field, ResultSet source, int idx) 
			throws SQLException 
	{
		Class<?> targetType = field.getType();
			try {
				if (targetType == int.class)
					field.setInt(object, source.getInt(idx));
				else if (targetType == long.class)
					field.setLong(object, source.getLong(idx));
				else if (targetType == double.class)
					field.setDouble(object, source.getDouble(idx));
				else if (targetType == String.class)
					field.set(object, source.getString(idx));
				else if (targetType == Date.class 
						|| targetType == java.util.Date.class)
					field.set(object, source.getDate(idx));
				else 
					throw new IllegalStateException(
							"Unknown field type: " + targetType.getName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

	/**
	 * Creates a new entity and masks all possible exceptions as runtime errors.
	 * 
	 * @return A new entity.
	 */
	private TEntity createNewEntity() {
		try {
			return getEntityType().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
