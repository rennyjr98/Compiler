package design;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *    @author rennyjr
**/

public class FontView extends JFrame implements ActionListener {
    private JComboBox fonts;
    private JComboBox fontWeight;
    private JComboBox fontSize;
    private JButton accept;
    
    public FontView() {
        this.setSize(390, 125);
        
        init();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void init() {
        JPanel main = new JPanel(null);
        main.setSize(390, 125);
        main.setLocation(0, 0);
        
        fontWeight = new JComboBox();
        fontSize = new JComboBox();
        accept = new JButton("Aceptar");
        
        GraphicsEnvironment environment = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        String [] availableFonts = environment.getAvailableFontFamilyNames();
        fonts = new JComboBox(availableFonts);
        
        fontWeight.addItem("BOLD");
        fontWeight.addItem("PLAIN");
        fontWeight.addItem("ITALIC");
        
        for(int i = 0; i < 10; i++) {
            int size = i*1 + 10;
            fontSize.addItem(size+"");
        }
        
        fonts.setLocation(10, 10);
        fonts.setSize(150, 30);
        fontWeight.setLocation(170, 10);
        fontWeight.setSize(100, 30);
        fontSize.setLocation(280, 10);
        fontSize.setSize(100, 30);
        accept.setLocation(180, 50);
        accept.setSize(200, 30);
        accept.addActionListener(this);
        
        main.add(fonts);
        main.add(fontWeight);
        main.add(fontSize);
        main.add(accept);
        this.getContentPane().add(main);
    }
    
    private Font getNewFont() {
        String codeFont = fonts.getSelectedItem().toString();
        int weightFont = getWeight(fontWeight.getSelectedIndex());
        int sizeFont = new Integer(fontSize.getSelectedItem().toString());
        Font font = new Font(codeFont, weightFont, sizeFont);
        return font;
    }
    
    private int getWeight(int weightSelection) {
        switch(weightSelection) {
            case 0: return Font.BOLD;
            case 1: return Font.PLAIN;
            case 2: return Font.ITALIC;
            default: return Font.PLAIN;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppStyle.getFontToCode(getNewFont());
        this.dispose();
    }
}
