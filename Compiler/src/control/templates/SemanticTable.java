package control.templates;

import java.util.LinkedList;

public class SemanticTable {
	public static LinkedList <SymbolSem2> table = new LinkedList<SymbolSem2>();
	
	public SemanticTable() {
		
	}
	
	public static void addRule(String regla, String topePila, String valorReal, int line, String state, int ambito) {
		SymbolSem2 rule = new SymbolSem2();
		rule.regla = regla;
		rule.topePila = topePila;
		rule.valorReal = valorReal;
		rule.line = line;
		rule.state = state;
		rule.ambito = ambito;
		table.add(rule);
	}
	
	public static void reset() {
		table.clear();
	}
}
