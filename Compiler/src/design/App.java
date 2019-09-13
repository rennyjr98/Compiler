package design;

import control.Ambit;
import control.Analyzer;
import control.Counter;
import control.FilesManager;
import control.Lexico;
import control.SemanticOne;
import control.Syntax;
import control.templates.Error;
import control.templates.Token;
import database.Sql;
import database.SqlEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.text.Element;

/**
 *     @author rennyjr
**/

public class App extends AppStyle implements KeyListener, ActionListener {
    private Lexico lexico;
    private Syntax syntax;
    
    public App() {
        super();
        preparedComponents();
        codeArea.requestFocus();
        loading.setVisible(false);
    }
    
    public void preparedComponents() {
        String [] columnTokensTable = {"Linea", "Token", "Lexema"};
        String [] columnErrorsTable = {"Linea", "Error", "Tipo", "Descripcion",
            "Lexema"};
        String [] columnAmbitTable = {"ID", "Tipo", "Clase", "Ambito", "Rango", "Avance", "Tarr", "Amb C",
        		"Valor", "NPosicion", "LLave", "List-Per", "NPar"};
        
        for(int i = 0; i < columnTokensTable.length; i++) 
            modelToken.addColumn(columnTokensTable[i]);
        for(int i = 0; i < columnErrorsTable.length;i++) 
            modelError.addColumn(columnErrorsTable[i]);
        for(int i = 0; i < columnAmbitTable.length;i++) 
            modelAmbit.addColumn(columnAmbitTable[i]);
        
        lexico = new Lexico(FilesManager.getLexicoMatriz());
        syntax = new Syntax(FilesManager.getSyntaxMatriz());
        SemanticOne.matrixSuma = FilesManager.getSemanticOneMatriz(0);
        SemanticOne.matrixResta = FilesManager.getSemanticOneMatriz(1);
        SemanticOne.matrixMulti = FilesManager.getSemanticOneMatriz(2);
        SemanticOne.matrixDiv = FilesManager.getSemanticOneMatriz(3);
        SemanticOne.matrixRel = FilesManager.getSemanticOneMatriz(4);
        SemanticOne.matrixOper = FilesManager.getSemanticOneMatriz(5);
        SemanticOne.matrixDespl = FilesManager.getSemanticOneMatriz(6);
        SemanticOne.matrixDivEn = FilesManager.getSemanticOneMatriz(7);
        
        run.addActionListener(this);
        add.addActionListener(this);
        export.addActionListener(this);
        codeArea.addKeyListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        codeLines.setText(getText());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        codeLines.setText(getText());
        
        if(e.isControlDown()) {
        	if(e.getKeyCode() == KeyEvent.VK_E)
        		FilesManager.exportExcel();
        	else if(e.getKeyCode() == KeyEvent.VK_O)
        		setNewCode();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	loading.setVisible(true);
        codeLines.setText(getText());
        if(e.getKeyCode() == KeyEvent.VK_F6)
        	runCompiler();
    	loading.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton)e.getSource();
    	loading.setVisible(true);
        
        if(btn.equals(add))
            setNewCode();
        else if(btn.equals(run)) 
            runCompiler();
        else if(btn.equals(export))
        	FilesManager.exportExcel();
    	loading.setVisible(false);
    }
    
