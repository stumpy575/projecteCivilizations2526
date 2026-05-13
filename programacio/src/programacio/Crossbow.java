package programacio;

public class Crossbow extends AttackUnit {

	public Crossbow(int armor, int baseDamage) {
		setArmor(armor);
		setInitialArmor(armor);
		setBaseDamage(baseDamage);
	}

	public Crossbow() {
		setArmor(Variables.ARMOR_CROSSBOW);
		setInitialArmor(Variables.ARMOR_CROSSBOW);
		setBaseDamage(Variables.BASE_DAMAGE_CROSSBOW);
	}

	public int attack() {
		int dmg = getBaseDamage();
		if (isSanctified())
			dmg += dmg * PLUS_ATTACK_UNIT_SANCTIFIED / 100;
		dmg += dmg * (getExperience() * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
		return dmg;
	}

	public int getFoodCost() {
		return FOOD_COST_CROSSBOW;
	}

	public int getWoodCost() {
		return WOOD_COST_CROSSBOW;
	}

	public int getIronCost() {
		return IRON_COST_CROSSBOW;
	}

	public int getManaCost() {
		return MANA_COST_CROSSBOW;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_CROSSBOW;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_CROSSBOW;
	}

	public String getName() {
		return "Crossbow";
	}

	public String toString() {
		return "Crossbow[]";
	}
}