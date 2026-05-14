package programacio;

import java.util.ArrayList;

public class Battle implements Variables {

	private ArrayList<MilitaryUnit>[] civilizationArmy;
	private ArrayList<MilitaryUnit>[] enemyArmy;

	private String battleDevelopment;

	private int initialFoodCostCivilization;
	private int initialWoodCostCivilization;
	private int initialIronCostCivilization;
	private int initialFoodCostEnemy;
	private int initialWoodCostEnemy;
	private int initialIronCostEnemy;
	private int losesFoodCivilization;
	private int losesWoodCivilization;
	private int losesIronCivilization;
	private int losesTotalCivilization;
	private int losesFoodEnemy;
	private int losesWoodEnemy;
	private int losesIronEnemy;
	private int losesTotalEnemy;
	private int wasteWood;
	private int wasteIron;
	private int civilizationDrops;
	private int enemyDrops;
	private int initialNumberUnitsCivilization;
	private int initialNumberUnitsEnemy;
	private int[] initialArmiesCivilization;
	private int[] initialArmiesEnemy;
	private int[] actualNumberUnitsCivilization;
	private int[] actualNumberUnitsEnemy;

	private boolean civilizationWon;
	private int battleNumber;

	public Battle(Civilization civilization, ArrayList<MilitaryUnit>[] enemyArmySlots, int battleNumber) {
		this.battleNumber = battleNumber;
		this.civilizationArmy = civilization.getArmy();
		this.enemyArmy = enemyArmySlots;
		this.battleDevelopment = "";
		this.wasteWood = 0;
		this.wasteIron = 0;
		this.civilizationDrops = 0;
		this.enemyDrops = 0;
		this.initialFoodCostCivilization = 0;
		this.initialWoodCostCivilization = 0;
		this.initialIronCostCivilization = 0;
		this.initialFoodCostEnemy = 0;
		this.initialWoodCostEnemy = 0;
		this.initialIronCostEnemy = 0;
		this.losesFoodCivilization = 0;
		this.losesWoodCivilization = 0;
		this.losesIronCivilization = 0;
		this.losesTotalCivilization = 0;
		this.losesFoodEnemy = 0;
		this.losesWoodEnemy = 0;
		this.losesIronEnemy = 0;
		this.losesTotalEnemy = 0;
		this.initialArmiesCivilization = new int[9];
		this.initialArmiesEnemy = new int[9];
		this.actualNumberUnitsCivilization = new int[9];
		this.actualNumberUnitsEnemy = new int[9];

		initInitialArmies();
		this.initialNumberUnitsCivilization = totalUnitsCivilization();
		this.initialNumberUnitsEnemy = totalUnitsEnemy();
		fleetResourceCost();
	}


	private void initInitialArmies() {
		for (int slot = 0; slot < 9; slot++) {
			initialArmiesCivilization[slot] = civilizationArmy[slot].size();
			initialArmiesEnemy[slot] = enemyArmy[slot].size();
			actualNumberUnitsCivilization[slot] = civilizationArmy[slot].size();
			actualNumberUnitsEnemy[slot] = enemyArmy[slot].size();
		}
	}

	private void fleetResourceCost() {
		for (int slot = 0; slot < 9; slot++) {
			if (civilizationArmy[slot].size() > 0) {
				MilitaryUnit sample = civilizationArmy[slot].get(0);
				int count = initialArmiesCivilization[slot];
				initialFoodCostCivilization += sample.getFoodCost() * count;
				initialWoodCostCivilization += sample.getWoodCost() * count;
				initialIronCostCivilization += sample.getIronCost() * count;
			}
			if (enemyArmy[slot].size() > 0) {
				MilitaryUnit sample = enemyArmy[slot].get(0);
				int count = initialArmiesEnemy[slot];
				initialFoodCostEnemy += sample.getFoodCost() * count;
				initialWoodCostEnemy += sample.getWoodCost() * count;
				initialIronCostEnemy += sample.getIronCost() * count;
			}
		}
	}

	private int totalUnitsCivilization() {
		int total = 0;
		for (int slot = 0; slot < 9; slot++) {
			total += civilizationArmy[slot].size();
		}
		return total;
	}

