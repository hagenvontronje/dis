package org.dis.sheet02.dal.dbcontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dis.sheet02.dal.QueryPrinter;
import org.dis.sheet02.dal.factories.EntityFactory;
import org.dis.sheet02.dal.factories.EntityInfo;
import org.dis.sheet02.dal.factories.QueryFactory;
import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;
import org.dis.sheet02.services.LoginService;

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
	public TEntity save(TEntity entity) throws SQLException {
		String query;
		boolean isNew = entityFactory.isNewEntity(entity);
		query = isNew ? queryFactory.buildInsertStatement(entity)
				: queryFactory.buildUpdateStatement(entity);
		try {
			System.out.println(query);
			PreparedStatement statement = connection.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			statement.execute();
			if (isNew) {
				ResultSet keys = statement.getGeneratedKeys();
				if (keys != null && keys.next()) {
					System.out.printf("New ID: %d%n", keys.getInt(1)); 
					entityFactory.setId(entity, keys.getInt(1));
				}
			}
			return entity;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#GetAll()
	 */
	@Override
	public List<TEntity> getAll() throws SQLException {
		String selectStatement = queryFactory.buildSelectAllStatement();
		if (LoginService.User != null && LoginService.User.getId() >= 0) {
			if (queryFactory.getEntityType() == House.class 
					||queryFactory.getEntityType() == Appartment.class) {
				selectStatement += " WHERE ESTATE_AGENT_ID = " + LoginService.User.getId(); 
			}
			else if (queryFactory.getEntityType() == PurchaseContract.class) {
				selectStatement += " WHERE HOUSE_ID IN (SELECT ID FROM HOUSE WHERE ESTATE_AGENT_ID = " + LoginService.User.getId() + ")";
			}
			else if (queryFactory.getEntityType() == TenancyContract.class) {
				selectStatement += " WHERE APPARTMENT_ID IN (SELECT ID FROM APPARTMENT WHERE ESTATE_AGENT_ID = " + LoginService.User.getId() + ")";
			}
		}
		List<TEntity> entities = null;
		try {
			System.out.println(selectStatement);
			PreparedStatement statement = connection
					.prepareStatement(selectStatement);
			statement.execute();
			ResultSet result = statement.getResultSet();
			QueryPrinter.printResult(result);
			statement.execute();
			result = statement.getResultSet();
			entities = entityFactory.createEntities(result);
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#delete(TEntity)
	 */
	@Override
	public void delete(TEntity entity) throws SQLException {
		if (entityFactory.isNewEntity(entity))
			throw new IllegalArgumentException(
					"Cannot delete non-persisted entity.");
		String deleteStatement = queryFactory.buildDeleteStatement(entity);
		try {
			System.out.println(deleteStatement);
			PreparedStatement statement = connection
					.prepareStatement(deleteStatement);
			statement.execute();
			System.out.printf("Deleted rows: %d%n", statement.getUpdateCount());
			if (statement.getUpdateCount() == 0)
				throw new SQLException("No rows were deleted.");
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dis.sheet02.dal.EntitySet#Get(java.lang.Object)
	 */
	@Override
	public TEntity get(Object id) throws SQLException {
		String query = queryFactory.buildSelectAllStatement(id);
		TEntity entity = null;
		try {
			System.out.println(query);
			PreparedStatement statement = connection.prepareStatement(query);
			statement.execute();
			ResultSet result = statement.getResultSet();
			QueryPrinter.printResult(result);
			statement.execute();
			result = statement.getResultSet();
			if (result != null && result.next())
				entity = entityFactory.createEntnity(result);
		} catch (Exception e) {
			throw new SQLException(e);
		}
		if (entity == null)
			throw new SQLException(
					String.format("Entity with key %s was not found.",
							queryFactory.formatValue(id)));
		return entity;
	}
}
