package database;

import java.sql.*;

import javax.swing.JOptionPane;

import control.Ambit;
import control.templates.Symbol;
import control.templates.SymbolData;
import design.App;

public class SqlEvent extends Sql {
	private static Connection database;
	
	public SqlEvent() {
		database = getConnection();
	}
	
	public static boolean ifSymbolExists(Symbol symbol) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+symbol.id+"' AND ambito = '"+symbol.ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			while(rs.next())
				return true;
		} catch(SQLException e) {
			App.showErrorMessage(); System.out.println(e.getMessage());
		} return false;
	}
	
	public static int getCountByTipe(String type, String clase, int ambito) {
		try {
			String consult = "SELECT COUNT(id) AS total FROM ambito WHERE type = '"+type+"' AND class = '"+clase+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			while(rs.next())
				return rs.getInt("total");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return 0;
	}
	
	public static int getCountByTipe(String type, int ambito) {
		try {
			String consult = "SELECT COUNT(id) AS total FROM ambito WHERE type = '"+type+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			while(rs.next())
				return rs.getInt("total");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return 0;
	}
	
	public static int getCountTotal() {
		try {
			String consult = "SELECT COUNT(id) AS total FROM ambito";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			while(rs.next())
				return rs.getInt("total");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return 0;
	}
	
	public static int getCountByTipeTotal(String type, String clase) {
		try {
			String consult = "SELECT COUNT(id) AS total FROM ambito WHERE type = '"+type+"' AND class = '"+clase+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			while(rs.next())
				return rs.getInt("total");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return 0;
	}
	
	public static int getCountByTipeTotal(String type) {
		try {
			String consult = "SELECT COUNT(id) AS total FROM ambito WHERE type = '"+type+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			while(rs.next())
				return rs.getInt("total");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return 0;
	}
	
	public static ResultSet getTable() {
		try {
			String consult = "SELECT * FROM ambito";
			Statement st = database.createStatement();
			return st.executeQuery(consult);
		} catch(SQLException e) {
			App.showErrorMessage(); System.out.println(e.getMessage());
		} return null;
	}
	
	public static void deleteAll() {
		try {
			String consult = "delete from ambito";
			PreparedStatement pstmt = database.prepareStatement(consult);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		}
	}
	
	public static void upParam(Symbol symbol) {
		try {
			String consult = "SELECT * FROM ambito WHERE class = 'func' AND ambito = '"+(symbol.ambito-1)+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				String id = rs.getString("id");
				int noPar = rs.getInt("nopar");
				noPar++;
				String consult2 = "UPDATE ambito set noPar = '"+noPar+"', tparr = '"+symbol.ambito+"' WHERE id = '"+id+"' ";
				PreparedStatement pstmt = database.prepareStatement(consult2);
				pstmt.executeUpdate();
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		}
	}
	
	public static void sendSymbol(Symbol symbol) {
		try {
			//System.out.println(symbol.id + " -" + symbol.ambito);
			Statement st = database.createStatement();
			st.executeUpdate("INSERT INTO ambito(id, type, class, ambito, rango, avance, tarr, tparr, value, nposicion, llave, list_per, nopar) " + 
			"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"','" +symbol.rango + "'," +
			"'"+symbol.avance+"', '"+symbol.tarr+"', '"+symbol.TParr+"', '"+symbol.value+"', '"+symbol.nposicion+"', '"+symbol.key+"'," +
			"'"+symbol.list_per+"', '"+symbol.noPar+"')");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
			System.out.println("INSERT INTO ambito(id, type, class, ambito, rango, avance, tarr, tparr, value, nposicion, llave, list_per, nopar) " + 
					"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"','" +symbol.rango + "'," +
					"'"+symbol.avance+"', '"+symbol.tarr+"', '"+symbol.TParr+"', '"+symbol.value+"', '"+symbol.nposicion+"', '"+symbol.key+"'," +
					"'"+symbol.list_per+"', '"+symbol.noPar+"')");
		}
	}
	
	public static void sendSymbol(SymbolData symbol) {
		try {
			//System.out.println(symbol.id + " -" + symbol.ambito);
			Statement st = database.createStatement();
			st.executeUpdate("INSERT INTO ambito(id, type, class, ambito, rango, avance, tarr, tparr, value, nposicion, llave, list_per, nopar) " + 
			"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"','" +symbol.rango + "'," +
			"'"+symbol.avance+"', '"+symbol.tarr+"', '"+symbol.TParr+"', '"+symbol.value+"', '"+symbol.nposicion+"', '"+symbol.key+"'," +
			"'"+symbol.list_per+"', '"+symbol.noPar+"')");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		}
	}
	
	public static String getClass(String id, int ambito) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return rs.getString("class");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return "NULL";
	}
	
	public static String getType(String id, int ambito) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return rs.getString("type");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return "NULL";
	}
}
