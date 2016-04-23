package org.dis.sheet02.dal.dbcontext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Table;

import org.dis.sheet02.dal.DB2ConnectionManager;
import org.dis.sheet02.dal.factories.SchemaFactory;

public class DbContext {

	protected final Connection connection;
	private final Map<Class<?>, EntitySet<?>> entitySets = new HashMap<>();
	
	public DbContext(Class<?>... entities) {
		connection = DB2ConnectionManager.getInstance().getConnection();
		for (Class<?> entity : entities) {
			entitySets.put(entity, createSet(entity));
		}
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
		for (Class<?> entityType : entitySets.keySet()) {
			System.out.printf(
					"Dropping table %s...\n", 
					entityType.getAnnotationsByType(Table.class)[0].name());
			dropTableIfExists(entityType, sf);
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

	private void dropTableIfExists(Class<?> entityType, SchemaFactory factory) 
			throws SQLException {
		String dropTableStatement = factory.buildDropTableStatement(entityType);
		try {
			connection.prepareStatement(dropTableStatement).execute();
		} catch (SQLException e) {
			// hide object not found errors: 
			if (e.getErrorCode() != -204)
				throw e;
		}
	}
}