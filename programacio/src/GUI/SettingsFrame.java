package GUI;

import clases.*;

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
import javax.swing.JTextArea;

public class SettingsFrame extends JFrame implements ActionListener {

    // Panels
    private JPanel mainPanel;
    private JPanel difficultyPanel, difficultyButtonsPanel;
    private JPanel changeNamePanel;
    private JPanel cheatsLabelPanel, cheatsRowA, cheatsRowB;
    private JPanel muteBGMPanel;

    // Controls
    private JLabel    difficultyLabel;
    private JButton   easyButton, mediumButton, hardButton;
    private JButton   changeNameButton;
    private JTextArea changeNameInput;
    private JTextArea foodInput, woodInput, ironInput, manaInput;
    private JButton   addFoodButton, addWoodButton, addIronButton, addManaButton;
    private JButton   muteBGMButton;

    // References
    private Civilization civilization;
    private MiddlePanel  mp;
    private RightPanel   rp;

    // Fonts
    private Font customFont, customFontSmall;

    // -----------------------------------------------------------------------
    // Constructor  —  Planet → Civilization; Metal/Deuterium → Food/Wood/Iron/Mana
    // -----------------------------------------------------------------------
    public SettingsFrame(Civilization civilization, MiddlePanel mp, RightPanel rp) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(new Dimension(560, 580));
        setTitle("Settings");
        setLayout(new BorderLayout());
        add(new PaddingPanel(), BorderLayout.WEST);
        add(new PaddingPanel(), BorderLayout.NORTH);
        add(new PaddingPanel(), BorderLayout.EAST);
        add(new PaddingPanel(), BorderLayout.SOUTH);

        this.civilization = civilization;
        this.mp           = mp;
        this.rp           = rp;

        try {
            customFont      = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(22f);
            customFontSmall = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // 7-row grid
        mainPanel = new JPanel(new GridLayout(7, 1));
        mainPanel.setBackground(Color.BLACK);

        // ── Row 1: Difficulty label ────────────────────────────────────────
        difficultyPanel = new JPanel(new BorderLayout());
        difficultyPanel.setBackground(Globals.settingsPanelColor);
        difficultyLabel = new JLabel("DIFFICULTY: " + civilization.getDifficultyStr());
        difficultyLabel.setFont(customFontSmall);
        difficultyLabel.setForeground(Color.WHITE);
        difficultyPanel.add(new PaddingPanel(Globals.settingsPanelColor,
                new Dimension(100, 20)), BorderLayout.WEST);
        difficultyPanel.add(difficultyLabel, BorderLayout.CENTER);
        mainPanel.add(difficultyPanel);

        // ── Row 2: Difficulty buttons ──────────────────────────────────────
        difficultyButtonsPanel = new JPanel(new GridLayout(1, 3));
        easyButton   = sBtn("Easy");
        mediumButton = sBtn("Medium");
        hardButton   = sBtn("Hard");
        easyButton.addActionListener(this);
        mediumButton.addActionListener(this);
        hardButton.addActionListener(this);
        difficultyButtonsPanel.add(easyButton);
        difficultyButtonsPanel.add(mediumButton);
        difficultyButtonsPanel.add(hardButton);
        mainPanel.add(difficultyButtonsPanel);

        // ── Row 3: Change name ────────────────────────────────────────────
        changeNamePanel = new JPanel();
        changeNamePanel.setBackground(Globals.settingsPanelColor);
        changeNameButton = sBtn("Change Name");
        changeNameButton.setPreferredSize(new Dimension(180, 40));
        changeNameButton.addActionListener(this);
        changeNameInput = textArea(200, 40);
        changeNamePanel.add(changeNameButton);
        changeNamePanel.add(changeNameInput);
        mainPanel.add(changeNamePanel);

        // ── Row 4: Cheats label ───────────────────────────────────────────
        cheatsLabelPanel = new JPanel(new BorderLayout());
        cheatsLabelPanel.setBackground(Globals.settingsPanelColor);
        JLabel cheatsLbl = new JLabel("Cheats");
        cheatsLbl.setFont(customFontSmall);
        cheatsLbl.setForeground(Globals.settingsFontColor);
        cheatsLabelPanel.add(new PaddingPanel(Globals.settingsPanelColor,
                new Dimension(210, 10)), BorderLayout.WEST);
        cheatsLabelPanel.add(cheatsLbl);
        mainPanel.add(cheatsLabelPanel);

        // ── Row 5: Food + Wood cheats ─────────────────────────────────────
        cheatsRowA = new JPanel(new GridLayout(1, 2));

        JPanel foodPanel = resourceCheatRow("Food");
        foodInput     = extractInput(foodPanel);
        addFoodButton = extractButton(foodPanel);
        addFoodButton.setActionCommand("addFood");
        addFoodButton.addActionListener(this);
        cheatsRowA.add(foodPanel);

        JPanel woodPanel = resourceCheatRow("Wood");
        woodInput     = extractInput(woodPanel);
        addWoodButton = extractButton(woodPanel);
        addWoodButton.setActionCommand("addWood");
        addWoodButton.addActionListener(this);
        cheatsRowA.add(woodPanel);

        mainPanel.add(cheatsRowA);

        // ── Row 6: Iron + Mana cheats ─────────────────────────────────────
        cheatsRowB = new JPanel(new GridLayout(1, 2));

        JPanel ironPanel = resourceCheatRow("Iron");
        ironInput     = extractInput(ironPanel);
        addIronButton = extractButton(ironPanel);
        addIronButton.setActionCommand("addIron");
        addIronButton.addActionListener(this);
        cheatsRowB.add(ironPanel);

        JPanel manaPanel = resourceCheatRow("Mana");
        manaInput     = extractInput(manaPanel);
        addManaButton = extractButton(manaPanel);
        addManaButton.setActionCommand("addMana");
        addManaButton.addActionListener(this);
        cheatsRowB.add(manaPanel);

        mainPanel.add(cheatsRowB);

        // ── Row 7: BGM toggle ─────────────────────────────────────────────
        muteBGMPanel = new JPanel();
        muteBGMPanel.setBackground(Globals.settingsPanelColor);
        muteBGMButton = sBtn("Music ON/OFF");
        muteBGMButton.addActionListener(this);
        muteBGMPanel.add(muteBGMButton);
        mainPanel.add(muteBGMPanel);

        add(mainPanel);
        setVisible(true);
    }

