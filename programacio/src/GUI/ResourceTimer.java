package GUI;

import clases.Civilization;

import java.util.Timer;
import java.util.TimerTask;

public class ResourceTimer extends TimerTask {

    private final Civilization civ;
    private final Timer timer = new Timer();

    public ResourceTimer(Civilization civ) {
        this.civ = civ;
        timer.schedule(this, Time.timeBetweenResources, Time.timeBetweenResources);
    }

    @Override
    public void run() {
        civ.setFood(civ.getFood() + civ.getFoodGenerated());
        civ.setWood(civ.getWood() + civ.getWoodGenerated());
        civ.setIron(civ.getIron() + civ.getIronGenerated());
        civ.setMana(civ.getMana() + civ.getManaGenerated());
    }
}