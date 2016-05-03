package org.dis.sheet02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dis.sheet02.dal.QueryPrinter;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.ui.JLoginDialog;
import org.dis.sheet02.ui.MainWindow;
import org.gnome.gtk.Gtk;

/**
 * The entry point class.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 * @author Silvestrim, Filipe
 *
 */
public class Program {

	public static void main(String[] args) {
		JLoginDialog jlogin = new JLoginDialog();
		try {
//			UIManager.setLookAndFeel(new GTKLookAndFeel());
			javax.swing.SwingUtilities.invokeAndWait(() -> jlogin.setVisible(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
//			while (jlogin.isVisible())
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			if (jlogin.wasSuccessful()) {
//				Gtk.init(args);
//				MainWindow oldMainWindow = new MainWindow();
//				oldMainWindow.showAll();
//				Gtk.main();
//			}
		}
		
        
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
		RealEstateContext context = new RealEstateContext();
		context.CreateSchema();
	}

	private static void printDbVersion(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT service_level, fixpack_num FROM TABLE (sysproc.env_get_inst_info()) as INSTANCEINFO");
		QueryPrinter.printResult(statement.executeQuery());
		statement.close();
	}

}
