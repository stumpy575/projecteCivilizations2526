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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomPanel extends JPanel {

    // -----------------------------------------------------------------------
    // One inner panel per unit type  (9 total)
    // -----------------------------------------------------------------------
    private JPanel shopPanel;

    // -- Buy buttons (9 units) --
    private JButton buySwordsmanButton, buySpearmanButton, buyCrossbowButton,
            buyCannonButton, buyArrowTowerButton, buyCatapultButton,
            buyRocketLauncherButton, buyMagicianButton, buyPriestButton;

    // -- Count labels — updated every tick by MainPanel.updateAll() --
    private JLabel swordsmanNameLabel, spearmanNameLabel, crossbowNameLabel,
            cannonNameLabel, arrowTowerNameLabel, catapultNameLabel,
            rocketLauncherNameLabel, magicianNameLabel, priestNameLabel;

    // -- Fonts --
    private Font customFont, customFontSmaller;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    BottomPanel(Civilization civilization) {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Padding (matches SpaceWars original style)
        add(new PaddingPanel(), BorderLayout.NORTH);
        add(new PaddingPanel(), BorderLayout.WEST);
        add(new PaddingPanel(), BorderLayout.EAST);
        add(new PaddingPanel(), BorderLayout.SOUTH);

        // Fonts
        try {
            customFont        = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(24f);
            customFontSmaller = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Tiny resource icons for the cost row
        Image foodTiny = loadScaled("./M3-Programacion/GUI/images/food_icon.png",  16, 16);
        Image woodTiny = loadScaled("./M3-Programacion/GUI/images/wood_icon.png",  16, 16);
        Image ironTiny = loadScaled("./M3-Programacion/GUI/images/iron_icon.png",  16, 16);
        Image manaTiny = loadScaled("./M3-Programacion/GUI/images/mana_icon.png",  16, 16);

        // Shop grid  — 9 columns
        shopPanel = new JPanel(new GridLayout(1, 9));
        shopPanel.setBackground(Globals.bottomBackgroundColor);

        // ===================================================================
        // 0  SWORDSMAN  – Attack
        // ===================================================================
        swordsmanNameLabel = new JLabel("Swordsman - " + civilization.getArmy()[0].size());
        buySwordsmanButton = imageButton("./M3-Programacion/GUI/images/swordsman.png", 70, 100);
        buySwordsmanButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newSwordsman(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(swordsmanNameLabel, buySwordsmanButton,
                new int[]{ Variables.FOOD_COST_SWORDSMAN, Variables.WOOD_COST_SWORDSMAN,
                           Variables.IRON_COST_SWORDSMAN, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 1  SPEARMAN  – Attack
        // ===================================================================
        spearmanNameLabel = new JLabel("Spearman - " + civilization.getArmy()[1].size());
        buySpearmanButton = imageButton("./M3-Programacion/GUI/images/spearman.png", 70, 100);
        buySpearmanButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newSpearman(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(spearmanNameLabel, buySpearmanButton,
                new int[]{ Variables.FOOD_COST_SPEARMAN, Variables.WOOD_COST_SPEARMAN,
                           Variables.IRON_COST_SPEARMAN, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 2  CROSSBOW  – Attack
        // ===================================================================
        crossbowNameLabel = new JLabel("Crossbow - " + civilization.getArmy()[2].size());
        buyCrossbowButton = imageButton("./M3-Programacion/GUI/images/crossbow.png", 70, 100);
        buyCrossbowButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newCrossbow(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(crossbowNameLabel, buyCrossbowButton,
                new int[]{ Variables.FOOD_COST_CROSSBOW, Variables.WOOD_COST_CROSSBOW,
                           Variables.IRON_COST_CROSSBOW, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 3  CANNON  – Attack
        // ===================================================================
        cannonNameLabel = new JLabel("Cannon - " + civilization.getArmy()[3].size());
        buyCannonButton = imageButton("./M3-Programacion/GUI/images/cannon.png", 70, 100);
        buyCannonButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newCannon(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(cannonNameLabel, buyCannonButton,
                new int[]{ Variables.FOOD_COST_CANNON, Variables.WOOD_COST_CANNON,
                           Variables.IRON_COST_CANNON, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 4  ARROW TOWER  – Defense
        // ===================================================================
        arrowTowerNameLabel = new JLabel("Arrow Tower - " + civilization.getArmy()[4].size());
        buyArrowTowerButton = imageButton("./M3-Programacion/GUI/images/arrowTower.png", 70, 100);
        buyArrowTowerButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newArrowTower(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(arrowTowerNameLabel, buyArrowTowerButton,
                new int[]{ Variables.FOOD_COST_ARROWTOWER, Variables.WOOD_COST_ARROWTOWER,
                           Variables.IRON_COST_ARROWTOWER, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 5  CATAPULT  – Defense
        // ===================================================================
        catapultNameLabel = new JLabel("Catapult - " + civilization.getArmy()[5].size());
        buyCatapultButton = imageButton("./M3-Programacion/GUI/images/catapult.png", 70, 100);
        buyCatapultButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newCatapult(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(catapultNameLabel, buyCatapultButton,
                new int[]{ Variables.FOOD_COST_CATAPULT, Variables.WOOD_COST_CATAPULT,
                           Variables.IRON_COST_CATAPULT, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 6  ROCKET LAUNCHER  – Defense
        // ===================================================================
        rocketLauncherNameLabel = new JLabel("Rocket - " + civilization.getArmy()[6].size());
        buyRocketLauncherButton = imageButton("./M3-Programacion/GUI/images/rocketLauncher.png", 70, 100);
        buyRocketLauncherButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newRocketLauncher(1); }
            catch (ResourceException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(rocketLauncherNameLabel, buyRocketLauncherButton,
                new int[]{ Variables.FOOD_COST_ROCKETLAUNCHERTOWER,
                           Variables.WOOD_COST_ROCKETLAUNCHERTOWER,
                           Variables.IRON_COST_ROCKETLAUNCHERTOWER, 0 },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 7  MAGICIAN  – Special  (requiere Magic Tower)
        // ===================================================================
        magicianNameLabel = new JLabel("Magician - " + civilization.getArmy()[7].size());
        buyMagicianButton = imageButton("./M3-Programacion/GUI/images/magician.png", 70, 100);
        buyMagicianButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newMagician(1); }
            catch (ResourceException | BuildingException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(magicianNameLabel, buyMagicianButton,
                new int[]{ Variables.FOOD_COST_MAGICIAN, Variables.WOOD_COST_MAGICIAN,
                           Variables.IRON_COST_MAGICIAN, Variables.MANA_COST_MAGICIAN },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // ===================================================================
        // 8  PRIEST  – Special  (requiere Church)
        // ===================================================================
        priestNameLabel = new JLabel("Priest - " + civilization.getArmy()[8].size());
        buyPriestButton = imageButton("./M3-Programacion/GUI/images/priest.png", 70, 100);
        buyPriestButton.addActionListener(e -> {
            try { AudioPlayer.buttonSound(); civilization.newPriest(1); }
            catch (ResourceException | BuildingException ex) { ex.printStackTrace(); }
        });
        shopPanel.add(buildUnitPanel(priestNameLabel, buyPriestButton,
                new int[]{ Variables.FOOD_COST_PRIEST, Variables.WOOD_COST_PRIEST,
                           Variables.IRON_COST_PRIEST, Variables.MANA_COST_PRIEST },
                foodTiny, woodTiny, ironTiny, manaTiny));

        // -----------------------------------------------------------------------
        // Wrap everything
        // -----------------------------------------------------------------------
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Globals.bottomBackgroundColor);
        mainPanel.add(shopPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // =======================================================================
    // Private helpers
    // =======================================================================

    /**
     * Builds one unit column:
     *   NORTH  – name / count label
     *   CENTER – image buy button
     *   SOUTH  – cost row (only shows non-zero costs)
     */
    private JPanel buildUnitPanel(JLabel nameLabel, JButton buyButton,
                                  int[] costs,
                                  Image foodImg, Image woodImg,
                                  Image ironImg, Image manaImg) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Globals.bottomBackgroundColor);

        // Name label
        nameLabel.setForeground(Globals.bottomFontColor);
        nameLabel.setFont(customFontSmaller);
        JPanel namePanel = new JPanel();
        namePanel.setBackground(Globals.bottomBackgroundColor);
        namePanel.add(nameLabel);
        panel.add(namePanel, BorderLayout.NORTH);

        // Buy button
        buyButton.setBackground(Globals.bottomImageBackgroundColor);
        buyButton.setFont(new Font("Arial", Font.BOLD, 0));   // hide default text
        buyButton.setPreferredSize(new Dimension(getWidth(), getHeight()));
        panel.add(buyButton, BorderLayout.CENTER);

        // Cost row  — only non-zero resources are shown
        JPanel costPanel = new JPanel();
        costPanel.setBackground(Globals.bottomBackgroundColor);
        addCostEntry(costPanel, foodImg, costs[0]);   // food
        addCostEntry(costPanel, woodImg, costs[1]);   // wood
        addCostEntry(costPanel, ironImg, costs[2]);   // iron
        addCostEntry(costPanel, manaImg, costs[3]);   // mana
        panel.add(costPanel, BorderLayout.SOUTH);

        return panel;
    }

    /** Adds [icon + label] to a cost panel only when amount > 0. */
    private void addCostEntry(JPanel costPanel, Image icon, int amount) {
        if (amount <= 0) return;
        costPanel.add(new JLabel(new ImageIcon(icon)));
        JLabel lbl = new JLabel("" + amount);
        lbl.setForeground(Globals.bottomFontColor);
        lbl.setFont(customFontSmaller);
        costPanel.add(lbl);
    }

    /** Loads an image from disk, scales it, and wraps it in a JButton. */
    private JButton imageButton(String path, int w, int h) {
        JButton btn = new JButton();
        try {
            Image img = new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception ignored) {
            // If the image file doesn't exist yet the button still works
        }
        return btn;
    }

    /** Loads and scales an image; returns a fallback 1×1 pixel on error. */
    private Image loadScaled(String path, int w, int h) {
        try {
            return new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            return new ImageIcon().getImage();
        }
    }

    // =======================================================================
    // Public getters — called by MainPanel.updateAll() every 100 ms
    // =======================================================================

    // Attack
    public JLabel getSwordsmanNameLabel()       { return swordsmanNameLabel; }
    public JLabel getSpearmanNameLabel()        { return spearmanNameLabel; }
    public JLabel getCrossbowNameLabel()        { return crossbowNameLabel; }
    public JLabel getCannonNameLabel()          { return cannonNameLabel; }
    // Defense
    public JLabel getArrowTowerNameLabel()      { return arrowTowerNameLabel; }
    public JLabel getCatapultNameLabel()        { return catapultNameLabel; }
    public JLabel getRocketLauncherNameLabel()  { return rocketLauncherNameLabel; }
    // Special
    public JLabel getMagicianNameLabel()        { return magicianNameLabel; }
    public JLabel getPriestNameLabel()          { return priestNameLabel; }
}