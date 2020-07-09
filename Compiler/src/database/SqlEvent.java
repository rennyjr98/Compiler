package database;

import java.io.IOException;
import java.sql.*;

import javax.swing.JOptionPane;

import control.Ambit;
import control.templates.SemanticTypes;
import control.templates.Symbol;
import control.templates.SymbolData;
import control.templates.Token;
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
			App.showErrorMessage(); System.out.println(e.getMessage() + "\n linea: " + symbol.line);
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
			System.out.println(e.getMessage() + "\n linea: ");
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
			System.out.println(e.getMessage() + "\n");
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
			System.out.println(e.getMessage() + "\n linea: ");
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
			String consult = "SELECT * FROM ambito WHERE id = '"+symbol.id+"'"
					+ " AND class = 'func' AND ambito = '"+symbol.ambito+"'";
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
			System.out.println(e.getMessage() + "\n linea: " + symbol.line);
		}
	}
	
	public static void sendSymbol(Symbol symbol) {
		try {
			//System.out.println(symbol.id + " -" + symbol.ambito);
			Statement st = database.createStatement();
			st.executeUpdate("INSERT INTO ambito(id, type, class, ambito, rango, avance, tarr, tparr, value, nposicion, llave, list_per, nopar, llave_type, data_return, ambito_padre) " + 
			"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"','" +symbol.rango + "'," +
			"'"+symbol.avance+"', '"+symbol.tarr+"', '"+symbol.TParr+"', '"+symbol.value+"', '"+symbol.nposicion+"', '"+symbol.key+"'," +
			"'"+symbol.list_per+"', '"+symbol.noPar+"', '"+symbol.key_type+"', '"+symbol.data_return+"', '"+symbol.ambito_padre+"')");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
			System.out.println("INSERT INTO ambito(id, type, class, ambito, rango, avance, tarr, tparr, value, nposicion, llave, list_per, nopar, data_return) " + 
					"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"','" +symbol.rango + "'," +
					"'"+symbol.avance+"', '"+symbol.tarr+"', '"+symbol.TParr+"', '"+symbol.value+"', '"+symbol.nposicion+"', '"+symbol.key+"'," +
					"'"+symbol.list_per+"', '"+symbol.noPar+"', '"+symbol.data_return+"')");
		}
	}
	
	public static void sendSymbol(SymbolData symbol) {
		try {
			//System.out.println(symbol.id + " -" + symbol.ambito);
			Statement st = database.createStatement();
			st.executeUpdate("INSERT INTO ambito(id, type, class, ambito, rango, avance, tarr, tparr, value, nposicion, llave, list_per, nopar, llave_type, data_return, ambito_padre) " + 
			"VALUES ('"+symbol.id+"', '"+symbol.type+"', '"+symbol.clase+"', '"+symbol.ambito+"','" +symbol.rango + "'," +
			"'"+symbol.avance+"', '"+symbol.tarr+"', '"+symbol.TParr+"', '"+symbol.value+"', '"+symbol.nposicion+"', '"+symbol.key+"'," +
			"'"+symbol.list_per+"', '"+symbol.noPar+"', '"+symbol.key_type+"', '"+symbol.data_return+"', '"+symbol.ambito_padre+"')");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		}
	}	
	
	public static String getClass(String id, int ambito, int line) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return rs.getString("class");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n" + line);
		} return "NULL";
	}
	
	public static String getType(String id, int ambito, int line) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return rs.getString("type");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n" + line);
		} return "NULL";
	}
	
	public static int getDimentionArr(String id, int ambito, int line) {
		try {
			String consult = "SELECT tparr FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return Integer.parseInt(rs.getString("tparr"));
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		} return -1;
	}
	
	public static boolean validKeyDicc(String id, int ambito, int llave, int line) {
		try {
			String consult = "SELECT llave_type FROM ambito WHERE list_per = '"+id+"' AND ambito_padre = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				if(rs.getInt("llave_type") == llave)
					return true;
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n" + line);
		} return false;
	}
	
	public static String getArrRange(String id, int ambito, int line) {
		try {
			String consult = "SELECT type, value FROM ambito WHERE list_per = '"+id+"' AND ambito = '"+ ambito +"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			String range = "";
			
			while(rs.next()) 
				range += rs.getString("value") + ":";
			return range;
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		} return "NULL";
	}
	
	public static String getArrLength(Token id, int ambito) {
		try {
			String consult = "SELECT type, value FROM ambito WHERE list_per = '"+id.getLexema()+"' AND ambito = '"+ ambito +"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			String range = "";
			
			while(rs.next()) 
				range += rs.getString("value") + ":";
			return range.split(":")[1];
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return "NULL";
	}
	
	public static String getTypeArray(String id, int ambito, int line) {
		try {
			String consult = "SELECT type FROM ambito WHERE list_per = '"+id+"' AND ambito = '"+ ambito +"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return rs.getString("type");
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		} return "NULL";
	}
	
	public static void updateReturns(Symbol symbol, String data, int line) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+symbol.id+"'"
					+ " AND class = 'func' AND ambito = '"+symbol.ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				String returns = rs.getString("data_return");
				returns = (returns.equals("")) ? data : returns + ":" + data;
				String consult2 = "UPDATE ambito set data_return = '"+returns+"' WHERE id = '"+symbol.id+"' AND ambito = '"+symbol.ambito+"'";
				PreparedStatement pstmt = database.prepareStatement(consult2);
				pstmt.executeUpdate();
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		}
	}
	
	public static String getReturns(String id, int ambito, int line) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return rs.getString("data_return");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		} return "NULL";
	}
	
	public static int getNoPar(String id, int ambito, int line) {
		try {
			String consult = "SELECT * FROM ambito WHERE id = '"+id+"' AND ambito = '"+ambito+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return rs.getInt("nopar");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n" + line);
		} return -1;
	}
	
	public static boolean existsKey(String id, int ambito, String key, int line) {
		try {
			String consult = "SELECT llave FROM ambito WHERE list_per = '"+id+"' AND ambito_padre = '"+ambito+"' AND llave = '"+key+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) 
				return true;
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		} return false;
	}
	
	public static String getTypeSubStruct(String id, int ambito, int line) {
		try {
			String consult = "SELECT type FROM ambito WHERE list_per = '"+id+"' AND ambito_padre = '"+ ambito +"' ORDER BY nposicion ASC";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			String subs = "";
			
			while(rs.next()) {
				subs += rs.getString("type") + ":";
			}
			return subs;
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n line: " + line);
		} return "NULL";
	}
	
	public static String getTypeValueDicc(String id, int ambito, String key, int line) {
		try {
			String consult = "SELECT type FROM ambito WHERE list_per = '"+id+"' AND ambito_padre = '"+ambito+"' AND llave = '"+key.toLowerCase()+"'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return rs.getString("type");
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + line);
		} return "NULL";
	}
	
	public static String getRandomSubDato(Token id, int ambito) {
		try {
			String consult = "SELECT type FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "' " +
							 "ORDER BY RAND() LIMIT 1";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return rs.getString("type");
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return "NULL";
	}
	
	public static int getSubdatoAmbito(Token id, int ambito) {
		try {
			String consult = "SELECT ambito FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return rs.getInt("ambito");
			}
		} catch(SQLException e) {
			App.showErrorMessage();
		} return -1;
	}
	
	public static int getLastIndex(Token id, int ambito) {
		try {
			String consult = "SELECT nposicion FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "' " +
							 "ORDER BY nposicion DESC";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return Integer.parseInt(rs.getString("nposicion"));
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage());
		} return -1;
	}
	
	public static void appendDecimalArr(Token id, int ambito, int append) {
		try {
			String [] range = getArrRange(id.getLexema(), ambito, id.getLine()).split(":");
			
			switch(range.length) {
			case 3:
				int tarr = Integer.parseInt(range[1]) + append; System.out.println("TARR: " + tarr);
				String update = "UPDATE ambito SET value = '" + tarr + "' WHERE list_per = '" + id.getLexema() + "' "
								+ "and ambito_padre = '" + ambito + "'" + " and nposicion = '1'";
				PreparedStatement pstmt = database.prepareStatement(update);
				pstmt.executeUpdate();
				break;
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		}
	}
	
	public static int getArrSize(Token id, int ambito) {
		try {
			String consult = "SELECT tarr FROM ambito WHERE id = '" + id.getLexema() + "' and ambito = '" + ambito + "'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return rs.getInt("tarr");
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return -1;
	}
	
	public static void appendArr(Token id, int ambito, int append) {
		try {
			int size = getArrSize(id, ambito);
			int tarr = size + append;
			String update = "UPDATE ambito SET tarr = '" + tarr + "' WHERE id = '" + id.getLexema() + "' "
							+ "and ambito = '" + ambito + "'";
			PreparedStatement pstmt = database.prepareStatement(update);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		}
	}
	
	public static Symbol[] getSubdatos(Token id, int ambito) {
		try {
			Symbol [] symbols = new Symbol[getArrSize(id, ambito)];
			String consult = "SELECT * FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			int index = 0;
			
			while(rs.next()) {
				Symbol tmp = new Symbol();
				tmp.type = rs.getString("type");
				tmp.clase = rs.getString("class");
				tmp.value = rs.getString("value");
				symbols[index] = tmp;
				index++;
			} return symbols;
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return null;
	}
	
	public static String Pop(Token id, int ambito, int index) {
		try {
			String consult = "SELECT type FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "'" +
							 " and nposicion = '" + index + "'";
			String delete = "DELETE FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "'" +
							 " and nposicion = '" + index + "'";
			
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			String type = "";
			
			while(rs.next())
				type = rs.getString("type");
			
			PreparedStatement pstmt = database.prepareStatement(delete);
			pstmt.executeUpdate();
			return type;
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return "NULL";
	}
	
	public static void updatePosition(Token id, int ambito, int position) {
		try {
			String update = "UPDATE ambito SET nposicion = '" + position + "' WHERE list_per = '" + id.getLexema() + "' "
							+ "and ambito_padre = '" + ambito + "' and nposicion = '" + (position+1) + "'";
			PreparedStatement pstmt = database.prepareStatement(update);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		}
	}
	
	public static void updatePositionInsert(Token id, int ambito, int position) {
		try {
			String update = "UPDATE ambito SET nposicion = '" + (position+1) + "' WHERE list_per = '" + id.getLexema() + "' "
							+ "and ambito_padre = '" + ambito + "' and nposicion = '" + position + "'";

			PreparedStatement pstmt = database.prepareStatement(update);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		}
	}
	
	public static int getFirstType(Token id, int ambito, Token value) {
		try {
			String consult = "SELECT nposicion FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "'"
					+ " and type = '" + SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(value.getToken())) + "'"
					+ " ORDER BY nposicion ASC LIMIT 1";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next()) {
				return Integer.parseInt(rs.getString("nposicion"));
			}
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine() + "EE");
		} return -1;
	}
	
	public static void remove(Token id, int ambito, Token value) {
		try {
			int posicion = getFirstType(id, ambito, value);
			String type = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(value));
			String consult = "DELETE FROM ambito WHERE list_per = '" + id.getLexema() + "' and ambito_padre = '" + ambito + "'" +
							 " and type = '" + type + "' and nposicion = '" + posicion + "'";
			PreparedStatement pstmt = database.prepareStatement(consult);
			int borro = pstmt.executeUpdate();
			if(borro > 0)
				appendArr(id, ambito, -1);
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine() + " TT");
		}
	}
	
	public static int getArrTam(Token id, int ambito) {
		try {
			String consult = "SELECT tarr FROM ambito WHERE id = '" + id.getLexema() + "' and ambito = '" + ambito + "'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next())
				return rs.getInt("tarr");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return -1;
	}
	
	public static int getArrDimension(Token id, int ambito) {
		try {
			String consult = "SELECT tparr FROM ambito WHERE id = '" + id.getLexema() + "' and ambito = '" + ambito + "'";
			Statement st = database.createStatement();
			ResultSet rs = st.executeQuery(consult);
			
			while(rs.next())
				return rs.getInt("tparr");
		} catch(SQLException e) {
			App.showErrorMessage();
			System.out.println(e.getMessage() + "\n linea: " + id.getLine());
		} return -1;
	}
}
