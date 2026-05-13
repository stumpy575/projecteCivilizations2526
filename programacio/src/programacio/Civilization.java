package programacio;

import java.util.ArrayList;

public class Civilization implements Variables {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------
	private String name;

	// Technologies
	private int technologyDefense;
	private int technologyAttack;

	// Resources
	private int food;
	private int wood;
	private int iron;
	private int mana;

	// Buildings
	private int farm;
	private int carpentry;
	private int smithy;
	private int magicTower;
	private int church;

	// Escalating technology upgrade costs (recalculated after each upgrade)
	private int upgradeDefenseTechnologyIronCost;
	private int upgradeDefenseTechnologyWoodCost;
	private int upgradeDefenseTechnologyFoodCost;
	private int upgradeAttackTechnologyIronCost;
	private int upgradeAttackTechnologyWoodCost;
	private int upgradeAttackTechnologyFoodCost;

	// Battle state
	private int nBattles;
	private boolean isActiveThreat;
	private Battle currentThreat;
	private Battle[] battleReports;

	// Army — 9 slots:
	// [0] Swordsman [1] Spearman [2] Crossbow [3] Cannon
	// [4] ArrowTower [5] Catapult [6] RocketLauncherTower
	// [7] Magician [8] Priest
	private ArrayList<MilitaryUnit>[] army;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public Civilization(String name, int food, int wood, int iron, int mana) {
		this.name = name;
		this.food = food;
		this.wood = wood;
		this.iron = iron;
		this.mana = mana;

		this.technologyDefense = 0;
		this.technologyAttack = 0;

		this.farm = 0;
		this.carpentry = 0;
		this.smithy = 0;
		this.magicTower = 0;
		this.church = 0;

		this.upgradeDefenseTechnologyIronCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST;
		this.upgradeDefenseTechnologyWoodCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST;
		this.upgradeDefenseTechnologyFoodCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_FOOD_COST;
		this.upgradeAttackTechnologyIronCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST;
		this.upgradeAttackTechnologyWoodCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST;
		this.upgradeAttackTechnologyFoodCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_FOOD_COST;

		this.nBattles = 0;
		this.isActiveThreat = false;
		this.currentThreat = null;
		this.battleReports = new Battle[5];

		this.army = new ArrayList[9];
		for (int i = 0; i < army.length; i++) {
			army[i] = new ArrayList<>();
		}
	}

	// -------------------------------------------------------------------------
	// Buildings
	// -------------------------------------------------------------------------

	public void newFarm() throws ResourceException {
		if (food >= FOOD_COST_FARM && wood >= WOOD_COST_FARM && iron >= IRON_COST_FARM) {
			food -= FOOD_COST_FARM;
			wood -= WOOD_COST_FARM;
			iron -= IRON_COST_FARM;
			farm++;
		} else {
			throw new ResourceException("Not enough resources to build a Farm.");
		}
	}

	public void newCarpentry() throws ResourceException {
		if (food >= FOOD_COST_CARPENTRY && wood >= WOOD_COST_CARPENTRY && iron >= IRON_COST_CARPENTRY) {
			food -= FOOD_COST_CARPENTRY;
			wood -= WOOD_COST_CARPENTRY;
			iron -= IRON_COST_CARPENTRY;
			carpentry++;
		} else {
			throw new ResourceException("Not enough resources to build a Carpentry.");
		}
	}

	public void newSmithy() throws ResourceException {
		if (food >= FOOD_COST_SMITHY && wood >= WOOD_COST_SMITHY && iron >= IRON_COST_SMITHY) {
			food -= FOOD_COST_SMITHY;
			wood -= WOOD_COST_SMITHY;
			iron -= IRON_COST_SMITHY;
			smithy++;
		} else {
			throw new ResourceException("Not enough resources to build a Smithy.");
		}
	}

	public void newMagicTower() throws ResourceException {
		if (food >= FOOD_COST_MAGIC_TOWER && wood >= WOOD_COST_MAGIC_TOWER && iron >= IRON_COST_MAGIC_TOWER) {
			food -= FOOD_COST_MAGIC_TOWER;
			wood -= WOOD_COST_MAGIC_TOWER;
			iron -= IRON_COST_MAGIC_TOWER;
			magicTower++;
		} else {
			throw new ResourceException("Not enough resources to build a Magic Tower.");
		}
	}

