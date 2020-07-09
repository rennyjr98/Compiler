package control.templates;

public class SymbolSem2 {
	public String regla;
	public String topePila;
	public String valorReal;
	public int line;
	public String state;
	public int ambito;
	
	public SymbolSem2() {
		
	}
	
	public String [] getSymbolConvertered() {
		String [] arr = {
				regla+"",
				topePila,
				valorReal,
				line+"",
				state,
				ambito+""
		};
		
		return arr;
	}
}
