package org.dis.sheet02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dis.sheet02.dal.ContextBuilder;
import org.dis.sheet02.dal.QueryPrinter;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.ui.LoginDialog;
import org.gnome.gtk.Gtk;

/**
 * The entry point class.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class Program {

	public static void main(String[] args) {
		Gtk.init(args);
		LoginDialog login = new LoginDialog();
		login.showAll();
        Gtk.main();
        System.exit(0);
//		try {
//			DB2ConnectionManager manager = DB2ConnectionManager.getInstance();
//			Connection connection = manager.getConnection();
//			
//			printDbVersion(connection);
//			createSchema(connection);
//			
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	private static void createSchema(Connection connection) throws SQLException {
		RealEstateContext context = ContextBuilder.build();
		context.CreateSchema();
	}

	private static void printDbVersion(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT service_level, fixpack_num FROM TABLE (sysproc.env_get_inst_info()) as INSTANCEINFO");
		QueryPrinter.printResult(statement.executeQuery());
		statement.close();
	}

}