    private void runCompiler() {
    	/*long iniTime = System.currentTimeMillis();
    	long finishTime;*/
    	
    	try {
    		resetCompiler();
    		runLexico();
    		runSyntax();
    		//finishTime = System.currentTimeMillis();
    		//System.out.println("Compilation time : " + (finishTime - iniTime) + " ms\n\n");
    		
    		JOptionPane.showMessageDialog(null, "Compilacion Exitosa", 
                    "Compilacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
    	}catch(Exception error) {
            JOptionPane.showMessageDialog(null, "No tengo idea que pudo salir mal.", 
                    "Error de Compilacion", JOptionPane.ERROR_MESSAGE);
            System.out.println(error.getMessage());
            resetCompiler();
        }
    }
    
    private void resetCompiler() {
    	SqlEvent.deleteAll();
    	lexico.resetLexico();
        syntax.reset();
        Ambit.resetAmbito();
        SemanticOne.reset();
        defaultTables();
        Counter.clearCounters();
    }
    
    private void setNewCode() {
        codeArea.setText(FilesManager.getCode());
        codeLines.setText(getText());
    }
    
    private void runLexico() {
        lexico.analyze(preparedCode());
        putTokensInTable();
    }
    
    private void runSyntax() {
        if(Analyzer.getSizeTokens() > 0) {
            syntax.prepareTokensForSyntax();
            syntax.analyze();
            putErrorsInTable();
            putAmbitInTable();
        }
    }
    
    private void defaultTables() { 
        defaultTokenTable();
        defaultErroTable();
        defaultAmbitTable();
    }
    
    private String preparedCode() {
        String code = codeArea.getText().toLowerCase();
        code += '\n';
        return code;
    }
    
    private void putTokensInTable() {
        String [] ar_token = new String[3];
        
        for(int i = 0; i < Analyzer.getSizeTokens(); i++) {
            Token token = Analyzer.getToken(i);
            ar_token[0] = token.getLine()+"";
            ar_token[1] = token.getToken()+"";
            ar_token[2] = token.getLexema();
            modelToken.addRow(ar_token);
        }
    }
    
    private void defaultTokenTable() {
        int a = modelToken.getRowCount()-1;
         for(int i = a; i >= 0; i--)
            modelToken.removeRow(i);
    }
    
    private void putErrorsInTable() {
        String [] ar_error = new String[5];
        
        for(int i = 0; i < Analyzer.getSizeError(); i++) {
            Error error = Analyzer.getError(i);
            ar_error[0] = error.getLine()+"";
            ar_error[1] = error.getError()+"";
            ar_error[2] = error.getType();
            ar_error[3] = error.getDescription();
            ar_error[4] = error.getLexema();
            modelError.addRow(ar_error);
        }
    }
    
    private void defaultErroTable() {
        int a = modelError.getRowCount()-1;
         for(int i = a; i >= 0; i--)
            modelError.removeRow(i);
    }
    
    private void defaultAmbitTable() {
        int a = modelAmbit.getRowCount()-1;
         for(int i = a; i >= 0; i--)
            modelAmbit.removeRow(i);
    }
    
    private void putAmbitInTable() {
        String [] ar_symbol = new String[13];
        ResultSet rs = SqlEvent.getTable();
        
        try {
	        while(rs.next()) {
	        	ar_symbol[0] = rs.getString("id");
	        	ar_symbol[1] = rs.getString("type");
	        	ar_symbol[2] = rs.getString("class");
	        	ar_symbol[3] = rs.getString("ambito");
	        	ar_symbol[4] = rs.getString("rango");
	        	ar_symbol[5] = rs.getString("avance");
	        	ar_symbol[6] = rs.getInt("tarr")+"";
	        	ar_symbol[7] = rs.getInt("tparr")+"";
	        	ar_symbol[8] = rs.getString("value");
	        	ar_symbol[9] = rs.getString("nposicion");
	        	ar_symbol[10] = rs.getString("llave");
	        	ar_symbol[11] = rs.getString("list_per");
	        	ar_symbol[12] = rs.getInt("nopar")+"";
	            modelAmbit.addRow(ar_symbol);
	        }
        } catch(SQLException e) {
        	showErrorMessage();
        }
    }
    
    public String getText() {
        int caretPosition = codeArea.getDocument().getLength();
		Element root = codeArea.getDocument().getDefaultRootElement();
		String text = "1\n";
		for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++)
	            text += i + "\n";
		return text;
    }
    
    public static void showErrorMessage() {
    	JOptionPane.showMessageDialog(null, 
				"Ups... Esto es un poco vergonzoso, no tengo idea de lo que ha ocurrido, pronto lo resolvere.",
				"Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String ... args) {
        new App();
    }
}
