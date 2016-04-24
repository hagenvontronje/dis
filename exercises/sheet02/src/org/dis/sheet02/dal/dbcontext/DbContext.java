package org.dis.sheet02.dal.dbcontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dis.sheet02.dal.DB2ConnectionManager;
import org.dis.sheet02.dal.factories.SchemaFactory;

public class DbContext {

	protected final Connection connection;
	private final Map<Class<?>, EntitySet<?>> entitySets = new HashMap<>();

	public DbContext(Class<?>... entities) {
		try {
			connection = DB2ConnectionManager.getInstance().getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (Class<?> entity : entities) {
			entitySets.put(entity, createSet(entity));
		}
	}

	public void Close() throws SQLException {
		connection.close();
	}

	private <TEntity> EntitySet<TEntity> createSet(Class<TEntity> entityType) {
		return new EntitySetImpl<>(connection, entityType);
	}

	@SuppressWarnings("unchecked")
	protected <TEntity> EntitySet<TEntity> getEntitySet(Class<TEntity> type) {
		if (!entitySets.containsKey(type))
			throw new IllegalArgumentException("Unknown entity type.");
		return (EntitySet<TEntity>) entitySets.get(type);
	}

	public void CreateSchema() throws SQLException {
		SchemaFactory sf = new SchemaFactory();
		String[] dropStatements = sf.buildDropTableStatements(
				entitySets.keySet().toArray(new Class<?>[0]));
		for (int i = dropStatements.length - 1; i >=0; i--) {
			System.out.println(dropStatements[i]);
			dropTableIfExists(dropStatements[i]);
		}
		String[] createStatements = sf.buildCreateTableStatements(
				entitySets.keySet().toArray(new Class<?>[0]));
		for (String createStatement : createStatements) {
			System.out.println(createStatement);
			PreparedStatement stmt = connection.prepareStatement(createStatement);
			stmt.execute();
			stmt.close();
		}
	}

	private void dropTableIfExists(String dropTableStatement)
			throws SQLException {
		try {
			connection.prepareStatement(dropTableStatement).execute();
		} catch (SQLException e) {
			// hide object not found errors:
			if (e.getErrorCode() != -204)
				throw e;
		}
	}
}