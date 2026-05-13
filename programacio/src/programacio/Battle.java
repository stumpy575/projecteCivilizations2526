package programacio;

import java.util.ArrayList;
import java.util.Random;

// Army slot order (both sides):
//   [0] Swordsman  [1] Spearman  [2] Crossbow  [3] Cannon
//   [4] ArrowTower [5] Catapult  [6] RocketLauncherTower
//   [7] Magician   [8] Priest
// Enemy only uses slots 0-3 (no defenses, no specials).

public class Battle implements Variables {

	private ArrayList<MilitaryUnit>[] civilizationArmy;
	private ArrayList<MilitaryUnit>[] enemyArmy;

	// armies[0] = civilizationArmy, armies[1] = enemyArmy
	private ArrayList<MilitaryUnit>[][] armies;

	// Step-by-step log, built with String concatenation
	private String battleDevelopment;

	// initialCostFleet[0] = {food, wood, iron} cost of civilization's initial army
	// initialCostFleet[1] = {food, wood, iron} cost of enemy's initial army
	private int[][] initialCostFleet;

	private int initialNumberUnitsCivilization;
	private int initialNumberUnitsEnemy;

	// wasteWoodIron[0] = wood, wasteWoodIron[1] = iron
	private int[] wasteWoodIron;

	private int civilizationDrops;
	private int enemyDrops;

	// resourcesLooses[side][0] = food losses
	// resourcesLooses[side][1] = wood losses
	// resourcesLooses[side][2] = iron losses
	// resourcesLooses[side][3] = weighted total: iron + wood/5 + food/10
	private int[][] resourcesLooses;

	// initialArmies[side][slot] = number of units of that type at battle start
	private int[][] initialArmies;

	// Current unit counts per slot, updated as units die
	private int[] actualNumberUnitsCivilization;
	private int[] actualNumberUnitsEnemy;

	private boolean civilizationWon;
	private int battleNumber;

	private Random rng;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public Battle(Civilization civilization, ArrayList<MilitaryUnit>[] enemyArmySlots, int battleNumber) {
		this.battleNumber = battleNumber;
		this.civilizationArmy = civilization.getArmy();
		this.enemyArmy = enemyArmySlots;

		this.armies = new ArrayList[2][9];
		armies[0] = civilizationArmy;
		armies[1] = enemyArmy;

		this.battleDevelopment = "";
		this.wasteWoodIron = new int[2];
		this.civilizationDrops = 0;
		this.enemyDrops = 0;
		this.initialCostFleet = new int[2][3];
		this.resourcesLooses = new int[2][4];
		this.initialArmies = new int[2][9];
		this.actualNumberUnitsCivilization = new int[9];
		this.actualNumberUnitsEnemy = new int[9];

		this.rng = new Random();

		initInitialArmies();
		this.initialNumberUnitsCivilization = totalUnits(0);
		this.initialNumberUnitsEnemy = totalUnits(1);
		fleetResourceCost();
	}

	// -------------------------------------------------------------------------
	// Initialization helpers
	// -------------------------------------------------------------------------

	private void initInitialArmies() {
		for (int slot = 0; slot < 9; slot++) {
			initialArmies[0][slot] = civilizationArmy[slot].size();
			initialArmies[1][slot] = enemyArmy[slot].size();
			actualNumberUnitsCivilization[slot] = civilizationArmy[slot].size();
			actualNumberUnitsEnemy[slot] = enemyArmy[slot].size();
		}
	}

	private void fleetResourceCost() {
		for (int slot = 0; slot < 9; slot++) {
			if (civilizationArmy[slot].size() > 0) {
				MilitaryUnit sample = civilizationArmy[slot].get(0);
				int count = initialArmies[0][slot];
				initialCostFleet[0][0] += sample.getFoodCost() * count;
				initialCostFleet[0][1] += sample.getWoodCost() * count;
				initialCostFleet[0][2] += sample.getIronCost() * count;
			}
			if (enemyArmy[slot].size() > 0) {
				MilitaryUnit sample = enemyArmy[slot].get(0);
				int count = initialArmies[1][slot];
				initialCostFleet[1][0] += sample.getFoodCost() * count;
				initialCostFleet[1][1] += sample.getWoodCost() * count;
				initialCostFleet[1][2] += sample.getIronCost() * count;
			}
		}
	}

	private int totalUnits(int side) {
		int total = 0;
		for (int slot = 0; slot < 9; slot++) {
			total += armies[side][slot].size();
		}
		return total;
	}

	private int remainderPercentageFleet(int side) {
		int initial;
		if (side == 0) {
			initial = initialNumberUnitsCivilization;
		} else {
			initial = initialNumberUnitsEnemy;
		}
		if (initial == 0)
			return 0;
		return 100 * totalUnits(side) / initial;
	}

	// -------------------------------------------------------------------------
	// Attacker-group selection
	// -------------------------------------------------------------------------