	private int totalUnitsEnemy() {
		int total = 0;
		for (int slot = 0; slot < 9; slot++) {
			total += enemyArmy[slot].size();
		}
		return total;
	}

	private int remainderPercentageCivilization() {
		if (initialNumberUnitsCivilization == 0)
			return 0;
		return 100 * totalUnitsCivilization() / initialNumberUnitsCivilization;
	}

	private int remainderPercentageEnemy() {
		if (initialNumberUnitsEnemy == 0)
			return 0;
		return 100 * totalUnitsEnemy() / initialNumberUnitsEnemy;
	}

	private int getCivilizationGroupAttacker() {
		int totalWeight = 0;
		int[] weights = new int[9];
		for (int i = 0; i < 9; i++) {
			if (civilizationArmy[i].size() > 0) {
				weights[i] = CHANCE_ATTACK_CIVILIZATION_UNITS[i];
				totalWeight += CHANCE_ATTACK_CIVILIZATION_UNITS[i];
			}
		}
		if (totalWeight == 0)
			return -1;

		int r = (int) (Math.random() * totalWeight) + 1;
		int cumulative = 0;
		for (int i = 0; i < 9; i++) {
			cumulative += weights[i];
			if (cumulative >= r)
				return i;
		}
		return -1;
	}

	private int getEnemyGroupAttacker() {
		int totalWeight = 0;
		int[] weights = new int[4];
		for (int i = 0; i < 4; i++) {
			if (enemyArmy[i].size() > 0) {
				weights[i] = CHANCE_ATTACK_ENEMY_UNITS[i];
				totalWeight += CHANCE_ATTACK_ENEMY_UNITS[i];
			}
		}
		if (totalWeight == 0)
			return -1;

		int r = (int) (Math.random() * totalWeight) + 1;
		int cumulative = 0;
		for (int i = 0; i < 4; i++) {
			cumulative += weights[i];
			if (cumulative >= r)
				return i;
		}
		return -1;
	}

	private int getGroupDefenderCivilization() {
		int total = totalUnitsCivilization();
		if (total == 0)
			return -1;
		int r = (int) (Math.random() * total) + 1;
		int cumulative = 0;
		for (int i = 0; i < 9; i++) {
			cumulative += civilizationArmy[i].size();
			if (cumulative >= r)
				return i;
		}
		return -1;
	}

	private int getGroupDefenderEnemy() {
		int total = totalUnitsEnemy();
		if (total == 0)
			return -1;
		int r = (int) (Math.random() * total) + 1;
		int cumulative = 0;
		for (int i = 0; i < 4; i++) {
			cumulative += enemyArmy[i].size();
			if (cumulative >= r)
				return i;
		}
		return -1;
	}

	private void tryGenerateWaste(MilitaryUnit unit) {
		int chance = unit.getChanceGeneratinWaste();
		if (chance == 0)
			return;
		if ((int) (Math.random() * 100) + 1 <= chance) {
			wasteWood += unit.getWoodCost() * PERCENTATGE_WASTE / 100;
			wasteIron += unit.getIronCost() * PERCENTATGE_WASTE / 100;
		}
	}

	// devuelve true si ataca otra vez
	private boolean doAttackCivilization(int attackerGroup, int defenderGroup) {
		ArrayList<MilitaryUnit> attackerList = civilizationArmy[attackerGroup];
		ArrayList<MilitaryUnit> defenderList = enemyArmy[defenderGroup];

		if (attackerList.size() == 0 || defenderList.size() == 0)
			return false;

		MilitaryUnit attacker = attackerList.get((int) (Math.random() * attackerList.size()));
		int defIdx = (int) (Math.random() * defenderList.size());
		MilitaryUnit defender = defenderList.get(defIdx);

		int damage = attacker.attack();
		defender.takeDamage(damage);

		battleDevelopment += "Attacks Civilization: " + attacker.getName() + " attacks " + defender.getName() + "\n";
		battleDevelopment += attacker.getName() + " generates damage = " + damage + "\n";
		battleDevelopment += defender.getName() + " stays with armor = " + defender.getActualArmor() + "\n";

		if (defender.getActualArmor() <= 0) {
			battleDevelopment += "we eliminate " + defender.getName() + "\n";
			tryGenerateWaste(defender);
			defenderList.remove(defIdx);
			actualNumberUnitsEnemy[defenderGroup]--;
			enemyDrops++;
		}

		return (int) (Math.random() * 100) + 1 <= attacker.getChanceAttackAgain();
	}

