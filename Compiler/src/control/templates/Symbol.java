package control.templates;

import java.util.LinkedList;

public class Symbol {
	public String id, type, clase;
	public int ambito;
	public String rango, avance;
	public int tarr, TParr;
	public String value;
	public String nposicion;
	public String key;
	public String list_per;
	public int noPar;
	public int key_type = 0;
	public String data_return = "";
	public int line;
	public String ambito_padre = "";
	public String tempDato;
	public Token tempToken;
	public SymbolData lineType = new SymbolData();
	public LinkedList<SymbolData> listDatos = new LinkedList<SymbolData>();
	
	public Symbol() { reset(); }
	
	public void reset() {
		id = type = clase = "";
		ambito = 0;
		rango = avance = "";
		tarr = TParr = 0;
		value = nposicion = key = list_per = "";
		noPar =  0;
		key_type = 0;
		line = 1;
		listDatos.clear();
	}
	
	public void newLineType() {
		lineType = new SymbolData();
	}
}
