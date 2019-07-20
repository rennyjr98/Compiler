package design;

import control.Lexico;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 *   @author rennyjr
**/

public class AppStyle extends JFrame {
    protected JTextArea codeLines = new JTextArea();
    protected static JTextPane codeArea = new JTextPane();
    protected JScrollPane codeAreaScroll = new JScrollPane(codeArea);
    
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
    
    
    protected JLabel titleToken = new JLabel("Tokens");
    protected JLabel titleError = new JLabel("Errors");
    
    protected JScrollPane tokenTableScroll = new JScrollPane(tokensTable);
    protected JScrollPane errorTableScroll = new JScrollPane(errorsTable);
    
    JPanel tableTitlesArea = new JPanel(null);
    JPanel header = new JPanel(null);
    
    protected AppStyle() {
        this.setTitle("Compilador");
        
        init();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        resizeComponent();
        
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                resizeComponent();
            }
        });
        
        this.setVisible(true);
        this.setMinimumSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    protected void init() {
        JPanel main = new JPanel(null);
        
        formatCodeComponents(main, header);
        formatTableTitlesArea(tableTitlesArea);
        
        formatTitlesTableOnArea(titleToken);
        formatTitlesTableOnArea(titleError);
        
        formatTables(tokenTableScroll, tokensTable);
        formatTables(errorTableScroll, errorsTable);
        formatButtons(header);
        
        header.add(run);
        header.add(add);
        header.add(export);
        header.add(settings);
        header.add(monitor);
        
        tableTitlesArea.add(titleToken);
        tableTitlesArea.add(titleError);
        
        main.add(header);
        main.add(codeAreaScroll);
        main.add(tableTitlesArea);
        main.add(tokenTableScroll);
        main.add(errorTableScroll);
        
        this.getContentPane().add(main);
    }
    
    protected void resizeComponent() {
        int halfScreenSize = this.getWidth()/2;
        
        header.setSize(this.getWidth(), 40);
        
        codeAreaScroll.setSize(this.getWidth(), (int)(this.getHeight()*.645));
        run.setLocation(halfScreenSize-16, 4);
        add.setLocation(30, 4);
        export.setLocation(72, 4);
        settings.setLocation(this.getWidth()-62, 5);
        monitor.setLocation(112, 5);
        
        tableTitlesArea.setLocation(0, (int)(this.getHeight()*.645)+40);
        tableTitlesArea.setSize(this.getWidth(), 30);
        
        titleToken.setLocation(halfScreenSize/2-40, 5);
        titleError.setLocation(halfScreenSize+(halfScreenSize/2), 5);
        
        int heighTables = this.getHeight()-tableTitlesArea.getY() - 70;
        tokenTableScroll.setLocation(0, tableTitlesArea.getY()+30);
        tokenTableScroll.setSize(halfScreenSize, heighTables);
        errorTableScroll.setLocation(halfScreenSize, tableTitlesArea.getY()+30);
        errorTableScroll.setSize(halfScreenSize, heighTables);
    }
    
    protected void formatCodeComponents(Object ... components) {
        formatMainPanel((JPanel)components[0]);
        formatHeader((JPanel)components[1]);
        formatCodeLines();
        formatCodeArea();
        formatCodeAreaScroll(codeAreaScroll);
    }
    
    protected void formatMainPanel(JPanel main) {
        main.setLocation(0, 0);
        main.setSize(2080, 1080);
    }
    
    protected void formatHeader(JPanel header) {
        header.setLocation(0,0); // 33
        header.setBackground(new Color(44,44,44));
    }
    
    protected void formatCodeLines() {
        codeLines.setText("1");
        codeLines.setFont(actualFont);
        codeLines.setEditable(false);
        codeLines.setForeground(new Color(5,255,113));
        codeLines.setBackground(new Color(12,12,12));
        codeLines.setBorder(BorderFactory.createMatteBorder(4,8,4,8, 
                new Color(12,12,12)));
    }
    
    protected void formatCodeArea() {
        codeArea.setCaretColor(Color.WHITE);
        codeArea.setFont(actualFont);
        codeArea.setForeground(Color.WHITE);
        codeArea.setBackground(new Color(22,22,22));
        codeArea.setBorder(BorderFactory.createMatteBorder(4,8,4,8, 
                new Color(22,22,22)));
        
    	FontMetrics fm = codeArea.getFontMetrics(codeArea.getFont());
        int charWidth = fm.charWidth('w');
        int tabWidth = charWidth * 4;

        TabStop[] tabs = new TabStop[10];

        for (int j = 0; j < tabs.length; j++) {
             int tab = j + 1;
             tabs[j] = new TabStop( tab * tabWidth );
        }

        TabSet tabSet = new TabSet(tabs);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        
        StyleConstants.setTabSet(attributes, tabSet);
        StyleConstants.setFontFamily(attributes, actualFont.getFamily());
        StyleConstants.setFontSize(attributes, actualFont.getSize());
        StyleConstants.setBold(attributes, true);
        StyleConstants.setForeground(attributes, Color.WHITE);
        
        int length = codeArea.getDocument().getLength();
        codeArea.getStyledDocument().setParagraphAttributes(0, length, attributes, true);
    }
    
    protected void formatCodeAreaScroll(JScrollPane codeAreaScroll) {
        codeAreaScroll.setLocation(0, 40);
        codeAreaScroll.setBorder(null);
        codeAreaScroll.getViewport().add(codeArea);
        codeAreaScroll.setRowHeaderView(codeLines);
        codeAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    
    protected void formatTableTitlesArea(JPanel tableTitlesArea) {
        tableTitlesArea.setBackground(new Color(44,44,44));
    }
    
    protected void formatTitlesTableOnArea(JLabel title) {
        title.setSize(100,30);
        title.setForeground(Color.WHITE);
    }
    
    protected void formatTables(JScrollPane tableScroll, JTable table) {
        DefaultTableModel modelTable = getModel(table);
        table.setModel(modelTable);
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
        
        setCleanButton(add);
        add.setSize(32, 32);
        
        setCleanButton(export);
        export.setSize(32,32);
        
        setCleanButton(settings);
        settings.setSize(32, 32);
        
        setCleanButton(monitor);
        monitor.setSize(32, 32);
    }
    
    protected void setCleanButton(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
    }
    
    protected void setIcons() {
    	String prefix = "/home/rennyjr/NetBeansProjects/Compiler/Compiler/";
        Icon addIcon = new ImageIcon(prefix + "src/res/img/file.png");
        Icon runIcon = new ImageIcon(prefix + "src/res/img/play-button.png");
        Icon exportIcon = new ImageIcon(prefix + "src/res/img/download.png");
        Icon settingsIcon = new ImageIcon(prefix + "src/res/img/settings.png");
        Icon monitorIcon = new ImageIcon(prefix + "src/res/img/console.png");
        
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
        
        switch(lastChar) {
            case ' ':
                return " ";
            case '\n':
                return "\n";
            default:
                return "";
        }
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
}
