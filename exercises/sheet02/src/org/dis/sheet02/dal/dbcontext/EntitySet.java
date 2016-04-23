package org.dis.sheet02.dal.dbcontext;

import java.util.List;

import org.dis.sheet02.dal.factories.QueryException;

public interface EntitySet<TEntity> {

	void save(TEntity entity) throws QueryException;

	/**
	 * Retrieves all entities from the database.
	 * 
	 * @return A list of all entities.
	 * @throws QueryException
	 *             When an error occurs during execution.
	 */
	List<TEntity> getAll() throws QueryException;

	void delete(TEntity entity) throws QueryException;

	TEntity get(Object id) throws QueryException;

	Class<TEntity> getEntityType();

}