package control;

import java.util.LinkedList;
import java.util.Stack;

import control.templates.Token;
import control.templates.Cuadruplo;
import control.templates.Error;
import control.templates.Etiqueta;
import control.templates.SemanticTable;
import control.templates.SemanticTypes;
import control.templates.Symbol;
import control.templates.SymbolData;
import database.SqlEvent;

public class Semantic {
	private static int elementOnArray = 0;
	private static int elementOnMethod = 0;
	private static boolean structError = false;
	public static boolean lessAparitions = false;
	public static boolean returnSomething = false;
	public static int lessCounter = 0;
	private static int lastLine = 0; 
	private static CalcSemantic calcSheet = new CalcSemantic();
	private static SemanticErrors errorSheet = new SemanticErrors(); 
	public static Symbol symbol = new Symbol();
	public static SemanticFunction functions = new SemanticFunction(calcSheet, errorSheet);
	
	public static Stack<Etiqueta> etqStack = new Stack<Etiqueta>();
	
	private static int ambito_tmp = 0;
	
	public static void addTokenToPila(Token token) {
		calcSheet.setToken(token);
	}
	
	public static void addOperatorToPila(String operator) {
		calcSheet.setOperator(operator);
	}
	
	public static void makeOperation() {
		calcSheet.makeOperation();
	}
	
	public static void arrElementUp() {
		elementOnArray++;
	}
	
	public static void methodElementUp() {
		elementOnMethod++;
	}
	
	public static void addRange() {
		if(lessAparitions)
			symbol.tempDato = "-" + calcSheet.getfToken().getLexema();
		else 
			symbol.tempDato = calcSheet.getfToken().getLexema();
		symbol.rango = symbol.tempDato;
		lessAparitions = false;
	}
	
	public static void updateRange() {
		if(lessAparitions)
			symbol.tempDato = "-" + calcSheet.getfToken().getLexema();
		else 
			symbol.tempDato = calcSheet.getfToken().getLexema();
		symbol.rango += "," + symbol.tempDato;
		lessAparitions = false;
	}
	
	public static void addAvance() {
		if(lessAparitions)
			symbol.tempDato = "-" + calcSheet.getfToken().getLexema();
		else
			symbol.tempDato = calcSheet.getfToken().getLexema();
		symbol.avance = symbol.tempDato;
		symbol.id = "range";
		symbol.line = calcSheet.getfToken().getLine();
		lessAparitions = false;
		Semantic.checkDimensionRange(symbol);
	}
	
