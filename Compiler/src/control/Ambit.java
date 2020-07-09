package control;

import java.util.Stack;

import control.templates.Symbol;
import control.templates.SymbolData;
import control.templates.Token;
import control.templates.Cuadruplo;
import control.templates.Error;
import control.templates.SemanticTable;
import database.SqlEvent;

public class Ambit extends Analyzer {
	public static boolean declarationArea = true;
	private static int ambito = 0;
	private static boolean lessSymbol = false;
	public static int lessCounter = 0;
	public static boolean sendArr = true;
	public static Symbol symbol = new Symbol();
	public static Stack<Symbol> methodsStack = new Stack<Symbol>();
	private static Stack<Integer> actual_ambito = new Stack<Integer>();
	
	public static void init() {
		actual_ambito.push(ambito);
	}
	
	public static void analyze(String id) { }
	
	public static Stack<Integer> getAmbitoStack() {
		return actual_ambito;
	}
	
	public static int getLastAmbit() {
		return ambito;
	}
	
	public static void setIdAmbito(String id, int line) {
		symbol.id = id;
		symbol.line = line;
		symbol.ambito = actual_ambito.peek();
		//System.out.println(id + " - " + line + " - " + actual_ambito.peek());
	}
	
	public static void addToListOfType() {
		//symbol.listDatos.add();
	}
	
	public static void setType(int type) {
		switch(type) {
		case 807: symbol.type = "float"; break;
		case 808: symbol.type = "string"; break;
		case 809: symbol.type = "char"; break;
		case 810: symbol.type = "comp"; break;
		case 811: case 812: symbol.type = "bool"; break;
		case 813: symbol.type = "none"; break;
		case 814: symbol.type = "decimal"; break;
		case 815: symbol.type = "bin"; break;
		case 816: symbol.type = "hex"; break;
		case 817: symbol.type = "oct"; break;
		}
		
		symbol.lineType.type = symbol.type;
		symbol.lineType.list_per = symbol.id;
		setVariableClass(type);
	}
	
	public static void setVariableClass(int type) {
		switch(type) {
		case 807: case 808: case 809: case 810:
		case 811: case 812: case 813: case 814:
		case 815: case 816: case 817:
			symbol.clase = "var";
			break;
		}
	}
	
	public static void tArrUp() {
		symbol.tarr++;
	}
	
