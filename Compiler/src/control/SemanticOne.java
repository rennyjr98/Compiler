package control;

import java.util.LinkedList;
import java.util.Stack;
import control.templates.Token;
import control.templates.Error;
import database.SqlEvent;

public class SemanticOne {
	private static Stack<Token> pilaToken = new Stack<Token>();
	private static Stack<String> pilaOperators = new Stack<String>();
	
	public static String[][] matrixSuma = new String[17][17];
	public static String[][] matrixResta = new String[17][17];
	public static String[][] matrixMulti = new String[17][17];
	public static String[][] matrixDiv = new String[17][17];
	public static String[][] matrixRel = new String[17][17];
	public static String[][] matrixOper = new String[17][17];
	public static String[][] matrixDespl = new String[17][17];
	public static String[][] matrixDivEn = new String[17][17];
	
	public static boolean semOneArea = false;
	public static String[] actualCounter = new String[15];
	public static int[] actualCounterInt = new int[14];
	public String assignActual = "";
	public static LinkedList<String[]> semOneCounters = new LinkedList<String[]>();
	
	private static final int DECIMAL = 0, FLOAT = 1, CADENA = 2,
			CHAR = 3, BIN = 4, HEX = 5, OCTAL = 6, COMP = 7,
			BOOL = 8, LIST = 9, ARR = 10, TUPLA = 11, CONJ = 12,
			DICC = 13, NONE = 14, RANGO = 15, VARIANT = 16;
	
	public static void addTokenToPila(Token token) {
		pilaToken.push(token);
	}
	
	public static void addOperatorToPila(String operator) {
		pilaOperators.push(operator);
	}
	
	public static void checkAssing() {
		if(pilaToken.size() == 2 && pilaOperators.size() == 1) { //System.out.println("Condicion cumplida");
			Token val = pilaToken.pop();
			Token id = pilaToken.pop();
			Token result;
			String assign = "";
			String oper = pilaOperators.pop();
			int type = getTypeFromId(id);
				
			switch(oper) {
			case "=": System.out.println("GG");
				if(val.getToken() == -1) {
					Token igualacion = new Token(val.getLine(), getTypeFromId(val), val.getLexema());
					pilaToken.push(igualacion);
				} else
					pilaToken.add(val);
				break;
			case "+=":  System.out.println("YY");
				operTokens(val, id, matrixSuma, oper);
				break;
			case "-=":  System.out.println("PP");
				operTokens(val, id, matrixResta, oper);
				break;
			case "*=":  case "**=": System.out.println("WW");
				operTokens(val, id, matrixMulti, oper);
				break;
			case "/=":  System.out.println("JJ");
				operTokens(val, id, matrixDiv, oper); 
				break;
			case "//=": case "%=": System.out.println("FFF");
				operTokens(val, id, matrixDivEn, oper);
				break;
			}
			
			result = pilaToken.pop();
			//System.out.println(id.getLexema() + " " + oper + " " + result.getLexema() + " - " + pilaToken.size() + " - " +pilaOperators.size());
			//System.out.println(pilaToken.pop().getLexema());
			
			if(type != result.getToken()) { //System.out.println("var: " + type + " result: " +result.getToken());
				String typeVal1 = getTypeOnString(getTypeToken(id));
				String typeVal2 = getTypeOnString(getTypeToken(result));
				
				Analyzer.listError.add(new Error(id.getLine(), 693, "Semantica 1", 
						typeVal1 + " no puede guardar un " + typeVal2, 
						id.getLexema() + " " + oper + " " + result.getLexema()));
			}
			
			actualCounterInt[0] = result.getLine();
			assign = getValueAssign(getTypeOnString(getTypeToken(id)));
			assign += "<-" + getVarAssign(getTypeOnString(getTypeToken(result))); //System.out.println("Tipo: " + getTypeOnString(getTypeToken(result)));
			actualCounter[14] = assign;
			
			for(int i = 0; i < actualCounterInt.length; i++)
				actualCounter[i] = actualCounterInt[i] + "";
			semOneCounters.add(actualCounter);
			actualCounterInt = new int[14];
			actualCounter = new String[15];
		}
	}
	
