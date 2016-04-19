package org.dis.sheet02.dal;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

/**
 * An abstraction for a database table.
 * 
 * @author Burkhart, Julian
 *
 * @param <TEntity>
 *            The type of the entities that the instance manages.
 */
public class EntitySet<TEntity> {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

	private static final String NULL_KEYWORD = "null";

	/** The connection to use for db access. */
	private final Connection connection;

	private final Class<TEntity> entityType;

	private ColumnDef[] columns;

	/**
	 * Creates a new entity set.
	 * 
	 * @param connection
	 *            The connection to use for db access.
	 * @param entityType
	 *            The class object of the entity type.
	 */
	public EntitySet(Connection connection, Class<TEntity> entityType) {
		this.connection = connection;
		this.entityType = entityType;
	}

	private ColumnDef[] getColumns() {
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

	private ColumnDef getIdColumn() {
		ColumnDef[] columnDefs = getColumns();
		for (ColumnDef def : columnDefs) {
			if (def.isId())
				return def;
		}
		throw new RuntimeException("No idenity column defined!");
	}

	private boolean isNewEntity(TEntity entity) {
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

	public void saveEntity(TEntity entity) throws QueryException {
		String query;
		boolean isNew = isNewEntity(entity);
		query = isNew ? getInsertStatement(entity) : getUpdateStatement(entity);
		try {
			PreparedStatement statement = connection.prepareStatement(
					query, 
					Statement.RETURN_GENERATED_KEYS);
			statement.execute();
			if (isNew) {
				ResultSet keys = statement.getGeneratedKeys();
				if (keys != null && keys.next())
					setId(entity, keys.getInt(1));
			}
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

	private void setId(TEntity entity, int idValue) {
		try {
			Field idField = getIdColumn().getField();
			idField.setAccessible(true);
			idField.set(entity, idValue);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getUpdateStatement(TEntity entity) {
		ColumnDef[] columns = getInsertableColumns();
		String[] values = formatColumnValues(entity, columns);
		String[] setFragments = new String[columns.length];
		for (int i = 0; i < columns.length; i++)
			setFragments[i] = String.format("%s = %s", columns[i].getColumnName(), values[i]);
		String query = String.format(
				"UPDATE %s SET ",
				getTableName(entityType),
				String.join(", ", setFragments));
		return query;
	}

	private String getInsertStatement(TEntity entity) {
		ColumnDef[] columns = getInsertableColumns();
		String[] values = formatColumnValues(entity, columns); 
		String query = String.format(
				"INSERT INTO %s (%s) VALUES (%s)",
				getTableName(entityType),
				String.join(", ", getColumnNames(columns)),
				String.join(", ", values));
		return query;
	}

	private String[] formatColumnValues(TEntity entity, ColumnDef[] columns) {
		String[] values = new String[columns.length];
		try {
			for (int i = 0; i < columns.length; i++){
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

	private String formatValue(Object value) {
		if (value == null)
			return NULL_KEYWORD;
		if (value instanceof Date)
			return String.format("'%s'", DATE_FORMAT.format((Date)value));
		if (value instanceof Integer || value instanceof Long || value instanceof Short)
			return value.toString();
		else 
			return String.format("'%s'", value);
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

	/**
	 * Retrieves all entities from the database.
	 * 
	 * @return A list of all entities.
	 * @throws QueryException
	 *             When an error occurs during execution.
	 */
	public List<TEntity> GetAll() throws QueryException {
		String selectStatement = getSelectAllStatement();
		List<TEntity> entities = null;
		try {
			PreparedStatement statement = connection.prepareStatement(selectStatement);
			statement.execute();
			ResultSet result = statement.getResultSet();
			entities = CreateEntitiesFromResultSet(result);
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return entities;
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
	private List<TEntity> CreateEntitiesFromResultSet(ResultSet result) throws QueryException {
		List<TEntity> entities = new ArrayList<>();
		try {
			while (result.next()) {
				TEntity entity = CreateEntnity(result);
				entities.add(entity);
			}
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			throw new QueryException(e);
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
	private TEntity CreateEntnity(ResultSet result) throws SQLException, IllegalAccessException {
		ColumnDef[] columnDefs = getColumns();
		TEntity entity = createNewEntity();
		for (int i = 0; i < columnDefs.length; i++) {
			ColumnDef columndef = columnDefs[i];
			columndef.getField().setAccessible(true);
			Object value = result.getObject(i+1);
			columndef.getField().set(entity, value);
		}
		return entity;
	}

	/**
	 * Creates a new entity and masks all possible exceptions as runtime errors.
	 * 
	 * @return A new entity.
	 */
	private TEntity createNewEntity() {
		try {
			return entityType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a SELECT statement to retrieve all rows from the entitie's table.
	 * 
	 * @return The SELECT statement.
	 */
	private String getSelectAllStatement() {
		ColumnDef[] columns = getColumns();
		String[] colnames = getColumnNames(columns);
		String tablename = getTableName(entityType);
		return String.format("SELECT %s FROM %s", String.join(", ", colnames), tablename);
	}

	private String[] getColumnNames(ColumnDef[] columns) {
		String[] colnames = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			colnames[i] = columns[i].getColumnName();
		}
		return colnames;
	}

	private String getTableName(Class<TEntity> type) {
		Table annotation = type.getAnnotation(Table.class);
		return annotation != null ? annotation.name() : type.getSimpleName().toUpperCase();
	}

	public void delete(TEntity entity) throws QueryException {
		if (isNewEntity(entity))
			throw new IllegalArgumentException("Cannot delete non-persisted entity.");
		String deleteStatement = getDeleteStatement(entity);
		try {
			PreparedStatement statement = connection.prepareStatement(deleteStatement);
			statement.execute();
			if (statement.getUpdateCount() == 0)
				throw new QueryException("No rows were deleted.");
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}
	
	public TEntity Get(Object id) throws QueryException {
		String query = getSelectAllStatement(id);
		TEntity entity = null;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.execute();
			ResultSet result = statement.getResultSet();
			if (result != null && result.next())
				entity = CreateEntnity(result);
		} catch (Exception e) {
			throw new QueryException(e);
		}
		if (entity == null)
			throw new QueryException(String.format("Entity with key %s was not found.", formatValue(id)));
		return entity;
	}

	private String getSelectAllStatement(Object id) {
		String query = getSelectAllStatement();
		query = query + String.format(" WHERE %s = %s", getIdColumn().getColumnName(), formatValue(id));
		return query;
	}

	private String getDeleteStatement(TEntity entity) {
		ColumnDef idColumn = getIdColumn();
		idColumn.getField().setAccessible(true);
		Object idValue = null;
		try {
			idValue = idColumn.getField().get(entity);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return String.format("DELETE FROM %s WHERE %s = %s", 
				getTableName(entityType),
				idColumn.getColumnName(),
				formatValue(idValue));
	}
}
