package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import clases.Battle;
import clases.Civilization;
import clases.MilitaryUnit;

public class ThreatFrame extends JFrame {

    private ThreatPanel threatPanel;
    private ArrayList<MilitaryUnit>[] enemyArmy;

    // -----------------------------------------------------------------------
    // Constructor — Civilization instead of Planet
    // -----------------------------------------------------------------------
    public ThreatFrame(Civilization civilization) {
        setSize(new Dimension(470, 230));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Current Threat");
        setResizable(false);

        Battle currentThreat = civilization.getCurrentThreat();
        if (currentThreat == null) { dispose(); return; }

        enemyArmy = currentThreat.getEnemyArmy();

        threatPanel = new ThreatPanel();
        threatPanel.setBackground(Color.BLACK);
        add(threatPanel);
        setVisible(true);
    }

    // -----------------------------------------------------------------------
    // Inner panel
    // -----------------------------------------------------------------------
    class ThreatPanel extends JPanel {
        private Timer timer = new Timer();

        ThreatPanel() {
            timer.schedule(new TimerTask() {
                public void run() { repaint(); }
            }, 0, 500);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);

            int offset = 0;
            for (int i = 0; i < enemyArmy.length; i++) {
                if (enemyArmy[i] == null || enemyArmy[i].size() == 0) continue;

                MilitaryUnit sample = enemyArmy[i].get(0);

                // Unit image
                if (sample.getBufferedImage() != null) {
                    g2d.drawImage(
                            sample.getBufferedImage()
                                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH),
                            30 + 110 * offset, 20, this);
                }
                // Name
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString(sample.getName(), 30 + 110 * offset, 140);
                // Count
                g2d.setFont(new Font("Arial", Font.BOLD, 28));
                g2d.drawString("x" + enemyArmy[i].size(), 60 + 110 * offset, 170);
                offset++;
            }

            if (offset == 0) {
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("Calculating enemy force...", 60, 120);
            }
        }
    }
}