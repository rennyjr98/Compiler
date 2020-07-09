package control.templates;

import java.util.LinkedList;
import java.util.Stack;

import control.Ambit;
import database.SqlEvent;

public class SemanticTypes {
	private static final int DECIMAL = 0, FLOAT = 1, CADENA = 2,
			CHAR = 3, BIN = 4, HEX = 5, OCTAL = 6, COMP = 7,
			BOOL = 8, LIST = 9, ARR = 10, TUPLA = 11, CONJ = 12,
			DICC = 13, NONE = 14, RANGO = 15, VARIANT = 16;
	
	public static LinkedList<String> sTD = new LinkedList<String>();
	public static LinkedList<String> sTF = new LinkedList<String>();
	public static LinkedList<String> sTS = new LinkedList<String>();
	public static LinkedList<String> sTCH = new LinkedList<String>();
	public static LinkedList<String> sTBIN = new LinkedList<String>();
	public static LinkedList<String> sTH = new LinkedList<String>();
	public static LinkedList<String> sTO = new LinkedList<String>();
	public static LinkedList<String> sTC = new LinkedList<String>();
	public static LinkedList<String> sTDB = new LinkedList<String>();
	public static LinkedList<String> sTL = new LinkedList<String>();
	public static LinkedList<String> sTT = new LinkedList<String>();
	public static LinkedList<String> sTCJ = new LinkedList<String>();
	public static LinkedList<String> sTDC = new LinkedList<String>();
	public static LinkedList<String> sTN = new LinkedList<String>();
	public static LinkedList<String> sTR = new LinkedList<String>();
	public static LinkedList<String> sTV = new LinkedList<String>();
	
	public static int cTD = 1;
	public static int cTF = 1;
	public static int cTS = 1;
	public static int cTCH = 1;
	public static int cTBIN = 1;
	public static int cTH = 1;
	public static int cTO = 1;
	public static int cTC = 1;
	public static int cTDB = 1;
	public static int cTL = 1;
	public static int cTT = 1;
	public static int cTCJ = 1;
	public static int cTDC = 1;
	public static int cTN = 1;
	public static int cTR = 1;
	public static int cTV = 1;
	
	public static int getTmpCounter(String tmp) {
		switch(tmp) {
		case "TD": return cTD;
		case "TF": return cTF;
		case "TS": return cTS;
		case "TCH": return cTCH;
		case "TDB": return cTBIN;
		case "TDH": return cTH;
		case "TDO": return cTO;
		case "TC": return cTC;
		case "TB": return cTDB;
		case "TL": return cTL;
		case "TT": return cTT;
		case "TCJ": return cTCJ;
		case "TDC": return cTDC;
		case "TN": return cTN;
		case "TR": return cTR;
		case "TV": return cTV;
		default: return cTN;
		}
	}
	
	public static void tmpUp(String tmp) {
		switch(tmp) {
		case "TD": ++cTD; break;
		case "TF": ++cTF; break;
		case "TS": ++cTS; break;
		case "TCH": ++cTCH; break;
		case "TDB": ++cTBIN; break;
		case "TDH": ++cTH; break;
		case "TDO": ++cTO; break;
		case "TC": ++cTC; break;
		case "TB": ++cTDB; break;
		case "TL": ++cTL; break;
		case "TT": ++cTT; break;
		case "TCJ": ++cTCJ; break;
		case "TDC": ++cTDC; break;
		case "TN": ++cTN; break;
		case "TR": ++cTR; break;
		case "TV": ++cTV; break;
		default: ++cTN; break;
		}
	}
	
	public static void addTmp(String prefix, String tmp) {
		switch(prefix) {
		case "TD": sTD.add(tmp); break;
		case "TF": sTF.add(tmp); break;
		case "TS": sTS.add(tmp); break;
		case "TCH": sTCH.add(tmp); break;
		case "TDB": sTBIN.add(tmp); break;
		case "TDH": sTH.add(tmp); break;
		case "TDO": sTO.add(tmp); break;
		case "TC": sTC.add(tmp); break;
		case "TB": sTDB.add(tmp); break;
		case "TL": sTL.add(tmp); break;
		case "TT": sTT.add(tmp); break;
		case "TCJ": sTCJ.add(tmp); break;
		case "TDC": sTDC.add(tmp); break;
		case "TN": sTN.add(tmp); break;
		case "TR": sTR.add(tmp); break;
		case "TV": sTV.add(tmp); break;
		default: sTN.add(tmp); break;
		}
	}
	
	public static String getTmp(String tmp) {
		switch(tmp) {
		case "TD": return sTD.remove(0);
		case "TF": return sTF.remove(0);
		case "TS": return sTS.remove(0);
		case "TCH": return sTCH.remove(0);
		case "TDB": return sTBIN.remove(0);
		case "TDH": return sTH.remove(0);
		case "TDO": return sTO.remove(0);
		case "TC": return sTC.remove(0);
		case "TB": return sTDB.remove(0);
		case "TL": return sTL.remove(0);
		case "TT": return sTT.remove(0);
		case "TCJ": return sTCJ.remove(0);
		case "TDC": return sTDC.remove(0);
		case "TN": return sTN.remove(0);
		case "TR": return sTR.remove(0);
		case "TV": return sTV.remove(0);
		default: return sTN.remove(0);
		}
	}
	
