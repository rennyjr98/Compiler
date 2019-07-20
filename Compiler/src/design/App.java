package design;

import control.Analyzer;
import control.FilesManager;
import control.Lexico;
import control.Syntax;
import control.Error;
import control.Token;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    }
    
    public void preparedComponents() {
        String [] columnTokensTable = {"Linea", "Token", "Lexema"};
        String [] columnErrorsTable = {"Linea", "Error", "Tipo", "Descripcion",
            "Lexema"};
        
        for(int i = 0; i < columnTokensTable.length; i++) 
            modelToken.addColumn(columnTokensTable[i]);
        for(int i = 0; i < columnErrorsTable.length;i++) 
            modelError.addColumn(columnErrorsTable[i]);
        
        lexico = new Lexico(FilesManager.getLexicoMatriz());
        syntax = new Syntax(FilesManager.getSyntaxMatriz());
        
        run.addActionListener(this);
        add.addActionListener(this);
        settings.addActionListener(this);
        export.addActionListener(this);
        monitor.addActionListener(this);
        codeArea.addKeyListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        codeLines.setText(getText());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        codeLines.setText(getText());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        codeLines.setText(getText());
        if(e.getKeyCode() == KeyEvent.VK_F6) {
        	runLexico();
            runSyntax();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton)e.getSource();
        
        if(btn.equals(add))
            setNewCode();
        else if(btn.equals(run)) 
                runCompiler();
        else if(btn.equals(monitor))
            (new MonitorView()).show();
        else if(btn.equals(export))
        	FilesManager.exportExcel();
    }
    
    private void runCompiler() {
    	try {
    		runLexico();
    		runSyntax();
    		JOptionPane.showMessageDialog(null, "Compilacion Exitosa", 
                    "Compilacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
    	}catch(Exception error) {
            JOptionPane.showMessageDialog(null, "No tengo idea que pudo salir mal.", 
                    "Error de Compilacion", JOptionPane.ERROR_MESSAGE);
            MonitorView.vitals += "[*] Error Fatal : " + error.getMessage();
            resetCompiler();
        }
    }
    
    private void resetCompiler() {
    	lexico.resetLexico();
        defaultTables();
    }
    
    private void setNewCode() {
        codeArea.setText(FilesManager.getCode());
        codeLines.setText(getText());
    }
    
    private void runLexico() {
        lexico.resetLexico();
        defaultTables();
        lexico.analyze(preparedCode());
        putTokensInTable();
    }
    
    private void runSyntax() {
        if(Analyzer.getSizeTokens() > 0) {
            syntax.reset();
            syntax.prepareTokensForSyntax();
            syntax.analyze();
        }
        putErrorsInTable();
    }
    
    private void defaultTables() { 
        defaultTokenTable();
        defaultErroTable();
    }
    
    private String preparedCode() {
        String code = codeArea.getText();
        code += '\n';
        return code.toLowerCase();
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
    
    public String getText() {
        int caretPosition = codeArea.getDocument().getLength();
		Element root = codeArea.getDocument().getDefaultRootElement();
		String text = "1\n";
		for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++)
	            text += i + "\n";
		return text;
    }
    
    public static void main(String ... args) {
        new App();
    }
}
