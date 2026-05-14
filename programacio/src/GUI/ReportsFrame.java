package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import clases.Battle;
import clases.Civilization;

public class ReportsFrame extends JFrame {

    private JPanel mainPanel;
    private Font   customFontSmall;

    // Civilization instead of Planet
    public ReportsFrame(Civilization civilization) {
        setTitle("Battle Reports");
        setSize(new Dimension(300, 200));
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.BLACK);

        try {
            customFontSmall = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        mainPanel = new JPanel();
        Battle[] reports = civilization.getBattleReports();

        // Count non-null reports
        int nBattles = 0;
        for (Battle r : reports) { if (r != null) nBattles++; }

        if (nBattles == 0) {
            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.setBackground(Color.BLACK);
            JLabel label = new JLabel("There are no reports to show");
            label.setFont(customFontSmall);
            label.setForeground(new Color(255, 255, 255, 150));
            textPanel.add(label, BorderLayout.CENTER);
            textPanel.add(new PaddingPanel(Color.BLACK, new Dimension(15, 20)),
                    BorderLayout.WEST);
            add(textPanel);
        } else {
            mainPanel.setLayout(new GridLayout(nBattles, 1));
            for (int i = 0; i < nBattles; i++) {
                final int idx = i;
                int battleNum = civilization.getNBattles() - i;
                JButton btn = new JButton("Battle N-" + battleNum);
                btn.setBackground(Color.WHITE);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AudioPlayer.buttonSound();
                        new ResultFrame(civilization.getBattleReport(idx), idx);
                    }
                });
                mainPanel.add(btn);
            }
            add(mainPanel);
        }
        setVisible(true);
    }
}