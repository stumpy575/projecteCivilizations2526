package GUI;

import clases.Civilization;
import clases.ResourceException;
import clases.BuildingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LeftPanel extends JPanel implements ActionListener {

    // ── Resources labels (updated every 100 ms by MainPanel) ──────────────
    private JLabel foodAmountLabel, woodAmountLabel, ironAmountLabel, manaAmountLabel;

    // ── Technology ────────────────────────────────────────────────────────
    private JTextArea techAttackLvlAmountTextArea, techDefenseLvlAmountTextArea;
    private JLabel    techAttackLvlCost, techDefenseLvlCost;

    // ── Buildings labels ──────────────────────────────────────────────────
    private JLabel farmLabel, carpentryLabel, smithyLabel, magicTowerLabel, churchLabel;

    private Font customFont, customFontSmaller;

    LeftPanel(Civilization civ) {
        setPreferredSize(new Dimension(230, 230));
        setLayout(new BorderLayout());
        setBackground(Globals.leftPanelColor);

        add(new PaddingPanel(Globals.leftPanelColor), BorderLayout.NORTH);
        add(new PaddingPanel(Globals.leftPanelColor), BorderLayout.WEST);
        add(new PaddingPanel(Globals.leftPanelColor), BorderLayout.EAST);

        // ── Fonts ──────────────────────────────────────────────────────────
        try {
            customFont        = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(24f);
            customFontSmaller = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            customFont        = new Font("Arial", Font.BOLD,  24);
            customFontSmaller = new Font("Arial", Font.PLAIN, 20);
        }

        // ── Load shared icons ──────────────────────────────────────────────
        ImageIcon ironIconSmall = scaledIcon("./programacio/src/GUI/images/iron_icon.png",   20, 20);
        ImageIcon plusIcon      = scaledIcon("./programacio/src/GUI/images/plus_icon.png",   30, 30);
        ImageIcon swordIcon     = scaledIcon("./programacio/src/GUI/images/sword_icon.png",  30, 30);
        ImageIcon shieldIcon    = scaledIcon("./programacio/src/GUI/images/shield_icon.png", 30, 30);

        // ── Main panel: 4 rows ─────────────────────────────────────────────
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        mainPanel.setBackground(Globals.leftPanelColor);

        mainPanel.add(buildCivImagePanel(civ));
        mainPanel.add(buildResourcesPanel(civ));
        mainPanel.add(buildTechnologyPanel(civ, plusIcon, swordIcon, shieldIcon, ironIconSmall));
        mainPanel.add(buildBuildingsPanel(civ, plusIcon));

        add(mainPanel, BorderLayout.CENTER);
    }

    // ══════════════════════════════════════════════════════════════════════
    // Row 0 – Civilization image (like rotating earth in SpaceWars)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildCivImagePanel(Civilization civ) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Globals.leftPanelColor);

        ImageIcon civIcon = scaledIcon("./programacio/src/GUI/images/civilization.gif", 150, 150);
        JLabel label;
        if (civIcon != null) {
            label = new JLabel(civIcon);
        } else {
            label = new JLabel(civ.getName(), SwingConstants.CENTER);
            label.setFont(customFont);
            label.setForeground(Globals.leftFontColor);
        }

        panel.add(new PaddingPanel(Globals.leftPanelColor, new Dimension(20, 0)),  BorderLayout.WEST);
        panel.add(new PaddingPanel(Globals.leftPanelColor, new Dimension(0,  10)), BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════
    // Row 1 – Resources: Food / Wood / Iron / Mana
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildResourcesPanel(Civilization civ) {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.setBackground(Globals.leftPanelColor);

        addSectionTitle(panel, "Resources");

        // Food
        JPanel foodRow = new JPanel();
        foodRow.setBackground(Globals.leftPanelSecColor);
        addIconIfPresent(foodRow, "./programacio/src/GUI/images/food_icon.png", 40, 40);
        foodAmountLabel = styledLabel(civ.getFood() + " +" + civ.getFoodGenerated());
        foodRow.add(foodAmountLabel);
        panel.add(foodRow);

        // Wood
        JPanel woodRow = new JPanel();
        woodRow.setBackground(Globals.leftPanelSecColor);
        addIconIfPresent(woodRow, "./programacio/src/GUI/images/wood_icon.png", 40, 40);
        woodAmountLabel = styledLabel(civ.getWood() + " +" + civ.getWoodGenerated());
        woodRow.add(woodAmountLabel);
        panel.add(woodRow);

        // Iron
        JPanel ironRow = new JPanel();
        ironRow.setBackground(Globals.leftPanelSecColor);
        addIconIfPresent(ironRow, "./programacio/src/GUI/images/iron_icon.png", 40, 40);
        ironAmountLabel = styledLabel(civ.getIron() + " +" + civ.getIronGenerated());
        ironRow.add(ironAmountLabel);
        panel.add(ironRow);

        // Mana
        JPanel manaRow = new JPanel();
        manaRow.setBackground(Globals.leftPanelSecColor);
        addIconIfPresent(manaRow, "./programacio/src/GUI/images/mana_icon.png", 30, 30);
        manaAmountLabel = styledLabel(civ.getMana() + " +" + civ.getManaGenerated());
        manaRow.add(manaAmountLabel);
        panel.add(manaRow);

        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════
    // Row 2 – Technology  (mirrors SpaceWars layout 1:1)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTechnologyPanel(Civilization civ,
                                        ImageIcon plusIcon, ImageIcon swordIcon,
                                        ImageIcon shieldIcon, ImageIcon ironIconSmall) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBackground(Globals.leftPanelColor);

        addSectionTitle(panel, "Technology");

        // ── Attack row ──────────────────────────────────────────────────
        JPanel attackRow = new JPanel();
        attackRow.setBackground(Globals.leftPanelSecColor);

        techAttackLvlAmountTextArea = makeLevelArea("" + civ.getTechnologyAttack());
        attackRow.add(techAttackLvlAmountTextArea);

        JButton lvlUpAttack = makePlusButton(plusIcon);
        lvlUpAttack.addActionListener(e -> {
            AudioPlayer.buttonSound();
            try { civ.upgradeTechnologyAttack(); }
            catch (ResourceException ex) { System.out.println(ex.getMessage()); }
        });
        attackRow.add(lvlUpAttack);

        if (swordIcon != null) attackRow.add(new JLabel(swordIcon));

        techAttackLvlCost = styledLabel("" + civ.getUpgradeAttackTechnologyIronCost());
        attackRow.add(techAttackLvlCost);
        if (ironIconSmall != null) attackRow.add(new JLabel(ironIconSmall));

        panel.add(attackRow);

        // ── Defense row ─────────────────────────────────────────────────
        JPanel defenseRow = new JPanel();
        defenseRow.setBackground(Globals.leftPanelSecColor);

        techDefenseLvlAmountTextArea = makeLevelArea("" + civ.getTechnologyDefense());
        defenseRow.add(techDefenseLvlAmountTextArea);

        JButton lvlUpDefense = makePlusButton(plusIcon);
        lvlUpDefense.addActionListener(e -> {
            AudioPlayer.buttonSound();
            try { civ.upgradeTechnologyDefense(); }
            catch (ResourceException ex) { System.out.println(ex.getMessage()); }
        });
        defenseRow.add(lvlUpDefense);

        if (shieldIcon != null) defenseRow.add(new JLabel(shieldIcon));

        techDefenseLvlCost = styledLabel("" + civ.getUpgradeDefenseTechnologyIronCost());
        defenseRow.add(techDefenseLvlCost);
        if (ironIconSmall != null) defenseRow.add(new JLabel(ironIconSmall));

        panel.add(defenseRow);

        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════
    // Row 3 – Buildings  (replaces Mines from SpaceWars)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildBuildingsPanel(Civilization civ, ImageIcon plusIcon) {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.setBackground(Globals.leftPanelColor);

        addSectionTitle(panel, "Buildings");

        farmLabel       = addBuildingRow(panel, "Farm: "        + civ.getFarm(),        plusIcon, () -> {
            try { civ.newFarm();       } catch (ResourceException e) { System.out.println(e.getMessage()); }
        });
        carpentryLabel  = addBuildingRow(panel, "Carpentry: "   + civ.getCarpentry(),   plusIcon, () -> {
            try { civ.newCarpentry();  } catch (ResourceException e) { System.out.println(e.getMessage()); }
        });
        smithyLabel     = addBuildingRow(panel, "Smithy: "      + civ.getSmithy(),      plusIcon, () -> {
            try { civ.newSmithy();     } catch (ResourceException e) { System.out.println(e.getMessage()); }
        });
        magicTowerLabel = addBuildingRow(panel, "Magic Tower: " + civ.getMagicTower(),  plusIcon, () -> {
            try { civ.newMagicTower(); } catch (ResourceException e) { System.out.println(e.getMessage()); }
        });
        churchLabel     = addBuildingRow(panel, "Church: "      + civ.getChurch(),      plusIcon, () -> {
            try { civ.newChurch(); }
            catch (ResourceException | BuildingException e) { System.out.println(e.getMessage()); }
        });

        return panel;
    }

    /** Creates one building row with [+] button and label; returns the label. */
    private JLabel addBuildingRow(JPanel parent, String text, ImageIcon plusIcon, Runnable action) {
        JPanel row = new JPanel();
        row.setBackground(Globals.leftPanelSecColor);

        JButton btn = makePlusButton(plusIcon);
        btn.addActionListener(e -> { AudioPlayer.buttonSound(); action.run(); });
        row.add(btn);

        JLabel lbl = styledLabel(text);
        row.add(lbl);

        parent.add(row);
        return lbl;
    }

    // ══════════════════════════════════════════════════════════════════════
    // Small helpers
    // ══════════════════════════════════════════════════════════════════════

    private void addSectionTitle(JPanel parent, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(customFont);
        lbl.setForeground(Globals.leftFontColor);
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Globals.leftPanelColor);
        titlePanel.add(lbl);
        parent.add(titlePanel);
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(customFontSmaller);
        lbl.setForeground(Globals.leftFontSecColor);
        return lbl;
    }

    private JTextArea makeLevelArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setFont(customFontSmaller);
        area.setPreferredSize(new Dimension(30, 30));
        area.setEditable(false);
        return area;
    }

    private JButton makePlusButton(ImageIcon icon) {
        JButton btn;
        if (icon != null) {
            btn = new JButton(icon);
            btn.setFont(new Font("Arial", Font.BOLD, 0));
        } else {
            btn = new JButton("+");
            btn.setFont(new Font("Arial", Font.BOLD, 14));
        }
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setBackground(Color.WHITE);
        return btn;
    }

    /** Adds an icon to a panel if the image file exists; silently skips otherwise. */
    private void addIconIfPresent(JPanel panel, String path, int w, int h) {
        ImageIcon icon = scaledIcon(path, w, h);
        if (icon != null) panel.add(new JLabel(icon));
    }

    /** Loads and scales an icon; returns null if the file is missing. */
    private ImageIcon scaledIcon(String path, int w, int h) {
        try {
            ImageIcon raw = new ImageIcon(path);
            if (raw.getIconWidth() <= 0) return null;
            return new ImageIcon(raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { /* handled via lambdas above */ }

    // ══════════════════════════════════════════════════════════════════════
    // Getters  (used by MainPanel.updateAll every 100 ms)
    // ══════════════════════════════════════════════════════════════════════

    public JLabel getFoodLabel()  { return foodAmountLabel; }
    public JLabel getWoodLabel()  { return woodAmountLabel; }
    public JLabel getIronLabel()  { return ironAmountLabel; }
    public JLabel getManaLabel()  { return manaAmountLabel; }

    public JTextArea getTechAttackArea()    { return techAttackLvlAmountTextArea; }
    public JTextArea getTechDefenseArea()   { return techDefenseLvlAmountTextArea; }
    public JLabel getTechAttackCostLabel()  { return techAttackLvlCost; }
    public JLabel getTechDefenseCostLabel() { return techDefenseLvlCost; }

    public JLabel getFarmLabel()       { return farmLabel; }
    public JLabel getCarpentryLabel()  { return carpentryLabel; }
    public JLabel getSmithyLabel()     { return smithyLabel; }
    public JLabel getMagicTowerLabel() { return magicTowerLabel; }
    public JLabel getChurchLabel()     { return churchLabel; }
}