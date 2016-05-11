package org.dis.sheet02.dal.dbcontext;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.QueryException;

public interface EntitySet<TEntity> {

	TEntity save(TEntity entity) throws SQLException;

	/**
	 * Retrieves all entities from the database.
	 * 
	 * @return A list of all entities.
	 * @throws QueryException
	 *             When an error occurs during execution.
	 */
	List<TEntity> getAll() throws SQLException;

	void delete(TEntity entity) throws SQLException;

	TEntity get(Object id) throws SQLException;

	Class<TEntity> getEntityType();

}