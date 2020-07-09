package control;

import java.util.LinkedList;
import java.util.Stack;

import control.templates.Cuadruplo;
import control.templates.Error;
import control.templates.SemanticTable;
import control.templates.SemanticTypes;
import control.templates.Token;
import database.SqlEvent;

public class CalcSemantic {
	private Stack<Token> pilaToken = new Stack<Token>();
	private Stack<String> pilaOperators = new Stack<String>();
	
	public String[][] matrixSuma = new String[17][17];
	public String[][] matrixResta = new String[17][17];
	public String[][] matrixMulti = new String[17][17];
	public String[][] matrixDiv = new String[17][17];
	public String[][] matrixRel = new String[17][17];
	public String[][] matrixOper = new String[17][17];
	public String[][] matrixDespl = new String[17][17];
	public String[][] matrixDivEn = new String[17][17];
	
	public static String[] actualCounter = new String[15];
	public static int[] actualCounterInt = new int[14];
	public static LinkedList<String[]> semOneCounters = new LinkedList<String[]>();
	
	public CalcSemantic() { }
	
	public void makeOperation() {
		String oper = pilaOperators.pop();
		Token val1 = pilaToken.pop();
		
		if(oper.equals("++") || oper.equals("--")) 
    		pilaToken.add(new Token(val1.getLine(), -11, "1"));
		
		Token val2 = pilaToken.pop();
		
		sustituteVariantAlert(val1);
		sustituteVariantAlert(val2);
		
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
	    		pilaToken.push(val1);
	    		pilaOperators.push("=");
	    		operTokens(val1, val2, matrixSuma, oper);
	    		break;
	    	case "--":
	    		pilaOperators.push("=");
	    		operTokens(val1, val2, matrixResta, oper);
	    		break;
		}
	}
	
	private void operTokens(Token val1, Token val2, String[][] arrOper, String oper) {
		String type = null;
		
		if(getTypeFromId(val1) == -806 || getTypeFromId(val2) == -806) {
			String [] returns1 = null;
			String [] returns2 = null;
			
			if(getTypeFromId(val1) == -806)
				returns1 = getReturns(val1).split(":");
			if(getTypeFromId(val2) == -806) {
				returns2 = getReturns(val2).split(":");
			}
			
			if((returns1 != null && !(returns1.length == 1 && returns1[0].equals(""))) && 
			returns2 != null && !(returns2.length == 1 && returns2[0].equals(""))) {
				type = getMethodOper(returns1, returns2, arrOper, val1, val2);
				
			} else if(returns1 != null && !(returns1.length == 1 && returns1[0].equals(""))) {
				
				type = getMethodOper(returns1, SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(val2)).split(":"), arrOper, val1, val2);
			} else if(returns2 != null && !(returns2.length == 1 && returns2[0].equals(""))) {
				
				type = getMethodOper(SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(val1)).split(":"), returns2, arrOper, val1, val2);
			} else  
				type = "error";
		} else 
			type = arrOper[SemanticTypes.getTypeToken(val1)][SemanticTypes.getTypeToken(val2)];
		
		String valorTmp = "";
		
		if(type.equals("error")) {
			String typeVal1 = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(val1));
			String typeVal2 = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(val2));
			valorTmp = "variant";
			
			Analyzer.listError.add(new Error(val2.getLine(), 692, "Semantica 1", 
					"La opercion " + typeVal2 + " " + oper + " " + typeVal1 + " no es valida", 
					val2.getLexema() + " " + oper + " " + val1.getLexema()));
		} else valorTmp = type;
		
		setCounter(valorTmp);
		pilaToken.push(new Token(val2.getLine(), SemanticTypes.getTokenFromType(valorTmp), valorTmp));
		
		if(!(Analyzer.listError.size() > 0)) {
			String tmp1 = getValueAssign(val1.getLexema());
			String tmp2 = getValueAssign(val2.getLexema());
			//System.out.println("Val 2 : " + ((tmp2.equals(val2.getLexema())) ? val2.getLexema() : SemanticTypes.getfTmp(getVarAssign(val2.getLexema()))));
			//System.out.println("Val 1 : " + ((tmp1.equals(val1.getLexema())) ? val1.getLexema() : SemanticTypes.getfTmp(getVarAssign(val1.getLexema()))));
			Analyzer.listCuad.add(new Cuadruplo("", oper, 
					(tmp2.equals(val2.getLexema())) ? val2.getLexema() : 
						(val2.getToken() != -1) ? SemanticTypes.getTmp(getVarAssign(val2.getLexema())) : val2.getLexema(),
					(tmp1.equals(val1.getLexema())) ? val1.getLexema() : 
						(val1.getToken() != -1) ? SemanticTypes.getTmp(getVarAssign(val1.getLexema())) : val1.getLexema(), 
					getVarAssign(valorTmp) + SemanticTypes.getTmpCounter(getVarAssign(valorTmp))));
		}
		//System.out.println(val1.getLexema() + oper + val2.getLexema() + getVarAssign(valorTmp) + SemanticTypes.getTmpCounter(getVarAssign(valorTmp)));
		SemanticTypes.addTmp(getVarAssign(valorTmp), getVarAssign(valorTmp) + SemanticTypes.getTmpCounter(getVarAssign(valorTmp)));
		SemanticTypes.tmpUp(getVarAssign(valorTmp));
	}
	
	private String getMethodOper(String [] returns1, String [] returns2, String [][] arrOper, Token val1, Token val2) {
		String type = "error";
		
		for(String dataTypeVal1 : returns1) {
			for(String dataTypeVal2 : returns2) {
				type = arrOper[SemanticTypes.getTypeToken(SemanticTypes.getTokenFromType(dataTypeVal1))]
						[SemanticTypes.getTypeToken(SemanticTypes.getTokenFromType(dataTypeVal2))];
				if(!type.equals("error"))
					break;
			}
		} return type;
	}
	
	public String getReturns(Token val) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			String datos = SqlEvent.getReturns(val.getLexema(), ambito, val.getLine());
			if(!datos.equals("NULL")) {
				return datos;
			}
		} return "NULL";
	}
	
	private void sustituteVariantAlert(Token variant) {
		String [] parts = variant.getLexema().split(":");
		if(parts.length > 1) {
			variant.setLexema("variant");
			SemanticErrors.setErrorVoidS(variant, parts[2]);
		}
	}
	
	public void addCounterToList(Token id, Token result) {
		actualCounterInt[0] = result.getLine();
		String assign = getValueAssign(SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(id)));
		assign += "<-" + getVarAssign(SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(result)));
		actualCounter[14] = assign;
		
		for(int i = 0; i < actualCounterInt.length; i++)
			actualCounter[i] = actualCounterInt[i] + "";
		semOneCounters.add(actualCounter);
		actualCounterInt = new int[14];
		actualCounter = new String[15];
	}
	
	public int getTypeFromId(Token id) {
		int type = 0;
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			type = SemanticTypes.getTokenFromType(SqlEvent.getType(id.getLexema(), ambito, id.getLine()));
			if(type != 0 && type != -56)
				break;
			else if(type == -56) {
				type = SemanticTypes.getTokenFromType(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()));
				if(type != 0 && type == -806)
					break;
				else return -56;
			} else {
				type = SemanticTypes.getTokenFromType(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()));
				if(type != 0)
					break;
				else type = 309;
			}
		}
		return type;
	}
	
	public void setCounter(String type) {
		switch(type) {
		case "decimal": actualCounterInt[1]++; break;
		case "bin": actualCounterInt[2]++; break;
		case "oct": actualCounterInt[3]++; break;
		case "hex": actualCounterInt[4]++; break;
		case "float": actualCounterInt[5]++; break;
		case "string": actualCounterInt[6]++; break;
		case "char": actualCounterInt[7]++; break;
		case "comp": actualCounterInt[8]++; break;
		case "bool": actualCounterInt[9]++; break;
		case "none": actualCounterInt[10]++; break;
		case "list": case "arr": actualCounterInt[11]++; break;
		case "range": actualCounterInt[12]++; break;
		case "variant": actualCounterInt[13]++; break;
		}
	}
	
	public String getVarAssign(String type) {
		return "T"+getValueAssign(type);
	}
	
	public String getValueAssign(String type) {
		switch(type) {
		case "tupla": return "T";
		case "range": return "R";
		case "diccionario": return "DC";
		case "conjunto": return "CJ";
		case "list": case "arr": return "L";
		case "float": return "F";
		case "string": return "S";
		case "char": return "CH";
		case "comp": return "C";
		case "bool": return "B";
		case "none": return "N";
		case "decimal": return "D";
		case "bin": return "DB";
		case "hex": return "DH";
		case "oct": return "DO";
		case "variant": return "V";
		default: return type;
		}
	}
	
	public void setToken(Token token) {
		pilaToken.push(token);
	}
	
	public Token getToken() {
		return pilaToken.pop();
	}
	
	public Token getfToken() {
		return pilaToken.peek();
	}
	
	public int getSizeTokenStack() {
		return pilaToken.size();
	}
	
	public int getSizeOperatorStack() {
		return pilaOperators.size();
	}
	
	public void setOperator(String oper) {
		pilaOperators.push(oper);
	}
	
	public String getOperator() {
		return pilaOperators.pop();
	}
	
	public Object copyStackToken() {
		return pilaToken.clone();
	}
	
	public void clearPilaToken() {
		pilaToken.clear();
	}
	
	public void clearPilaOper() {
		pilaOperators.clear();
	}
	
	public void reset() {
		pilaToken.clear();
		pilaOperators.clear();
		actualCounter = new String[15];
		actualCounterInt = new int[14];
		semOneCounters.clear();
	}
}
