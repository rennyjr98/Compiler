package database;

import java.sql.*;

import javax.swing.JOptionPane;

public class Sql {
    protected String driver = "com.mysql.jdbc.Driver";
    protected String database = "compi";
    protected String hostname = "localhost";
    protected String port = "3306";
    protected String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
    protected String username = "root";
    protected String password = "";
    
	protected Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch(ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(null, 
					"Ups... Esto es un poco vergonzoso, no tengo idea de lo que ha ocurrido, pronto lo resolvere.",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
}