	public static void sendFunction() {
		symbol.clase = "func";
		symbol.type = "none";

		if(!SqlEvent.ifSymbolExists(symbol)) {
			SqlEvent.sendSymbol(symbol);
			methodsStack.add(getCopyOfSymbol());
			SemanticTable.addRule("1130", "id", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
			Analyzer.listCuad.add(new Cuadruplo("def", symbol.id + ":", "", "", ""));
		} else { 
			addAmbitDupError();
			SemanticTable.addRule("1130", "id", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
		}
		symbol.reset();
	}
	
	private static Symbol getCopyOfSymbol() {
		Symbol method = new Symbol();
		method.ambito = symbol.ambito;
		method.id = symbol.id;
		return method;
	}
	
	private static void addAmbitDupError() {
		int error = 690;
		String type = "Ambito";
		String desc = "Elemento duplicado";
		Analyzer.listError.add(new Error(symbol.line, error, type, desc, symbol.id));
	}
	
	public static void addAmbitDeclaError(int line) {
		int error = 691;
		String type = "Ambito";
		String desc = "Elemento no declarado";
		Analyzer.listError.add(new Error(line, error, type, desc, symbol.id));
		SemanticTable.addRule("1130", "id", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
	}
	
	public static void sendVariable() {
		if(!SqlEvent.ifSymbolExists(symbol)) {
			if(symbol.type.equals("struct") && symbol.clase.equals("list")) {
				if(isListFormatArr()) 
					setConfFormatArr();
			} if(symbol.type.equals("struct"))
				if(symbol.TParr == 0)
					symbol.TParr = 1;
			
			SqlEvent.sendSymbol(symbol); 
			SemanticTable.addRule("1130", "id", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
			
			if(symbol.type.equals("struct") && !symbol.clase.equals("range")) {
				setPositions();
				for(int i = 0; i < symbol.listDatos.size(); i++)
					SqlEvent.sendSymbol(symbol.listDatos.get(i));
			}
		} else { 
			addAmbitDupError();
			SemanticTable.addRule("1130", "id", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
		}
		symbol.reset();
	}
	
	private static void setPositions() {
		for(int i = 0; i < symbol.listDatos.size(); i++)
			symbol.listDatos.get(i).nposicion = i + "";
	}
	
	private static void setConfFormatArr() {
		symbol.clase = "arr";
		ambito--;
		Semantic.checkDimensionStruct(symbol);
		
		for(SymbolData symbol : symbol.listDatos)
			symbol.ambito = ambito;
	}
	
	public static boolean isListFormatArr() {
		String type = symbol.listDatos.get(0).type;
		for(int i = 0; i < symbol.listDatos.size(); i++) {
			if(!type.equals(symbol.listDatos.get(i).type))
				return false;
		}
		
		return true;
	}
	
	public static void sendParam() {
		symbol.clase = "param";
		symbol.type = "none";
		symbol.list_per = methodsStack.peek().id;
		
		if(!SqlEvent.ifSymbolExists(symbol)) {
			SqlEvent.sendSymbol(symbol);
			SemanticTable.addRule("1130", "Param", symbol.id, symbol.line, "ACEPTA", Ambit.getAmbitoStack().peek());
			SqlEvent.upParam(methodsStack.peek());
		} else { 
			addAmbitDupError();
			SemanticTable.addRule("1130", "Param", symbol.id, symbol.line, "ERROR", Ambit.getAmbitoStack().peek());
		}
		symbol.reset();
	}
	
	public static void setTupla() {
		symbol.clase = "tupla";
		symbol.type = "struct";
	}
	
	public static void setRange() {
		symbol.clase = "range";
		symbol.type = "struct";
	}
	
	public static void addRange() {
		if(lessSymbol)
			symbol.tempDato = "-" + symbol.tempDato;
		symbol.rango = symbol.tempDato;
		lessSymbol = false;
	}
	
	public static void updateRange() {
		if(lessSymbol)
			symbol.tempDato = "-" + symbol.tempDato;
		symbol.rango += "," + symbol.tempDato;
		lessSymbol = false;
	}
	
	public static void addAvance() {
		if(lessSymbol)
			symbol.tempDato = "-" + symbol.tempDato;
		symbol.avance = symbol.tempDato;
		lessSymbol = false;
		addToListOfData();
		Semantic.checkDimensionRange(symbol);
	}
	
	public static void setDictionary() {
		symbol.clase = "diccionario";
		symbol.type = "struct";
	}
	
	public static void setConjunto() {
		symbol.clase = "conjunto";
		symbol.type = "struct";
	}
	
	public static void setArr() {
		symbol.clase = "list";
		symbol.type = "struct";
		symbol.tarr = symbol.listDatos.size();
	}
	
	public static void setValuesTuplas() {
		symbol.lineType.ambito = actual_ambito.peek();
		symbol.lineType.clase = "datoTupla";
		symbol.lineType.ambito_padre = symbol.ambito + "";
		addToListOfData();
	}
	
	public static void setValue() {
		symbol.lineType.value = symbol.tempDato;
		symbol.lineType.ambito = actual_ambito.peek();
		symbol.lineType.ambito_padre = symbol.ambito + "";
		symbol.lineType.clase = "datoConj";
		addToListOfData();
	}
	
	public static void setKey() {
		int actualSize = symbol.listDatos.size() - 1;
		symbol.listDatos.get(actualSize).clase = "datoDic";
		symbol.listDatos.get(actualSize).key = symbol.tempDato;
		symbol.listDatos.get(actualSize).key_type = symbol.tempToken.getToken();
		symbol.listDatos.get(actualSize).ambito_padre = symbol.ambito + "";
	}
	
	public static void setElementList() {
		symbol.lineType.value = symbol.tempDato;
		symbol.lineType.ambito = actual_ambito.peek();
		symbol.lineType.clase = "datoLista";
		symbol.lineType.ambito_padre = symbol.ambito + "";
		addToListOfData();
	}
	
	public static void addToListOfData() {
		if(lessSymbol)
			symbol.lineType.value = "-" + symbol.lineType.value; 
		symbol.listDatos.add(symbol.lineType);
		symbol.newLineType();
		lessSymbol = false;
	}
	
	public static void lessSymbolAparition() {
		lessSymbol = true;
	}
	
	public static void turnOfLessSymbolAparition() {
		lessSymbol = false;
	}
	
	public static void inDeclarationArea() {
		declarationArea = true;
	}
	
	public static void ambitoUp() {
		ambito++;
		actual_ambito.push(ambito);
	}
	
	public static void ambitoDown() {
		actual_ambito.pop();
	}
	
	public static void outDeclarationArea() {
		declarationArea = false;
	}
	
	public static void resetAmbito() {
		declarationArea = true;
		ambito = 0;
		symbol = new Symbol();
		actual_ambito.clear();
		actual_ambito.add(ambito);
		lessCounter = 0;
	}
}
