package GUI;

import clases.Civilization;
import clases.Main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainScreen extends JFrame {

    private MainPanel mainPanel;
    private ImageIcon icon;

    public MainScreen(Civilization civilization) {
        super();
        setTitle("Civilizations");
        setResizable(false);

        icon = new ImageIcon("./programacio/src/GUI/images/icon.png");
        setIconImage(icon.getImage());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1480, 920);

        mainPanel = new MainPanel(civilization, this);
        add(mainPanel);

        // Save & exit cleanly
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing Civilizations...");
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }
}
