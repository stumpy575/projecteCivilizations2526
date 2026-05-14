package GUI;

import clases.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class MainPanel extends JPanel {

    private LeftPanel   leftPanel;
    private RightPanel  rightPanel;
    private BottomPanel bottomPanel;
    private MiddlePanel middlePanel;

    // -----------------------------------------------------------------------
    // Constructor — Planet → Civilization
    // -----------------------------------------------------------------------
    MainPanel(Civilization civilization, MainScreen ms) {
        setBackground(Color.BLUE);
        setLayout(new BorderLayout());

        leftPanel   = new LeftPanel(civilization);
        rightPanel  = new RightPanel(civilization, ms);
        bottomPanel = new BottomPanel(civilization);
        middlePanel = new MiddlePanel(civilization);

        add(leftPanel,   BorderLayout.WEST);
        add(rightPanel,  BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        add(middlePanel, BorderLayout.CENTER);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() { updateAll(civilization); }
        }, 0, 100);

        setVisible(true);
    }

    // -----------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------
    public LeftPanel   getLeftPanel()   { return leftPanel;   }
    public RightPanel  getRightPanel()  { return rightPanel;  }
    public BottomPanel getBottomPanel() { return bottomPanel; }
    public MiddlePanel getMiddlePanel() { return middlePanel; }

    // -----------------------------------------------------------------------
    // updateAll — single method that keeps all three panels in sync
    // -----------------------------------------------------------------------
    public void updateAll(Civilization civilization) {

        // ── LEFT: resources ────────────────────────────────────────────────
        leftPanel.getFoodAmountLabel().setText(
                civilization.getFood() + " +" + civilization.getFoodGenerated());
        leftPanel.getWoodAmountLabel().setText(
                civilization.getWood() + " +" + civilization.getWoodGenerated());
        leftPanel.getIronAmountLabel().setText(
                civilization.getIron() + " +" + civilization.getIronGenerated());
        leftPanel.getManaAmountLabel().setText(
                civilization.getMana() + " +" + civilization.getManaGenerated());

        // ── LEFT: technology ───────────────────────────────────────────────
        leftPanel.getTechAttackLvlTextArea().setText(
                "" + civilization.getTechnologyAttack());
        leftPanel.getTechAttackLvlCost().setText(
                "" + civilization.getUpgradeAttackTechnologyIronCost() + " iron");
        leftPanel.getTechDefenseLvlTextArea().setText(
                "" + civilization.getTechnologyDefense());
        leftPanel.getTechDefenseLvlCost().setText(
                "" + civilization.getUpgradeDefenseTechnologyIronCost() + " iron");

        // ── LEFT: buildings ────────────────────────────────────────────────
        leftPanel.getFarmCountLabel().setText(       "x" + civilization.getFarm());
        leftPanel.getCarpentryCountLabel().setText(  "x" + civilization.getCarpentry());
        leftPanel.getSmithyCountLabel().setText(     "x" + civilization.getSmithy());
        leftPanel.getMagicTowerCountLabel().setText( "x" + civilization.getMagicTower());
        leftPanel.getChurchCountLabel().setText(     "x" + civilization.getChurch());

        // ── BOTTOM: attack units ───────────────────────────────────────────
        bottomPanel.getSwordsmanNameLabel().setText(
                "Swordsman - "  + civilization.getArmy()[0].size());
        bottomPanel.getSpearmanNameLabel().setText(
                "Spearman - "   + civilization.getArmy()[1].size());
        bottomPanel.getCrossbowNameLabel().setText(
                "Crossbow - "   + civilization.getArmy()[2].size());
        bottomPanel.getCannonNameLabel().setText(
                "Cannon - "     + civilization.getArmy()[3].size());

        // ── BOTTOM: defense units ──────────────────────────────────────────
        bottomPanel.getArrowTowerNameLabel().setText(
                "Arrow Tower - " + civilization.getArmy()[4].size());
        bottomPanel.getCatapultNameLabel().setText(
                "Catapult - "    + civilization.getArmy()[5].size());
        bottomPanel.getRocketLauncherNameLabel().setText(
                "Rocket - "      + civilization.getArmy()[6].size());

        // ── BOTTOM: special units ──────────────────────────────────────────
        bottomPanel.getMagicianNameLabel().setText(
                "Magician - "   + civilization.getArmy()[7].size());
        bottomPanel.getPriestNameLabel().setText(
                "Priest - "     + civilization.getArmy()[8].size());

        // ── RIGHT: Fix Army cost ───────────────────────────────────────────
        int[] cost = civilization.getFixArmyCost();   // [food, wood, iron]
        rightPanel.getFoodCostFixLabel().setText("" + cost[0]);
        rightPanel.getWoodCostFixLabel().setText("" + cost[1]);
        rightPanel.getIronCostFixLabel().setText("" + cost[2]);
    }
}