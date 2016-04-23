package org.dis.sheet02.dal.dbcontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.dis.sheet02.dal.factories.EntityFactory;
import org.dis.sheet02.dal.factories.EntityInfo;
import org.dis.sheet02.dal.factories.QueryException;
import org.dis.sheet02.dal.factories.QueryFactory;

/**
 * An abstraction for a database table.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 * @param <TEntity>
 *            The type of the entities that the instance manages.
 */
class EntitySetImpl<TEntity> 
		extends EntityInfo<TEntity>
		implements EntitySet<TEntity> {

	/** The connection to use for db access. */
	private final Connection connection;

	private final QueryFactory<TEntity> queryFactory;

	private final EntityFactory<TEntity> entityFactory;

	/**
	 * Creates a new entity set.
	 * 
	 * @param connection
	 *            The connection to use for db access.
	 * @param entityType
	 *            The class object of the entity type.
	 */
	public EntitySetImpl(Connection connection, Class<TEntity> entityType) {
		super(entityType);
		this.connection = connection;
		this.queryFactory = new QueryFactory<>(entityType);
		this.entityFactory = new EntityFactory<>(entityType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#saveEntity(TEntity)
	 */
	@Override
	public void save(TEntity entity) throws QueryException {
		String query;
		boolean isNew = entityFactory.isNewEntity(entity);
		query = isNew ? queryFactory.buildInsertStatement(entity)
				: queryFactory.buildUpdateStatement(entity);
		try {
			PreparedStatement statement = connection.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			statement.execute();
			if (isNew) {
				ResultSet keys = statement.getGeneratedKeys();
				if (keys != null && keys.next())
					entityFactory.setId(entity, keys.getInt(1));
			}
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#GetAll()
	 */
	@Override
	public List<TEntity> getAll() throws QueryException {
		String selectStatement = queryFactory.buildSelectAllStatement();
		List<TEntity> entities = null;
		try {
			PreparedStatement statement = connection
					.prepareStatement(selectStatement);
			statement.execute();
			ResultSet result = statement.getResultSet();
			entities = entityFactory.createEntities(result);
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#delete(TEntity)
	 */
	@Override
	public void delete(TEntity entity) throws QueryException {
		if (entityFactory.isNewEntity(entity))
			throw new IllegalArgumentException(
					"Cannot delete non-persisted entity.");
		String deleteStatement = queryFactory.buildDeleteStatement(entity);
		try {
			PreparedStatement statement = connection
					.prepareStatement(deleteStatement);
			statement.execute();
			if (statement.getUpdateCount() == 0)
				throw new QueryException("No rows were deleted.");
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#Get(java.lang.Object)
	 */
	@Override
	public TEntity get(Object id) throws QueryException {
		String query = queryFactory.buildSelectAllStatement(id);
		TEntity entity = null;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.execute();
			ResultSet result = statement.getResultSet();
			if (result != null && result.next())
				entity = entityFactory.createEntnity(result);
		} catch (Exception e) {
			throw new QueryException(e);
		}
		if (entity == null)
			throw new QueryException(
					String.format("Entity with key %s was not found.",
							queryFactory.formatValue(id)));
		return entity;
	}
}
