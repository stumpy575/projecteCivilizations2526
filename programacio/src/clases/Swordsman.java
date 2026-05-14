package clases;

public class Swordsman extends AttackUnit {

	public Swordsman(int armor, int baseDamage) {
		setArmor(armor);
		setInitialArmor(armor);
		setBaseDamage(baseDamage);
	}

	public Swordsman() {
		setArmor(Variables.ARMOR_SWORDSMAN);
		setInitialArmor(Variables.ARMOR_SWORDSMAN);
		setBaseDamage(Variables.BASE_DAMAGE_SWORDSMAN);
	}

	public int attack() {
		int dmg = getBaseDamage();
		if (isSanctified())
			dmg += dmg * PLUS_ATTACK_UNIT_SANCTIFIED / 100;
		dmg += dmg * (getExperience() * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
		return dmg;
	}

	public int getFoodCost() {
		return FOOD_COST_SWORDSMAN;
	}

	public int getWoodCost() {
		return WOOD_COST_SWORDSMAN;
	}

	public int getIronCost() {
		return IRON_COST_SWORDSMAN;
	}

	public int getManaCost() {
		return MANA_COST_SWORDSMAN;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_SWORDSMAN;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_SWORDSMAN;
	}

	public String getName() {
		return "Swordsman";
	}

	public String toString() {
		return "Swordsman[]";
	}
}