	// lo mismo que la de arriba
	private boolean doAttackEnemy(int attackerGroup, int defenderGroup) {
		ArrayList<MilitaryUnit> attackerList = enemyArmy[attackerGroup];
		ArrayList<MilitaryUnit> defenderList = civilizationArmy[defenderGroup];

		if (attackerList.size() == 0 || defenderList.size() == 0)
			return false;

		MilitaryUnit attacker = attackerList.get((int) (Math.random() * attackerList.size()));
		int defIdx = (int) (Math.random() * defenderList.size());
		MilitaryUnit defender = defenderList.get(defIdx);

		int damage = attacker.attack();
		defender.takeDamage(damage);

		battleDevelopment += "Attacks Enemy: " + attacker.getName() + " attacks " + defender.getName() + "\n";
		battleDevelopment += attacker.getName() + " generates damage = " + damage + "\n";
		battleDevelopment += defender.getName() + " stays with armor = " + defender.getActualArmor() + "\n";

		if (defender.getActualArmor() <= 0) {
			battleDevelopment += "we eliminate " + defender.getName() + "\n";
			tryGenerateWaste(defender);
			defenderList.remove(defIdx);
			actualNumberUnitsCivilization[defenderGroup]--;
			civilizationDrops++;
		}

		return (int) (Math.random() * 100) + 1 <= attacker.getChanceAttackAgain();
	}

	private void updateSanctification(Civilization civilization) {
		boolean active = civilizationArmy[8].size() > 0 && civilization.getMana() > 0;
		civilization.sanctifyArmy(active);
	}

	private void incrementExperience() {
		for (int slot = 0; slot < 9; slot++) {
			for (int j = 0; j < civilizationArmy[slot].size(); j++) {
				MilitaryUnit u = civilizationArmy[slot].get(j);
				u.setExperience(u.getExperience() + 1);
			}
		}
	}

	public String runBattle(Civilization civilization) {
		battleDevelopment += "\n==================== BATTLE #" + battleNumber + " START ====================\n";

		boolean civilizationAttacksFirst = Math.random() < 0.5;
		updateSanctification(civilization);

		while (remainderPercentageCivilization() > BATTLE_END_PERCENTAGE
				&& remainderPercentageEnemy() > BATTLE_END_PERCENTAGE) {

			battleDevelopment += "\n********************CHANGE ATTACKER********************\n";

			if (civilizationAttacksFirst) {
				int attackerGroup = getCivilizationGroupAttacker();
				if (attackerGroup != -1) {
					int defenderGroup = getGroupDefenderEnemy();
					if (defenderGroup == -1)
						break;
					boolean attackAgain = doAttackCivilization(attackerGroup, defenderGroup);
					while (attackAgain) {
						defenderGroup = getGroupDefenderEnemy();
						if (defenderGroup == -1)
							break;
						if (remainderPercentageEnemy() <= BATTLE_END_PERCENTAGE)
							break;
						attackAgain = doAttackCivilization(attackerGroup, defenderGroup);
					}
				}
			} else {
				int attackerGroup = getEnemyGroupAttacker();
				if (attackerGroup != -1) {
					int defenderGroup = getGroupDefenderCivilization();
					if (defenderGroup == -1)
						break;
					boolean attackAgain = doAttackEnemy(attackerGroup, defenderGroup);
					while (attackAgain) {
						defenderGroup = getGroupDefenderCivilization();
						if (defenderGroup == -1)
							break;
						if (remainderPercentageCivilization() <= BATTLE_END_PERCENTAGE)
							break;
						attackAgain = doAttackEnemy(attackerGroup, defenderGroup);
					}
				}
			}

			updateSanctification(civilization);
			civilizationAttacksFirst = !civilizationAttacksFirst;
		}

		incrementExperience();
		updateResourcesLooses();
		civilizationWon = losesTotalCivilization <= losesTotalEnemy;

		battleDevelopment += "\n==================== BATTLE #" + battleNumber + " END ====================\n";

		return getBattleReport();
	}

