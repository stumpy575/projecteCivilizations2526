package GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import classes.*;

public class MiddlePanel extends JPanel {

    // -----------------------------------------------------------------------
    // Background images
    // -----------------------------------------------------------------------
    private BufferedImage activeImage;
    private BufferedImage civilizationImage;   // replaces earthImage
    private BufferedImage battleScene;
    private BufferedImage explosionImage;

    // -----------------------------------------------------------------------
    // Current battle state
    // -----------------------------------------------------------------------
    private MilitaryUnit allyUnit;
    private MilitaryUnit enemyUnit;
    private Battle       battle;

    // -----------------------------------------------------------------------
    // References & state
    // -----------------------------------------------------------------------
    private Civilization civilization;
    private String       threatDisplayStr = "THREAT DETECTED";
    private String       activeDisplayStr;
    private String       messageStr       = "";
    private int          timerCountdown;
    private Color        threatDisplayColor;

    // -----------------------------------------------------------------------
    // Fonts
    // -----------------------------------------------------------------------
    private Font customFontBiggest, customFontBig, customFont, customFontSmall;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    MiddlePanel(Civilization civilization) {
        setLayout(new BorderLayout());
        add(new PaddingPanel(), BorderLayout.NORTH);
        setFocusable(true);

        this.civilization  = civilization;
        activeDisplayStr   = threatDisplayStr;
        timerCountdown     = Time.secondsCountdownBattle;
        threatDisplayColor = Color.WHITE;

        // Fonts
        try {
            customFontBiggest = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(68f);
            customFontBig     = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(48f);
            customFont        = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(32f);
            customFontSmall   = Font.createFont(Font.TRUETYPE_FONT,
                    new File(Globals.customFont)).deriveFont(18f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Space bar — skip remaining battle animation
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && battle != null) {
                    System.out.println("Skipping battle animation...");
                    // hook: battle.setSkipBattle(true) if you add that field
                }
            }
        });

        // Load images
        try {
            // Use a medieval/nature background if you have one, else reuse earth3
            civilizationImage = ImageIO.read(new File(
                    "./M3-Programacion/GUI/images/civilization_bg.png"));
        } catch (IOException e) {
            // fallback — try the original earth image
            try {
                civilizationImage = ImageIO.read(new File(
                        "./M3-Programacion/GUI/images/earth3.png"));
            } catch (IOException ex) { ex.printStackTrace(); }
        }
        try {
            battleScene   = ImageIO.read(new File(
                    "./M3-Programacion/GUI/images/space2.png"));
            explosionImage = ImageIO.read(new File(
                    "./M3-Programacion/GUI/images/explosion.png"));
        } catch (IOException e) { e.printStackTrace(); }

        activeImage = civilizationImage;
    }

    // -----------------------------------------------------------------------
    // Paint
    // -----------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Background
        if (activeImage != null) {
            g2d.drawImage(activeImage.getScaledInstance(
                    getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
        }

        // Civilization name on default scene
        if (activeImage == civilizationImage) {
            g2d.setFont(customFontBig);
            g2d.setColor(threatDisplayColor);
            g2d.drawString(civilization.getName(), 50, 75);
        }

        // Threat / battle rendering
        if (civilization.getCurrentThreat() != null) {
            Battle current = civilization.getCurrentThreat();

            // ── Countdown before combat ────────────────────────────────────
            if (!current.isCivilizationWon()) {
                g2d.setFont(customFontBig);
                g2d.setColor(threatDisplayColor);
                g2d.drawString(activeDisplayStr,
                        getWidth() / 2 - (activeDisplayStr.length() * 13), 75);
                g2d.drawString(String.valueOf(timerCountdown),
                        getWidth() / 2, 160);
            }

            // ── Live combat scene ──────────────────────────────────────────
            if (battle != null && allyUnit != null && enemyUnit != null) {

                // Skip hint
                g2d.setFont(customFont);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.drawString("\"Space\" to skip",
                        getWidth() / 2 - 140, getHeight() - 30);

                // Diagonal divider
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(10));
                g2d.drawLine(0, 0, getWidth(), getHeight());

                // VS
                g2d.setFont(new Font("Arial", Font.ITALIC, 96));
                g2d.setColor(Color.WHITE);
                g2d.drawString("VS", getWidth() / 2 - 48, getHeight() / 2 + 48);

                // Unit images
                drawUnit(g2d, allyUnit,  30,               getHeight() - 330);   // ally  bottom-left
                drawUnit(g2d, enemyUnit, getWidth() - 330, 30);                   // enemy top-right

                // Explosion overlays
                if (explosionImage != null) {
                    if (allyUnit.getActualArmor() <= 0) {
                        g2d.drawImage(explosionImage.getScaledInstance(
                                300, 300, Image.SCALE_SMOOTH),
                                30, getHeight() - 330, this);
                    }
                    if (enemyUnit.getActualArmor() <= 0) {
                        g2d.drawImage(explosionImage.getScaledInstance(
                                300, 300, Image.SCALE_SMOOTH),
                                getWidth() - 330, 30, this);
                    }
                }

                // Health bars
                paintHealthBar(g2d, getAllyPercRemaining(),
                        20, getHeight() - 410, 300, 60,
                        30, getHeight() - 400);
                paintHealthBar(g2d, getEnemyPercRemaining(),
                        getWidth() - 330, getHeight() - 350, 300, 60,
                        getWidth() - 320, getHeight() - 340);

                // Civilization names above bars
                g2d.setFont(customFontBig);
                g2d.setColor(Color.WHITE);
                g2d.drawString(civilization.getName(), 50, getHeight() - 430);
                g2d.drawString("Enemy", getWidth() - 320, 450);
            }
        }

        // Floating message
        if (!messageStr.isEmpty()) {
            g2d.setColor(new Color(0, 0, 0, 220));
            g2d.fillRect(0, getHeight() / 2 - 30, getWidth(), 60);
            g2d.setFont(customFontBig);
            g2d.setColor(Color.RED);
            g2d.drawString(messageStr,
                    getWidth() - messageStr.length() * 54,
                    getHeight() / 2 + 20);
        }
    }

    // -----------------------------------------------------------------------
    // Private paint helpers
    // -----------------------------------------------------------------------

    private void drawUnit(Graphics2D g2d, MilitaryUnit unit, int x, int y) {
        if (unit == null || unit.getBufferedImage() == null) return;
        g2d.drawImage(unit.getBufferedImage()
                .getScaledInstance(300, 300, Image.SCALE_SMOOTH), x, y, this);
    }

    private void paintHealthBar(Graphics2D g2d, int perc,
                                 int bgX, int bgY, int bgW, int bgH,
                                 int barX, int barY) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(bgX, bgY, bgW, bgH);
        g2d.setColor(perc < 50
                ? Globals.healthBarInjuredColor
                : Globals.healthBarHealthyColor);
        g2d.fillRect(barX, barY, (int) (280 * (perc / 100f)), 40);
    }

    // -----------------------------------------------------------------------
    // Percentage helpers
    // (Battle gives us initialArmies + live arrays so we compute here)
    // -----------------------------------------------------------------------

    private int getAllyPercRemaining() {
        if (battle == null) return 100;
        int alive = 0;
        for (var slot : battle.getCivilizationArmy()) alive += slot.size();
        int initial = sum(battle.getInitialArmiesCivilization());
        return initial == 0 ? 0 : alive * 100 / initial;
    }

    private int getEnemyPercRemaining() {
        if (battle == null) return 100;
        int alive = 0;
        for (var slot : battle.getEnemyArmy()) alive += slot.size();
        int initial = sum(battle.getInitialArmiesEnemy());
        return initial == 0 ? 0 : alive * 100 / initial;
    }

    private int sum(int[] arr) {
        int s = 0; for (int v : arr) s += v; return s;
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /** Called each combat step by Battle to update the visual. */
    public void paintCurrentBattleState(Battle battle,
                                         MilitaryUnit allyUnit,
                                         MilitaryUnit enemyUnit) {
        this.battle    = battle;
        this.allyUnit  = allyUnit;
        this.enemyUnit = enemyUnit;
        repaint();
    }

    public void changeScreenToBattleScene()  { activeImage = battleScene;        repaint(); }
    public void changeScreenToDefaultScene() { activeImage = civilizationImage;  repaint(); }

    /** Starts the red/white flashing "THREAT DETECTED" countdown. */
    public void doThreatDisplay() { new ThreatDisplayTimer(); }

    /** Shows a short error message in the center of the screen. */
    public void doShowMessage(String message, int seconds) {
        if (messageStr.isEmpty()) new ShowMessage(message, seconds);
    }

    // -----------------------------------------------------------------------
    // Inner timer classes (identical pattern to SpaceWars)
    // -----------------------------------------------------------------------

    class ThreatDisplayTimer extends TimerTask {
        private int   elapsed = 0;
        private Timer timer;

        ThreatDisplayTimer() {
            activeDisplayStr = threatDisplayStr;
            timer = new Timer();
            timer.schedule(this, 0, Time.secInMs);
        }

        @Override
        public void run() {
            elapsed += Time.secInMs;
            timerCountdown--;
            threatDisplayColor = threatDisplayColor == Color.WHITE
                    ? Color.RED : Color.WHITE;

            if (elapsed > Time.countdownBattleTime) {
                cancel();
                threatDisplayColor = Color.WHITE;
                timerCountdown     = Time.secondsCountdownBattle;
            }
            repaint();
        }
    }

    class ShowMessage extends TimerTask {
        private int    elapsed = 0;
        private Timer  timer;
        private String message;
        private int    seconds;

        ShowMessage(String msg, int sec) {
            this.message = msg;
            this.seconds = sec;
            timer = new Timer();
            AudioPlayer.doError();
            timer.schedule(this, 0, Time.secInMs);
        }

        @Override
        public void run() {
            elapsed++;
            messageStr = message;
            repaint();
            if (elapsed > seconds) {
                messageStr = "";
                repaint();
                cancel();
            }
        }
    }
}
