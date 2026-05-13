package programacio;

public class Cannon extends AttackUnit {

	public Cannon(int armor, int baseDamage) {
		setArmor(armor);
		setInitialArmor(armor);
		setBaseDamage(baseDamage);
	}

	public Cannon() {
		setArmor(Variables.ARMOR_CANNON);
		setInitialArmor(Variables.ARMOR_CANNON);
		setBaseDamage(Variables.BASE_DAMAGE_CANNON);
	}

	public int attack() {
		int dmg = getBaseDamage();
		if (isSanctified())
			dmg += dmg * PLUS_ATTACK_UNIT_SANCTIFIED / 100;
		dmg += dmg * (getExperience() * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
		return dmg;
	}

	public int getFoodCost() {
		return FOOD_COST_CANNON;
	}

	public int getWoodCost() {
		return WOOD_COST_CANNON;
	}

	public int getIronCost() {
		return IRON_COST_CANNON;
	}

	public int getManaCost() {
		return MANA_COST_CANNON;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_CANNON;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_CANNON;
	}

	public String getName() {
		return "Cannon";
	}

	public String toString() {
		return "Cannon[]";
	}
}