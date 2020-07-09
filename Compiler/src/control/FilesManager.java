package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import control.templates.Cuadruplo;
import control.templates.Error;
import control.templates.SemanticTable;
import control.templates.Token;
import database.SqlEvent;
import design.App;

/**
 *   @author rennyjr
**/

public class FilesManager {
    private static JFileChooser explorer = new JFileChooser();
    private static String dirPrefix = 
            "/home/rennyjr/eclipse-workspace/Compiler/Compiler/src/res/";
    
    public static String getCode() {
        try {
            return extractCodeFromFile();
        } catch(IOException e) {
            return "";
        }
    }
    
    private static String extractCodeFromFile() throws IOException {
        String code = "";
        String lineOfCode = "";
        BufferedReader br = getReader(); 
        int i= 0;
        
        while((lineOfCode = br.readLine()) != null) {
            code += lineOfCode + "\n";
        }
        analyzeCharInvisible(code);
        return code;
    }
    
    private static void analyzeCharInvisible(String code) {
    	try {
            File file = new File(pathname);
            
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(code);
            bw.close();
        } catch (Exception e) { }
    }
    
    private static BufferedReader getReader() throws IOException {
        File fileCode = getCodeFile();
        FileInputStream fis = new FileInputStream(fileCode);
        InputStreamReader isr = new InputStreamReader(fis, "utf-8");
        return new BufferedReader(isr);
    }
    
    private static File getCodeFile() throws IOException {
        explorer.showOpenDialog(null);
        
        if(explorer.getSelectedFile() != null)
            return explorer.getSelectedFile();
        else throw new IOException();
    }
    
