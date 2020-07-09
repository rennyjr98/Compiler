package control.templates;

public class Cuadruplo {
	private String etiqueta;
	private String action;
	private String arg1, arg2;
	private String result;
	
	
	public Cuadruplo(String etiqueta, String action, String arg1, String arg2, String result) {
		this.etiqueta = etiqueta;
		this.action = action;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.result = result;
	}
	
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
	
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getEtiqueta() {
		return this.etiqueta;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public String getArg1() {
		return this.arg1;
	}
	
	public String getArg2() {
		return this.arg2;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public String [] getSymbolConvertered() {
		String [] arr = {
				etiqueta,
				action,
				arg1,
				arg2,
				result
		};
		
		return arr;
	}
}