	private void updateResourcesLooses() {
		int survivingFoodCivilization = 0;
		int survivingWoodCivilization = 0;
		int survivingIronCivilization = 0;
		for (int slot = 0; slot < 9; slot++) {
			for (int j = 0; j < civilizationArmy[slot].size(); j++) {
				MilitaryUnit u = civilizationArmy[slot].get(j);
				survivingFoodCivilization += u.getFoodCost();
				survivingWoodCivilization += u.getWoodCost();
				survivingIronCivilization += u.getIronCost();
			}
		}
		losesFoodCivilization = initialFoodCostCivilization - survivingFoodCivilization;
		losesWoodCivilization = initialWoodCostCivilization - survivingWoodCivilization;
		losesIronCivilization = initialIronCostCivilization - survivingIronCivilization;
		losesTotalCivilization = losesIronCivilization + losesWoodCivilization / 5 + losesFoodCivilization / 10;

		int survivingFoodEnemy = 0;
		int survivingWoodEnemy = 0;
		int survivingIronEnemy = 0;
		for (int slot = 0; slot < 9; slot++) {
			for (int j = 0; j < enemyArmy[slot].size(); j++) {
				MilitaryUnit u = enemyArmy[slot].get(j);
				survivingFoodEnemy += u.getFoodCost();
				survivingWoodEnemy += u.getWoodCost();
				survivingIronEnemy += u.getIronCost();
			}
		}
		losesFoodEnemy = initialFoodCostEnemy - survivingFoodEnemy;
		losesWoodEnemy = initialWoodCostEnemy - survivingWoodEnemy;
		losesIronEnemy = initialIronCostEnemy - survivingIronEnemy;
		losesTotalEnemy = losesIronEnemy + losesWoodEnemy / 5 + losesFoodEnemy / 10;
	}

	public String getBattleReport() {
		String report = "";
		report += "\nBATTLE NUMBER: " + battleNumber + "\n";
		report += "BATTLE STATISTICS\n";
		report += "************************************************************************************\n";

		report += "Army Civilization:\n";
		report += "  Swordsman        survived: " + civilizationArmy[0].size() + "   drops: "
				+ (initialArmiesCivilization[0] - civilizationArmy[0].size()) + "\n";
		report += "  Spearman         survived: " + civilizationArmy[1].size() + "   drops: "
				+ (initialArmiesCivilization[1] - civilizationArmy[1].size()) + "\n";
		report += "  Crossbow         survived: " + civilizationArmy[2].size() + "   drops: "
				+ (initialArmiesCivilization[2] - civilizationArmy[2].size()) + "\n";
		report += "  Cannon           survived: " + civilizationArmy[3].size() + "   drops: "
				+ (initialArmiesCivilization[3] - civilizationArmy[3].size()) + "\n";
		report += "  Arrow Tower      survived: " + civilizationArmy[4].size() + "   drops: "
				+ (initialArmiesCivilization[4] - civilizationArmy[4].size()) + "\n";
		report += "  Catapult         survived: " + civilizationArmy[5].size() + "   drops: "
				+ (initialArmiesCivilization[5] - civilizationArmy[5].size()) + "\n";
		report += "  Rocket Launcher  survived: " + civilizationArmy[6].size() + "   drops: "
				+ (initialArmiesCivilization[6] - civilizationArmy[6].size()) + "\n";
		report += "  Magician         survived: " + civilizationArmy[7].size() + "   drops: "
				+ (initialArmiesCivilization[7] - civilizationArmy[7].size()) + "\n";
		report += "  Priest           survived: " + civilizationArmy[8].size() + "   drops: "
				+ (initialArmiesCivilization[8] - civilizationArmy[8].size()) + "\n";

		report += "************************************************************************************\n";
		report += "Army Enemy:\n";
		report += "  Swordsman  survived: " + enemyArmy[0].size() + "   drops: "
				+ (initialArmiesEnemy[0] - enemyArmy[0].size()) + "\n";
		report += "  Spearman   survived: " + enemyArmy[1].size() + "   drops: "
				+ (initialArmiesEnemy[1] - enemyArmy[1].size()) + "\n";
		report += "  Crossbow   survived: " + enemyArmy[2].size() + "   drops: "
				+ (initialArmiesEnemy[2] - enemyArmy[2].size()) + "\n";
		report += "  Cannon     survived: " + enemyArmy[3].size() + "   drops: "
				+ (initialArmiesEnemy[3] - enemyArmy[3].size()) + "\n";

		report += "************************************************************************************\n";
		report += "Cost Army Civilization\n";
		report += "  Food: " + initialFoodCostCivilization + "\n";
		report += "  Wood: " + initialWoodCostCivilization + "\n";
		report += "  Iron: " + initialIronCostCivilization + "\n";
		report += "Cost Army Enemy\n";
		report += "  Food: " + initialFoodCostEnemy + "\n";
		report += "  Wood: " + initialWoodCostEnemy + "\n";
		report += "  Iron: " + initialIronCostEnemy + "\n";

		report += "************************************************************************************\n";
		report += "Losses Army Civilization\n";
		report += "  Food: " + losesFoodCivilization + "\n";
		report += "  Wood: " + losesWoodCivilization + "\n";
		report += "  Iron: " + losesIronCivilization + "\n";
		report += "Losses Army Enemy\n";
		report += "  Food: " + losesFoodEnemy + "\n";
		report += "  Wood: " + losesWoodEnemy + "\n";
		report += "  Iron: " + losesIronEnemy + "\n";

		report += "************************************************************************************\n";
		report += "Waste Generated:\n";
		report += "  Wood: " + wasteWood + "\n";
		report += "  Iron: " + wasteIron + "\n";

		if (civilizationWon) {
			report += "\nBattle Won by Civilization - We Collect Rubble!\n";
		} else {
			report += "\nBattle Lost by Civilization.\n";
		}
		report += "####################################################################################\n";
		return report;
	}

