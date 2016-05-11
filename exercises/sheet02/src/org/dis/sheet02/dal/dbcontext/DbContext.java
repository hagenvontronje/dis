package org.dis.sheet02.dal.dbcontext;

import java.sql.SQLException;

public interface DbContext {

	void Close() throws SQLException;

	void CreateSchema() throws SQLException;

}