package control.templates;

public class Etiqueta {
	private String type;
	private String etiqueta_1;
	private String etiqueta_2;
	private String etiqueta_3;
	
	public int tfor1;
	public int tfor2;
	public int tfor3;
	
	public boolean firstElif = false;
	
	public static int IF_ETQ = 0;
	public static int WHILE_ETQ = 0;
	public static int FOR_ETQ = 0;
	public static int FOR_ID = 0;
	
	public String splice = "";
	
	public Etiqueta(String type, String etiqueta_1, String etiqueta_2, String etiqueta_3) {
		this.type = type;
		
		switch(type) {
		case "if":
			IF_ETQ++;
			this.etiqueta_1 = etiqueta_1 + IF_ETQ;
			this.etiqueta_2 = etiqueta_2;
			this.etiqueta_3 = etiqueta_3;
			break;
		case "while":
			WHILE_ETQ++;
			this.etiqueta_1 = etiqueta_1 + WHILE_ETQ;
			this.etiqueta_2 = etiqueta_2;
			this.etiqueta_3 = etiqueta_3;
			break;
		case "for":
			FOR_ETQ++;
			tfor1 = ++FOR_ID;
			this.etiqueta_1 = etiqueta_1 + FOR_ETQ;
			this.etiqueta_2 = etiqueta_2;
			this.etiqueta_3 = etiqueta_3;
			break;
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEtiqueta_1() {
		return etiqueta_1;
	}
	
	public void setEtiqueta_1(String etiqueta_1) {
		this.etiqueta_1 = etiqueta_1;
	}
	
	public String getEtiqueta_2() {
		return etiqueta_2;
	}
	
	public void setEtiqueta_2(String etiqueta_2) {
		this.etiqueta_2 = etiqueta_2;
	}
	
	public String getEtiqueta_3() {
		return etiqueta_3;
	}
	
	public void setEtiqueta_3(String etiqueta_3) {
		this.etiqueta_3 = etiqueta_3;
	}
	
	public boolean emptyETQ() {
		return etiqueta_1.equals("") &&
			   etiqueta_2.equals("") &&
			   etiqueta_2.equals("");
	}
}
