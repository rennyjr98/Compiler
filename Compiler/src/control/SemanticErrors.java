package control;

import control.templates.Error;
import control.templates.SemanticTypes;
import control.templates.Symbol;
import control.templates.Token;

public class SemanticErrors {
	public void setErrorAssign(Token id, Token result, String oper) {
		String typeVal1 = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(id));
		String typeVal2 = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(result));
		
		Analyzer.listError.add(new Error(id.getLine(), 693, "Semantica 1", 
				typeVal1 + " no puede guardar un " + typeVal2, 
				id.getLexema() + " " + oper + " " + result.getLexema()));
	}
	
	public void setIfError(Token result) {
		String typeVal = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(result));
		
		Analyzer.listError.add(new Error(result.getLine(), 694, "Semantica 2",
				"Se esperaba un bool y se obtuvo un " + typeVal,
				"if " + typeVal + " :"));
	}
	
	public void setElifError(Token result) {
		String typeVal = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(result));
		
		Analyzer.listError.add(new Error(result.getLine(), 694, "Semantica 2",
				"Se esperaba un bool y se obtuvo un " + typeVal,
				"elif " + typeVal + " :"));
	}
	
	public void setWhileError(Token result) {
		String typeVal = SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(result));
		
		Analyzer.listError.add(new Error(result.getLine(), 694, "Semantica 2",
				"Se esperaba un bool y se obtuvo un " + typeVal,
				"while " + typeVal + " :"));
	}
	
	public void setArrDimError(Token arr, int dim) {
		Analyzer.listError.add(new Error(arr.getLine(), 694, "Semantica 2",
				"Dimension fuera de rango: " + dim,
				arr.getLexema() + "[" + dim + "]"));
	}
	
	public void setArrIntError(Token arr, int valor) {
		Analyzer.listError.add(new Error(arr.getLine(), 694, "Semantica 2",
				"Valor incorrecto : " + valor + " - Se esperaba un entero",
				arr.getLexema() + "[" + SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(valor)) + "]"));
	}
	
	public void setArrRangeError(Token arr, String valor) {
		Analyzer.listError.add(new Error(arr.getLine(), 694, "Semantica 2",
				"El valor " + valor + " se sale del rango del arreglo",
				arr.getLexema() + "[" + valor + "]"));
	}
	
	public void setIndexOutOfBoundDicc(Token dicc, Token [] attrs) {
		String lexema = dicc.getLexema() + "[";
		for(Token token : attrs)
			lexema += token.getLexema() + ",";
		lexema += "]";
		Analyzer.listError.add(new Error(dicc.getLine(), 694, "Semantica 2",
				"Dimension fuera de rango",
				lexema));
	}
	
	public void setDiccTypeError(Token dicc, int valor, String lexema) {
		Analyzer.listError.add(new Error(dicc.getLine(), 694, "Semantica 2",
				"Valor incorrecto, no existe una llave de tipo " + SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(valor)) +
				" o no existe la llave " + lexema,
				dicc.getLexema() + "[" + SemanticTypes.getTypeOnString(SemanticTypes.getTypeToken(valor)) + "]"));
	}
	
	public void setTuplaDimError(Token tupla, int valor) {
		Analyzer.listError.add(new Error(tupla.getLine(), 694, "Semantica 2",
				"Dimension fuera del rango: " + valor ,
				tupla.getLexema() + "[" + valor + "]"));
	}
	
	public void setTamParamsError(Token param, int length) {
		Analyzer.listError.add(new Error(param.getLine(), 694, "Semantica 2",
				"Parametros fuera del rango: " + length ,
				param.getLexema() + "(" + length + ")"));
	}
	
	public void setNoReturnError(Token method) {
		Analyzer.listError.add(new Error(method.getLine(), 694, "Semantica 2",
				"La funcion " + method.getLexema(),
				method.getLexema() + "(...)"));
	}
	
	public void setForError(Token id, String dato) {
		Analyzer.listError.add(new Error(id.getLine(), 694, "Semantica 2",
				"Tipo de datos incorrecto " + id.getLexema(),
				"for " + id.getLexema() + " to " + dato));
	}
	
	public void setForIdError(Token id, String dato) {
		Analyzer.listError.add(new Error(id.getLine(), 694, "Semantica 2",
				"Se esperaba un identificador " + id.getLexema(),
				"for " + id.getLexema() + " to " + dato));
	}
	
	public void setMethodIgualationError(Token id, Token result) {
		Analyzer.listError.add(new Error(id.getLine(), 694, "Semantica 2",
				"Un metodo no puede ser igualado a un valor",
				id.getLexema() + " = " + result.getLexema()));
	}
	
	public void setMinusError(Symbol id, String value) {
		Analyzer.listError.add(new Error(id.line, 694, "Semantica 2",
				"Desplazamiento fuera de rango",
				id.id + " = [ ...:" + value + "];"));
	}
	
	public void setDesplazamientoError(Symbol id) {
		Analyzer.listError.add(new Error(id.line, 694, "Semantica 2",
				"Desplazamiento fuera de rango",
				id.id + " = [ ...: 0 ];"));
	}
	
	public void setMinusAparitionError(Token id) {
		Analyzer.listError.add(new Error(id.getLine(), 694, "Semantica 2",
				"Caracter no valido -",
				id.getLexema() + " = [ ...: - :...];"));
	}
	
	public void setMinusAparitionError(Symbol id) {
		Analyzer.listError.add(new Error(id.line, 694, "Semantica 2",
				"Caracter no valido -",
				id.id + " = [ ...: - :...];"));
	}
	
	public void setArrDimensionError(int val1, int val2, Symbol symbol) {
		Analyzer.listError.add(new Error(symbol.line, 694, "Semantica 2",
				"Error de declaración, un arreglo de dos valores debe ser declarado como dimensiones.",
				symbol.id + "[" + val1 + ":" + val2 + "]"));
	}
	
	public void setArrDimensionError(int val1, Symbol symbol) {
		Analyzer.listError.add(new Error(symbol.line, 694, "Semantica 2",
				"Error de declaración, un arreglo de un valor debe ser declarado como dimensiones.",
				symbol.id + "[" + val1 + "]"));
	}
	
	public void setRangeMinusDimensionError(Symbol symbol) {
		Analyzer.listError.add(new Error(symbol.line, 694, "Semantica 2",
				"Error de declaración, un range. Tercer valor tiene que ser > 0",
				symbol.id + "[" + symbol.avance + "]"));
	}
	
	public void setRangeMaxDimensionError(Symbol symbol) {
		Analyzer.listError.add(new Error(symbol.line, 694, "Semantica 2",
				"Error de declaración, un range. Tercer valor tiene que ser < 0",
				symbol.id + "[" + symbol.avance + "]"));
	}
	
	public void setFindallP1Error(Token param1) {
		Analyzer.listError.add(new Error(param1.getLine(), 695, "Semantica 3",
				"Error findall_Param1 se esperaba una string",
				param1.getLexema()));
	}
	
	public void setFindallP2Error(Token param2) {
		Analyzer.listError.add(new Error(param2.getLine(), 695, "Semantica 3",
				"Error findall_Param2 se esperaba un string o lista",
				param2.getLexema()));
	}
	
	public void setLenP1Error(Token param1) {
		Analyzer.listError.add(new Error(param1.getLine(), 695, "Semantica 3",
				"Error len_Param1 se esperaba una lista, string, diccionario, tupla, range o arr",
				param1.getLexema()));
	}
	
	public void setSampleP1Error(Token param1) {
		Analyzer.listError.add(new Error(param1.getLine(), 695, "Semantica 3",
				"Error sample_Param1 se esperaba una lista, string, diccionario, tupla, range o arr",
				param1.getLexema()));
	}
	
	public void setSampleP2Error(Token param2) {
		Analyzer.listError.add(new Error(param2.getLine(), 695, "Semantica 3",
				"Error sample_Param2 se esperaba un decimal",
				param2.getLexema()));
	}
	
	public void setChoiceP1Error(Token param1) {
		Analyzer.listError.add(new Error(param1.getLine(), 695, "Semantica 3",
				"Error choice_Param1 se esperaba una lista, string, diccionario, tupla, range",
				param1.getLexema()));
	}
	
	public void setRandRangeParamError(Token param, int posicion) {
		Analyzer.listError.add(new Error(param.getLine(), 695, "Semantica 3",
				"Error randrange_Param" + posicion + " se esperaba una decimal",
				param.getLexema()));
	}
	
	public void setMeanParamError(Token param, String method) {
		Analyzer.listError.add(new Error(param.getLine(), 695, "Semantica 3",
				"Error " + method + "_Param1 se esperaba un arr decimal",
				param.getLexema()));
	}
	
	public void setSumParamError(Token param) {
		Analyzer.listError.add(new Error(param.getLine(), 695, "Semantica 3",
				"Error sum_Param1 se esperaba un arr numerico",
				param.getLexema()));
	}
	
	public void setSortError(Token id) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error sort solo pueden usarlos los datos tipo listas",
				id.getLexema()+".sort()"));
	}
	
	public void setCountError(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error count solo pueden usarlos los datos tipo listas y cadenas",
				id.getLexema()+".count("+value.getLexema()+")"));
	}
	
	public void setCountP1Error(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error count_P1 se esperaba una lista, cadena, diccionario, tupla o rango",
				id.getLexema()+".count("+value.getLexema()+")"));
	}
	
	public void setIndexError(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error index solo pueden usarlos los datos tipo listas y cadenas",
				id.getLexema()+".index("+value.getLexema()+")"));
	}
	
	public void setIndexP1Error(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error index_P1 se esperaba un decimal, float, hex, oct, bin, comp, string o char",
				id.getLexema()+".index("+value.getLexema()+")"));
	}
	
	public void setAppendError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error append solo pueden usarlos los datos lista",
				id.getLexema()+".append("+value.getLexema()+")"));
	}
	
	public void setAppendP1Error(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error append_P1 se esperaba un decimal, float, hex, oct, bin, comp, string o char",
				id.getLexema()+".append("+value.getLexema()+")"));
	}
	
	public void setExtendError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error extend solo pueden usarlos los datos lista",
				id.getLexema()+".extend("+value.getLexema()+")"));
	}
	
	public void setExtendP1Error(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error extend_P1 se esperaba una lista",
				id.getLexema()+".extend("+value.getLexema()+")"));
	}
	
	public void setExtendArrError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error extend_P1 los arreglos no son del mismo tipo de dato",
				id.getLexema()+".extend("+value.getLexema()+")"));
	}
	
	public void setPopError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error pop solo pueden usarlos los datos lista",
				id.getLexema()+".pop("+value.getLexema()+")"));
	}
	
	public void setPopP1Error(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error pop_P1 se esperaba un decimal",
				id.getLexema()+".pop("+value.getLexema()+")"));
	}
	
	public void setIndexPopError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error pop el indice indicado no existe",
				id.getLexema()+".pop("+value.getLexema()+")"));
	}
	
	public void setRemoveError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error remove solo pueden usarlos los datos lista",
				id.getLexema()+".remove("+value.getLexema()+")"));
	}
	
	public void setReplace(Token param1, Token param2, Token param3) {
		Analyzer.listError.add(new Error(param3.getLine(), 695, "Semantica 3",
				"Error replace solo acepta parametros de tipo string",
				"replace("+param1.getLexema()+", "+param2.getLexema()+", "+param3.getLexema()+")"));
	}
	
	public void setAppendStringError(Token id, Token value) {
		Analyzer.listError.add(new Error(value.getLine(), 695, "Semantica 3",
				"Error append con string esperaba un string",
				id.getLexema()+".append("+value.getLexema()+")"));
	}
	
	public void setErrorDimensionList(Token id, int length) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error las listas son solo de una dimension",
				id.getLexema() + "[" + length + "]"));
	}
	
	public void setErrorTypeValueList(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error se esperaba un valor entero en la posicion de la lista",
				id.getLexema() + "[" + value.getLexema() + "]"));
	}
	
	public void setErrorRangeValueList(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error indice fuera de rango",
				id.getLexema() + "[" + value.getLexema() + "]"));
	}
	
	public void setErrorArrFunctDim(Token id, Token value, String func) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error " + func + " solo permite arreglos con rango",
				id.getLexema() + "." + func + "(" + value.getLexema() + ")"));
	}
	
	public void setErrorInsertDataNotCompatible(Token id, Token value, String typeId, String typeValue) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error datos incompatibles " + typeId + " <---> " + typeValue,
				id.getLexema() + ".insert( ... , " + value.getLexema() + " )"));
	}
	
	public void setErrorParam1Insert(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error se esperaba un tipo de dato decimal",
				id.getLexema() + ".insert( "+ value.getLexema() +" , ... )"));
	}
	
	public void setErrorParam2Insert(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error se esperaba una literal decimal, float, hex, octal, binario, complejo, string o char",
				id.getLexema() + ".insert( ... , " + value.getLexema() + " )"));
	}
	
	public void setIndexOutOfBound(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error indice fuera de rango",
				value.getLexema()));
	}
	
	public void setErrorRemoveDataNotCompatible(Token id, Token value, String typeId, String typeValue) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error datos incompatibles " + typeId + " <---> " + typeValue,
				id.getLexema() + ".remove( ... , " + value.getLexema() + " )"));
	}
	
	public void setErrorStructEmpty(Token id, Token value) {
		Analyzer.listError.add(new Error(id.getLine(), 695, "Semantica 3",
				"Error estructura vacia",
				value.getLexema()));
	}
	
	public void setErrorVoid(Token line, String func) {
		Analyzer.listError.add(new Error(line.getLine(), 695, "Semantica 3",
				"Error la funcion " + func + " es un void",
				"..."+func+"(...)"));
	}
	
	public static void setErrorVoidS(Token line, String func) {
		Analyzer.listError.add(new Error(line.getLine(), 695, "Semantica 3",
				"Error la funcion " + func + " es un void",
				"..."+func+"(...)"));
	}
}
