package clases;

public interface MilitaryUnit {

	abstract int attack();

	abstract void takeDamage(int receivedDamage);

	abstract int getActualArmor();

	abstract int getFoodCost();

	abstract int getWoodCost();

	abstract int getIronCost();

	abstract int getManaCost();

	abstract int getChanceGeneratinWaste();

	abstract int getChanceAttackAgain();

	abstract void resetArmor();

	abstract void setExperience(int n);

	abstract int getExperience();

	abstract String getName();
}