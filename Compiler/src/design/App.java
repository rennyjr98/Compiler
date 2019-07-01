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

/**
 *     @author rennyjr
**/

public class App extends AppStyle implements KeyListener, ActionListener {
    private Lexico lexico;
    private Syntax syntax;
    private AppFont appFont;
    
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
        
        int [][] tmpTransitionTable = FilesManager.getLexicoMatriz();
        lexico = new Lexico(tmpTransitionTable);
        tmpTransitionTable = FilesManager.getSyntaxMatriz();
        syntax = new Syntax(tmpTransitionTable);
        
        run.addActionListener(this);
        add.addActionListener(this);
        settings.addActionListener(this);
        export.addActionListener(this);
        monitor.addActionListener(this);
        codeArea.addKeyListener(this);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_BACK_SPACE:
                codeLines.setText("");
                setLinesOfCode();
                break;
            case KeyEvent.VK_F6:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_BACK_SPACE:
                codeLines.setText("");
                setLinesOfCode();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //setColorToLastWord();
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_BACK_SPACE:
                codeLines.setText("");
                setLinesOfCode();
                break;
            case KeyEvent.VK_F6:
                runLexico();
                runSyntax();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton)e.getSource();
        
        if(btn.equals(add))
            setNewCode();
        else if(btn.equals(run)) {
            try {
                runLexico();
                runSyntax();
                JOptionPane.showMessageDialog(null, "Compilacion Exitosa", 
                        "Compilacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } catch(Exception error) {
                JOptionPane.showMessageDialog(null, 
                        "Ocurrio un error durante la compilacion.", 
                        "Error de Compilacion", 
                        JOptionPane.ERROR_MESSAGE);
                AppMonitor.vitals += "[*] Error Fatal : s" + error.getMessage();
                lexico.resetLexico();
                defaultTables();
            }
        } else if(btn.equals(settings)) {
            appFont = new AppFont();
            appFont.setVisible(true);
        } else if(btn.equals(monitor))
            (new AppMonitor()).show();
    }
    
    private void setNewCode() {
        codeArea.setText(FilesManager.getCode());
        codeLines.setText("");
        setLinesOfCode();
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
    
    public void setLinesOfCode() {
        String counts = countLinesOfCode();
        codeLines.setText(counts);
    }
    
    public String countLinesOfCode() {
        String counts = "";
        
        /*for(int i = 0; i < codeArea; i++) {
            int actualLine = i+1;
            String postfix = actualLine + "\n";
            
            if(actualLine < 10)
                counts += "    " + postfix;
            else if(actualLine >= 10 && actualLine < 100)
                counts += "   " + postfix;
            else
                counts += " " + postfix;
        }*/
        
        return counts;
    }
    
    
    public static void main(String ... args) {
        new App();
    }
}
