package control;

public class Counter {
	private static int [] lexicalCounts = new int[21];
	
	/* 
	  ------------------------------------------
	   DECLARACIÓN DE CONSTANTES DE POSICIÓN EN
	   COUNTS, AL FINAL DEL CÓDIGO
	  ------------------------------------------ 
	*/
	
	public static int[] getCounters() {
		return lexicalCounts;
	}
	
	public static void setCounter(int type) {
		if(type == -1)
			lexicalCounts[identificadores]++;
		else if(type == -2 || type == -3)
			lexicalCounts[comentarios]++;
		else if(type <= -4 && type >= -11)
			constantCounter(type);
		else if(isAritmetic(type))
			lexicalCounts[aritmetic]++;
		else if(type == -13 || type == -16)
			lexicalCounts[monogamo]++;
		else if(isAsig(type))
			lexicalCounts[asig]++;
		else if(isRelacional(type))
			lexicalCounts[relacional]++;
		else if(isBit(type))
			lexicalCounts[bit]++;
		else if(isLogic(type))
			lexicalCounts[logico]++;
		else if(isPuntuation(type))
			lexicalCounts[puntuacion]++;
		else if(isAgrup(type))
			lexicalCounts[agrup]++;
		else if(type == -98 || type == -93)
			lexicalCounts[identidad]++;
		else if(type <= -52 && type <= -92)
			lexicalCounts[reservP]++;
		else
			checkForErrors(type);
	}
	
	private static void constantCounter(int type) {
		switch(type) {
			case -4:  lexicalCounts[ccar]++;   break; 
	        case -5:  lexicalCounts[ctext]++;  break; 
	        case -6:  lexicalCounts[ce_bin]++; break;
	        case -7:  lexicalCounts[ce_hex]++; break; 
	        case -8:  lexicalCounts[ce_oct]++; break; 
	        case -9:  lexicalCounts[cfloat]++; break;
	        case -10: lexicalCounts[cncomp]++; break;
	        case -11: lexicalCounts[ce_dec]++; break; 
		}
	}
	
	private static boolean isAritmetic(int type) {
		return  type == -12 || type == -15 || type == -18 ||
				type == -19 || type == -22 || type == -23 ||
				type == -26;
	}
	
	private static boolean isAsig(int type) {
		return 	type == -14 || type == -17 || type ==-20 ||
				type == -21 || type == -24 || type == -25 || 
				type == -27 || type == -28;
	}
	
	private static boolean isRelacional(int type) {
    	return type <=-29 && type >= -33 && type != -31;
	}
	
	private static boolean isBit(int type) {
		return 	type == -31 || type == -34 && type == -38 ||
				type == -40;
	}
	
	private static boolean isLogic(int type) {
    	return  type == -36 || type == -37 || type == -39 || 
    			type == -41 || type == -42 || type == -96;
	}
	
	private static boolean isPuntuation(int type) {
    	return  type == -43 || type == -44 || type == -95 || 
    			type == -94;
	}
	
	private static boolean isAgrup(int type) {
		return type == -45 || (type <= -47 && type <= -51) || 
				type == -97;
	}
	
	private static void checkForErrors(int type) {
        if(type >= 500 && type <= 632)
        	lexicalCounts[errores]++;
    }
	
	/* 
	  ------------------------------------------
	            DECLARACIONES LARGAS
	  ------------------------------------------ 
	*/
	
	private static final int errores = 0, 
			identificadores = 1, 
			comentarios = 2, 
			reservP = 3, 
			ce_dec = 4,
	        ce_bin = 5, 
	        ce_hex = 6, 
	        ce_oct = 7, 
	        ctext = 8, 
	        cfloat = 9, 
	        cncomp = 10,
	        ccar = 11, 
	        aritmetic = 12, 
	        monogamo = 13, 
	        logico = 14, 
	        bit = 15, 
	        identidad = 16,
	        puntuacion = 17, 
	        agrup = 18, 
	        asig = 19, 
	        relacional = 20;
}
