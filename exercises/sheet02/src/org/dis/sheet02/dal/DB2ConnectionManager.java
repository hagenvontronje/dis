package org.dis.sheet02.dal;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Einfaches Singleton zur Verwaltung von Datenbank-Verbindungen.
 * 
 * @author Michael von Riegen
 * @version April 2009
 */
public class DB2ConnectionManager {

	// instance of Driver Manager
	private static DB2ConnectionManager _instance = null;

	private String jdbcUser;

	private String jdbcPass;

	private String jdbcUrl;

	/**
	 * Erzeugt eine Datenbank-Verbindung 
	 */
	private DB2ConnectionManager() {
		try {
			// Holen der Einstellungen aus der db2.properties Datei
			Properties properties = new Properties();
			URL url = ClassLoader.getSystemResource("db2.properties");
			FileInputStream stream = new FileInputStream(new File(url.toURI()));
			properties.load(stream);
			stream.close();

			jdbcUser = properties.getProperty("jdbc_user");
			jdbcPass = properties.getProperty("jdbc_pass");
			jdbcUrl = properties.getProperty("jdbc_url");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Liefert Instanz des Managers
	 * 
	 * @return DB2ConnectionManager
	 */
	public static DB2ConnectionManager getInstance() {
		if (_instance == null) {
			_instance = new DB2ConnectionManager();
		}
		return _instance;
	}

	/**
	 * Liefert eine Verbindung zur DB2 zurC<ck
	 * 
	 * @return Connection A new connection to the database.
	 * @throws SQLException Whe the connection fails.
	 */
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection c = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
			c.setAutoCommit(true);
			return c;
			
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

}