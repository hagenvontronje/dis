package org.dis.sheet02.dal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class DB2ConnectionManagerTest {

	@Test
	public void testGetInstance() {
		try {
			DB2ConnectionManager mgr = DB2ConnectionManager.getInstance();
			assertNotNull(mgr);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetConnection() {
		try {
			DB2ConnectionManager mgr = DB2ConnectionManager.getInstance();
			Connection connection = mgr.getConnection();
			assertTrue(!connection.isClosed());
			// assertTrue(connection.isValid(10));
			connection.close();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testConnection() {
		try {
			DB2ConnectionManager mgr = DB2ConnectionManager.getInstance();
			Connection connection = mgr.getConnection();
			printTables(connection);
			System.out.println();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	private void printTables(Connection connection) throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("select tabname from syscat.tables where tabschema = 'VSISP13'");
		
		statement.execute();
		ResultSet resultSet = statement.getResultSet();
		int columnCount = resultSet.getMetaData().getColumnCount();
		while (resultSet.next()){
			for (int col = 0; col < columnCount; col++){
				System.out.print(resultSet.getObject(col+1) + "; ");
			}
			System.out.println();
			printColumns(connection, resultSet.getString(1));
		}
	}

	private void printColumns(Connection connection, String tablename) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(
					String.format(
							"select colname from syscat.columns where tabschema = 'VSISP13' and tabname = '%s'", 
							tablename.trim()));
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			int columnCount = resultSet.getMetaData().getColumnCount();
			while (resultSet.next()) {
				System.out.print("\t");
				for (int col = 0; col < columnCount; col++){
					System.out.print(resultSet.getObject(col+1) + "; ");
				}
				System.out.println();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
