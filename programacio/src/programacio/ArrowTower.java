package programacio;

public class ArrowTower extends DefenseUnit {

	public ArrowTower(int armor, int baseDamage) {
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
		return FOOD_COST_ARROWTOWER;
	}

	public int getWoodCost() {
		return WOOD_COST_ARROWTOWER;
	}

	public int getIronCost() {
		return IRON_COST_ARROWTOWER;
	}

	public int getManaCost() {
		return MANA_COST_ARROWTOWER;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_ARROWTOWER;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_ARROWTOWER;
	}

	public String getName() {
		return "Arrow Tower";
	}

	public String toString() {
		return "ArrowTower[]";
	}

}