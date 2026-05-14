package clases;

public class Magician extends SpecialUnit {

	public Magician(int armor, int baseDamage) {
		setBaseDamage(baseDamage);
	}

	public int attack() {
		int dmg = getBaseDamage();
		dmg += dmg * (getExperience() * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
		return dmg;
	}

	public int getFoodCost() {
		return FOOD_COST_MAGICIAN;
	}

	public int getWoodCost() {
		return WOOD_COST_MAGICIAN;
	}

	public int getIronCost() {
		return IRON_COST_MAGICIAN;
	}

	public int getManaCost() {
		return MANA_COST_MAGICIAN;
	}

	public int getChanceGeneratinWaste() {
		return CHANCE_GENERATING_WASTE_MAGICIAN;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_MAGICIAN;
	}

	public String getName() {
		return "Magician";
	}

	public String toString() {
		return "Magician[]";
	}
}