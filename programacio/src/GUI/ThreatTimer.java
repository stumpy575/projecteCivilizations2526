package GUI;

import clases.Battle;
import clases.Civilization;
import clases.Main;
import clases.MilitaryUnit;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ThreatTimer extends TimerTask {

    private final Civilization civ;
    private final MainScreen   ms;
    private final Timer        timer = new Timer();

    /** Standard: triggers after the normal countdown delay. */
    public ThreatTimer(Civilization civ, MainScreen ms) {
        this.civ = civ;
        this.ms  = ms;
        timer.schedule(this, Time.timeBetweenBattles);
    }

    /** Immediate: triggers right away (used by "Start a Fight" button). */
    public ThreatTimer(Civilization civ, MainScreen ms, int immediateFlag) {
        this.civ = civ;
        this.ms  = ms;
        timer.schedule(this, 0);
    }

    @Override
    public void run() {
        if (civ.getTotalTroops() == 0) {
            // No troops → skip this round and reschedule
            new ThreatTimer(civ, ms);
            return;
        }
        if (civ.getCurrentThreat() == null) {
            // Create enemy army and battle
            ArrayList<MilitaryUnit>[] enemyArmy = Main.createEnemyArmy(civ);
            int battleNumber = civ.getNBattles() + 1;
            Battle battle = new Battle(civ, enemyArmy, battleNumber);
            civ.setCurrentThreat(battle);
            civ.setActiveThreat(true);

            ms.getMainPanel().getMiddlePanel().doThreatDisplay();
            AudioPlayer.doAlarm();

            // Schedule actual combat after countdown
            new Timer().schedule(new TimerTask() {
                public void run() {
                    ms.getMainPanel().getMiddlePanel().changeScreenToBattleScene();
                    String report = battle.runBattle(civ);

                    civ.setNBattles(battleNumber);
                    civ.addBattleReport(battle);
                    civ.setActiveThreat(false);
                    civ.setCurrentThreat(null);
                    civ.resetArmyArmor();

                    if (battle.isCivilizationWon()) {
                        civ.setWood(civ.getWood() + battle.getWasteWood());
                        civ.setIron(civ.getIron() + battle.getWasteIron());
                    }

                    ms.getMainPanel().getMiddlePanel().changeScreenToDefaultScene();
                    new ResultFrame(battle);

                    // Schedule the next threat
                    new ThreatTimer(civ, ms);
                }
            }, Time.countdownBattleTime);
        }
    }
}