	public static void checkAssingRule() {
		if(calcSheet.getSizeTokenStack() == 2 && calcSheet.getSizeOperatorStack() == 1) { 
			Token val = calcSheet.getToken();
			Token id = calcSheet.getToken();
			sustituteVariantAlert(val);
			sustituteVariantAlert(id);
			Token result;
			String oper = calcSheet.getOperator();
			int type = (id.getToken() == -1) ? calcSheet.getTypeFromId(id) : id.getToken();
			int rule = 1020;
			
			switch(oper) {
			case "=": 
				if(val.getToken() == -1) {
					Token igualacion = new Token(val.getLine(), calcSheet.getTypeFromId(val), val.getLexema());
					calcSheet.setToken(igualacion);
				} else calcSheet.setToken(val);
				break;
			default: 
				rule = 1021;
				compuestAssign(oper);
				break;
			}
			
			if(!oper.equals("=")) {
				calcSheet.setToken(id);
				calcSheet.setToken(val);
				calcSheet.makeOperation();
			} result = calcSheet.getToken();
			
			if(isFunction(id)) {
				SemanticTable.addRule("1090", "Var/Param/Arr/List/Dicc/range", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
				errorSheet.setMethodIgualationError(id, result);
			} else { 
				SemanticTable.addRule("1090", "Var/Param/Arr/List/Dicc/range", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
				if(result.getToken() == -806)
					checkOperWithMethod(id, result, oper, type, rule);
				else if(type != result.getToken() && result.getToken() != 309 && type != 309) {
					SemanticTable.addRule(rule+"", id.getLexema(), result.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
					errorSheet.setErrorAssign(id, result, oper);
				} else 
					SemanticTable.addRule(rule+"", id.getLexema(), result.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			}
			calcSheet.addCounterToList(id, result);
			if(Analyzer.listError.isEmpty()) {
				String tmp1 = calcSheet.getValueAssign(id.getLexema());
				String tmp2 = calcSheet.getValueAssign(result.getLexema());
				System.out.println("TOKEN: " + result.getToken());
				Analyzer.listCuad.add(new Cuadruplo("", oper, 
						(tmp1.equals(id.getLexema())) ? id.getLexema() : 
							(id.getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(id.getLexema())) : id.getLexema(), 
						"", 
						(tmp2.equals(result.getLexema())) ? result.getLexema() : 
							(val.getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(result.getLexema())) : result.getLexema()));
			}
		} else { 
			calcSheet.clearPilaToken();
			calcSheet.clearPilaOper();
			Ambit.turnOfLessSymbolAparition();
		}
	}
	
	private static void sustituteVariantAlert(Token variant) {
		String [] parts = variant.getLexema().split(":");
		if(parts.length > 1) {
			variant.setLexema("variant");
			SemanticErrors.setErrorVoidS(variant, parts[2]);
		}
	}
	
	public static boolean isFunction(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getType(id.getLexema(), ambito, id.getLine()).equals("none"))
				if(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()).equals("func"))
					return true;
		} return false;
	}
	
	private static void checkOperWithMethod(Token id, Token result, String oper, int type, int rule) { 
		String [] returns = null; 
		Stack<Integer> copyAmbitStack = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			String datos = SqlEvent.getReturns(result.getLexema(), ambito, result.getLine());
			if(!datos.equals("NULL")) {
				returns = datos.split(":");
				break;
			}
		}
		
		if(returns != null && !(returns.length == 1 && returns[0].equals(""))) { 
			for(int i = 0; i < returns.length; i++) {
				if(SemanticTypes.getTokenFromType(returns[i]) == type) { //System.out.println("Regla " + rule);
					SemanticTable.addRule(rule+"", id.getLexema(), result.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
					return;
				}
			}
			SemanticTable.addRule(rule+"", id.getLexema(), result.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setErrorAssign(id, result, oper);
		} else 
			errorSheet.setNoReturnError(id);
	}

	private static void compuestAssign(String oper) {
		switch(oper) {
		case "+=":  
			calcSheet.setOperator("+");
			//operTokens(val, id, calcSheet.matrixSuma, oper);
			break;
		case "-=":
			calcSheet.setOperator("-");
			//operTokens(val, id, matrixResta, oper);
			break;
		case "*=":  case "**=":
			calcSheet.setOperator((oper.equals("*=")) ? "*" : "**");
			//operTokens(val, id, matrixMulti, oper);
			break;
		case "/=":
			calcSheet.setOperator("/");
			//operTokens(val, id, matrixDiv, oper); 
			break;
		case "//=": case "%=":
			calcSheet.setOperator((oper.equals("//=")) ? "//" : "%");
			//operTokens(val, id, matrixDivEn, oper);
			break;
		}
	}
	
	public static void checkLogicalResultRule(int rule) {
			Token result = calcSheet.getToken();
			int type = 0;
			if(calcSheet.getTypeFromId(result) == -806) {
				//if(checkParamSize()) {
					String [] returns = calcSheet.getReturns(result).split(":");
					if(returns != null && !(returns.length == 1 && returns[0].equals(""))) {
						for(String dataType : returns) {
							if(dataType.equals("bool")) {
								type = -53;
								break;
							} else type = SemanticTypes.getTokenFromType(dataType);
						}
					} else {
						type = -56;
					}
				//}
			} else {
				type = SemanticTypes.getTokenFromType(
					   SemanticTypes.getTypeOnString(
					   SemanticTypes.getTypeToken(result)));
			}
			
			switch(type) {
			case -53: case -54: case 309: 
				switch(rule) {
				case 0: 
					addCuadComp("if", result);
					SemanticTable.addRule("1010", "CB", result.getLexema(), result.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
					break;
				case 1:
					addCuadComp("while", result);
					SemanticTable.addRule("1011", "CB", result.getLexema(), result.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
					break;
				case 2:
					addCuadComp("elif", result);
					SemanticTable.addRule("1012", "CB", result.getLexema(), result.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek()); 
					break;
				}
				break;
			default:
				switch(rule) {
				case 0:
					SemanticTable.addRule("1010", "CB", result.getLexema(), result.getLine(), "ERROR", Ambit.getAmbitoStack().peek()); 
					errorSheet.setIfError(result);
					break;
				case 1:
					SemanticTable.addRule("1011", "CB", result.getLexema(), result.getLine(), "ERROR", Ambit.getAmbitoStack().peek()); 
					errorSheet.setWhileError(result); 
					break;
				case 2:
					SemanticTable.addRule("1012", "CB", result.getLexema(), result.getLine(), "ERROR", Ambit.getAmbitoStack().peek()); 
					errorSheet.setElifError(result);
					break;
				}
			}
	}
	
	private static void addCuadComp(String type, Token result) {
		switch(type) {
		case "if":
			addCuadIf(type, result);
			break;
		case "elif":
			addCuadElifJMP(type, result);
			break;
		case "while":
			addCuadWhile(type, result);
			break;
		}
	}
	
	public static void addCuadIfETQ() {
		if(Analyzer.listError.isEmpty())
			etqStack.push(new Etiqueta("if", "if-E", "", ""));
	}
	
	public static void addCuadIf(String type, Token result) {
		if(Analyzer.listError.isEmpty()) {
			String etq = getEtiqueta(); 
			String tmp1 = calcSheet.getValueAssign(result.getLexema());
			String tmpPut = (tmp1.equals(result.getLexema())) ? result.getLexema() : 
				SemanticTypes.getTmp(calcSheet.getVarAssign(result.getLexema()));
			
			if(result.getLexema().equals("true") || result.getLexema().equals("false")) {
				Analyzer.listCuad.add(new Cuadruplo("", "==", 
						tmpPut, 
						tmpPut, 
						calcSheet.getVarAssign("bool") + SemanticTypes.getTmpCounter(calcSheet.getVarAssign("bool"))));
				SemanticTypes.tmpUp("TB");
			}
			
			Analyzer.listCuad.add(new Cuadruplo("", "JF", tmpPut, "", etq));
			
			if(etqStack.peek().getEtiqueta_1().equals(""))
				etqStack.peek().setEtiqueta_1(etq);
			else
				etqStack.peek().setEtiqueta_3(etq);
			etqStack.peek().setEtiqueta_2(etq);
		}
	}
	
	public static void addCuadIfJMP() {
		if(Analyzer.listError.isEmpty()) {
			Etiqueta.IF_ETQ++;
			etqStack.peek().setEtiqueta_2("if-E" + Etiqueta.IF_ETQ);
			Analyzer.listCuad.add(new Cuadruplo("", "JMP", "", "", etqStack.peek().getEtiqueta_2()));
			etqStack.peek().setEtiqueta_2("if-E" + Etiqueta.IF_ETQ);
			Etiqueta.IF_ETQ--;
		}
	}
	
	public static void addCuadJMP() {
		if(Analyzer.listError.isEmpty())
			Analyzer.listCuad.add(new Cuadruplo("", "JMP", "", "", etqStack.peek().getEtiqueta_2()));
	}
	
	public static void addCuadElif(String type) {
		if(Analyzer.listError.isEmpty()) {
			if(!etqStack.peek().firstElif)
				addCuadIfJMP();
			Etiqueta.IF_ETQ++;
			if(etqStack.peek().getEtiqueta_2().equals("if-E" + Etiqueta.IF_ETQ))
				Etiqueta.IF_ETQ++;
			
			String etq = getEtiqueta();
			Analyzer.listCuad.add(new Cuadruplo(etq, "", "", "", ""));
			
			if(etqStack.peek().getEtiqueta_1().equals(""))
				etqStack.peek().setEtiqueta_1("if-E" + Etiqueta.IF_ETQ);
			else
				etqStack.peek().setEtiqueta_3("if-E" + Etiqueta.IF_ETQ);
		}
	}
	
	public static void addCuadElifJMP(String type, Token result) {
		if(Analyzer.listError.isEmpty()) {
			String etq = getEtiqueta();
			Analyzer.listCuad.add(new Cuadruplo("", "JF", "", "", etq));
			
			if(etqStack.peek().getEtiqueta_1().equals(""))
				etqStack.peek().setEtiqueta_1("if-E" + Etiqueta.IF_ETQ);
			else
				etqStack.peek().setEtiqueta_3("if-E" + Etiqueta.IF_ETQ);
			etqStack.peek().firstElif = true;
		}
	}
	
	public static void addCuadElse() {
		if(Analyzer.listError.isEmpty()) {
			if(!etqStack.peek().firstElif)
				addCuadIfJMP();
			Etiqueta.IF_ETQ++;
			/*if(etqStack.peek().getEtiqueta_2().equals("if-E" + Etiqueta.IF_ETQ))
				Etiqueta.IF_ETQ++;*/
			
			String etq = getEtiqueta();
			Analyzer.listCuad.add(new Cuadruplo(etq, "", "", "", ""));
			
			if(etqStack.peek().getEtiqueta_1().equals(""))
				etqStack.peek().setEtiqueta_1("if-E" + Etiqueta.IF_ETQ);
			else
				etqStack.peek().setEtiqueta_3("if-E" + Etiqueta.IF_ETQ);
		}
	}
	
	public static void addCuadWhileETQ() {
		if(Analyzer.listError.isEmpty()) {
			etqStack.push(new Etiqueta("while", "whi-E", "", ""));
			String etq = etqStack.peek().getEtiqueta_1();
	
			etqStack.peek().setEtiqueta_2("whi-E" + (++Etiqueta.WHILE_ETQ));
			Analyzer.listCuad.add(new Cuadruplo(etq, "", "", "", ""));
		}
	}
	
	public static void addCuadWhile(String type, Token result) {
		if(Analyzer.listError.isEmpty()) {
			String etq = etqStack.peek().getEtiqueta_2();
			String tmp1 = calcSheet.getValueAssign(result.getLexema());
			String tmpPut = (tmp1.equals(result.getLexema())) ? result.getLexema() : 
				SemanticTypes.getTmp(calcSheet.getVarAssign(result.getLexema()));
			
			Analyzer.listCuad.add(new Cuadruplo("", "JF", tmpPut, "", etq));
		}
	}
	
	public static void addCuadWhileJMP() {
		if(Analyzer.listError.isEmpty()) {
			String etq = etqStack.peek().getEtiqueta_1();
			Analyzer.listCuad.add(new Cuadruplo("", "JMP", "", "", etq));
		}
	}
	
	public static void addCuadEndIf() {
		if(Analyzer.listError.isEmpty()) {
			if(etqStack.peek().firstElif) {
				int lastIndex = Analyzer.listCuad.size() - 1;
				Analyzer.listCuad.get(lastIndex-1).setResult(etqStack.peek().getEtiqueta_2());
			}
			
			String etq = etqStack.peek().getEtiqueta_2();
			Analyzer.listCuad.add(new Cuadruplo(etq, "", "", "", ""));
			etqStack.pop();
		}
	}
	
	public static void addCuadEnd() {
		if(Analyzer.listError.isEmpty()) {
			String etq = etqStack.peek().getEtiqueta_2();
			Analyzer.listCuad.add(new Cuadruplo(etq, "", "", "", ""));
			etqStack.pop();
		}
	}
	
	private static String getEtiqueta() {
		boolean empty = etqStack.empty();
		
		if(!empty) {
			Etiqueta etq = etqStack.peek();
			boolean etq_1 = etq.getEtiqueta_1().equals("");
			boolean etq_3 = etq.getEtiqueta_3().equals("");
			String etq_tmp = "";
			
			if(etq_1 && etq_3)
				return "";
			else if(!etq_1) {
				etq_tmp = etq.getEtiqueta_1();
				etq.setEtiqueta_1("");
				return etq_tmp;
			} else {
				etq_tmp = etq.getEtiqueta_3();
				etq.setEtiqueta_3("");
				return etq_tmp;
			}
		} return "";
	}
	
	private static void removeETQ() {
		if(!etqStack.empty()) {
			Etiqueta etq = etqStack.peek();
			boolean etq_1 = etq.getEtiqueta_1().equals("");
			boolean etq_2 = etq.getEtiqueta_2().equals("");
			boolean etq_3 = etq.getEtiqueta_3().equals("");
			
			if(etq_1 && etq_2 && etq_3)
				etqStack.pop();
		}
	}
	
	public static void checkStructRules() {
		Token [] attrs = getAttrsFromArr();
		Token id = calcSheet.getToken();
		
		if(isArray(id)) {
			checkArrRule(attrs, id);
			setDataConverted(id, attrs);
			addCuadList(attrs, id);
		} else if(isDicc(id)) {
			checkDiccRule(attrs, id);
		} else if(isTupla(id)) {
			checkDimTupla(attrs, id);
		} else if(isList(id)) {
			checkList(attrs, id);
			sustituteStructForValue(id, attrs);
			addCuadList(attrs, id);
		} else if(isString(id)) {
			if(attrs.length > 0 && attrs.length <= 3)
				calcSheet.setToken(new Token(attrs[0].getLine(), -5, "string"));
			else
				calcSheet.setToken(new Token(attrs[0].getLine(), 309, "variant"));
		} else {
			calcSheet.setToken(new Token(id.getLine(), -56, "~"));
		}
	}
	
	private static void addCuadList(Token [] attrs, Token id) {
		if(Analyzer.listError.isEmpty()) {
			Analyzer.listCuad.add(new Cuadruplo("", "lista", id.getLexema(), "", ""));
			String tmp1, tmp2, tmp3, result;
			switch(attrs.length){
			case 3:
				tmp1 = calcSheet.getValueAssign(attrs[0].getLexema());
				tmp2 = calcSheet.getValueAssign(attrs[1].getLexema());
				tmp3 = calcSheet.getValueAssign(attrs[2].getLexema());
				result = SemanticTypes.getfTmp(calcSheet.getVarAssign(getTypeArray(id)));
				
				Analyzer.listCuad.add(new Cuadruplo("", "", 
						(tmp1.equals(attrs[0].getLexema())) ? attrs[0].getLexema() : 
							(attrs[0].getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[0].getLexema())) : attrs[0].getLexema(), 
						(tmp2.equals(attrs[1].getLexema())) ? attrs[1].getLexema() : 
							(attrs[1].getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[1].getLexema())) : attrs[1].getLexema(), 
						""));
				Analyzer.listCuad.add(new Cuadruplo("", "", 
						(tmp3.equals(attrs[2].getLexema())) ? attrs[2].getLexema() : 
							(attrs[2].getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[2].getLexema())) : attrs[2].getLexema(),
						"", 
						result));
				break;
			case 2:
				tmp1 = calcSheet.getValueAssign(attrs[0].getLexema());
				tmp2 = calcSheet.getValueAssign(attrs[1].getLexema());
				result = SemanticTypes.getfTmp(calcSheet.getVarAssign(getTypeArray(id)));
				
				Analyzer.listCuad.add(new Cuadruplo("", "", 
						(tmp1.equals(attrs[0].getLexema())) ? attrs[0].getLexema() : 
							(attrs[0].getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[0].getLexema())) : attrs[0].getLexema(), 
						(tmp2.equals(attrs[1].getLexema())) ? attrs[1].getLexema() : 
							(attrs[1].getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[1].getLexema())) : attrs[1].getLexema(),
						result));
				break;
			case 1:
				tmp1 = calcSheet.getValueAssign(attrs[0].getLexema());
				String arr;
				if(isArray(id))
					arr = getArrType(id);
				else arr = calcSheet.getfToken().getLexema();
				
				result = SemanticTypes.getfTmp(calcSheet.getVarAssign(arr));
				System.out.println("tmp1: " + tmp1);
				Analyzer.listCuad.add(new Cuadruplo("", "", 
						(tmp1.equals(attrs[0].getLexema())) ? attrs[0].getLexema() : 
							(attrs[0].getToken() != -1) ? SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[0].getLexema())) : attrs[0].getLexema(),  
						"", result));
				break;
			}
		}
	}
	
	private static void checkList(Token [] attrs, Token id) {
		if(attrs.length > 1) {
			errorSheet.setErrorDimensionList(id, attrs.length);
		} else {
			int size = SqlEvent.getArrSize(id, functions.getAmbitID(id));
			if(attrs[0].getToken() == -1) {
				if(!functions.getCategory(id, false).equals("decimal"))
					errorSheet.setErrorTypeValueList(id, attrs[0]);
			} else if(attrs[0].getToken() == -11) {
				if(functions.isNumeric(attrs[0].getLexema()))
					if(Integer.parseInt(attrs[0].getLexema()) >= size)
						errorSheet.setErrorRangeValueList(id, attrs[0]);
			}
		}
	}
	
	private static void setDataConverted(Token id, Token [] attrs) {
		if(structError)
			calcSheet.setToken(new Token(id.getLine(), 309, "variant"));
		else {
			int size = SqlEvent.getArrSize(id, functions.getAmbitID(id));
			boolean acceptIndex = (functions.isNumeric(attrs[0].getLexema())) ?
								  size > Integer.parseInt(attrs[0].getLexema()) :
								  true;
								  
		    if(!acceptIndex) {
				errorSheet.setErrorRangeValueList(id, attrs[0]);
				calcSheet.setToken(new Token(id.getLine(), 309, "variant"));
				
				SemanticTypes.addTmp(calcSheet.getVarAssign("variant"), 
						calcSheet.getVarAssign("variant") + SemanticTypes.getTmpCounter(calcSheet.getVarAssign("variant")));
				SemanticTypes.tmpUp(calcSheet.getVarAssign("variant"));
		    } else {
				calcSheet.setToken(new Token(id.getLine(), SemanticTypes.getTokenFromType(getTypeArray(id)), 
						getTypeArray(id)));
				
				SemanticTypes.addTmp(calcSheet.getVarAssign(getTypeArray(id)), 
						calcSheet.getVarAssign(getTypeArray(id)) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(getTypeArray(id))));
				SemanticTypes.tmpUp(calcSheet.getVarAssign(getTypeArray(id)));
		    }
		} structError = false;
	}
	
	private static void checkArrRule(Token [] attrs, Token id) {
		if(checkArrDimRule(attrs, id)) { 
			if(isArray(id)) {
				if(!checkTypeArrDim(attrs)) {
					SemanticTable.addRule("1040", "CE", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
					errorSheet.setArrIntError(id, getWrongValue(attrs));
					structError = true;
				} else {
					SemanticTable.addRule("1040", "CE", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
					checkRangeArr(attrs, id);
				}
			}
		} else { 
			SemanticTable.addRule("1030", "CE", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setArrDimError(id, attrs.length);
			structError = true;
		}
	}
	
	private static boolean isArray(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()).equals("arr"))
				return true;
		}
		return false;
	}
	
	private static boolean isList(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()).equals("list"))
				return true;
		}
		return false;
	}
	
	private static int getWrongValue(Token [] attrs) {
		for(int i = 0; i < attrs.length; i++)
			if(attrs[i].getToken() != -11 && attrs[i].getToken() != 309)
				return attrs[i].getToken();
		return 600;
	}
	
	private static Token[] getAttrsFromArr() {
		Token [] attrs = new Token [elementOnArray];
		int size = elementOnArray;
		for(int i = 0; i < size; i++)
			attrs[i] = calcSheet.getToken();
		elementOnArray = 0;
		return attrs;
	}
	
	private static boolean checkArrDimRule(Token [] attrs, Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(attrs.length == SqlEvent.getDimentionArr(id.getLexema(), ambito, id.getLine()))
				return true;
		}
		return false;
	}
	
	private static boolean checkTypeArrDim(Token [] attrs) { 
		int type = attrs[0].getToken();
		for(Token dim : attrs) {
			type = dim.getToken();
			if(type != -11) {
				if(type == -1) {
					Stack <Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
					for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
						int ambito = copyAmbitStack.pop();
						String typeIndex = SqlEvent.getType(attrs[0].getLexema(), ambito, attrs[0].getLine()); 
						if(typeIndex.equals("decimal")) {
							return true;
						} else if(typeIndex.equals("none")) {
							String clase = SqlEvent.getClass(attrs[0].getLexema(), ambito, attrs[0].getLine());
							
							if(clase.equals("func")) {
								String returns = SqlEvent.getReturns(attrs[i].getLexema(), ambito, attrs[i].getLine());
								System.out.println("RETURN: " + attrs[i].getLexema());
								if(returns.contains("decimal"))
									return true;
							}
						}
					}
				}
				return false;
			}
		} return true;
	}
	
	private static void checkRangeArr(Token [] attrs, Token id) {
		for(int i = 0; i < attrs.length; i++) {
			if(attrs.length == 3 && i == 0 && !checkArrDimRule(attrs, id))
				continue;
			else if(isDecimalArray(id)) {
				String [] values = getValuesRange(id);
				boolean error = false;// System.out.println("Values length: " + values.length);
				if(attrs[i].getLexema().equals("decimal") || attrs[i].getLexema().equals("variant"))
					continue;
				else if(values != null) {
					switch(values.length) {
					case 1: //System.out.println("ENTRO CASE");
						error = !checkRangeForOneValue(attrs[i], values[0]);
						break;
					case 2: case 3:
						error = !checkRangeForMultiValues(attrs[i], values);
						break;
					} 
					if(error) { 
						SemanticTable.addRule("1050", "CE", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
						errorSheet.setArrRangeError(id, attrs[i].getLexema());
						structError = true;
					}  else 
						SemanticTable.addRule("1050", "CE", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
				}
			}
		}
	}
	
	private static boolean isDecimalArray(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop(); //System.out.println("FROM DB : " + SqlEvent.getTypeArray(id.getLexema(), ambito, id.getLine()));
			if(SqlEvent.getTypeArray(id.getLexema(), ambito, id.getLine()).equals("decimal") || 
					SqlEvent.getTypeArray(id.getLexema(), ambito, id.getLine()).equals("variant")) {
				//System.out.println("ARRAYCORRECT");
				return true;
			}
		}
		return false;
	}
	
	private static String getTypeArray(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			String type = SqlEvent.getTypeArray(id.getLexema(), ambito, id.getLine());
			if(!type.equals("NULL"))
				return type;
		}
		return "none";
	}
	
	private static boolean checkRangeForOneValue(Token pos, String values) {
		if(pos.getToken() == -11) {
			int position = Integer.parseInt(pos.getLexema());
			int limit = Integer.parseInt(values);
			//System.out.println(position + ">= 0 && " + position + " < " + limit);
			return position >= 0 && position < limit;
		} else return true;
	}
	
	private static boolean checkRangeForMultiValues(Token pos, String [] values) {
		if(pos.getToken() == -11) {
			int position = Integer.parseInt(pos.getLexema());
			int minLimit = (int)Math.min(Integer.parseInt(values[0]), Integer.parseInt((values[1])));
			int maxLimit = (int)Math.max(Integer.parseInt(values[0]), Integer.parseInt((values[1])));
			
			if(minLimit < 0 || maxLimit < 0)
				return true;
			else
				return position >= 0 && position < maxLimit;
		} else return true;
	}
	
	private static String [] getValuesRange(Token id) {
		String [] values = null;
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int j = 0; j < Ambit.getAmbitoStack().size(); j++) {
			int ambito = copyAmbitStack.pop();
			if(!SqlEvent.getArrRange(id.getLexema(), ambito, id.getLine()).equals("NULL")) {
				values = SqlEvent.getArrRange(id.getLexema(), ambito, id.getLine()).split(":");
				break;
			}
		} return values;
	}
	
	private static void checkDiccRule(Token [] attrs, Token id) {
		if(!lessAparitions) {
			SemanticTable.addRule("1061", "OR", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(attrs.length > 1) {
				SemanticTable.addRule("1060", "OR", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
				errorSheet.setIndexOutOfBoundDicc(id,  attrs);
				calcSheet.setToken(new Token(attrs[0].getLine(), 309, "variant"));
			} else if(!getIfKeyIsValid(attrs, id)) {
				SemanticTable.addRule("1060", "OR", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
				errorSheet.setDiccTypeError(id, attrs[0].getToken(), attrs[0].getLexema());
				calcSheet.setToken(new Token(attrs[0].getLine(), 309, "variant"));
			} else {
				SemanticTable.addRule("1060", "OR", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
				String pureKey = attrs[0].getLexema().replace("\'", "").replace("\"","");
				String value = SqlEvent.getTypeValueDicc(id.getLexema(), ambito_tmp, pureKey, id.getLine());
				calcSheet.setToken(new Token(attrs[0].getLine(), SemanticTypes.getTokenFromType(value), value));
			}
		} else {
			SemanticTable.addRule("1061", "OR", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			deleteLessTrash();
			errorSheet.setMinusAparitionError(id);
			calcSheet.setToken(new Token(attrs[0].getLine(), 309, "variant"));
		} lessAparitions = false;
	}
	
	private static boolean getIfKeyIsValid(Token [] attrs, Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.validKeyDicc(id.getLexema(), ambito, attrs[0].getToken(), attrs[0].getLine())) {
				String val = attrs[0].getLexema().replace("\'","");
				val = val.replace("\"","");
				if(SqlEvent.existsKey(id.getLexema(), ambito, val, id.getLine())) {
					ambito_tmp = ambito;
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isDicc(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()).equals("diccionario"))
				return true;
		}
		return false;
	}
	
	private static void checkDimTupla(Token [] attrs, Token id) {
		if(!lessAparitions) { 
			SemanticTable.addRule("1071", "CE", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(attrs.length > 1 || attrs.length <= 0) {
				SemanticTable.addRule("1070", "CE", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
				errorSheet.setTuplaDimError(id, attrs.length);
			} else {
				SemanticTable.addRule("1070", "CE", id.getLexema(), id.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			}
			sustituteStructForValue(id, attrs);
		} else {
			SemanticTable.addRule("1071", "CE", id.getLexema(), id.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			deleteLessTrash();
			calcSheet.setToken(new Token(id.getLine(), 309, "variant"));
			errorSheet.setMinusAparitionError(id);
		}
	}
	
	private static void deleteLessTrash() {
		for(int i = 0; i < lessCounter; i++)
			calcSheet.getOperator();
		lessCounter = 0;
		lessAparitions = false;
	}
	
	private static boolean isTupla(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getClass(id.getLexema(), ambito, id.getLine()).equals("tupla"))
				return true;
		}
		return false;
	}
	
	public static void updateReturns() { 
		Token dataReturn = calcSheet.getToken();
		if(dataReturn.getToken() != -1) {
			String type = "";
			type = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(dataReturn));
			if(Ambit.methodsStack.empty()) {
				Symbol tmp = new Symbol();
				tmp.ambito = Ambit.getAmbitoStack().peek();
				tmp.id = dataReturn.getLexema();
				SqlEvent.updateReturns(tmp, type, dataReturn.getLine());
			} else SqlEvent.updateReturns(Ambit.methodsStack.peek(), type, dataReturn.getLine());
		} else  { 
			String type = "variant"; 
			Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
			for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
				int ambito = copyAmbitStack.pop();
				String typeFromDB = SqlEvent.getType(dataReturn.getLexema(), ambito, dataReturn.getLine());
					if(!typeFromDB.equals("NULL")) {
						if(typeFromDB.equals("struct") || typeFromDB.equals("none")) {
							typeFromDB = SqlEvent.getClass(dataReturn.getLexema(), ambito, dataReturn.getLine());
							if(typeFromDB.equals("func"))
								typeFromDB = calcSheet.getReturns(dataReturn);
							if(typeFromDB.equals("arr"))
								typeFromDB += ":arr" + getTypeArray(dataReturn);
						}
						type = typeFromDB;
						break;
					}
			} 
			if(!Ambit.methodsStack.empty()) {
			SqlEvent.updateReturns(Ambit.methodsStack.peek(), type, dataReturn.getLine());
			}
		} returnSomething = true;
		addCuadReturn(dataReturn);
	}
	
	private static void addCuadReturn(Token returns) {
		if(Analyzer.listError.isEmpty()) {
			Analyzer.listCuad.add(new Cuadruplo("", "call", "return", "", ""));
			String funcReturn = "Tdef" + ((Ambit.methodsStack.empty()) ? "Main" : Ambit.methodsStack.peek().id);
			Analyzer.listCuad.add(new Cuadruplo("", "", returns.getLexema(), "", funcReturn));
		}
	}
	
	public static boolean checkParamSize() {
		Token [] params = new Token [elementOnMethod]; 
		for(int i = 0; i < elementOnMethod; i++)
			params[i] = calcSheet.getToken();
		
		Token func = calcSheet.getfToken();
		elementOnMethod = 0;
		
		if(ifElementExists(func)) {
			checkReturn(func);
	
			Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
			for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
				int ambito = copyAmbitStack.pop();
				if(SqlEvent.getNoPar(func.getLexema(), ambito, func.getLine()) == params.length) {
					SemanticTable.addRule("1010", "Param", calcSheet.getfToken().getLexema(), calcSheet.getfToken().getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
					String returns = calcSheet.getReturns(func);
					if(returns.equals("NULL") || returns.equals(""))
						returns = "void";
					else
						returns = "Tdef" + func.getLexema();
					functions.addCuadFunc(func.getLexema(), returns, "", params);
					return true;
				}
			}
			SemanticTable.addRule("1010", "Param", calcSheet.getfToken().getLexema(), calcSheet.getfToken().getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setTamParamsError(func, params.length);
			return false;
		} else return false;
	}
	
	private static boolean ifElementExists(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			Symbol tmp = new Symbol();
			tmp.ambito = ambito;
			tmp.id = id.getLexema();
			if(SqlEvent.ifSymbolExists(tmp))
				return true;
		} return false;
	}
	
	private static void checkReturn(Token func) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < copyAmbitStack.size(); i++) {
			int ambito = copyAmbitStack.size();
			String [] arr = SqlEvent.getReturns(func.getLexema(), ambito, func.getLine()).split(":"); 
			if(arr.length >= 1 && !arr[0].equals("") && !arr.equals("NULL")) {
				SemanticTable.addRule("1110", "id", func.getLexema(), func.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
				return;
			}
		}
		SemanticTable.addRule("1100", "id", func.getLexema(), func.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
	}
	
	public static void checkFor() {
		Token [] attrs = getAttrsFor();
		Token idFor = calcSheet.getToken();
		int typeId = (idFor.getToken() == -1) ? getTypeId(idFor) : idFor.getToken();
		SemanticTable.addRule("1080", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
		
		boolean idVariant = typeId == 309;
		boolean attrsRange = attrs.length > 2;
		boolean attrsType = (attrsRange) ? attrsRange : isValidTypes(getTypeAttrs(attrs[0]));
		
		if(calcSheet.getTypeFromId(attrs[0]) == -806) {
			String [] returns = calcSheet.getReturns(attrs[0]).split(":");
			Token [] attrstmp = (Token[]) attrs.clone();
			
			for(String tipo : returns) {
				attrstmp[0] = new Token(attrs[0].getLine(), SemanticTypes.getTokenFromType(tipo), attrs[0].getLexema());
				if(isValidTypes(tipo) && checkCasesForMethodLoop(attrstmp, attrsRange, idFor, typeId, idVariant, attrsType))
					return;
			} checkCasesForMethod(attrstmp, attrsRange, idFor, typeId, idVariant, attrsType);
			return;
		}
		
		if(idFor.getToken() != -1 || calcSheet.getTypeFromId(idFor) == -806) {
			errorSheet.setForIdError(idFor, " ...");
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			//System.out.println("1");
		} else if(getStructType(attrs[0]) == 309 && !attrsRange) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setForError(idFor, attrs[0].getLexema());
			//System.out.println("2");
			return;
		} else if(typeId == -4 && attrs[0].getToken() == -5) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(Analyzer.listError.isEmpty())
				addCuadForNotRange(idFor, attrs, typeId);
			//System.out.println("3");
			return;
		} else if(idVariant && (attrsRange || attrsType)) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			//System.out.println("4");
			return;
		} else if(typeId == -11 && attrsRange) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(Analyzer.listError.isEmpty())
				addCuadForRange(idFor, attrs, typeId);
			//System.out.println("5");
			return;
		} else if(typeId == getStructType(attrs[0])) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(Analyzer.listError.isEmpty())
				addCuadForNotRange(idFor, attrs, typeId);
			//System.out.println("6");
			return;
		} else {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(attrsRange)
				errorSheet.setForError(idFor, "range(...)");
			else 
				errorSheet.setForError(idFor, attrs[0].getLexema());

			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
		}
	}

	private static void addCuadForNotRange(Token id, Token [] attrs, int typeId) {
		if(Analyzer.listError.isEmpty()) {
			String tmp1 = calcSheet.getValueAssign(attrs[0].getLexema());
			String tmpPut = (tmp1.equals(attrs[0].getLexema())) ? attrs[0].getLexema() : 
				SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[0].getLexema()));
			
			etqStack.add(new Etiqueta("for", "for-E", "", ""));
			Analyzer.listCuad.add(new Cuadruplo("", "=", "Tfor" + etqStack.peek().tfor1, "", "0"));
			Analyzer.listCuad.add(new Cuadruplo("", "call", "len", "", ""));
			etqStack.peek().tfor2 = ++Etiqueta.FOR_ID;
			Analyzer.listCuad.add(new Cuadruplo("", "", tmpPut, "", "Tfor" + etqStack.peek().tfor2));
			etqStack.peek().tfor3 = ++Etiqueta.FOR_ID;
			Analyzer.listCuad.add(new Cuadruplo(etqStack.peek().getEtiqueta_1(), "<", 
					"Tfor" + etqStack.peek().tfor1+"", "Tfor" + etqStack.peek().tfor2,
					"Tfor" + etqStack.peek().tfor3));
			etqStack.peek().setEtiqueta_2("for-E" + (++Etiqueta.FOR_ETQ));
			Analyzer.listCuad.add(new Cuadruplo("", "JF", "Tfor" + etqStack.peek().tfor3, "", etqStack.peek().getEtiqueta_2()));
			Analyzer.listCuad.add(new Cuadruplo("", "lista", tmpPut,"", ""));
			Analyzer.listCuad.add(new Cuadruplo("", "", "Tfor" + etqStack.peek().tfor1, "", SemanticTypes.getTypeFromToken(typeId)));
			Analyzer.listCuad.add(new Cuadruplo("", "=", id.getLexema(),"", SemanticTypes.getTypeFromToken(typeId)));
		}
	}
	
	private static void addCuadForRange(Token id, Token [] attrs, int typeId) {
		if(Analyzer.listError.isEmpty()) {
			String tmp1 = calcSheet.getValueAssign(attrs[1].getLexema());
			String tmpPut = (tmp1.equals(attrs[1].getLexema())) ? attrs[1].getLexema() : 
				SemanticTypes.getTmp(calcSheet.getVarAssign(attrs[1].getLexema()));
			
			String minus = (Integer.parseInt(attrs[2].getLexema()) < Integer.parseInt(attrs[1].getLexema())) ? "" : "-";
			String symbol = (Integer.parseInt(minus + attrs[0].getLexema()) > 0) ? "<" : ">";
			etqStack.add(new Etiqueta("for", "for-E", "", ""));
			etqStack.peek().splice = minus + attrs[0].getLexema();
			
			Analyzer.listCuad.add(new Cuadruplo("", "=", "Tfor" + etqStack.peek().tfor1, "", attrs[2].getLexema()));
			etqStack.peek().tfor2 = ++Etiqueta.FOR_ID;
			etqStack.peek().tfor3 = ++Etiqueta.FOR_ID;
			
			Analyzer.listCuad.add(new Cuadruplo(etqStack.peek().getEtiqueta_1(), symbol, 
					"Tfor" + etqStack.peek().tfor1+"", tmpPut,
					"Tfor" + etqStack.peek().tfor3));
			
			etqStack.peek().setEtiqueta_2("for-E" + (++Etiqueta.FOR_ETQ));
			Analyzer.listCuad.add(new Cuadruplo("", "JF", "Tfor" + etqStack.peek().tfor3, "", etqStack.peek().getEtiqueta_2()));
			Analyzer.listCuad.add(new Cuadruplo("", "=", id.getLexema(),"", "Tfor" + etqStack.peek().tfor1));
		}
	}
	
	public static void addCuadFinalFor() {
		if(Analyzer.listError.isEmpty()) {
			String splice = etqStack.peek().splice;
			if(splice.equals(""))
				splice = "1";
			Analyzer.listCuad.add(new Cuadruplo("", "+=", "Tfor" + etqStack.peek().tfor1, splice, "Tfor" + etqStack.peek().tfor1));
			Analyzer.listCuad.add(new Cuadruplo("", "JMP", "", "", etqStack.peek().getEtiqueta_1()));
		}
	}
	
	private static boolean checkCasesForMethodLoop(Token [] attrs, boolean attrsRange, Token idFor, int typeId, boolean idVariant, boolean attrsType) {
		if(attrs[0].getToken() == 309 && !attrsRange) {
			return true;
		} else if(typeId == -4 && attrs[0].getToken() == -5) {
			return true;
		} else if(idVariant && (attrsRange || attrsType)) {
			return true;
		} else if((typeId == -11 && attrsRange) || (typeId == -11 && attrs[0].getToken()  == -801)) {
			return true;
		} else if(typeId == getStructType(attrs[0])) {
			return true;
		} else { //System.out.println("7");
			return false;
		}
	}
	
	private static boolean checkCasesForMethod(Token [] attrs, boolean attrsRange, Token idFor, int typeId, boolean idVariant, boolean attrsType) {
		if(attrs[0].getToken() == 309 && !attrsRange) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setForError(idFor, attrs[0].getLexema());
			//System.out.println("2");
			return true;
		} else if(typeId == -4 && attrs[0].getToken() == -5) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			//System.out.println("3");
			return true;
		} else if(idVariant && (attrsRange || attrsType)) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			//System.out.println("4");
			return true;
		} else if((typeId == -11 && attrsRange) || (typeId == -11 && attrs[0].getToken()  == -801)) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			//System.out.println("5");
			return true;
		} else if(typeId == getStructType(attrs[0])) {
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			//System.out.println("6");
			return true;
		} else { //System.out.println("7");
			SemanticTable.addRule("1081", "id", idFor.getLexema(), idFor.getLine(), "ACEPTA", Ambit.getAmbitoStack().peek());
			if(attrsRange)
				errorSheet.setForError(idFor, "range(...)");
			else 
				errorSheet.setForError(idFor, attrs[0].getLexema());

			SemanticTable.addRule("1082", "String/Range/Listas", idFor.getLexema(), idFor.getLine(), "ERROR", Ambit.getAmbitoStack().peek());
			return false;
		}
	}
	
	private static int getTypeId(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			String type = SqlEvent.getType(id.getLexema(), ambito, id.getLine());
			if(type.equals("NULL"))
				continue;
			else if(type.equals("struct")) {
				String clase = SqlEvent.getClass(id.getLexema(),ambito, id.getLine());
				return SemanticTypes.getTokenFromType(clase);
			} else {
				return SemanticTypes.getTokenFromType(type);
			}
		} return 309;
	}
	
	private static String getTypeAttrs(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getType(id.getLexema(), ambito, id.getLine()).equals("struct")) {
				String type = SqlEvent.getClass(id.getLexema(), ambito, id.getLine());
				if(isValidTypes(type))
					return type;
			} else if(SqlEvent.getType(id.getLexema(), ambito, id.getLine()).equals("string"))
				return "string";
		}
		return "ERROR";
	}
	
	private static boolean isValidTypes(String type) {
		switch(type) {
		case "string":
		case "range":
		case "arr":
			return true;
		default:
			return false;
		}
	}
	
	private static int getStructType(Token id) {
		if(id.getToken() == -1) { 
			String type = getTypeAttrs(id); //System.out.println("TOKEN : " + id.getLexema());
			
			switch(type) {
			case "string": return -4;
			case "range": return -11;
			case "arr":
				String typeArr = getArrType(id);
				if(typeArr.equals("NULL"))
					return 309;
				else return SemanticTypes.getTokenFromType(typeArr);
			default:
				return 309;
			}
		} else return id.getToken();
	}
	
	private static String getArrType(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>)Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			String type = SqlEvent.getTypeArray(id.getLexema(), copyAmbitStack.pop(), id.getLine());
			if(!type.equals("NULL"))
				return type;
		} return "NULL";
	}
	
	private static Token [] getAttrsFor() {
		int size = calcSheet.getSizeTokenStack();
		Token [] attrs = new Token[size-1];
		for(int i = 0; i < size-1; i++) {
			attrs[i] = calcSheet.getToken();
		}
		
		return attrs;
	}
	
	public static void checkDimensionStruct(Symbol symbol) {
		String type = symbol.listDatos.get(0).type;
		if(isDecimalArray(new Token(symbol.line, SemanticTypes.getTypeToken(SemanticTypes.getTokenFromType(symbol.type)), symbol.id))
		   || type.equals("decimal")) {
			SymbolData [] values = new SymbolData[symbol.listDatos.size()];
			for(int i = 0; i < values.length; i++)
				values[i] = symbol.listDatos.get(i);
			//System.out.println("LENGTH " + values.length);
			if(values.length == 3) {			 //System.out.println("TRES");
				int val1 = Integer.parseInt(values[0].value);
				int val2 = Integer.parseInt(values[1].value);
				int val3 = Integer.parseInt(values[2].value);
				
				if(Ambit.lessCounter == 3) { 
					for(SymbolData symboll : symbol.listDatos)
						symboll.value = symboll.value.replace("-", "");
					symbol.TParr = symbol.listDatos.size();
					val1 *= -1;
					val2 *= -1;
					val3 *= -1;
				}
					//System.out.println("LLEGO");
				if(!checkDeclarationError(val1, val2, val3, symbol)) { //System.out.println("NO ERROR");
					checkDeclarationRangeError(val1, val2, val3, symbol);
					//SemanticTable.addRule(1030, "Decimal", "[" + val1 + ":" + val2 + ":" + val3 + "]", symbol.line, "ACEPTA", symbol.ambito);
				} 
				
			} else if(values.length == 2) {
				int val1 = Integer.parseInt(values[0].value);
				int val2 = Integer.parseInt(values[1].value);
				
				if(Ambit.lessCounter == 2) {
					for(SymbolData symboll : symbol.listDatos)
						symboll.value = symboll.value.replace("-", "");
					symbol.TParr = symbol.listDatos.size();
					val1 *= -1;
					val2 *= -1;
					//SemanticTable.addRule(1030, "Decimal", "[" + val1 + ":" + val2 + "]", symbol.line, "ACEPTA", symbol.ambito);
					return;
				} else {
					//SemanticTable.addRule(1030, "Decimal", "[" + val1 + ":" + val2 + "]", symbol.line, "ERROR", symbol.ambito);
					errorSheet.setArrDimensionError(val1, val2, symbol);
				}
			} else if(values.length == 1) {
				if(Ambit.lessCounter != 1) {
					//SemanticTable.addRule(1030, "Decimal", "[" + values[0].id + "]", symbol.line, "ERROR", symbol.ambito);
					errorSheet.setArrDimensionError(Integer.parseInt(values[0].value), symbol);
				} else {
					for(SymbolData symboll : symbol.listDatos)
						symboll.value = symboll.value.replace("-", "");
					symbol.TParr = symbol.listDatos.size();
					//SemanticTable.addRule(1030, "Decimal", "[" + values[0].id + "]", symbol.line, "ACEPTA", symbol.ambito);
				}
			} Ambit.lessCounter = 0;
		} else {
			if(Ambit.lessCounter > 0) {
				errorSheet.setMinusAparitionError(symbol);
				symbol.TParr = 1;
			}
		}
	}
	
	private static boolean checkDeclarationError(int val1, int val2, int val3, Symbol symbol) {
		if(val1 < 0 && val2 < 0 && val3 < 0)
			return false;
		else if(val1 >= 0 && val2 >= 0)
			return false;
		else {
			errorSheet.setMinusAparitionError(symbol);
			if(val1 < 0)
				symbol.listDatos.get(0).type = "variant";
			if(val2 < 0)
				symbol.listDatos.get(1).type = "variant";
			return true;
		}
	}
	
	private static void checkDeclarationRangeError(int val1, int val2, int val3, Symbol symbol) {
		if(val1 < val2 && val3 > 0) {
			SemanticTable.addRule("1031", "CE", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
			return;
		} else if(val1 > val2 && val3 < 0) {
			SemanticTable.addRule("1031", "CE", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
			return;
		} else if(val3 == 0) {
			SemanticTable.addRule("1031", "CE", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setDesplazamientoError(symbol);
			symbol.listDatos.get(2).type = "variant";
			symbol.listDatos.get(2).clase = "variant";
		} else if(val3 < 0){
			SemanticTable.addRule("1031", "CE", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
			errorSheet.setMinusError(symbol, val3+"");
			symbol.listDatos.get(2).type = "variant";
			symbol.listDatos.get(2).clase = "variant";
		}
	}
	
	public static void sustituteStructForValue(Token id, Token [] attrs) {
		if(attrs.length > 1 || (SemanticTypes.getTokenFromType(attrs[0].getLexema()) != 0 && attrs[0].getToken() != -1) || 
	       (attrs[0].getToken() != -1 && attrs[0].getToken() != -11)) {
			calcSheet.setToken(new Token(attrs[0].getLine(), 309, "variant"));
			SemanticTypes.addTmp(calcSheet.getVarAssign("variant"), 
				calcSheet.getVarAssign("variant") + SemanticTypes.getTmpCounter(calcSheet.getVarAssign("variant")));
			SemanticTypes.tmpUp(calcSheet.getVarAssign("variant"));
		} else {
			String type = dataSub(id, attrs);
			calcSheet.setToken(new Token(attrs[0].getLine(), SemanticTypes.getTokenFromType(type), type));
			
			SemanticTypes.addTmp(calcSheet.getVarAssign(type), 
					calcSheet.getVarAssign(type) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(type)));
			SemanticTypes.tmpUp(calcSheet.getVarAssign(type));
			System.out.println("TYpe: " + type);
		}
	}
	
	private static String dataSub(Token id, Token [] attrs) {
		Stack<Integer> copyAmibtStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < copyAmibtStack.size(); i++) {
			int ambito = copyAmibtStack.pop();
			String result = SqlEvent.getTypeSubStruct(id.getLexema(), ambito, id.getLine());
			if(!result.equals("NULL")) {
				String [] values = result.split(":");
				int index = Integer.parseInt(attrs[0].getLexema());
				if(index < 0 || index > values.length-1)
					return "variant";
				else
					return values[index];
			}
		} return "variant";
	}
	
	public static void checkDimensionRange(Symbol symbol) {
		String [] valRange = symbol.rango.split(","); 
		
			int val1 = Integer.parseInt(valRange[0]);
			int val2 = Integer.parseInt(valRange[1]);
			int val3 = Integer.parseInt(symbol.avance);
			
			if(val1 < val2 && val3 > 0) {
				SemanticTable.addRule("1160", "CE", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
				return;
			} else if(val1 > val2 && val3 < 0) {
				SemanticTable.addRule("1161", "CE", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
				return;
			} else if(val1 < val2 && val3 < 0) { 
				SemanticTable.addRule("1160", "CE", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
				errorSheet.setRangeMinusDimensionError(symbol);
			} else if(val1 > val2 && val3 > 0){ 
				SemanticTable.addRule("1161", "CE", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
				errorSheet.setRangeMaxDimensionError(symbol);
			}
	}
	
	private static boolean isString(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			if(SqlEvent.getType(id.getLexema(), ambito, id.getLine()).equals("string"))
				return true;
		}
		return false;
	}
	
	public static void outPrints(String func) {
		Token [] params = new Token[elementOnMethod];
		if(calcSheet.getSizeTokenStack() > 0) {
			for(int i = 0; i < elementOnMethod; i++) {
				params[i] = calcSheet.getToken();
			} 
		} elementOnMethod = 0;
		functions.addCuadFunc(func, "void", "", params);
	}
	
	public static CalcSemantic getMatrix() {
		return calcSheet;
	}
	
	public static LinkedList<String[]> getCounter() {
		return calcSheet.semOneCounters;
	}
	
	public static void reset() {
		calcSheet.reset();
		elementOnArray = 0;
		elementOnMethod = 0;
		structError = false;
		lessCounter = 0;
		lastLine = 1;
		errorSheet = new SemanticErrors();
		SemanticTable.reset();
	}
}