	private static int getTypeFromId(Token id) {
		int type = 0;
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			type = getTokenFromType(SqlEvent.getType(id.getLexema(), Ambit.getAmbitoStack().get(i)));
			if(type != 0)
				break;
			else {
				type = getTokenFromType(SqlEvent.getClass(id.getLexema(), Ambit.getAmbitoStack().get(i)));
				if(type != 0)
					break;
			}
		}
		return type;
	}
	
	public static void makeOperation() {
		String oper = pilaOperators.pop();
		Token val1 = pilaToken.pop();
		
		if(oper.equals("++") || oper.equals("--")) 
    		pilaToken.add(new Token(val1.getLine(), -11, "1"));
		
		Token val2 = pilaToken.pop();
		
		switch(oper) {
			case "+": operTokens(val1, val2, matrixSuma, oper); break;
			case "-": operTokens(val1, val2, matrixResta, oper); break;
			case "*": case "**": operTokens(val1, val2, matrixMulti, oper); break;
			case "/": operTokens(val1, val2, matrixDiv, oper); break;
			case "==": case "!=": case "<":
			case ">": case ">=": case "<=":
			case "is": case "isnot": case "in":
			case "innot":
				operTokens(val1, val2, matrixRel, oper);
				break;
			case "!": case "||": case "&&": case "##":
	    	case "&": case "|": case "^": 
	    		operTokens(val1, val2, matrixOper, oper);
	    		break;
	    	case "<<": case ">>":
	    		operTokens(val1, val2, matrixDespl, oper);
	    		break;
	    	case "%": case "//":
	    		operTokens(val1, val2, matrixDivEn, oper);
	    		break;
	    	case "++":
	    		pilaToken.push(val1); //System.out.println("Val2 : " + val2.getLexema() + " val1: " + val1.getLexema());
	    		pilaOperators.push("=");
	    		operTokens(val1, val2, matrixSuma, oper);
	    		break;
	    	case "--":
	    		pilaOperators.push("=");
	    		operTokens(val1, val2, matrixResta, oper);
	    		break;
		}
	}
	
	private static void operTokens(Token val1, Token val2, String[][] arrOper, String oper) {
		String type = arrOper[getTypeToken(val1)][getTypeToken(val2)];
		String valorTmp = "";
		
		if(type.equals("error")) {
			String typeVal1 = getTypeOnString(getTypeToken(val1));
			String typeVal2 = getTypeOnString(getTypeToken(val2));
			valorTmp = "variante";
			
			Analyzer.listError.add(new Error(val2.getLine(), 692, "Semantica 1", 
					"La opercion " + typeVal2 + " " + oper + " " + typeVal1 + " no es valida", 
					val2.getLexema() + " " + oper + " " + val1.getLexema()));
		} else {
			valorTmp = type;
		}
		
		setCounter(type); System.out.println("Type: " + type);
		pilaToken.push(new Token(val2.getLine(), getTokenFromType(type), valorTmp));
	}
	
	private static void setCounter(String type) {
		switch(type) {
		case "decimal": actualCounterInt[1]++; break;
		case "binario": case "bin": actualCounterInt[2]++; break;
		case "octal": actualCounterInt[3]++; break;
		case "hexadecimal": case "hex": actualCounterInt[4]++; break;
		case "float": actualCounterInt[5]++; break;
		case "cadena": case "string": actualCounterInt[6]++; break;
		case "char": actualCounterInt[7]++; break;
		case "comp": case "compleja": actualCounterInt[8]++; break;
		case "booleana": case "bool": case "boolean": actualCounterInt[9]++; break;
		case "none": actualCounterInt[10]++; break;
		case "list": case "listas": case "arr": case "arreglos": case "listas-arreglos": actualCounterInt[11]++; break;
		case "rango": case "range": actualCounterInt[12]++; break;
		case "variant": case "variante": actualCounterInt[13]++; break;
		}
	}
	
	private static String getTypeOnString(int pos) {
		switch(pos) {
		case DECIMAL: return "decimal";
		case FLOAT: return "float";
		case CADENA: return "cadena";
		case CHAR: return "char";
		case BIN: return "bin";
		case HEX: return "hex";
		case OCTAL: return "octal";
		case COMP: return "compleja";
		case BOOL: return "bool";
		case LIST: return "list";
		case ARR: return "arr";
		case TUPLA: return "tupla";
		case CONJ: return "conjunto";
		case DICC: return "diccionario";
		case NONE: return "none";
		case RANGO: return "rango";
		case VARIANT: return "variante";
		default: return "none";
		}
	}
	
	private static int getTypeToken(Token token) {
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
		return NONE;
	}
	
	private static int getTypeToken(int token) {
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
		return NONE;
	}
	
	private static int getTypeTokenStruct(Token token) {
		Stack<Integer> pilaAmbit = Ambit.getAmbitoStack();
		for(int i = 0; i < pilaAmbit.size(); i++) {
			switch(SqlEvent.getClass(token.getLexema(), pilaAmbit.get(i))) {
			case "tupla": case "tuplas": return TUPLA;
			case "range": case "rango": return RANGO;
			case "diccionario": return DICC;
			case "conjunto": case "conjuntos": return CONJ;
			case "list": case "listas": return LIST;
			case "arr": case "arreglos": case "listas-arreglos": return ARR;
			default:
				int type = getTokenFromType(SqlEvent.getType(token.getLexema(), pilaAmbit.get(i)));
				int columnType = getTypeToken(type); 
				if(columnType != -1)
					return columnType;
			}
		} return NONE;
	}
	
	private static int getTokenFromType(String type) {
		switch(type) {
		//case -1: break;
		case "float": return -9;
		case "cadena": case "string": return -5;
		case "char": return -4;
		case "compleja": case "comp": return -10;
		case "boolean": case "bool": case "booleana": return -53;
		case "none": return -56;
		case "decimal": return -11;
		case "binario": case "bin": return -6;
		case "hexadecimal": case "hex": return -7;
		case "octal": return -8;
		case "error": return 309;
		case "tupla": case "tuplas": return -800;
		case "range": case "rango": return -801;
		case "diccionario": return -802;
		case "conjunto": case "conjuntos": return -803;
		case "list": case "listas": return -804;
		case "arr": case "arreglos": case "listas-arreglos": return -805;
		default: return 0;
		}
	}
	
	private static String getVarAssign(String type) {
		return "T"+getValueAssign(type);
	}
	
	private static String getValueAssign(String type) {
		switch(type) {
		case "tupla": case "tuplas": return "";
		case "range": return "R";
		case "diccionario": return "";
		case "conjunto": case "conjuntos": return "";
		case "list": case "listas": case "arr": case "arreglos": case "listas-arreglos": return "L";
		case "float": return "F";
		case "cadena": case "string": return "S";
		case "char": return "CH";
		case "compleja": case "comp": return "C";
		case "boolean": case "bool": case "booleana": return "B";
		case "none": return "N";
		case "decimal": return "D";
		case "binario": case "bin": return "DB";
		case "hexadecimal": case "hex": return "DH";
		case "octal": return "DO";
		case "variant": case "variante": return "V";
		default: return "N";
		}
	}
	
	public static void reset() {
		pilaToken.clear();
		pilaOperators.clear();
		semOneArea = false;
		actualCounter = new String[15];
		actualCounterInt = new int[14];
		semOneCounters.clear();
	}
}
