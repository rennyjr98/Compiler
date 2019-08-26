package control.templates;

public class Symbol {
	public String id, type, clase;
	public int ambito;
	public int tarr, dimarr;
	public int noPar, TParr;
	public int line;
	
	public Symbol() { }
	
	public void reset() {
		id = type = clase = "";
		tarr = 0;
		noPar = TParr = 0;
		ambito = 0;
		dimarr = 0;
		line = 1;
	}
}
