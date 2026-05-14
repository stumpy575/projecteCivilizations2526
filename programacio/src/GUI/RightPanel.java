package GUI;

import clases.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class RightPanel extends JPanel {

    // -----------------------------------------------------------------------
    // Sub-panels
    // -----------------------------------------------------------------------
    private JPanel mainPanel, upperPanel, fixArmyPanel,
            costFixArmyPanel, costFixArmyAmountPanel, buttonsPanel;

    // -----------------------------------------------------------------------
    // Buttons
    // -----------------------------------------------------------------------
    private JButton fixArmyButton;
    private JButton startABattleButton;
    private JButton viewCurrentThreatButton;
    private JButton newGameButton;
    private JButton battleReportButton;
    private JButton settingsButton;
    private JButton exitButton;

    // -----------------------------------------------------------------------
    // Fix-Army cost labels  (updated every tick by MainPanel.updateAll)
    // -- Civilizations uses food / wood / iron instead of metal / deuterium --
    // -----------------------------------------------------------------------
    private JLabel foodCostFixLabel;
    private JLabel woodCostFixLabel;
    private JLabel ironCostFixLabel;

    // -----------------------------------------------------------------------
    // References
    // -----------------------------------------------------------------------
    private Civilization civilization;
    private MainScreen    ms;
    private AudioPlayer   bgmPlayer;

    // -----------------------------------------------------------------------
    // Fonts
    // -----------------------------------------------------------------------
    private Font customFont, customFontSmaller, customFontSmallest;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    RightPanel(Civilization civilization, MainScreen ms) {
        this.civilization = civilization;
        this.ms           = ms;

        bgmPlayer = AudioPlayer.doBGM();

        // Fonts
        try {
            customFont         = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(24f);
            customFontSmaller  = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(18f);
            customFontSmallest = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(14f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Panel sizing / padding (mirrors SpaceWars)
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BorderLayout());
        add(new PaddingPanel(), BorderLayout.NORTH);
        add(new PaddingPanel(), BorderLayout.WEST);
        add(new PaddingPanel(), BorderLayout.EAST);

        // Tiny resource icons for the Fix-Army cost row
        Image foodImg = loadScaled("./M3-Programacion/GUI/images/food_icon.png", 20, 20);
        Image woodImg = loadScaled("./M3-Programacion/GUI/images/wood_icon.png", 20, 20);
        Image ironImg = loadScaled("./M3-Programacion/GUI/images/iron_icon.png", 20, 20);

        // ===================================================================
        // ROOT — vertical BoxLayout
        // ===================================================================
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // ===================================================================
        // UPPER PANEL:  Fix Army  |  Start a fight  |  View Threat
        // ===================================================================
        upperPanel = new JPanel(new GridLayout(3, 1));

        // ── Fix Army ──────────────────────────────────────────────────────
        fixArmyPanel = new JPanel(new GridLayout(2, 1));

        fixArmyButton = styledButton("Fix Army", customFont);
        fixArmyButton.addActionListener(e -> fixArmyEvent());
        fixArmyPanel.add(fixArmyButton);

        // Cost display row  (food + wood + iron)
        costFixArmyPanel = new JPanel(new BorderLayout());
        costFixArmyAmountPanel = new JPanel();
        costFixArmyAmountPanel.setBackground(Globals.rightSecPanelColor);

        costFixArmyAmountPanel.add(new JLabel(new ImageIcon(foodImg)));
        foodCostFixLabel = costLabel("" + civilization.getFixArmyCost()[0]);
        costFixArmyAmountPanel.add(foodCostFixLabel);

        costFixArmyAmountPanel.add(new JLabel(new ImageIcon(woodImg)));
        woodCostFixLabel = costLabel("" + civilization.getFixArmyCost()[1]);
        costFixArmyAmountPanel.add(woodCostFixLabel);

        costFixArmyAmountPanel.add(new JLabel(new ImageIcon(ironImg)));
        ironCostFixLabel = costLabel("" + civilization.getFixArmyCost()[2]);
        costFixArmyAmountPanel.add(ironCostFixLabel);

        costFixArmyPanel.add(costFixArmyAmountPanel, BorderLayout.CENTER);
        fixArmyPanel.add(costFixArmyPanel);
        upperPanel.add(fixArmyPanel);

        // ── Start a fight ─────────────────────────────────────────────────
        startABattleButton = styledButton("Start a fight", customFontSmaller);
        startABattleButton.addActionListener(e -> {
            if (civilization.getTotalTroops() > 0 && !civilization.isActiveThreat()) {
                AudioPlayer.buttonSound();
                new ThreatTimer(civilization, ms, 1);   // immediate trigger
            } else {
                ms.getMainPanel().getMiddlePanel().doShowMessage("Can't do that", 2);
            }
        });
        upperPanel.add(startABattleButton);

        // ── View Threat ───────────────────────────────────────────────────
        viewCurrentThreatButton = styledButton("View Threat", customFontSmaller);
        viewCurrentThreatButton.addActionListener(e -> {
            if (civilization.isActiveThreat()) {
                AudioPlayer.buttonSound();
                new ThreatFrame(civilization);
            } else {
                ms.getMainPanel().getMiddlePanel().doShowMessage("No threat incoming", 2);
            }
        });
        upperPanel.add(viewCurrentThreatButton);

        mainPanel.add(upperPanel);

        // ===================================================================
        // BUTTONS PANEL:  New Game | Battle Report | Settings | Exit
        // ===================================================================
        buttonsPanel = new JPanel(new GridLayout(4, 1));
        buttonsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        buttonsPanel.setBackground(Color.BLACK);

        newGameButton = styledButton("New Game", customFontSmaller);
        newGameButton.addActionListener(e -> {
            AudioPlayer.buttonSound();
            if (!civilization.isActiveThreat()) {
                newGameEvent();
            } else {
                ms.getMainPanel().getMiddlePanel().doShowMessage("Can't do that", 3);
            }
        });
        buttonsPanel.add(newGameButton);

        battleReportButton = styledButton("Battle Report", customFontSmaller);
        battleReportButton.addActionListener(e -> {
            AudioPlayer.buttonSound();
            new ReportsFrame(civilization);
        });
        buttonsPanel.add(battleReportButton);

        settingsButton = styledButton("Settings", customFontSmaller);
        settingsButton.addActionListener(e -> {
            AudioPlayer.buttonSound();
            new SettingsFrame(civilization, ms.getMainPanel().getMiddlePanel(), this);
        });
        buttonsPanel.add(settingsButton);

        exitButton = styledButton("Exit", customFontSmaller);
        exitButton.addActionListener(e -> {
            AudioPlayer.buttonSound();
            exitEvent();
        });
        buttonsPanel.add(exitButton);

        mainPanel.add(buttonsPanel);
    }

    // =======================================================================
    // Event methods
    // =======================================================================

    /**
     * Fix Army — deducts food / wood / iron and resets all unit armor.
     * Blocked while a battle is in progress.
     */
    private void fixArmyEvent() {
        if (civilization.isActiveThreat()) {
            ms.getMainPanel().getMiddlePanel().doShowMessage("Can't do that", 3);
            return;
        }
        int[] cost = civilization.getFixArmyCost();   // [food, wood, iron]
        if (civilization.getFood() >= cost[0]
                && civilization.getWood() >= cost[1]
                && civilization.getIron() >= cost[2]) {
            civilization.setFood(civilization.getFood() - cost[0]);
            civilization.setWood(civilization.getWood() - cost[1]);
            civilization.setIron(civilization.getIron() - cost[2]);
            civilization.resetArmyArmor();
        } else {
            ms.getMainPanel().getMiddlePanel().doShowMessage("Not enough resources", 3);
        }
    }

    /** Resets the civilization to its initial state. */
    private void newGameEvent() {
        try {
            civilization.newGame();
            ms.repaint();
        } catch (ResourceException e) {
            e.printStackTrace();
        }
    }

    /** Confirmation dialog before closing. */
    private void exitEvent() {
        JDialog exitWindow = new JDialog(
                SwingUtilities.getWindowAncestor(this), "Exit");
        exitWindow.setResizable(false);
        exitWindow.setLocationRelativeTo(null);
        exitWindow.setSize(300, 100);
        exitWindow.getContentPane().setLayout(
                new BoxLayout(exitWindow.getContentPane(), BoxLayout.Y_AXIS));

        exitWindow.add(new JLabel("Are you sure you want to exit?"));

        JPanel exitBtns = new JPanel();

        JButton yes = new JButton("Yes");
        yes.addActionListener(e -> { ms.dispose(); Main.closeGame(); });
        exitBtns.add(yes);

        JButton no = new JButton("No");
        no.addActionListener(e -> exitWindow.dispose());
        exitBtns.add(no);

        exitWindow.add(exitBtns);
        exitWindow.setVisible(true);
    }

    // =======================================================================
    // Private helpers
    // =======================================================================

    private JButton styledButton(String text, Font font) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setBackground(Globals.rightButtonsColor);
        btn.setForeground(Globals.rightButtonsFontColor);
        return btn;
    }

    private JLabel costLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Globals.rightSecFontColor);
        lbl.setFont(customFontSmallest);
        return lbl;
    }

    private Image loadScaled(String path, int w, int h) {
        try {
            return new ImageIcon(path).getImage()
                    .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            return new ImageIcon().getImage();
        }
    }

    // =======================================================================
    // Public getters — called by MainPanel.updateAll() every 100 ms
    // =======================================================================

    /** Food portion of the Fix-Army cost display. */
    public JLabel getFoodCostFixLabel() { return foodCostFixLabel; }

    /** Wood portion of the Fix-Army cost display. */
    public JLabel getWoodCostFixLabel() { return woodCostFixLabel; }

    /** Iron portion of the Fix-Army cost display. */
    public JLabel getIronCostFixLabel() { return ironCostFixLabel; }

    /** Exposed so SettingsFrame can mute/unmute background music. */
    public AudioPlayer getBgmPlayer()   { return bgmPlayer; }
}