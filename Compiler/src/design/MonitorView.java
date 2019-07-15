package design;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *    @author rennyjr
**/

public class MonitorView extends JFrame {
    private JScrollPane scroll_vitals;
    private JTextPane pane_vitals;
    public static String vitals = "[*] Sistema Inicializado.\n"
            + "-----------------------------------------------------------\n\n";
    
    public MonitorView() {
        this.setSize(800, 400);
        this.setTitle("Monitor");
        this.setLocationRelativeTo(null);
        
        init();
        
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void init() {
        JPanel main = new JPanel(null);
        main.setSize(800, 400);
        main.setLocation(0, 0);
        main.setBackground(new Color(22,22,22));
        
        formatPaneVitals();
        formatScrollVitals();
        
        main.add(scroll_vitals);
        this.getContentPane().add(main);
    }
    
    private void formatPaneVitals() {
        pane_vitals = new JTextPane();
        pane_vitals.setFont(AppStyle.actualFont);
        pane_vitals.setCaretColor(Color.WHITE);
        pane_vitals.setForeground(Color.WHITE);
        pane_vitals.setBackground(new Color(22,22,22));
        pane_vitals.setBorder(BorderFactory.createMatteBorder(4,4,4,4, 
                new Color(22,22,22)));
        pane_vitals.setEditable(false);
        pane_vitals.setText(vitals + "[*] Compilacion completada!\n\n\n\n");
    }
    
    private void formatScrollVitals() {
        scroll_vitals = new JScrollPane(pane_vitals);
        scroll_vitals.setLocation(0, 0);
        scroll_vitals.setSize(800, 400);
        scroll_vitals.setBorder(null);
        scroll_vitals.setEnabled(false);
    }
}