	private int getCivilizationGroupAttacker() {
		int totalWeight = 0;
		int[] weights = new int[9];
		for (int i = 0; i < 9; i++) {
			if (armies[0][i].size() > 0) {
				weights[i] = CHANCE_ATTACK_CIVILIZATION_UNITS[i];
				totalWeight += CHANCE_ATTACK_CIVILIZATION_UNITS[i];
			}
		}
		if (totalWeight == 0)
			return -1;

		int r = rng.nextInt(totalWeight) + 1;
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
			if (armies[1][i].size() > 0) {
				weights[i] = CHANCE_ATTACK_ENEMY_UNITS[i];
				totalWeight += CHANCE_ATTACK_ENEMY_UNITS[i];
			}
		}
		if (totalWeight == 0)
			return -1;

		int r = rng.nextInt(totalWeight) + 1;
		int cumulative = 0;
		for (int i = 0; i < 4; i++) {
			cumulative += weights[i];
			if (cumulative >= r)
				return i;
		}
		return -1;
	}

	// -------------------------------------------------------------------------
	// Defender-group selection (proportional to unit count in each group)
	// -------------------------------------------------------------------------

	private int getGroupDefender(int side) {
		int total = totalUnits(side);
		if (total == 0)
			return -1;

		int r = rng.nextInt(total) + 1;
		int cumulative = 0;
		for (int i = 0; i < 9; i++) {
			cumulative += armies[side][i].size();
			if (cumulative >= r)
				return i;
		}
		return -1;
	}

	// -------------------------------------------------------------------------
	// Waste generation
	// -------------------------------------------------------------------------

	private void tryGenerateWaste(MilitaryUnit unit) {
		int chance = unit.getChanceGeneratinWaste();
		if (chance == 0)
			return;
		if (rng.nextInt(100) + 1 <= chance) {
			wasteWoodIron[0] += unit.getWoodCost() * PERCENTATGE_WASTE / 100;
			wasteWoodIron[1] += unit.getIronCost() * PERCENTATGE_WASTE / 100;
		}
	}

	// -------------------------------------------------------------------------
	// Core combat step — returns true if the attacker should attack again
	// -------------------------------------------------------------------------

	private boolean doAttack(int attackerSide, int attackerGroup, int defenderSide, int defenderGroup) {
		ArrayList<MilitaryUnit> attackerList = armies[attackerSide][attackerGroup];
		ArrayList<MilitaryUnit> defenderList = armies[defenderSide][defenderGroup];

		if (attackerList.size() == 0 || defenderList.size() == 0)
			return false;

		MilitaryUnit attacker = attackerList.get(rng.nextInt(attackerList.size()));
		int defIdx = rng.nextInt(defenderList.size());
		MilitaryUnit defender = defenderList.get(defIdx);

		int damage = attacker.attack();
		defender.takeDamage(damage);

		String attackerLabel;
		if (attackerSide == 0) {
			attackerLabel = "Civilization";
		} else {
			attackerLabel = "Enemy";
		}

		battleDevelopment += "Attacks " + attackerLabel + ": " + attacker.getName() + " attacks " + defender.getName()
				+ "\n";
		battleDevelopment += attacker.getName() + " generates damage = " + damage + "\n";
		battleDevelopment += defender.getName() + " stays with armor = " + defender.getActualArmor() + "\n";

		if (defender.getActualArmor() <= 0) {
			battleDevelopment += "we eliminate " + defender.getName() + "\n";
			tryGenerateWaste(defender);
			defenderList.remove(defIdx);

			if (defenderSide == 0) {
				actualNumberUnitsCivilization[defenderGroup]--;
				civilizationDrops++;
			} else {
				actualNumberUnitsEnemy[defenderGroup]--;
				enemyDrops++;
			}
		}

		return rng.nextInt(100) + 1 <= attacker.getChanceAttackAgain();
	}

	// -------------------------------------------------------------------------
	// Sanctification — called via Civilization.sanctifyArmy()
	// -------------------------------------------------------------------------

	private void updateSanctification(Civilization civilization) {
		boolean active = civilizationArmy[8].size() > 0 && civilization.getMana() > 0;
		civilization.sanctifyArmy(active);
	}

	// -------------------------------------------------------------------------
	// Experience increment (surviving units gain 1 exp after battle)
	// -------------------------------------------------------------------------

	private void incrementExperience() {
		for (int slot = 0; slot < 9; slot++) {
			for (int j = 0; j < civilizationArmy[slot].size(); j++) {
				MilitaryUnit u = civilizationArmy[slot].get(j);
				u.setExperience(u.getExperience() + 1);
			}
		}
	}

	// -------------------------------------------------------------------------
	// Main battle loop
	// -------------------------------------------------------------------------

	public String runBattle(Civilization civilization) {
		battleDevelopment += "\n==================== BATTLE #" + battleNumber + " START ====================\n";

		int attacker = rng.nextInt(2);
		int defender = 1 - attacker;

		updateSanctification(civilization);

		while (remainderPercentageFleet(0) > BATTLE_END_PERCENTAGE
				&& remainderPercentageFleet(1) > BATTLE_END_PERCENTAGE) {

			battleDevelopment += "\n********************CHANGE ATTACKER********************\n";

			int attackerGroup;
			if (attacker == 0) {
				attackerGroup = getCivilizationGroupAttacker();
			} else {
				attackerGroup = getEnemyGroupAttacker();
			}

			if (attackerGroup == -1) {
				int tmp = attacker;
				attacker = defender;
				defender = tmp;
				continue;
			}

			int defenderGroup = getGroupDefender(defender);
			if (defenderGroup == -1)
				break;

			boolean attackAgain = doAttack(attacker, attackerGroup, defender, defenderGroup);

			while (attackAgain) {
				defenderGroup = getGroupDefender(defender);
				if (defenderGroup == -1)
					break;
				if (remainderPercentageFleet(defender) <= BATTLE_END_PERCENTAGE)
					break;
				attackAgain = doAttack(attacker, attackerGroup, defender, defenderGroup);
			}

			updateSanctification(civilization);

			int tmp = attacker;
			attacker = defender;
			defender = tmp;
		}

		incrementExperience();
		updateResourcesLooses();
		civilizationWon = resourcesLooses[0][3] <= resourcesLooses[1][3];

		battleDevelopment += "\n==================== BATTLE #" + battleNumber + " END ====================\n";

		return getBattleReport();
	}

	// -------------------------------------------------------------------------
	// Losses calculation
	// -------------------------------------------------------------------------

	private void updateResourcesLooses() {
		for (int side = 0; side < 2; side++) {
			int survivingFood = 0;
			int survivingWood = 0;
			int survivingIron = 0;
			for (int slot = 0; slot < 9; slot++) {
				for (int j = 0; j < armies[side][slot].size(); j++) {
					MilitaryUnit u = armies[side][slot].get(j);
					survivingFood += u.getFoodCost();
					survivingWood += u.getWoodCost();
					survivingIron += u.getIronCost();
				}
			}
			resourcesLooses[side][0] = initialCostFleet[side][0] - survivingFood;
			resourcesLooses[side][1] = initialCostFleet[side][1] - survivingWood;
			resourcesLooses[side][2] = initialCostFleet[side][2] - survivingIron;
			resourcesLooses[side][3] = resourcesLooses[side][2] + resourcesLooses[side][1] / 5
					+ resourcesLooses[side][0] / 10;
		}
	}

	// -------------------------------------------------------------------------
	// Reports
	// -------------------------------------------------------------------------

	public String getBattleReport() {
		String report = "";
		report += "\nBATTLE NUMBER: " + battleNumber + "\n";
		report += "BATTLE STATISTICS\n";
		report += "************************************************************************************\n";

		// Civilization units
		String[] civNames = { "Swordsman", "Spearman", "Crossbow", "Cannon", "Arrow Tower", "Catapult",
				"Rocket Launcher", "Magician", "Priest" };
		for (int i = 0; i < 9; i++) {
			int survived = civilizationArmy[i].size();
			int drops = initialArmies[0][i] - survived;
			report += "  " + civNames[i] + "   survived: " + survived + "   drops: " + drops + "\n";
		}

		report += "************************************************************************************\n";

		// Enemy units
		String[] enemyNames = { "Swordsman", "Spearman", "Crossbow", "Cannon" };
		for (int i = 0; i < 4; i++) {
			int survived = enemyArmy[i].size();
			int drops = initialArmies[1][i] - survived;
			report += "  " + enemyNames[i] + "   survived: " + survived + "   drops: " + drops + "\n";
		}

		report += "************************************************************************************\n";
		report += "Cost Army Civilization\n";
		report += "  Food: " + initialCostFleet[0][0] + "\n";
		report += "  Wood: " + initialCostFleet[0][1] + "\n";
		report += "  Iron: " + initialCostFleet[0][2] + "\n";
		report += "Cost Army Enemy\n";
		report += "  Food: " + initialCostFleet[1][0] + "\n";
		report += "  Wood: " + initialCostFleet[1][1] + "\n";
		report += "  Iron: " + initialCostFleet[1][2] + "\n";

		report += "************************************************************************************\n";
		report += "Losses Army Civilization\n";
		report += "  Food: " + resourcesLooses[0][0] + "\n";
		report += "  Wood: " + resourcesLooses[0][1] + "\n";
		report += "  Iron: " + resourcesLooses[0][2] + "\n";
		report += "Losses Army Enemy\n";
		report += "  Food: " + resourcesLooses[1][0] + "\n";
		report += "  Wood: " + resourcesLooses[1][1] + "\n";
		report += "  Iron: " + resourcesLooses[1][2] + "\n";

		report += "************************************************************************************\n";
		report += "Waste Generated:\n";
		report += "  Wood: " + wasteWoodIron[0] + "\n";
		report += "  Iron: " + wasteWoodIron[1] + "\n";

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

	// -------------------------------------------------------------------------
	// Getters
	// -------------------------------------------------------------------------

	public boolean isCivilizationWon() {
		return civilizationWon;
	}

	public int[] getWasteWoodIron() {
		return wasteWoodIron;
	}

	public int getBattleNumber() {
		return battleNumber;
	}

	public int[][] getResourcesLooses() {
		return resourcesLooses;
	}

	public int[][] getInitialCostFleet() {
		return initialCostFleet;
	}

	public int[][] getInitialArmies() {
		return initialArmies;
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