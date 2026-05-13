package programacio;

public abstract class SpecialUnit implements MilitaryUnit, Variables {

	// las specialunits tienen 0 de armadura, los metodos de set o reset armor no
	// funcionan obviamente y al recibir daño mueren directamente
	private final int armor = 0;
	private final int initialArmor = 0;

	private int baseDamage;
	private int experience;

	public int getArmor() {
		return armor;
	}

	public int getInitialArmor() {
		return initialArmor;
	}

	public void resetArmor() {
	}

	public void takeDamage(int receivedDamage) {
	}

	public int getActualArmor() {
		return armor;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int n) {
		this.experience = n;
	}
}