    // -----------------------------------------------------------------------
    // ActionListener
    // -----------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        AudioPlayer.buttonSound();
        switch (e.getActionCommand()) {
            case "Easy"   -> { civilization.setDifficulty(1); refreshDifficulty(); }
            case "Medium" -> { civilization.setDifficulty(2); refreshDifficulty(); }
            case "Hard"   -> { civilization.setDifficulty(3); refreshDifficulty(); }

            case "addFood" -> {
                if (isNum(foodInput.getText())) {
                    civilization.setFood(civilization.getFood()
                            + Integer.parseInt(foodInput.getText()));
                    foodInput.setText("");
                }
            }
            case "addWood" -> {
                if (isNum(woodInput.getText())) {
                    civilization.setWood(civilization.getWood()
                            + Integer.parseInt(woodInput.getText()));
                    woodInput.setText("");
                }
            }
            case "addIron" -> {
                if (isNum(ironInput.getText())) {
                    civilization.setIron(civilization.getIron()
                            + Integer.parseInt(ironInput.getText()));
                    ironInput.setText("");
                }
            }
            case "addMana" -> {
                if (isNum(manaInput.getText())) {
                    civilization.setMana(civilization.getMana()
                            + Integer.parseInt(manaInput.getText()));
                    manaInput.setText("");
                }
            }
            case "Change Name" -> {
                if (isText(changeNameInput.getText())) {
                    civilization.setName(changeNameInput.getText());
                    changeNameInput.setText("");
                    mp.repaint();
                }
            }
            case "Music ON/OFF" -> {
                AudioPlayer bgm = rp.getBgmPlayer();
                if (!bgm.isMuted()) { bgm.changeVolume(-50); bgm.setMuted(true); }
                else                { bgm.changeVolume(0);   bgm.setMuted(false); }
            }
        }
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /** Builds a [Label | TextArea | + button] row and returns the panel. */
    private JPanel resourceCheatRow(String label) {
        JPanel row = new JPanel();
        row.setBackground(Globals.settingsSecPanelColor);
        row.add(new JLabel(label));
        JTextArea ta = textArea(80, 40);
        row.add(ta);
        JButton btn = new JButton("+");
        btn.setFont(customFont);
        btn.setPreferredSize(new Dimension(40, 40));
        row.add(btn);
        return row;
    }

    /** Extracts the JTextArea child from a resource cheat row panel. */
    private JTextArea extractInput(JPanel row) {
        for (int i = 0; i < row.getComponentCount(); i++) {
            if (row.getComponent(i) instanceof JTextArea ta) return ta;
        }
        return new JTextArea();
    }

    /** Extracts the JButton child from a resource cheat row panel. */
    private JButton extractButton(JPanel row) {
        for (int i = 0; i < row.getComponentCount(); i++) {
            if (row.getComponent(i) instanceof JButton btn) return btn;
        }
        return new JButton();
    }

    private void refreshDifficulty() {
        difficultyLabel.setText("DIFFICULTY: " + civilization.getDifficultyStr());
    }

    private boolean isNum(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) { if (!Character.isDigit(c)) return false; }
        return true;
    }

    private boolean isText(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) { if (Character.isDigit(c)) return false; }
        return true;
    }

    private JButton sBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(customFontSmall);
        b.setBackground(Globals.settingsButtonColor);
        b.setForeground(Globals.settingsButtonFontColor);
        return b;
    }

    private JTextArea textArea(int w, int h) {
        JTextArea ta = new JTextArea();
        ta.setPreferredSize(new Dimension(w, h));
        ta.setFont(customFont);
        ta.setLineWrap(true);
        return ta;
    }
}