package GUI;

import clases.Battle;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ResultFrame extends JFrame {

    private JTextArea reportText;
    private Battle battle;
    private Font customFontBiggest, customFont, customFontSmall;

    /** Called immediately after a battle finishes. */
    public ResultFrame(Battle battle) {
        initFonts();
        this.battle = battle;
        buildUI(battle.isCivilizationWon() ? "You won!" : "You lost...", battle.getBattleDevelopment());
    }

    /** Called from ReportsFrame (viewing an old report). */
    public ResultFrame(Battle battle, int battleN) {
        initFonts();
        this.battle = battle;
        buildUI("Report #" + battle.getBattleNumber(), battle.getBattleDevelopment());
    }

    private void initFonts() {
        try {
            customFontBiggest = Font.createFont(Font.TRUETYPE_FONT, new File(Globals.customFont)).deriveFont(68f);
            customFont        = Font.createFont(Font.TRUETYPE_FONT, new File(Globals.customFont)).deriveFont(22f);
            customFontSmall   = Font.createFont(Font.TRUETYPE_FONT, new File(Globals.customFont)).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            customFontBiggest = new Font("Arial", Font.BOLD, 68);
            customFont        = new Font("Arial", Font.BOLD, 22);
            customFontSmall   = new Font("Arial", Font.PLAIN, 16);
        }
    }

    private void buildUI(String title, String initialText) {
        setSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new PaddingPanel(), BorderLayout.WEST);
        add(new PaddingPanel(), BorderLayout.NORTH);
        add(new PaddingPanel(), BorderLayout.SOUTH);
        add(new PaddingPanel(), BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(customFontBiggest);
        titleLbl.setForeground(Color.WHITE);
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.add(titleLbl);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Report text
        reportText = new JTextArea(initialText);
        reportText.setEditable(false);
        reportText.setFont(customFontSmall);
        mainPanel.add(new JScrollPane(reportText), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.BLACK);

        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setFont(customFont);
        acceptBtn.setPreferredSize(new Dimension(200, 50));
        acceptBtn.setBackground(Color.WHITE);
        acceptBtn.addActionListener(e -> { AudioPlayer.buttonSound(); dispose(); });

        JButton switchBtn = new JButton("Switch View");
        switchBtn.setFont(customFont);
        switchBtn.setPreferredSize(new Dimension(200, 50));
        switchBtn.setBackground(Color.WHITE);
        switchBtn.addActionListener(e -> {
            AudioPlayer.buttonSound();
            if (reportText.getText().equals(battle.getBattleDevelopment())) {
                reportText.setText(battle.getBattleReport());
            } else {
                reportText.setText(battle.getBattleDevelopment());
            }
        });

        btnPanel.add(acceptBtn);
        btnPanel.add(switchBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}