package programacio;

public class RocketLauncherTower extends DefenseUnit {

	public RocketLauncherTower(int armor, int baseDamage) {
		setArmor(armor);
		setInitialArmor(armor);
		setBaseDamage(baseDamage);
	}

	public int attack() {
		int dmg = getBaseDamage();
		if (isSanctified())
			dmg += dmg * PLUS_ATTACK_UNIT_SANCTIFIED / 100;
		dmg += dmg * (getExperience() * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
		return dmg;
	}

	public int getFoodCost() {
		return FOOD_COST_ROCKETLAUNCHERTOWER;
	}

	public int getWoodCost() {
		return WOOD_COST_ROCKETLAUNCHERTOWER;
	}

	public int getIronCost() {
		return IRON_COST_ROCKETLAUNCHERTOWER;
	}

	public int getManaCost() {
		return MANA_COST_ROCKETLAUNCHERTOWER;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_ROCKETLAUNCHERTOWER;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_ROCKETLAUNCHERTOWER;
	}

	public String getName() {
		return "Rocket Launcher Tower";
	}

	public String toString() {
		return "RocketLauncherTower[]";
	}
}