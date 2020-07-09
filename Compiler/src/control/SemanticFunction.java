package control;

import java.util.Stack;

import control.templates.Cuadruplo;
import control.templates.SemThreeConsts;
import control.templates.SemanticTable;
import control.templates.SemanticTypes;
import control.templates.Symbol;
import control.templates.Token;
import database.SqlEvent;

public class SemanticFunction {
	private CalcSemantic calcSheet;
	private SemanticErrors errorSheet;
	
	public SemanticFunction(CalcSemantic calcSheet, SemanticErrors calcErrors) {
		this.calcSheet = calcSheet;
		this.errorSheet = calcErrors;
	}
	
	public void checkFindall() {
		Token result = null;
		Token param2 = calcSheet.getToken();
		Token param1 = calcSheet.getToken();
		
		sustituteVariantAlert(param1);
		sustituteVariantAlert(param2);
		
		boolean acceptParam1 = checkFindallP1(param1);
		boolean acceptParam2 = checkFindallP2(param2);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		String typeParam2 = (param1.getToken() != -1) ?  getType(param2) : getCategory(param2, false);

		addRuleSem3("Findall-Par1", param1, acceptParam1, typeParam1 + "/1");
		addRuleSem3("Findall-Par2", param2, acceptParam2, typeParam2 + "/2");
		
		if(!(acceptParam1 && acceptParam2)) {
			if(!acceptParam1)
				errorSheet.setFindallP1Error(param1);
			if(!acceptParam2) 
				errorSheet.setFindallP2Error(param2);
			result = new Token(param2.getLine(), SemanticTypes.getTokenFromType("list"), "list");
		} else {
			switch(getCategory(param2, false)) {
			case "arr":
				result = new Token(param2.getLine(), SemanticTypes.getTokenFromType("arr"), "arr");
				break;
			case "list": default:
				result = new Token(param2.getLine(), SemanticTypes.getTokenFromType("list"), "list");
				break;
			}
		}
		
		
		addRuleSem3("Findall", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/8");
		calcSheet.setToken(result);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), 
	    calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		addCuadFunc("findall", result.getLexema(), "", param1, param2);
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkFindallP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("string,variant", getCategory(param1, false));
		case -5: case 309: return true;
		default: return false;
		}
	}
	
	private boolean checkFindallP2(Token param2) {
		switch(param2.getToken()) {
		case -1:
			return matchTypes("string,list,arr,variant", getCategory(param2, false));
		case -5: case 309: return true;
		default: return false;
		}
	}
	
	public void checkReplace() {
		Token param3 = calcSheet.getToken();
		Token param2 = calcSheet.getToken();
		Token param1 = calcSheet.getToken();

		sustituteVariantAlert(param1);
		sustituteVariantAlert(param2);
		sustituteVariantAlert(param3);
		
		boolean acceptParam1 = checkReplaceParam(param1);
		boolean acceptParam2 = checkReplaceParam(param2);
		boolean acceptParam3 = checkReplaceParam(param3);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		String typeParam2 = (param2.getToken() != -1) ?  getType(param2) : getCategory(param2, false);
		String typeParam3 = (param3.getToken() != -1) ?  getType(param3) : getCategory(param3, false);
		
		addRuleSem3("Replace-Par1", param1, acceptParam1, typeParam1 + "/1");
		addRuleSem3("Replace-Par2", param1, acceptParam2, typeParam2 + "/1");
		addRuleSem3("Replace-Par3", param1, acceptParam3, typeParam3 + "/1");
		
		if(!(acceptParam1 && acceptParam2 && acceptParam3))
			errorSheet.setReplace(param1, param2, param3);
		
		Token result = new Token(param3.getLine(), SemanticTypes.getTokenFromType("string"), "string");
		addRuleSem3("Replace", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/9");
		calcSheet.setToken(result);
		addCuadFunc("replace", result.getLexema(), "", param1, param2, param3);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkReplaceParam(Token param) {
		switch(param.getToken()) {
		case -1:
			return matchTypes("string,variant", getCategory(param, false));
		case -5: case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkLen() {
		Token param = calcSheet.getToken();
		sustituteVariantAlert(param);
		boolean acceptParam = checkLenP1(param);

		String typeParam1 = (param.getToken() != -1) ?  getType(param) : getCategory(param, false);
		
		addRuleSem3("Len-Param1", param, acceptParam, typeParam1 + "/3");
		
		if(!acceptParam) 
			errorSheet.setLenP1Error(param);
		
		Token result = new Token(param.getLine(), -11, "decimal");
		addRuleSem3("Len", result, acceptParam, SemanticTypes.getTypeFromToken(result.getToken()) + "/10");
		calcSheet.setToken(result);
		addCuadFunc("len", result.getLexema(), "", param);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkLenP1(Token param) {
		switch(param.getToken()) {
		case -1:
			return matchTypes("list,string,diccionario,tupla,range,arr,variant", getCategory(param, false));
		case 309: case -804: case -5:
			return true;
		default:
			return false;
		}
	}
	
	public void checkSample() {
		Token result = null;
		Token param2 = calcSheet.getToken();
		Token param1 = calcSheet.getToken();

		sustituteVariantAlert(param1);
		sustituteVariantAlert(param2);
		
		boolean acceptParam1 = checkSampleP1(param1);
		boolean acceptParam2 = checkSampleP2(param2);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		String typeParam2 = (param2.getToken() != -1) ?  getType(param2) : getCategory(param2, false);

		addRuleSem3("Sample-Param1", param1, acceptParam1, typeParam1 + "/3");
		addRuleSem3("Sample-Param2", param2, acceptParam2, typeParam2 + "/4");
		
		if(!(acceptParam1 && acceptParam2)) {
			if(!acceptParam1)
				errorSheet.setSampleP1Error(param1);
			if(!acceptParam2)
				errorSheet.setSampleP2Error(param2);
			result = new Token(param2.getLine(), SemanticTypes.getTokenFromType("list"), "list");
		} else {
			switch(getCategory(param1, false)) {
			case "list": default:
				result = new Token(param2.getLine(), SemanticTypes.getTokenFromType("list"), "list");
				break;
			case "arr":
				result = new Token(param2.getLine(), SemanticTypes.getTokenFromType("arr"), "arr");
				break;
			}
		}
		
		addRuleSem3("Sample", param2, acceptParam2, SemanticTypes.getTypeFromToken(result.getToken()) + "/8");
		calcSheet.setToken(result);
		addCuadFunc("sample", result.getLexema(), "", param1, param2);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkSampleP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("list,string,diccionario,tupla,range,arr,variant", getCategory(param1, false));
		case 309:
			return true;
		default:
			return false;
		}
	}
	
	private boolean checkSampleP2(Token param2) {
		switch(param2.getToken()) {
		case -1:
			return matchTypes("decimal,variant", getCategory(param2, false));
		case -11: case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkChoice() {
		Token result = null;
		Token param1 = calcSheet.getToken();

		sustituteVariantAlert(param1);
		boolean acceptParam1 = checkChoiceP1(param1);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		
		addRuleSem3("Choice-Param1", param1, acceptParam1, typeParam1 + "/3");
		
		if(!acceptParam1) {
			errorSheet.setChoiceP1Error(param1);
			result = new Token(param1.getLine(), 309, "variante");
		} else {
			String paramType = getCategory(param1, false);
			String typeReturn = "variant";
			
			switch(paramType) {
			case "string":
				typeReturn = "char";
				break;
			case "range":
				typeReturn = "decimal";
				break;
			default:
				int ambito = getAmbitID(param1);
				typeReturn = SqlEvent.getRandomSubDato(param1, ambito);
				break;
			}
			
			result = new Token(param1.getLine(), SemanticTypes.getTokenFromType(typeReturn), typeReturn);
		}
		
		addRuleSem3("Choice", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/11");
		calcSheet.setToken(result);
		addCuadFunc("choice", result.getLexema(), "", param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkChoiceP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("list,string,diccionario,tupla,range,arr,variant", getCategory(param1, false));
		case 309: //case -804: case -5:
			return true;
		default:
			return false;
		}
	}
	
	public void checkRandRange() {
		Token param2 = calcSheet.getToken();
		Token param1 = calcSheet.getToken();

		sustituteVariantAlert(param1);
		sustituteVariantAlert(param2);
		
		boolean acceptParam1 = checkRandRangeParam(param1);
		boolean acceptParam2 = checkRandRangeParam(param2);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		String typeParam2 = (param2.getToken() != -1) ?  getType(param2) : getCategory(param2, false);
		
		addRuleSem3("Randrange-Param1", param1, acceptParam1, typeParam1 + "/4");
		addRuleSem3("Randrange-Param2", param2, acceptParam2, typeParam2 + "/4");
		
		if(!acceptParam1)
			errorSheet.setRandRangeParamError(param1, 1);
		if(!acceptParam2)
			errorSheet.setRandRangeParamError(param2, 2);
		
		Token result = new Token(param2.getLine(), -11, "decimal");
		addRuleSem3("Randrange", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/10");
		calcSheet.setToken(result);
		addCuadFunc("randrange", result.getLexema(), "", param1, param2);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkRandRangeParam(Token param) {
		switch(param.getToken()) {
		case -1:
			return matchTypes("decimal,variant", getCategory(param, false));
		case -11: case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkMeanVariance(String method) {
		Token param1 = calcSheet.getToken();
		sustituteVariantAlert(param1);
		boolean acceptParam1 = checkMeanVarianceSumP1(param1);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3(method + "-Param1", param1, acceptParam1, typeParam1 + "/6");
		
		if(!acceptParam1)
			errorSheet.setMeanParamError(param1, method);

		Token result = new Token(param1.getLine(), -9, "float");
		addRuleSem3(method, result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/12");
		calcSheet.setToken(result);
		addCuadFunc(method, result.getLexema(), "", param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkMeanVarianceSumP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("arrdecimal,arrfloat,arrhex,arroct,arrbin,arrcomp,variant", 
					getCategory(param1, true));
		case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkSum() {
		Token result = null;
		Token param1 = calcSheet.getToken();
		sustituteVariantAlert(param1);
		boolean acceptParam1 = checkMeanVarianceSumP1(param1);

		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Sum-Par1", param1, acceptParam1, typeParam1 + "/6");
		
		if(!acceptParam1) {
			errorSheet.setSumParamError(param1);
			result = new Token(param1.getLine(), 309, "variant");
		} else {
			String typeParam = getCategory(param1, true);
			String typeReturn = "variant";
			
			switch(typeParam) {
			case "arrdecimal":
				typeReturn = "decimal";
				break;
			case "arrfloat":
				typeReturn = "float";
				break;
			case "arrhex":
				typeReturn = "hex";
				break;
			case "arroct":
				typeReturn = "oct";
				break;
			case "arrbin":
				typeReturn = "bin";
				break;
			default:
				typeReturn= "variant";
				break;
			}
			
			result = new Token(param1.getLine(), 
					                     SemanticTypes.getTokenFromType(typeReturn), 
					                     typeReturn);
		}
		
		addRuleSem3("Sum", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/13");
		calcSheet.setToken(result);
		addCuadFunc("sum", result.getLexema(), "", param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	public void checkSort() {
		Token result = null;
		Token id = calcSheet.getToken();
		String type = getCategory(id, false);
		
		switch(type) {
		case "list":
			result = new Token(id.getLine(), -804, "list");
			break;
		case "arr":
			result = new Token(id.getLine(), -805, "arr");
			break;
		default:
			errorSheet.setSortError(id);
			result = new Token(id.getLine(), -804, "list");
			break;
		}
		
		addRuleSem3("Sort", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/15");
		calcSheet.setToken(result);
		addCuadFunc("sort", result.getLexema(), id.getLexema(), new Token(id.getLine(), 309, ""));
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	public void checkReverse() {
		Token result = null;
		Token id = calcSheet.getToken();
		String type = getCategory(id, false);
		
		switch(type) {
		case "list":
			result = new Token(id.getLine(), -804, "list");
			break;
		case "arr":
			result = new Token(id.getLine(), -805, "arr");
			break;
		default:
			errorSheet.setSortError(id);
			result = new Token(id.getLine(), -804, "list");
			break;
		}
		
		addRuleSem3("Reverse", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/15");
		calcSheet.setToken(result);
		addCuadFunc("reverse", result.getLexema(), id.getLexema(), new Token(id.getLine(), 309, ""));
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	public void checkCount() {
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		boolean acceptParam1 = checkCountP1(param1);
		
		String type = getCategory(id, false);
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Count-Par1", param1, acceptParam1, typeParam1 + "/3");
		
		switch(type) {
		case "list": case "arr":
		case "string": case "variant":
			
			if(!acceptParam1)
				errorSheet.setCountP1Error(id, param1);
			break;
		default:
			errorSheet.setCountError(id, param1);
			break;
		}
		
		Token result = new Token(param1.getLine(), -11, "decimal");
		addRuleSem3("Count", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/10");
		calcSheet.setToken(result);
		addCuadFunc("count", result.getLexema(), id.getLexema(), param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkCountP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("list,string,diccionario,tupla,range,arr,variant", getCategory(param1, false));
		case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkIndex() {
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		boolean acceptParam1 = checkIndexP1(param1);
		
		String types = getCategory(id, false);
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Index-Par1", param1, acceptParam1, typeParam1 + "/7");
		
		switch(types) {
		case "list": case "arr":
		case "string": case "variant":
			
			if(!acceptParam1) 
				errorSheet.setIndexP1Error(id, param1);
			break;
		default:
			errorSheet.setIndexError(id, param1);
			break;
		}
		
		Token result = new Token(id.getLine(), -11, "decimal");
		addRuleSem3("Index", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/10");
		calcSheet.setToken(result);
		addCuadFunc("index", result.getLexema(), id.getLexema(), param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkIndexP1(Token params) {
		switch(params.getToken()) {
		case -1:
			return false;
		case -9: case -5: case -4:
		case -10: case -11: case -6:
		case -7: case -8: case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkAppend() {
		Token result = null;
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		String types = getCategory(id, false);
		boolean acceptParam1 = checkAppendP1(param1);
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Append-Par1", param1, acceptParam1, typeParam1 + "/7");
		
		switch(types) {
		case "list":
			if(!acceptParam1)
				errorSheet.setAppendP1Error(id, param1);
			else 
				appendParam(id, param1);
			result = new Token(id.getLine(), -804, "list");
			break;
		case "arr":
			if(!acceptParam1)
				errorSheet.setAppendP1Error(id, param1);
			else if(SemanticTypes.getTokenFromType(getCategory(id, true)) == -11) {
				int ambito = getAmbitID(id);
				if(ambito != -1)
					SqlEvent.appendDecimalArr(id, ambito, 1);
			} else {
				int ambito = getAmbitID(id);
				if(ambito != -1)
					SqlEvent.appendArr(id, ambito, 1);
			}
			
			result = new Token(id.getLine(), -805, "arr");
			break;
		case "string":
			if(param1.getToken() != -5)
				errorSheet.setAppendStringError(id, param1);
			result = new Token(id.getLine(), -5, "string");
			break;
		case "variant":
			result = new Token(id.getLine(), 309, "variant");
			break;
		default:
			errorSheet.setAppendError(id, param1);
			result = new Token(id.getLine(), 309, "variant");
			break;
		}
		
		addRuleSem3("Append", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/14");
		calcSheet.setToken(result);
		addCuadFunc("append", result.getLexema(), id.getLexema(), param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkAppendP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return false;
		case -9: case -5: case -4:
		case -10: case -11: case -6:
		case -7: case -8: case 309:
			return true;
		default:
			return false;
		}
	}
	
	private void appendParam(Token id, Token param1) {
		int ambito = getAmbitID(id);
		if(ambito != -1) {
			int index = SqlEvent.getLastIndex(id, ambito) + 1;
			int ambitoSub = SqlEvent.getSubdatoAmbito(id, ambito);
			
			Symbol symbol = new Symbol();
			symbol.reset();
			symbol.type = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(param1));
			symbol.clase = "datoLista";
			symbol.ambito = ambitoSub;
			symbol.list_per = id.getLexema();
			symbol.nposicion = index + "";
			symbol.ambito_padre = ambito + "";
			symbol.value = param1.getLexema();
			
			SqlEvent.sendSymbol(symbol);
			SqlEvent.appendArr(id, ambitoSub, 2);
		}
	}
	
	public void checkExtend() {
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		String types = getCategory(id, false);
		boolean acceptParam1 = checkExtendP1(param1); System.out.println("acceptParam1: " + acceptParam1);
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Extend-Par1", param1, acceptParam1, typeParam1 + "/5");
		
		switch(types) {
		case "list":
			if(acceptParam1)
				extendList(id, param1);
			else
				errorSheet.setExtendP1Error(id, param1);
			break;
		case "arr":
			if(acceptParam1)
				extendArr(id, param1);
			else
				errorSheet.setExtendP1Error(id, param1);
			break;
		case "variant": break;
		default:
			errorSheet.setExtendError(id, param1);
			break;
		}
		calcSheet.setToken(new Token(id.getLine(), 309, "variant:alert:extend"));
		addCuadFunc("extend", "void", id.getLexema(), param1);
	}
	
	private boolean checkExtendP1(Token param1) {
		switch(param1.getToken()) {
		case -1: case 309:
			return matchTypes("list,arr,variant", getCategory(param1, false));
		default:
			return false;
		}
	}
	
	private void extendList(Token id, Token param1) {
		int ambito = getAmbitID(param1);
		Symbol [] subParam = SqlEvent.getSubdatos(param1, ambito);
		
		
		if(subParam != null) {
			int ambitoID = getAmbitID(id);
			int ambitoIDSub = SqlEvent.getSubdatoAmbito(id, ambitoID);
			int index = SqlEvent.getLastIndex(id, ambitoID);
			
			for(Symbol symbol : subParam) {
				symbol.ambito_padre = ambitoID + "";
				symbol.nposicion = (++index) +"";
				symbol.list_per = id.getLexema();
				symbol.ambito = ambitoIDSub;
				SqlEvent.sendSymbol(symbol);
			}
			
			SqlEvent.appendArr(id, ambitoID, subParam.length);
		}
	}
	
	private void extendArr(Token id, Token param1) {
		if(getCategory(id, true).equals(getCategory(param1, true))) {
			int size = SqlEvent.getArrSize(param1, getAmbitID(param1));
			SqlEvent.appendArr(id, getAmbitID(id), size);
		} else
			errorSheet.setExtendArrError(id, param1);
	}
	
	public void checkPop() {
		Token result = null;
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		String type = getCategory(id, false);
		boolean acceptParam1 = checkPopP1(param1);
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Pop-Par1", param1, acceptParam1, typeParam1 + "/4");
		String resultType = "";
		
		switch(type) {
		case "list":
			if(acceptParam1)
				resultType = popList(id, param1);
			else {
				param1.setToken(-1);
				resultType = popList(id, param1);
				errorSheet.setPopP1Error(id, param1);
			} break;
		case "arr":
			if(acceptParam1) {
				resultType = popArr(id, param1);
			} else {
				resultType = type;
				errorSheet.setPopP1Error(id, param1);
			}
			break;
		case "variant": break;
		default:
			errorSheet.setPopError(id, param1);
		}
		
		result = new Token(id.getLine(), SemanticTypes.getTokenFromType(resultType), resultType);
		addRuleSem3("Pop", result, true, SemanticTypes.getTypeFromToken(result.getToken()) + "/11");
		calcSheet.setToken(result);
		addCuadFunc("pop", result.getLexema(), id.getLexema(), param1);
		SemanticTypes.addTmp(calcSheet.getVarAssign(result.getLexema()), calcSheet.getVarAssign(result.getLexema()) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(result.getLexema())));
		SemanticTypes.tmpUp(calcSheet.getVarAssign(result.getLexema()));
	}
	
	private boolean checkPopP1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("decimal,variant", getCategory(param1, false));
		case -11: case 309:
			return true;
		default:
			return false;
		}
	}
	
	private String popList(Token id, Token param1) {
		int ambitoID = getAmbitID(id);
		int size = SqlEvent.getArrSize(id, ambitoID);
		String elementPop = "";
		
		if(param1.getToken() != -1) {
			int indexPop = Integer.parseInt(param1.getLexema());
			if(size > indexPop) {
				int loops = (indexPop == 0) ? size : indexPop;
				elementPop = SqlEvent.Pop(id, getAmbitID(id), indexPop);
				
				for(int i = 0; i < loops; i++) {
					SqlEvent.updatePosition(id, ambitoID, indexPop);
					indexPop++;
				}
				
				SqlEvent.appendArr(id, ambitoID, -1);
			} else
				errorSheet.setIndexPopError(id, param1);
		} else 
			elementPop = SqlEvent.Pop(id, ambitoID, size-1);
		
		return elementPop;
	}
	
	private String popArr(Token id, Token param1) {
		int ambitID = getAmbitID(id);
		String resultType = "";
		String idType = getCategory(id, true);
		int size = SqlEvent.getArrSize(id, ambitID);
		
		switch(idType) {
		case "arrdecimal":
			resultType = popDecimalArr(id, param1);
			break;
		default:
			int position = 0;
			if(isNumeric(param1.getLexema()))
				position = Integer.parseInt(param1.getLexema());
			else
				position = -1;
			
			if(size > 0 && (size > position || position == -1))
				SqlEvent.appendArr(id, ambitID, -1);
			else
				errorSheet.setIndexOutOfBound(id, param1);
			resultType = idType;
		}

		return resultType;
	}
	
	private String popDecimalArr(Token id, Token param1) {
		int ambitID = getAmbitID(id);
		int size = SqlEvent.getArrSize(id, ambitID);
		int dim = SqlEvent.getArrDimension(id, ambitID);
		String resultType = "";
		
		if(size == 3 && dim == 1) {
			if(isNumeric(param1.getLexema())) {
				int length = Integer.parseInt(SqlEvent.getArrLength(id, ambitID));
				
				if(length > Integer.parseInt(param1.getLexema()))
					SqlEvent.appendDecimalArr(id, ambitID, -1);
				else
					errorSheet.setIndexOutOfBound(id, param1);
			} else
				SqlEvent.appendDecimalArr(id, ambitID, -1);
		} else 
	    	errorSheet.setErrorArrFunctDim(id, param1, "pop");
		return "decimal";
	}
	
	public void checkRemove() {
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		int ambitID = getAmbitID(id);
		String type = getCategory(id, false);
		boolean acceptParam1 = checkRemoveP1(param1);
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		addRuleSem3("Remove-Par1", param1, acceptParam1, typeParam1 + "/7");
		
		switch(type) {
		case "list":
			if(acceptParam1)
				removeList(id, param1);
			break;
		case "arr":
			int size = SqlEvent.getArrSize(id, ambitID);
			
			if(size > 0) {
				if(param1.getToken() == SemanticTypes.getTokenFromType(getCategory(id, true))) {
					SqlEvent.appendArr(id, ambitID, -1);
				} else 
					errorSheet.setErrorRemoveDataNotCompatible(id, param1, type, getCategory(param1, true));
			} else
				errorSheet.setErrorStructEmpty(id, param1);
			break;
		default:
			errorSheet.setRemoveError(id, param1);
		}
		
		calcSheet.setToken(new Token(id.getLine(), 309, "variant:alert:remove"));
		addCuadFunc("remove", "void", id.getLexema(), param1);
	}
	
	private void removeList(Token id, Token param1) {
		int ambitoID = getAmbitID(id);
		SqlEvent.remove(id, ambitoID, param1);
		int size = SqlEvent.getArrSize(id, ambitoID);
		
		for(int i = 0; i < size; i++)
			SqlEvent.updatePosition(id, ambitoID, i);
	}
	
	private boolean checkRemoveP1(Token param1) {
		switch(param1.getToken()) {
		case -9: case -5: case -4:
		case -10: case -11: case -6:
		case -7: case -8: case 309:
			return true;
		default:
			return false;
		}
	}
	
	public void checkInsert() {
		Token param2 = calcSheet.getToken();
		Token param1 = calcSheet.getToken();
		Token id = calcSheet.getToken();

		sustituteVariantAlert(param1);
		sustituteVariantAlert(param2);
		
		String type = getCategory(id, false);
		boolean acceptParam1 = checkInsertParam1(param1);
		boolean acceptParam2 = checkInsertParam2(param2);
		
		String typeParam1 = (param1.getToken() != -1) ?  getType(param1) : getCategory(param1, false);
		String typeParam2 = (param2.getToken() != -1) ?  getType(param2) : getCategory(param2, false);
		
		addRuleSem3("Insert-Par1", param1, acceptParam1, typeParam1 + "/4");
		addRuleSem3("Insert-Par2", param2, acceptParam2, typeParam2 + "/7");
		
		switch(type) {
		case "list":
			if(acceptParam1 && acceptParam2)
				insertList(id, param1, param2);
			else if(!acceptParam1)
				errorSheet.setErrorParam1Insert(id, param1);
			else if(!acceptParam2)
				errorSheet.setErrorParam2Insert(id, param2);
			break;
		case "arr":
			if(acceptParam1 && acceptParam2) {
				if(checkInsertAvailableParam2(id, param2)) { 
					int ambitoID = getAmbitID(id);
					int tam = SqlEvent.getArrTam(id, ambitoID);
					int dim = SqlEvent.getArrDimension(id, ambitoID);

					int index = (isNumeric(param1.getLexema())) ? Integer.parseInt(param1.getLexema())
							:  0;
					
					if(getCategory(id, true).equals("arrdecimal")) {
						if(tam == 3 && dim == 1) {
							int size = Integer.parseInt(SqlEvent.getArrLength(id, ambitoID));
							SqlEvent.appendDecimalArr(id, ambitoID, 1 + Math.abs(size - index));
						} else
							errorSheet.setErrorArrFunctDim(id, param1, "insert");
					} else {
						if(dim == 1) 
							SqlEvent.appendArr(id, ambitoID, 1 + Math.abs(tam - index));
						else
							errorSheet.setErrorArrFunctDim(id, param1, "insert");
					}
				} else {
					errorSheet.setErrorInsertDataNotCompatible(id, param2, type, 
							SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(param2)));
				}
			} else if(!acceptParam1) {
				errorSheet.setErrorParam1Insert(id, param1);
			} else if(!acceptParam2)
				errorSheet.setErrorParam2Insert(id, param2);
			break;
		default:
			
		}
		
		calcSheet.setToken(new Token(id.getLine(), 309, "variant:alert:insert"));
		addCuadFunc("insert", "void", id.getLexema(), param1, param2);
	}
	
	private boolean checkInsertParam1(Token param1) {
		switch(param1.getToken()) {
		case -1:
			return matchTypes("decimal,variant", getCategory(param1, false));
		case -11: case 309:
			return true;
		default:
			return false;
		}
	}
	
	private boolean checkInsertParam2(Token param2) {
		switch(param2.getToken()) {
		case -9: case -5: case -4:
		case -10: case -11: case -6:
		case -7: case -8: case 309:
			return true;
		default:
			return false;
		}
	}
	
	private boolean checkInsertAvailableParam2(Token id, Token param2) {
		return (SemanticTypes.getTokenFromType(getCategory(id, true)) == param2.getToken());
	}
	
	private void insertList(Token id, Token param1, Token param2) {
		int ambitoID = getAmbitID(id);
		Symbol [] subdatos = SqlEvent.getSubdatos(id, ambitoID);
		
		if(param1.getToken() != -1) {
			int size = SqlEvent.getArrSize(id, ambitoID);
			int index = (isNumeric(param1.getLexema())) ? Integer.parseInt(param1.getLexema())
					: size - 1;
			
			for(int i = size; i >= index; i--) {
				SqlEvent.updatePositionInsert(id, ambitoID, i);
			}
			
			Symbol symbol = new Symbol();
			symbol.type = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(param2.getToken()));
			symbol.clase = "datoLista";
			symbol.ambito = SqlEvent.getSubdatoAmbito(id, ambitoID);
			symbol.value = param2.getLexema();
			symbol.nposicion = index + "";
			symbol.list_per = id.getLexema();
			symbol.ambito_padre = ambitoID + "";
			SqlEvent.sendSymbol(symbol);
			SqlEvent.appendArr(id, ambitoID, 1 + Math.abs(size - Integer.parseInt(param1.getLexema())));
		} else
			appendParam(id, param2);
	}
	
	public String getCategory(Token param, boolean mood) {
		int ambito = getAmbitID(param);
		
		if(ambito != -1) {
			String type = SqlEvent.getType(param.getLexema(), ambito, param.getLine());
			String clase = (type.equals("struct") || type.equals("none")) ?
						   SqlEvent.getClass(param.getLexema(), ambito, param.getLine()) : "";
			
		switch(clase) {
			case "func": 
				clase = SqlEvent.getReturns(param.getLexema(), ambito, param.getLine());
				break;
			case "arr":
				if(mood)
					clase += SqlEvent.getTypeArray(param.getLexema(), ambito, param.getLine());
				break;
			}
			if(type.equals("NULL")) type = "variant";
			return (clase.equals("")) ? type : clase;
		} return "variant";
	}
	
	public String getType(Token val) {
		return SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(val.getToken()));
	}
	
	private boolean matchTypes(String expectValues, String realValue) {
		String [] expectedUnitValue = expectValues.split(",");
		String [] realUnitValue = realValue.split(":");
		
		for(String expected : expectedUnitValue) 
			for(String value : realUnitValue) 
				if(expected.equals(value)) 
					return true;
		return false;
	}
	
	public boolean isNumeric(String str) {
        boolean isNumeric;

        try {
            Integer.parseInt(str);
            isNumeric = true;
        } catch (NumberFormatException excepcion) {
            isNumeric = false;
        }

        return isNumeric;
    }
	
	
	public int getAmbitID(Token id) {
		Stack<Integer> copyAmbitStack = (Stack<Integer>) Ambit.getAmbitoStack().clone();
		for(int i = 0; i < Ambit.getAmbitoStack().size(); i++) {
			int ambito = copyAmbitStack.pop();
			Symbol symbol = new Symbol();
			symbol.id = id.getLexema();
			symbol.ambito = ambito;
			
			if(SqlEvent.ifSymbolExists(symbol))
				return ambito;
		} return -1;
	}
	
	public void addCuadFunc(String func, String returnVal, String args, Token ... params) {
		if(Analyzer.listError.isEmpty()) {
			Analyzer.listCuad.add(new Cuadruplo("", "call", func, args, ""));
			if(params.length%2 == 0)
				addCuadFuncParamsPar(func, returnVal, params);
			else 
				addCuadFuncParamsInpar(func, returnVal, params);
		}
	}
	
	private void addCuadFuncParamsPar(String func, String returnVal, Token ... params) {
		if(Analyzer.listError.isEmpty()) {
			for(int i = 0; i < params.length; i+=2) {
				String tmp1 = calcSheet.getValueAssign(params[i].getLexema());
				String tmp2 = calcSheet.getValueAssign(params[i+1].getLexema());
				
				Analyzer.listCuad.add(new Cuadruplo("", "", 
						(tmp1.equals(params[i].getLexema())) ? params[i].getLexema() : SemanticTypes.getTmp(calcSheet.getVarAssign(params[i].getLexema())), 
						(tmp2.equals(params[i+1].getLexema())) ? params[i+1].getLexema() : SemanticTypes.getTmp(calcSheet.getVarAssign(params[i+1].getLexema())), 
						""));
			}
			
			String tmp3 = calcSheet.getValueAssign(returnVal);
			Analyzer.listCuad.getLast().setResult((tmp3.equals(returnVal)) ? returnVal : 
				calcSheet.getVarAssign(returnVal) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(returnVal)));
		}
	}
	
	private void addCuadFuncParamsInpar(String func, String returnVal, Token ... params) {
		if(Analyzer.listError.isEmpty()) {
			for(int i = 0; i < params.length-1; i+=2) {
				String tmp1 = calcSheet.getValueAssign(params[i].getLexema());
				String tmp2 = calcSheet.getValueAssign(params[i+1].getLexema());
				Analyzer.listCuad.add(new Cuadruplo("", "", 
						(tmp1.equals(params[i].getLexema())) ? params[i].getLexema() : 
							SemanticTypes.getTmp(calcSheet.getVarAssign(params[i].getLexema())), 
						(tmp2.equals(params[i+1].getLexema())) ? params[i+1].getLexema() : 
							SemanticTypes.getTmp(calcSheet.getVarAssign(params[i+1].getLexema())),
						""));
			}
			
			String tmp3 = calcSheet.getValueAssign(params[params.length-1].getLexema());
			Analyzer.listCuad.add(new Cuadruplo("", "", 
					(tmp3.equals(params[params.length-1].getLexema())) ? params[params.length-1].getLexema() : 
						SemanticTypes.getTmp(calcSheet.getVarAssign(params[params.length-1].getLexema())),
					"", ""));
			
			String tmp4 = calcSheet.getValueAssign(returnVal);
			Analyzer.listCuad.getLast().setResult((tmp4.equals(returnVal)) ? returnVal : 
				calcSheet.getVarAssign(returnVal) + SemanticTypes.getTmpCounter(calcSheet.getVarAssign(returnVal)));
		}
	}
	
	private void addRuleSem3(String nrule, Token val, boolean accept, String rule) {
		String type = (val.getToken() == -1) ? getCategory(val, false) : 
			                                   SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(val.getToken())); 
		String prefixType = (val.getToken() == -1) ? "id/" : "valor/";
		
		SemanticTable.addRule(nrule, rule,  prefixType + type, val.getLine(), 
							  (accept) ? "ACEPTA" : "ERROR", Ambit.getAmbitoStack().peek());
	}
	
	private void sustituteVariantAlert(Token variant) {
		String [] parts = variant.getLexema().split(":");
		if(parts.length > 1) {
			variant.setLexema("variant");
			errorSheet.setErrorVoid(variant, parts[2]);
		}
	}
}