	public void newChurch() throws ResourceException, BuildingException {
		if (magicTower < 1) {
			throw new BuildingException("You need at least 1 Magic Tower to build a Church.");
		}
		if (food >= FOOD_COST_CHURCH && wood >= WOOD_COST_CHURCH && iron >= IRON_COST_CHURCH
				&& mana >= MANA_COST_CHURCH) {
			food -= FOOD_COST_CHURCH;
			wood -= WOOD_COST_CHURCH;
			iron -= IRON_COST_CHURCH;
			mana -= MANA_COST_CHURCH;
			church++;
		} else {
			throw new ResourceException("Not enough resources to build a Church.");
		}
	}

	// -------------------------------------------------------------------------
	// Technology upgrades
	// -------------------------------------------------------------------------

	public void upgradeTechnologyDefense() throws ResourceException {
		if (food >= upgradeDefenseTechnologyFoodCost && wood >= upgradeDefenseTechnologyWoodCost
				&& iron >= upgradeDefenseTechnologyIronCost) {

			food -= upgradeDefenseTechnologyFoodCost;
			wood -= upgradeDefenseTechnologyWoodCost;
			iron -= upgradeDefenseTechnologyIronCost;
			technologyDefense++;

			// Increase cost for next upgrade
			upgradeDefenseTechnologyIronCost += upgradeDefenseTechnologyIronCost
					* (UPGRADE_PLUS_DEFENSE_TECHNOLOGY_IRON_COST / 100f);
			upgradeDefenseTechnologyWoodCost += upgradeDefenseTechnologyWoodCost
					* (UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST / 100f);
			upgradeDefenseTechnologyFoodCost += upgradeDefenseTechnologyFoodCost
					* (UPGRADE_PLUS_DEFENSE_TECHNOLOGY_FOOD_COST / 100f);

			System.out.println("Defense technology now level: " + technologyDefense);
		} else {
			throw new ResourceException("Not enough resources to upgrade Defense Technology.");
		}
	}

	public void upgradeTechnologyAttack() throws ResourceException {
		if (food >= upgradeAttackTechnologyFoodCost && wood >= upgradeAttackTechnologyWoodCost
				&& iron >= upgradeAttackTechnologyIronCost) {

			food -= upgradeAttackTechnologyFoodCost;
			wood -= upgradeAttackTechnologyWoodCost;
			iron -= upgradeAttackTechnologyIronCost;
			technologyAttack++;

			upgradeAttackTechnologyIronCost += upgradeAttackTechnologyIronCost
					* (UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST / 100f);
			upgradeAttackTechnologyWoodCost += upgradeAttackTechnologyWoodCost
					* (UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST / 100f);
			upgradeAttackTechnologyFoodCost += upgradeAttackTechnologyFoodCost
					* (UPGRADE_PLUS_ATTACK_TECHNOLOGY_FOOD_COST / 100f);

			System.out.println("Attack technology now level: " + technologyAttack);
		} else {
			throw new ResourceException("Not enough resources to upgrade Attack Technology.");
		}
	}

	// -------------------------------------------------------------------------
	// Helper — calculate stat (armor or damage) with technology bonus
	// formula: base + (techLevel * plusPct * base) / 100
	// -------------------------------------------------------------------------
	private int calcStat(int base, int techLevel, int plusPct) {
		return base + (techLevel * plusPct * base) / 100;
	}

