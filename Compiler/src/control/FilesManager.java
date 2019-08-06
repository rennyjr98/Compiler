package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *   @author rennyjr
**/

public class FilesManager {
    private static JFileChooser explorer = new JFileChooser();
    private static String dirPrefix = 
            "/home/rennyjr/NetBeansProjects/Compiler/Compiler/src/res/";
    
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
        
        while((lineOfCode = br.readLine()) != null)
            code += lineOfCode + "\n";
        return code;
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
            return extractMatrizData(dirExcelLexico, 70, 61);
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
            return extractMatrizData(dirExcelSyntax, 50, 95);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null, 
                    "No se ha podido abrir el archivo MatrizLexico.xlsx", 
                    "Error", 
                    JOptionPane.ERROR);
            return getMatrixForEmpty();
        }
    }
    
    private static int[][] getMatrixForEmpty() {
        int [][] matrix = {{-1}};
        return matrix;
    }
    
    private static int[][] extractMatrizData(String dirExcel, int row, int col) 
    throws IOException {
        int [][] transitionTable = new int[row][col];
        List rowList = extractExcelData(dirExcel);
        
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
    
    private static List extractExcelData(String dirExcel) throws IOException {
        Iterator<Row> rowIterator = getExcel(dirExcel);
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
    
    private static Iterator<Row> getExcel(String dirExcel) throws IOException {
        File fileExcel = new File(dirExcel);
        FileInputStream file = new FileInputStream(fileExcel);
        
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        
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
        
        try {
        	FileOutputStream out = new FileOutputStream(compileResult);
        	workbook.write(out);
            workbook.close();
            
            JOptionPane.showMessageDialog(null, "Excel generado con exito.",
        			"Exportar Excel", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(null, "No tengo idea de que pudo salir mal.",
        			"Error de Exportación", JOptionPane.ERROR_MESSAGE);
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
}

    