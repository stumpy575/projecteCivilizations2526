package clases;

public class Spearman extends AttackUnit {

	public Spearman(int armor, int baseDamage) {
		setArmor(armor);
		setInitialArmor(armor);
		setBaseDamage(baseDamage);
	}

	public Spearman() {
		setArmor(Variables.ARMOR_SPEARMAN);
		setInitialArmor(Variables.ARMOR_SPEARMAN);
		setBaseDamage(Variables.BASE_DAMAGE_SPEARMAN);
	}

	public int attack() {
		int dmg = getBaseDamage();
		if (isSanctified())
			dmg += dmg * PLUS_ATTACK_UNIT_SANCTIFIED / 100;
		dmg += dmg * (getExperience() * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
		return dmg;
	}

	public int getFoodCost() {
		return FOOD_COST_SPEARMAN;
	}

	public int getWoodCost() {
		return WOOD_COST_SPEARMAN;
	}

	public int getIronCost() {
		return IRON_COST_SPEARMAN;
	}

	public int getManaCost() {
		return MANA_COST_SPEARMAN;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_SPEARMAN;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_SPEARMAN;
	}

	public String getName() {
		return "Spearman";
	}

	public String toString() {
		return "Spearman[]";
	}
}