	// -------------------------------------------------------------------------
	// Helper — create n units of any type, spending resources, adding to army slot
	// -------------------------------------------------------------------------
	private void createUnits(int n, int slot, int foodCost, int woodCost, int ironCost, int manaCost,
			UnitFactory factory, String unitName) throws ResourceException {
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (food >= foodCost && wood >= woodCost && iron >= ironCost && mana >= manaCost) {
				food -= foodCost;
				wood -= woodCost;
				iron -= ironCost;
				mana -= manaCost;
				army[slot].add(factory.create());
				added++;
			} else {
				System.out.println("Added " + added + " " + unitName + "(s).");
				throw new ResourceException("Not enough resources to add more " + unitName + "s. Added: " + added);
			}
		}
	}

	// Simple functional interface so createUnits() can call any constructor
	@FunctionalInterface
	private interface UnitFactory {
		MilitaryUnit create();
	}

	// -------------------------------------------------------------------------
	// New unit methods — Attack
	// -------------------------------------------------------------------------

	public void newSwordsman(int n) throws ResourceException {
		int armor = calcStat(ARMOR_SWORDSMAN, technologyDefense, PLUS_ARMOR_SWORDSMAN_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_SWORDSMAN, technologyAttack, PLUS_ATTACK_SWORDSMAN_BY_TECHNOLOGY);
		createUnits(n, 0, FOOD_COST_SWORDSMAN, WOOD_COST_SWORDSMAN, IRON_COST_SWORDSMAN, MANA_COST_SWORDSMAN,
				() -> new Swordsman(armor, damage), "Swordsman");
	}

	public void newSpearman(int n) throws ResourceException {
		int armor = calcStat(ARMOR_SPEARMAN, technologyDefense, PLUS_ARMOR_SPEARMAN_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_SPEARMAN, technologyAttack, PLUS_ATTACK_SPEARMAN_BY_TECHNOLOGY);
		createUnits(n, 1, FOOD_COST_SPEARMAN, WOOD_COST_SPEARMAN, IRON_COST_SPEARMAN, MANA_COST_SPEARMAN,
				() -> new Spearman(armor, damage), "Spearman");
	}

	public void newCrossbow(int n) throws ResourceException {
		int armor = calcStat(ARMOR_CROSSBOW, technologyDefense, PLUS_ARMOR_CROSSBOW_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_CROSSBOW, technologyAttack, PLUS_ATTACK_CROSSBOW_BY_TECHNOLOGY);
		createUnits(n, 2, FOOD_COST_CROSSBOW, WOOD_COST_CROSSBOW, IRON_COST_CROSSBOW, MANA_COST_CROSSBOW,
				() -> new Crossbow(armor, damage), "Crossbow");
	}

	public void newCannon(int n) throws ResourceException {
		int armor = calcStat(ARMOR_CANNON, technologyDefense, PLUS_ARMOR_CANNON_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_CANNON, technologyAttack, PLUS_ATTACK_CANNON_BY_TECHNOLOGY);
		createUnits(n, 3, FOOD_COST_CANNON, WOOD_COST_CANNON, IRON_COST_CANNON, MANA_COST_CANNON,
				() -> new Cannon(armor, damage), "Cannon");
	}

	// -------------------------------------------------------------------------
	// New unit methods — Defense
	// -------------------------------------------------------------------------

	public void newArrowTower(int n) throws ResourceException {
		int armor = calcStat(ARMOR_ARROWTOWER, technologyDefense, PLUS_ARMOR_ARROWTOWER_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_ARROWTOWER, technologyAttack, PLUS_ATTACK_ARROWTOWER_BY_TECHNOLOGY);
		createUnits(n, 4, FOOD_COST_ARROWTOWER, WOOD_COST_ARROWTOWER, IRON_COST_ARROWTOWER, MANA_COST_ARROWTOWER,
				() -> new ArrowTower(armor, damage), "Arrow Tower");
	}

	public void newCatapult(int n) throws ResourceException {
		int armor = calcStat(ARMOR_CATAPULT, technologyDefense, PLUS_ARMOR_CATAPULT_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_CATAPULT, technologyAttack, PLUS_ATTACK_CATAPULT_BY_TECHNOLOGY);
		createUnits(n, 5, FOOD_COST_CATAPULT, WOOD_COST_CATAPULT, IRON_COST_CATAPULT, MANA_COST_CATAPULT,
				() -> new Catapult(armor, damage), "Catapult");
	}

	public void newRocketLauncher(int n) throws ResourceException {
		int armor = calcStat(ARMOR_ROCKETLAUNCHERTOWER, technologyDefense,
				PLUS_ARMOR_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY);
		int damage = calcStat(BASE_DAMAGE_ROCKETLAUNCHERTOWER, technologyAttack,
				PLUS_ATTACK_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY);
		createUnits(n, 6, FOOD_COST_ROCKETLAUNCHERTOWER, WOOD_COST_ROCKETLAUNCHERTOWER, IRON_COST_ROCKETLAUNCHERTOWER,
				MANA_COST_ROCKETLAUNCHERTOWER, () -> new RocketLauncherTower(armor, damage), "Rocket Launcher Tower");
	}

	// -------------------------------------------------------------------------
	// New unit methods — Special
	// -------------------------------------------------------------------------

	public void newMagician(int n) throws ResourceException, BuildingException {
		if (magicTower < 1) {
			throw new BuildingException("You need at least 1 Magic Tower to create Magicians.");
		}
		int damage = calcStat(BASE_DAMAGE_MAGICIAN, technologyAttack, PLUS_ATTACK_MAGICIAN_BY_TECHNOLOGY);
		createUnits(n, 7, FOOD_COST_MAGICIAN, WOOD_COST_MAGICIAN, IRON_COST_MAGICIAN, MANA_COST_MAGICIAN,
				() -> new Magician(0, damage), "Magician");
	}

	public void newPriest(int n) throws ResourceException, BuildingException {
		if (church < 1) {
			throw new BuildingException("You need at least 1 Church to create Priests.");
		}
		if (army[7].isEmpty()) { // needs Magician / MagicTower indirectly via church
			// Church already requires MagicTower, so no extra check needed
		}
		// Max priests == number of churches
		int currentPriests = army[8].size();
		int canAdd = church - currentPriests;
		if (canAdd <= 0) {
			throw new BuildingException("You need more Churches to have more Priests (1 Church per Priest).");
		}
		int toAdd = Math.min(n, canAdd);
		createUnits(toAdd, 8, FOOD_COST_PRIEST, WOOD_COST_PRIEST, IRON_COST_PRIEST, MANA_COST_PRIEST,
				() -> new Priest(0, 0), "Priest");
		if (toAdd < n) {
			throw new BuildingException("Only " + toAdd + " Priest(s) added. Build more Churches to train more.");
		}
	}

	// -------------------------------------------------------------------------
	// Sanctify / desanctify all units
	// -------------------------------------------------------------------------

	public void sanctifyArmy(boolean sanctified) {
		for (int i = 0; i < 7; i++) { // Attack + Defense slots (not special units)
			for (MilitaryUnit unit : army[i]) {
				if (unit instanceof AttackUnit)
					((AttackUnit) unit).setSanctified(sanctified);
				if (unit instanceof DefenseUnit)
					((DefenseUnit) unit).setSanctified(sanctified);
			}
		}
	}

	// -------------------------------------------------------------------------
	// Utility
	// -------------------------------------------------------------------------

	/**
	 * Returns the army subarray containing only attack units (slots 0-3), used in
	 * Battle.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<MilitaryUnit>[] getAttackerArmy() {
		ArrayList<MilitaryUnit>[] attackerArmy = new ArrayList[9];
		for (int i = 0; i < 9; i++) {
			attackerArmy[i] = (i < 4) ? army[i] : new ArrayList<>();
		}
		return attackerArmy;
	}

	public void resetArmyArmor() {
		for (ArrayList<MilitaryUnit> slot : army) {
			for (MilitaryUnit unit : slot) {
				unit.resetArmor();
			}
		}
	}

	public int getTotalTroops() {
		int total = 0;
		for (ArrayList<MilitaryUnit> slot : army)
			total += slot.size();
		return total;
	}

	// Calculates the escalating upgrade cost for a technology at a given level
	public int calculateUpgradeTechnologyCost(int baseCost, int percentage, int targetLevel) {
		int cost = baseCost;
		for (int i = 1; i < targetLevel; i++) {
			cost += cost * percentage / 100;
		}
		return cost;
	}

	// Resource generation per tick (for ResourceTimer)
	public int getFoodGenerated() {
		return CIVILIZATION_FOOD_GENERATED + farm * CIVILIZATION_FOOD_GENERATED_PER_FARM;
	}

	public int getWoodGenerated() {
		return CIVILIZATION_WOOD_GENERATED + carpentry * CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY;
	}

	public int getIronGenerated() {
		return CIVILIZATION_IRON_GENERATED + smithy * CIVILIZATION_IRON_GENERATED_PER_SMITHY;
	}

	public int getManaGenerated() {
		return magicTower * CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER;
	}

	// -------------------------------------------------------------------------
	// Battle reports
	// -------------------------------------------------------------------------

	public void addBattleReport(Battle report) {
		// Shift all reports one position forward, keeping the last 5
		for (int i = battleReports.length - 1; i > 0; i--) {
			battleReports[i] = battleReports[i - 1];
		}
		battleReports[0] = report;
	}

	public Battle[] getBattleReports() {
		return battleReports;
	}

	public Battle getBattleReport(int n) {
		return battleReports[n];
	}

	// -------------------------------------------------------------------------
	// printStats
	// -------------------------------------------------------------------------

	public void printStats() {
		System.out.println("\n ***************************CIVILIZATION STATS*************************** ");
		System.out.printf("%-30s%s%n", "--------------------------------------------------TECHNOLOGY",
				"----------------------------------------");
		System.out.printf("  %-20s%s%n", "Attack", "Defense");
		System.out.printf("  %-20d%d%n", technologyAttack, technologyDefense);

		System.out.printf("%-30s%s%n", "---------------------------------------------------BUILDINGS",
				"----------------------------------------");
		System.out.printf("  %-12s%-12s%-12s%-14s%s%n", "Farm", "Smithy", "Carpentry", "Magic Tower", "Church");
		System.out.printf("  %-12d%-12d%-12d%-14d%d%n", farm, smithy, carpentry, magicTower, church);

		System.out.printf("%-30s%s%n", "----------------------------------------------------DEFENSES",
				"----------------------------------------");
		System.out.printf("  %-14s%-14s%s%n", "Arrow Tower", "Catapult", "Rocket Launcher");
		System.out.printf("  %-14d%-14d%d%n", army[4].size(), army[5].size(), army[6].size());

		System.out.printf("%-30s%s%n", "------------------------------------------------ATTACK UNITS",
				"----------------------------------------");
		System.out.printf("  %-14s%-14s%-14s%s%n", "Swordsman", "Spearman", "Crossbow", "Cannon");
		System.out.printf("  %-14d%-14d%-14d%d%n", army[0].size(), army[1].size(), army[2].size(), army[3].size());

		System.out.printf("%-30s%s%n", "----------------------------------------------SPECIAL UNITS",
				"----------------------------------------");
		System.out.printf("  %-14s%s%n", "Magician", "Priest");
		System.out.printf("  %-14d%d%n", army[7].size(), army[8].size());

		System.out.printf("%-30s%s%n", "---------------------------------------------------RESOURCES",
				"----------------------------------------");
		System.out.printf("  %-14s%-14s%-14s%s%n", "Food", "Wood", "Iron", "Mana");
		System.out.printf("  %-14d%-14d%-14d%d%n", food, wood, iron, mana);

		System.out.printf("%-30s%s%n", "----------------------------------------GENERATION RESOURCES",
				"----------------------------------------");
		System.out.printf("  %-14s%-14s%-14s%s%n", "Food", "Wood", "Iron", "Mana");
		System.out.printf("  %-14d%-14d%-14d%d%n", getFoodGenerated(), getWoodGenerated(), getIronGenerated(),
				getManaGenerated());
		System.out.println();
	}

	// -------------------------------------------------------------------------
	// Getters & Setters
	// -------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTechnologyDefense() {
		return technologyDefense;
	}

	public void setTechnologyDefense(int v) {
		this.technologyDefense = v;
	}

	public int getTechnologyAttack() {
		return technologyAttack;
	}

	public void setTechnologyAttack(int v) {
		this.technologyAttack = v;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getFarm() {
		return farm;
	}

	public void setFarm(int farm) {
		this.farm = farm;
	}

	public int getCarpentry() {
		return carpentry;
	}

	public void setCarpentry(int carpentry) {
		this.carpentry = carpentry;
	}

	public int getSmithy() {
		return smithy;
	}

	public void setSmithy(int smithy) {
		this.smithy = smithy;
	}

	public int getMagicTower() {
		return magicTower;
	}

	public void setMagicTower(int magicTower) {
		this.magicTower = magicTower;
	}

	public int getChurch() {
		return church;
	}

	public void setChurch(int church) {
		this.church = church;
	}

	public int getNBattles() {
		return nBattles;
	}

	public void setNBattles(int nBattles) {
		this.nBattles = nBattles;
	}

	public boolean isActiveThreat() {
		return isActiveThreat;
	}

	public void setActiveThreat(boolean v) {
		this.isActiveThreat = v;
	}

	public Battle getCurrentThreat() {
		return currentThreat;
	}

	public void setCurrentThreat(Battle b) {
		this.currentThreat = b;
	}

	public ArrayList<MilitaryUnit>[] getArmy() {
		return army;
	}

	public void setArmy(ArrayList<MilitaryUnit>[] army) {
		this.army = army;
	}

	public int getUpgradeDefenseTechnologyIronCost() {
		return upgradeDefenseTechnologyIronCost;
	}

	public int getUpgradeAttackTechnologyIronCost() {
		return upgradeAttackTechnologyIronCost;
	}

	@Override
	public String toString() {
		return "Civilization{name='" + name + "', tDefense=" + technologyDefense + ", tAttack=" + technologyAttack
				+ ", food=" + food + ", wood=" + wood + ", iron=" + iron + ", mana=" + mana + ", battles=" + nBattles
				+ "}";
	}
}