    public static int[][] getLexicoMatriz() {
        String dirExcelLexico = dirPrefix + "MatrizLexico.xlsx";
        
        try {
            return extractMatrizData(dirExcelLexico, 70, 61, 0);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null, 
                    "No se ha podido abrir el archivo MatrizLexico.xlsx", 
                    "Error", 
                    JOptionPane.ERROR);
            return getMatrixForEmpty();
        }
    }
    
    public static int[][] getSyntaxMatriz() {
        String dirExcelSyntax = dirPrefix + "Matrix_Sintactico.xlsx";
        
        try {
            return extractMatrizData(dirExcelSyntax, 50, 95, 0);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null, 
                    "No se ha podido abrir el archivo MatrizLexico.xlsx", 
                    "Error", 
                    JOptionPane.ERROR);
            return getMatrixForEmpty();
        }
    }
    
    public static String[][] getSemanticOneMatriz(int numSheet) {
    	String dirExcelSem1 = dirPrefix + "MatricesSemantica1.xlsx";
        
        try {
            return extractMatrizDataString(dirExcelSem1, 17, 17, numSheet);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null, 
                    "No se ha podido abrir el archivo MatricesSemantica1.xlsx", 
                    "Error", 
                    JOptionPane.ERROR);
            return null;
        }
    }
    
    private static int[][] getMatrixForEmpty() {
        int [][] matrix = {{-1}};
        return matrix;
    }
    
    private static int[][] extractMatrizData(String dirExcel, int row, int col, int numSheet) 
    throws IOException {
        int [][] transitionTable = new int[row][col];
        List rowList = extractExcelData(dirExcel, numSheet);
        
        for(int i = 0; i < row; i++) {
            List rowOnList = (List)rowList.get(i+1);
            for(int j = 0; j < col; j++) { 
                double num = ((XSSFCell)rowOnList.get(j+1))
                        .getNumericCellValue();
                transitionTable[i][j] = (int)num;
            }
        }
        
        return transitionTable;
    }
    
    private static String[][] extractMatrizDataString(String dirExcel, int row, int col, int numSheet) 
    	    throws IOException {
    	        String [][] transitionTable = new String[row][col];
    	        List rowList = extractExcelData(dirExcel, numSheet);
    	        
    	        for(int i = 0; i < row; i++) {
    	            List rowOnList = (List)rowList.get(i+1);
    	            for(int j = 0; j < col; j++) { 
    	                String num = ((XSSFCell)rowOnList.get(j+1))
    	                        .getStringCellValue();
    	                transitionTable[i][j] = num.toLowerCase();
    	            }
    	        }
    	        
    	        return transitionTable;
    	    }
    
    private static List extractExcelData(String dirExcel, int numSheet) throws IOException {
        Iterator<Row> rowIterator = getExcel(dirExcel, numSheet);
        List rowList = new ArrayList();
        
        while(rowIterator.hasNext()) {
            XSSFRow hssfRow = (XSSFRow) rowIterator.next();
            Iterator iterator = hssfRow.cellIterator();
            List cellTempList = new ArrayList();
                
            while (iterator.hasNext()){
                XSSFCell hssfCell = (XSSFCell) iterator.next();
                cellTempList.add(hssfCell);
            }
                
            rowList.add(cellTempList);
        }
        
        return rowList;
    }
    
    private static Iterator<Row> getExcel(String dirExcel, int numSheet) throws IOException {
        File fileExcel = new File(dirExcel);
        FileInputStream file = new FileInputStream(fileExcel);
        
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(numSheet);
        
        Iterator<Row> rowIterator = sheet.iterator();
        return rowIterator;
    }
    
    
    
    public static void exportExcel() {
        File compileResult = new File("Rene-Rodriguez-Luquin.xlsx");
        Workbook workbook = new XSSFWorkbook();
        tokenSheet(workbook);
        errorSheet(workbook);
        tokenCountersSheet(workbook);
        tokenCounterByLine(workbook);
        productionCounterSheet(workbook);
        AmbitCounterSheet(workbook);
        SemanticOneCounters(workbook);
        SymbolTable(workbook);
        SemanticTwoCounters(workbook);
        CuadruplosCounters(workbook);
        
        try {
        	FileOutputStream out = new FileOutputStream(compileResult);
        	workbook.write(out);
            workbook.close();
            
            JOptionPane.showMessageDialog(null, "Excel generado con exito.",
        			"Exportar Excel", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(null, "No tengo idea de que pudo salir mal.",
        			"Error de Exportación", JOptionPane.ERROR_MESSAGE);
        	System.out.println(e.getMessage());
        }
    }
    
    private static void createHeaders(Sheet page, String [] titles) {
    	Row filaT0 = page.createRow(0);
    	for (int i = 0; i < titles.length; i++) {
            Cell celda = filaT0.createCell(i);
            celda.setCellValue(titles[i]);
        }
    }
    
    private static void setInformationToPage(Row row, Sheet page, String ... information) {
    	for(int i = 0; i < information.length; i++) {
    		Cell celda = row.createCell(i);
    		celda.setCellValue(information[i]);
    	}
    }
    
    private static void setInformationToPage(Row row, Sheet page, int [] information) {
    	for(int i = 0; i < information.length; i++) {
    		Cell celda = row.createCell(i);
    		if(information[i] == -1000000)
    			celda.setCellValue("Total");
    		else
    			celda.setCellValue(information[i]);
    	}
    }
    
    private static void tokenSheet(Workbook workbook) {
    	int nRow = 1;
    	String pageName = "Token List";
    	String [] header = {"Linea", "Token", "Lexema"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	
    	for(Token token : Analyzer.listToken) {
    		setInformationToPage(
    				page.createRow(nRow++),
    				page,
    				token.getLine() + "",
    				token.getToken() + "",
    				token.getLexema()
    		);
    	}
    }
    
    private static void errorSheet(Workbook workbook) {
    	int nRow = 1;
    	String pageName = "Error List";
        String[] header = {"Linea", "Error", "Tipo", "Descripción", "Lexema"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	
    	for(Error error : Analyzer.listError)
    		setInformationToPage(
    				page.createRow(nRow++),
    				page,
    				error.getLine() + "",
    				error.getError() + "",
    				error.getType(),
    				error.getDescription(),
    				error.getLexema()
    		);
    }
    
    private static void tokenCountersSheet(Workbook workbook) {
    	String pageName = "Token Counters";
    	String [] header = {"Errores", "Identificadores", "Comentarios", 
    			"Palabras Reservadas", "CE-DEC", "CE-BIN", "CE-HEX", "CE-OCT",
    			"CText","CFLOAT", "CNCOMP", "CCAR", "Aritmeticos", "Monogamo",
    			"Logico", "Bit", "Identidad", "Puntuacion", "Agrupacion", 
    			"Asignacion", "Relacional"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	Row row = page.createRow(1);
    	
    	setInformationToPage(row, page, Counter.getCounters());
    }
    
    private static void tokenCounterByLine(Workbook workbook) {
    	String pageName = "Token Counters by Line";
    	String [] header = {"Linea","Errores", "Identificadores", "Comentarios", 
    			"Palabras Reservadas", "CE-DEC", "CE-BIN", "CE-HEX", "CE-OCT", 
    			"CText","CFLOAT", "CNCOMP", "CCAR", "Aritmeticos", "Monogamo", 
    			"Logico", "Bit", "Identidad", "Puntuacion", "Agrupacion", 
    			"Asignacion", "Relacional"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	sendToWriteCountersByLine(page);
    }
    
    private static void sendToWriteCountersByLine(Sheet page) {
    	LinkedList <Token> listTokenForCounter = 
    			(LinkedList <Token>) Analyzer.listToken.clone();
    	LinkedList <Error> listErrorForCounter = 
    			(LinkedList <Error>) Analyzer.listError.clone();
    	
    	int [] matrix = new int[22];
    	int lastTokenIndex = Analyzer.getSizeTokens() - 1;
    	int totalLines = Analyzer.listToken.get(lastTokenIndex).getLine();
    	
    	for(int i = 0; i < totalLines; i++) {
    		Row row = page.createRow(i+1);
    		Counter.clearArray();
    		
    		sendToCountTokenByLine(listTokenForCounter, i+1);
    		sendToCountErrorByLine(listErrorForCounter, i+1);
    		int [] tmp = Counter.getCounters();
    		matrix[0] = i+1;
    		
    		for(int j = 1; j < tmp.length; j++)
    			matrix[j] = tmp[j-1];
    		
    		setInformationToPage(row, page, matrix);
    	}
    }
    
    private static void sendToCountTokenByLine(LinkedList <Token> listToken, int line) {
    	for(int j = 0; j < listToken.size(); j++) {
			if(listToken.get(j).getLine() == line) {
				Token token = listToken.remove(j);
				Counter.setCounter(token.getToken());
				j--;
			}
		}
    }
    
    private static void sendToCountErrorByLine(LinkedList <Error> listError, int line) {
    	for(int j = 0; j < listError.size(); j++) {
			if(listError.get(j).getLine() == line) {
				Error error = listError.remove(j);
				Counter.setCounter(error.getError());
				j--;
			}
		}
    }
    
    private static void productionCounterSheet(Workbook workbook) {
    	String pageName = "Productions Counters";
    	String [] header = {"Program", "Constante", "Const-Entero",
                "List-Up-Rangos", "Term-Pascal", "Elevacion", "Simple-Exp-Pas", 
                "Factor", "Not", "OR", "OP-BIT", "AND", "ANDLOG", "ORLOG", "XORLOG",
                "EST", "ASIGN", "FUNLIST", "ARR", "FUNCIONES", "EXP-PAS"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	Row row = page.createRow(1);
    	setInformationToPage(row, page, Counter.getProductionsCounter());
    }
    
    private static void AmbitCounterSheet(Workbook workbook) {
    	String pageName = "Cont Amb";
    	String [] header = {"Ambito", "Decimal", "Binario", "Octal",
                "Hexadecimal", "Flotante", "Cadena", "Caracter", 
                "Compleja", "Booleana", "None", "Arreglo", "Tuplas", "Listas", "Registro", "Rango",
                "Conjuntos", "Diccionarios", "Total/Ambito"};
    	int [] totals = new int[19];
    	int [] line = new int[19];
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	
    	for(int i = 0; i <= Ambit.getLastAmbit(); i++) {
    		totals[0] = line[0] = i;
    		int [] counters = getCounterByAmbit(i);
    		int tTotal = 0;
    		
    		for(int j = 0; j < counters.length; j++)
    			tTotal += counters[j];
    		for(int j = 1; j < counters.length+1; j++)
    			line[j] = counters[j-1];
    		line[18] = tTotal;
    		
    		Row row = page.createRow(i+1);
    		setInformationToPage(row, page, line);
    	}
    	
    	totals[0] = -1000000;
    	int [] counters = getCounterByType();
    	for(int j = 1; j < counters.length+1; j++)
			totals[j] = counters[j-1];
		totals[18] = SqlEvent.getCountTotal();
		Row row = page.createRow(Ambit.getLastAmbit()+2);
		setInformationToPage(row, page, totals);
    }
    
    private static int[] getCounterByAmbit(int ambito) {
    	int []counters = new int[17];
    	counters[0] = SqlEvent.getCountByTipe("decimal", ambito);
    	counters[1] = SqlEvent.getCountByTipe("bin", ambito);
    	counters[2] = SqlEvent.getCountByTipe("oct", ambito);
    	counters[3] = SqlEvent.getCountByTipe("hex", ambito);
    	counters[4] = SqlEvent.getCountByTipe("float", ambito);
    	counters[5] = SqlEvent.getCountByTipe("string", ambito);
    	counters[6] = SqlEvent.getCountByTipe("char", ambito);
    	counters[7] = SqlEvent.getCountByTipe("comp", ambito);
    	counters[8] = SqlEvent.getCountByTipe("bool", ambito);
    	counters[9] = SqlEvent.getCountByTipe("none", ambito);
    	counters[10] = SqlEvent.getCountByTipe("struct", "arr", ambito);
    	counters[11] = SqlEvent.getCountByTipe("struct", "tupla", ambito);
    	counters[12] = SqlEvent.getCountByTipe("struct", "list", ambito);
    	counters[13] = SqlEvent.getCountByTipe("struct", "reg", ambito);
    	counters[14] = SqlEvent.getCountByTipe("struct", "range", ambito);
    	counters[15] = SqlEvent.getCountByTipe("struct", "conjunto", ambito);
    	counters[16] = SqlEvent.getCountByTipe("struct", "diccionario", ambito);
    	return counters;
    }
    
    private static int[] getCounterByType() {
    	int []counters = new int[17];
    	counters[0] = SqlEvent.getCountByTipeTotal("decimal");
    	counters[1] = SqlEvent.getCountByTipeTotal("bin");
    	counters[2] = SqlEvent.getCountByTipeTotal("oct");
    	counters[3] = SqlEvent.getCountByTipeTotal("hex");
    	counters[4] = SqlEvent.getCountByTipeTotal("float");
    	counters[5] = SqlEvent.getCountByTipeTotal("string");
    	counters[6] = SqlEvent.getCountByTipeTotal("char");
    	counters[7] = SqlEvent.getCountByTipeTotal("comp");
    	counters[8] = SqlEvent.getCountByTipeTotal("bool");
    	counters[9] = SqlEvent.getCountByTipeTotal("none");
    	counters[10] = SqlEvent.getCountByTipeTotal("struct", "arr");
    	counters[11] = SqlEvent.getCountByTipeTotal("struct", "tupla");
    	counters[12] = SqlEvent.getCountByTipeTotal("struct", "list");
    	counters[13] = SqlEvent.getCountByTipeTotal("struct", "reg");
    	counters[14] = SqlEvent.getCountByTipeTotal("struct", "range");
    	counters[15] = SqlEvent.getCountByTipeTotal("struct", "conjunto");
    	counters[16] = SqlEvent.getCountByTipeTotal("struct", "diccionario");
    	return counters;
    }
    
    private static void SemanticOneCounters(Workbook workbook) {
    	String pageName = "Semantica 1 Counters";
    	String [] header = {"Linea", "Decimal", "Binario",
                "Octal", "Hexadecimal", "Flotante", "Cadena", 
                "Caracter", "Compleja", "Booleana", "None", "Lista", "Rango", "Variante", "Asignacion"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);

    	for(int i = 0; i < Semantic.getCounter().size(); i++) {
        	Row row = page.createRow(i+1);
    		setInformationToPage(row, page, Semantic.getCounter().get(i));
    	}
    }
    
    private static void SymbolTable(Workbook workbook) {
    	String pageName = "Symbol Table";
    	String [] header = {"ID", "Tipo", "Clase",
                "Ambito", "Rango", "Avance", "Tamaño Arreglo", 
                "Dimension Arreglo", "Value", "Posicion", "Llave", "Lista Pertenece", "No. Parametro", "Tipo Llave", "Return"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	
    	ResultSet tableAmbit = SqlEvent.getTable();
    	int i = 0;
    	try {
	    	while(tableAmbit.next()) {
	    		Row row = page.createRow(i+1);
	    		String data [] = {
	    				tableAmbit.getString("id"),
	    				tableAmbit.getString("type"),
	    				tableAmbit.getString("class"),
	    				tableAmbit.getInt("ambito")+"",
	    				tableAmbit.getString("rango"),
	    				tableAmbit.getString("avance"),
	    				tableAmbit.getInt("tarr")+"",
	    				tableAmbit.getInt("tparr")+"",
	    				tableAmbit.getString("value"),
	    				tableAmbit.getString("nposicion"),
	    				tableAmbit.getString("llave"),
	    				tableAmbit.getString("list_per"),
	    				tableAmbit.getInt("nopar")+"",
	    				tableAmbit.getString("llave_type"),
	    				tableAmbit.getString("data_return"),
	    		};
	    		setInformationToPage(row, page, data);
	    		i++;
	    	}
    	} catch(SQLException e) {
    		App.showErrorMessage();
    		System.out.println(e.getMessage());
    	}
    }
    
    private static void SemanticTwoCounters(Workbook workbook) {
    	String pageName = "Semantica 2 Counters";
    	String [] header = {"Regla", "Tope Pila", "Valor Real",
                "Linea", "Estado", "Ambito"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);

    	for(int i = 0; i < SemanticTable.table.size(); i++) {
        	Row row = page.createRow(i+1);
    		setInformationToPage(row, page, SemanticTable.table.get(i).getSymbolConvertered());
    	}
    }
    
    private static void CuadruplosCounters(Workbook workbook) {
    	String pageName = "Cuadruplos Counters";
    	String [] header = {"Etiqueta", "Accion", "Arg1",
                "Arg2", "Result"};
    	
    	Sheet page = workbook.createSheet(pageName);
    	createHeaders(page, header);
    	
		if(Analyzer.listError.isEmpty())
			Analyzer.listCuad.add(new Cuadruplo("ENDMAIN", "", "", "", ""));

    	for(int i = 0; i < Analyzer.listCuad.size(); i++) {
        	Row row = page.createRow(i+1);
    		setInformationToPage(row, page, Analyzer.listCuad.get(i).getSymbolConvertered());
    	}
    }
    
    private static String pathname = "/home/rennyjr/Documents/School/Requerimientos/bibliografia_ensayo.txt";
}

    