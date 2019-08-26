package control;

import java.util.Stack;

import control.templates.Symbol;
import control.templates.Error;
import database.SqlEvent;

public class Ambit extends Analyzer {
	public static boolean declarationArea = true;
	private static int ambito = 0;
	public static Symbol symbol = new Symbol();
	private static Stack<Integer> actual_ambito = new Stack<Integer>();
	
	public static void init() {
		actual_ambito.push(ambito);
	}
	
	public static void analyze(String id) { }
	
	public static Stack<Integer> getAmbitoStack() {
		return actual_ambito;
	}
	
	public static void setIdAmbito(String id, int line) {
		symbol.id = id;
		symbol.line = line;
		symbol.ambito = actual_ambito.peek();
		//System.out.println(id + " - " + line + " - " + actual_ambito.peek());
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

		if(!SqlEvent.ifSymbolExists(symbol))
			SqlEvent.sendSymbol(symbol);
		else addAmbitDupError();
		symbol.reset();
	}
	
	private static void addAmbitDupError() {
		int error = 690;
		String type = "Ambito";
		String desc = "Elemento duplicado";
		Analyzer.listError.add(new Error(symbol.line, error, type, desc, symbol.id));
	}
	
	public static void addAmbitDeclaError() {
		int error = 691;
		String type = "Ambito";
		String desc = "Elemento no declarado";
		Analyzer.listError.add(new Error(symbol.line, error, type, desc, symbol.id));
	
	}
	
	public static void sendVariable() {
		if(!SqlEvent.ifSymbolExists(symbol))
			SqlEvent.sendSymbol(symbol);
		else addAmbitDupError();
		symbol.reset();
	}
	
	public static void sendParam() {
		symbol.clase = "param";
		symbol.type = "none";
		
		if(!SqlEvent.ifSymbolExists(symbol)) {
			SqlEvent.sendSymbol(symbol);
			SqlEvent.upParam(symbol);
		} else addAmbitDupError();
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
	
	public static void setDictionary() {
		symbol.clase = "diccionario";
		symbol.type = "struct";
	}
	
	public static void setConjunto() {
		symbol.clase = "conjunto";
		symbol.type = "struct";
	}
	
	public static void setArr() {
		symbol.clase = "arr";
		symbol.type = "struct";
	}
	
	public static void inDeclarationArea() {
		declarationArea = true;
		ambito++;
		actual_ambito.push(ambito);
		//System.out.println("In " + Ambit.declarationArea + "\n--------");
	}
	
	public static void outDeclarationArea() {
		declarationArea = false;
		//System.out.println("Out " + Ambit.declarationArea + "\n--------");
	}
	
	public static void inDeclarationAreaLess() {
		declarationArea = true;
		actual_ambito.pop();
		//if(actual_ambito.size() > 0)
			//System.out.println("In " + Ambit.declarationArea + "\n--------");
	}
	
	public static void resetAmbito() {
		declarationArea = true;
		ambito = 0;
		symbol = new Symbol();
		actual_ambito.clear();
		actual_ambito.add(ambito);
	}
}