	public static String getfTmp(String tmp) {
		switch(tmp) {
		case "TD": return sTD.get(0);
		case "TF": return sTF.get(0);
		case "TS": return sTS.get(0);
		case "TCH": return sTCH.get(0);
		case "TDB": return sTBIN.get(0);
		case "TDH": return sTH.get(0);
		case "TDO": return sTO.get(0);
		case "TC": return sTC.get(0);
		case "TB": return sTDB.get(0);
		case "TL": return sTL.get(0);
		case "TT": return sTT.get(0);
		case "TCJ": return sTCJ.get(0);
		case "TDC": return sTDC.get(0);
		case "TN": return sTN.get(0);
		case "TR": return sTR.get(0);
		case "TV": return sTV.get(0);
		default: return sTN.get(0);
		}
	}
	
	public static String getTypeOnString(int pos) {
		switch(pos) {
		case DECIMAL: return "decimal";
		case FLOAT: return "float";
		case CADENA: return "string";
		case CHAR: return "char";
		case BIN: return "bin";
		case HEX: return "hex";
		case OCTAL: return "oct";
		case COMP: return "comp";
		case BOOL: return "bool";
		case LIST: return "list";
		case ARR: return "arr";
		case TUPLA: return "tupla";
		case CONJ: return "conjunto";
		case DICC: return "diccionario";
		case NONE: return "none";
		case RANGO: return "range";
		case VARIANT: return "variant";
		default: return "none";
		}
	}
	
	public static int getTypeToken(Token token) {
		switch(token.getToken()) {
		case -1: return getTypeTokenStruct(token);
		case -9: return FLOAT;
		case -5: return CADENA;
		case -4: return CHAR;
		case -10: return COMP;
		case -53: case -54: return BOOL;
		case -56: return NONE;
		case -11: return DECIMAL;
		case -6: return BIN;
		case -7: return HEX;
		case -8: return OCTAL;
		case 309: return VARIANT;
		case -800: return TUPLA;
		case -801: return RANGO;
		case -802: return DICC;
		case -803: return CONJ;
		case -804: return LIST;
		case -805: return ARR;
		}
		return -1;
	}
	
	public static int getTypeToken(int token) {
		switch(token) {
		case -9: return FLOAT;
		case -5: return CADENA;
		case -4: return CHAR;
		case -10: return COMP;
		case -53: case -54: return BOOL;
		case -56: return NONE;
		case -11: return DECIMAL;
		case -6: return BIN;
		case -7: return HEX;
		case -8: return OCTAL;
		case 309: return VARIANT;
		case -800: return TUPLA;
		case -801: return RANGO;
		case -802: return DICC;
		case -803: return CONJ;
		case -804: return LIST;
		case -805: return ARR;
		}
		return -1;
	}
	
	public static int getTypeTokenStruct(Token token) {
		Stack<Integer> pilaAmbit = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = pilaAmbit.pop();
			switch(SqlEvent.getClass(token.getLexema().replace("'", "").replace("\"", ""), ambito, token.getLine())) {
			case "tupla": return TUPLA;
			case "range": return RANGO;
			case "diccionario": return DICC;
			case "conjunto": return CONJ;
			case "list": return LIST;
			case "arr": return ARR;
			default:
				int type = getTokenFromType(SqlEvent.getType(token.getLexema().replace("'", "").replace("\"", ""), ambito, token.getLine()));
				int columnType = getTypeToken(type); 
				if(columnType != -1)
					return columnType;
			}
		} return VARIANT;
	}
	
	public static int getTokenFromType(String type) {
		switch(type) {
		case "float": return -9;
		case "string": return -5;
		case "char": return -4;
		case "comp": return -10;
		case "bool": return -53;
		case "none": return -56;
		case "decimal": return -11;
		case "bin": return -6;
		case "hex": return -7;
		case "oct": return -8;
		case "variant": return 309;
		case "tupla": return -800;
		case "range": return -801;
		case "diccionario": return -802;
		case "conjunto": return -803;
		case "list": return -804;
		case "arr": return -805;
		case "func": return -806;
		case "arrdecimal": return -11;
		case "arrfloat": return -9;
		case "arrstring": return -5;
		case "arrchar": return -4;
		case "arrcomp": return -10;
		case "arrbool": return -53;
		case "arrnone": return -56;
		case "arrbin": return -6;
		case "arrhex": return -7;
		case "arroct": return -8;
		case "arrvariant": return -800;
		default: return 0;
		}
	}
	
	public static String getTypeFromToken(int type) {
		switch(type) {
		case -9: return "float";
		case -5: return "string";
		case -4: return "char";
		case -10: return "comp";
		case -53: case -54: return "bool";
		case -56: return "none";
		case -11: return "decimal";
		case -6: return "bin";
		case -7: return "hex";
		case -8: return "oct";
		case 309: return "variant";
		case -800: return "tupla";
		case -801: return "range";
		case -802: return "diccionario";
		case -803: return "conjunto";
		case -804: return "list";
		case -805: return "arr";
		case -806: return "func";
		default: return "none";
		}
	}
	
	public static void reset() {
		cTD = 1;
		cTF = 1;
		cTS = 1;
		cTCH = 1;
		cTBIN = 1;
		cTH = 1;
		cTO = 1;
		cTC = 1;
		cTDB = 1;
		cTL = 1;
		cTT = 1;
		cTCJ = 1;
		cTDC = 1;
		cTN = 1;
		cTR = 1;
		cTV = 1;
		
		sTD.clear();
		sTF.clear();
		sTS.clear();
		sTCH.clear();
		sTBIN.clear();
		sTH.clear();
		sTO.clear();
		sTC.clear();
		sTDB.clear();
		sTL.clear();
		sTT.clear();
		sTCJ.clear();
		sTDC.clear();
		sTN.clear();
		sTR.clear();
		sTV.clear();
	}
}