	public String getBattleDevelopment() {
		return battleDevelopment;
	}

	public String getEnemyStats() {
		String result = "\nNEW THREAT COMING\n";
		result += "  Swordsman: " + enemyArmy[0].size() + "\n";
		result += "  Spearman:  " + enemyArmy[1].size() + "\n";
		result += "  Crossbow:  " + enemyArmy[2].size() + "\n";
		result += "  Cannon:    " + enemyArmy[3].size() + "\n";
		return result;
	}
	public boolean isCivilizationWon() {
		return civilizationWon;
	}

	public int getWasteWood() {
		return wasteWood;
	}

	public int getWasteIron() {
		return wasteIron;
	}

	public int getBattleNumber() {
		return battleNumber;
	}

	public int getLosesFoodCivilization() {
		return losesFoodCivilization;
	}

	public int getLosesWoodCivilization() {
		return losesWoodCivilization;
	}

	public int getLosesIronCivilization() {
		return losesIronCivilization;
	}

	public int getLosesFoodEnemy() {
		return losesFoodEnemy;
	}

	public int getLosesWoodEnemy() {
		return losesWoodEnemy;
	}

	public int getLosesIronEnemy() {
		return losesIronEnemy;
	}

	public int getInitialFoodCostCivilization() {
		return initialFoodCostCivilization;
	}

	public int getInitialWoodCostCivilization() {
		return initialWoodCostCivilization;
	}

	public int getInitialIronCostCivilization() {
		return initialIronCostCivilization;
	}

	public int getInitialFoodCostEnemy() {
		return initialFoodCostEnemy;
	}

	public int getInitialWoodCostEnemy() {
		return initialWoodCostEnemy;
	}

	public int getInitialIronCostEnemy() {
		return initialIronCostEnemy;
	}

	public int[] getInitialArmiesCivilization() {
		return initialArmiesCivilization;
	}

	public int[] getInitialArmiesEnemy() {
		return initialArmiesEnemy;
	}

	public ArrayList<MilitaryUnit>[] getCivilizationArmy() {
		return civilizationArmy;
	}

	public ArrayList<MilitaryUnit>[] getEnemyArmy() {
		return enemyArmy;
	}

	public String toString() {
		return getBattleReport();
	}
}