package programacio;

public class Catapult extends DefenseUnit {

	public Catapult(int armor, int baseDamage) {
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
		return FOOD_COST_CATAPULT;
	}

	public int getWoodCost() {
		return WOOD_COST_CATAPULT;
	}

	public int getIronCost() {
		return IRON_COST_CATAPULT;
	}

	public int getManaCost() {
		return MANA_COST_CATAPULT;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_CATAPULT;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_CATAPULT;
	}

	public String getName() {
		return "Catapult";
	}

	public String toString() {
		return "Catapult[]";
	}
}