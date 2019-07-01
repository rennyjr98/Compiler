package design;

import control.Lexico;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *   @author rennyjr
**/

public class AppStyle extends JFrame {
    protected Dimension screenSize = Toolkit.getDefaultToolkit()
            .getScreenSize();
    
    protected JTextArea codeLines = new JTextArea();
    protected static JTextPane codeArea = new JTextPane();
    protected JTable tokensTable = new JTable(10, 3);
    protected JTable errorsTable = new JTable(10, 4);
    public static Font actualFont = new Font("Hack", Font.BOLD, 14);
    protected DefaultTableModel modelToken = new DefaultTableModel();
    protected DefaultTableModel modelError = new DefaultTableModel();
    protected JButton run = new JButton();
    protected JButton add = new JButton();
    protected JButton export = new JButton();
    protected JButton settings = new JButton();
    protected JButton monitor = new JButton();
    
    protected AppStyle() {
        this.setTitle("Compilador");
        
        init();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    protected void init() {
        JPanel main = new JPanel(null);
        JPanel header = new JPanel(null);
        JPanel tableTitlesArea = new JPanel(null);
        JScrollPane codeLinesScroll = new JScrollPane(codeLines);
        JScrollPane codeAreaScroll = new JScrollPane(codeArea);
        JScrollPane tokenTableScroll = new JScrollPane(tokensTable);
        JScrollPane errorTableScroll = new JScrollPane(errorsTable);
        JLabel titleToken = new JLabel("Tokens");
        JLabel titleError = new JLabel("Errors");
        
        formatCodeComponents(main, header, codeLinesScroll, codeAreaScroll);
        formatTableTitlesArea(tableTitlesArea);
        
        int [] titleTokenTableOnAreaLocation = {(screenSize.width/4)-30, 20};
        formatTitlesTableOnArea(titleToken, titleTokenTableOnAreaLocation);
        int [] titleErrorTableOnAreaLocation = {((screenSize.width/4)*3)-28, 
            20};
        formatTitlesTableOnArea(titleError, titleErrorTableOnAreaLocation);
        
        int [] tokensLocation = {0, 570};
        formatTables(tokenTableScroll, tokensTable, tokensLocation);
        int [] errorsLocation = {screenSize.width/2, 570};
        formatTables(errorTableScroll, errorsTable, errorsLocation);
        formatButtons(header);
        
        JScrollBar codeAreaVerticalScroll = codeLinesScroll
                .getVerticalScrollBar();
        BoundedRangeModel codeLinesModel = codeAreaScroll.
                getVerticalScrollBar().getModel();
        
        codeAreaVerticalScroll.setModel(codeLinesModel);
        
        header.add(run);
        header.add(add);
        header.add(export);
        header.add(settings);
        header.add(monitor);
        
        tableTitlesArea.add(titleToken);
        tableTitlesArea.add(titleError);
        
        main.add(header);
        main.add(codeLinesScroll);
        main.add(codeAreaScroll);
        main.add(tableTitlesArea);
        main.add(tokenTableScroll);
        main.add(errorTableScroll);
        
        this.getContentPane().add(main);
    }
    
    protected void formatCodeComponents(Object ... components) {
        formatMainPanel((JPanel)components[0]);
        formatHeader((JPanel)components[1]);
        formatCodeLines();
        formatCodeLinesScroll((JScrollPane)components[2]);
        formatCodeArea();
        formatCodeAreaScroll((JScrollPane)components[3]);
    }
    
    protected void formatMainPanel(JPanel main) {
        main.setLocation(0, 0);
        main.setSize(screenSize.width, screenSize.height-50);
    }
    
    protected void formatHeader(JPanel header) {
        header.setSize(screenSize.width, 40);
        header.setLocation(0,0); // 33
        header.setBackground(new Color(44,44,44));
    }
    
    protected void formatCodeLines() {
        codeLines.setText("    1");
        codeArea.setFont(actualFont);
        codeLines.setForeground(new Color(5,255,113));
        codeLines.setBackground(new Color(12,12,12));
    }
    
    protected void formatCodeLinesScroll(JScrollPane codeLinesScroll) {
        codeLinesScroll.setForeground(new Color(2,166,118));
        codeLinesScroll.setBackground(new Color(22,22,22));
        codeLinesScroll.setLocation(0, 40);
        codeLinesScroll.setBorder(null);
        codeLinesScroll.setSize(40, 500);
        
        codeLinesScroll.setBorder(BorderFactory.createMatteBorder(4,4,4,4, 
                new Color(12,12,12)));
        codeLinesScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        codeLinesScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    protected void formatCodeArea() {
        codeArea.setFont(actualFont);
        codeArea.setCaretColor(Color.WHITE);
        codeArea.setForeground(Color.WHITE);
        codeArea.setBackground(new Color(22,22,22));
        codeArea.setBorder(BorderFactory.createMatteBorder(4,4,4,4, 
                new Color(22,22,22)));
    }
    
    protected void formatCodeAreaScroll(JScrollPane codeAreaScroll) {
        codeAreaScroll.setLocation(40, 40);
        codeAreaScroll.setBorder(null);
        codeAreaScroll.setSize(screenSize.width-40, 500);
    }
    
    protected void formatTableTitlesArea(JPanel tableTitlesArea) {
        tableTitlesArea.setLocation(0, 520);
        tableTitlesArea.setSize(screenSize.width,50);
        tableTitlesArea.setBackground(new Color(44,44,44));
    }
    
    protected void formatTitlesTableOnArea(JLabel title, int [] location) {
        title.setSize(100,30);
        title.setForeground(Color.WHITE);
        title.setLocation(location[0], location[1]);
    }
    
    protected void formatTables(JScrollPane tableScroll, JTable table, 
            int [] location) {
        
        DefaultTableModel modelTable = getModel(table);
        table.setModel(modelTable);
        tableScroll.setLocation(location[0], location[1]);
        tableScroll.setSize(screenSize.width/2,150);
    }
    
    protected DefaultTableModel getModel(JTable table) {
        if(table.equals(tokensTable)) 
            return modelToken;
        return modelError;
    }
    
    protected void formatButtons(JPanel header) {
        setIcons();
        
        setCleanButton(run);
        run.setSize(32, 32);
        run.setLocation((header.getWidth()/2)-16, 4);
        
        setCleanButton(add);
        add.setSize(32, 32);
        add.setLocation(30, 4);
        
        setCleanButton(export);
        export.setSize(32,32);
        export.setLocation(72, 4);
        
        setCleanButton(settings);
        settings.setSize(32, 32);
        settings.setLocation(header.getWidth()-62, 5);
        
        setCleanButton(monitor);
        monitor.setSize(32, 32);
        monitor.setLocation(112, 5);
    }
    
    protected void setCleanButton(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
    }
    
    protected void setIcons() {
        Icon addIcon = new ImageIcon("src/res/img/file.png");
        Icon runIcon = new ImageIcon("src/res/img/play-button.png");
        Icon exportIcon = new ImageIcon("src/res/img/download.png");
        Icon settingsIcon = new ImageIcon("src/res/img/settings.png");
        Icon monitorIcon = new ImageIcon("src/res/img/console.png");
        
        add.setIcon(addIcon);
        run.setIcon(runIcon);
        export.setIcon(exportIcon);
        settings.setIcon(settingsIcon);
        monitor.setIcon(monitorIcon);
    }
    
    protected void setColorToLastWord() {
        String lastWord = getLastWord();
        String finalCharacter = getFinalCharacter();
        removeLastWord(lastWord+finalCharacter);
        Color colorForWord;
        
        if(Lexico.isSpecialWord(lastWord))
            colorForWord = Color.RED;
        else
            colorForWord = Color.WHITE;
        
        appendToPane(codeArea, lastWord + finalCharacter, colorForWord);
    }
    
    protected void removeLastWord(String lastWord) {
        int startSelection = codeArea.getText().length() - lastWord.length();
        int endSelection = codeArea.getText().length();
        codeArea.setSelectionStart(startSelection);
        codeArea.setSelectionEnd(endSelection);
        codeArea.replaceSelection("");
    }
    
    protected String getLastWord() {
        String [] wordsOfCode = codeArea.getText().split("\\s+");
        String lastWord = wordsOfCode[wordsOfCode.length-1];
        return lastWord;
    }
    
    protected String getFinalCharacter() {
        String code = codeArea.getText();
        char lastChar = code.charAt(code.length()-1);
        
        if(lastChar == ' ')
            return " ";
        else if(lastChar == '\n')
            return "\n";
        else return "";
    }
    
    protected void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Hack");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
        
       aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.white);
       tp.setCharacterAttributes(aset, false);
    }
    
    public static void getFontToCode(Font codeFont) {
       actualFont = codeFont;
       setFontToCode();
    }
    
    private static void setFontToCode() {
        codeArea.setFont(actualFont);
    }
}
