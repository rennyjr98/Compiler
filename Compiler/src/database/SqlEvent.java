package database;

import java.sql.*;

import javax.swing.JOptionPane;

import control.Ambit;
import control.templates.Symbol;
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
			App.showErrorMessage();
		} return false;
	}
	
	public static ResultSet getTable() {
		try {
			String consult = "SELECT * FROM ambito ORDER BY ambito ASC";
			Statement st = database.createStatement();
			return st.executeQuery(consult);
		} catch(SQLException e) {
			App.showErrorMessage();
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
			st.executeUpdate("INSERT INTO ambito(id, type, class, ambito, tarr, dimarr, nopar, tparr) " + 
			"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"'," +
			"'"+symbol.tarr+"', '"+symbol.dimarr+"', '"+symbol.noPar+"', '"+symbol.TParr+"')");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		}
	}
}
