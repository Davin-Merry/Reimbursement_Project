package com.project1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private static ConnectionUtil c = null;
	private Connection conn = null;
	private final String URL = System.getenv("jdbc_URL");
	private final String USERNAME = System.getenv("jdbc_USERNAME");
	private final String PASSWORD = System.getenv("jdbc_PASSWORD");
	
	private ConnectionUtil() {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			//In case the lib was not found for any reason
			e.printStackTrace();
		} catch (SQLException e) {
			//In case the database could not establish a connection
			e.printStackTrace();
		}
	}
	
	public static ConnectionUtil getInstance() throws SQLException {
		if (c == null) {
			c = new ConnectionUtil();
		} else if (c.getConnection().isClosed()) {
			c = new ConnectionUtil();
		}
		return c;
	}
	
	public Connection getConnection() {
		return conn;
	}
}
