package control.templates;

public class SymbolData {
	public String id, type, clase;
	public int ambito;
	public String rango, avance;
	public int tarr, TParr;
	public String value;
	public String nposicion;
	public String key;
	public String list_per;
	public int noPar;
	public int line;
	
	public SymbolData() { reset(); }
	
	public void reset() {
		id = type = clase = "";
		ambito = 0;
		rango = avance = "";
		tarr = TParr = 0;
		value = nposicion = key = list_per = "";
		noPar =  0;
		line = 1;
	}
}
