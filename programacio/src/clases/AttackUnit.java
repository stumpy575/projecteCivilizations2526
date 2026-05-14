package clases;

public abstract class AttackUnit implements MilitaryUnit, Variables {

	private int armor;
	private int initialArmor;
	private int baseDamage;
	private int experience;
	private boolean sanctified;

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getInitialArmor() {
		return initialArmor;
	}

	public void setInitialArmor(int initialArmor) {
		this.initialArmor = initialArmor;
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

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public boolean isSanctified() {
		return sanctified;
	}

	public void setSanctified(boolean sanctified) {
		this.sanctified = sanctified;
	}

	public void resetArmor() {
		this.armor = this.initialArmor;
	}

	public void takeDamage(int receivedDamage) {
		armor -= receivedDamage;
		if (armor < 0)
			armor = 0;
	}

	public int getActualArmor() {
		return armor;
	}
}