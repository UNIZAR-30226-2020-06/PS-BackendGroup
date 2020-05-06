package com.espotify.model;
import java.sql.*;
/**
* Clase que abstrae la conexion con la base de datos.
* @author
*
*/
public class ConnectionManager {

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://34.69.44.48:3306/";

	private static final String USER = "root";
	private static final String PASS = "CentralTeruel1.";

	public final static Connection getConnection() throws SQLException {
		Connection conn = null;

		try{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database... NEW!");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static void releaseConnection(Connection conn) throws SQLException {
		conn.close();
	}
}
