package programacio;

import java.util.ArrayList;

public class Civilization implements Variables {

	private ArrayList<MilitaryUnit>[] army;
	private String name;
	private int technologyDefense;
	private int technologyAttack;
	private int food;
	private int wood;
	private int iron;
	private int mana;
	private int farm;
	private int carpentry;
	private int smithy;
	private int magicTower;
	private int church;
	private int upgradeDefenseTechnologyIronCost;
	private int upgradeDefenseTechnologyWoodCost;
	private int upgradeDefenseTechnologyFoodCost;
	private int upgradeAttackTechnologyIronCost;
	private int upgradeAttackTechnologyWoodCost;
	private int upgradeAttackTechnologyFoodCost;
	private int battles;
	private boolean isActiveThreat;
	private Battle currentThreat;
	private Battle[] battleReports;


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

		this.battles = 0;
		this.isActiveThreat = false;
		this.currentThreat = null;
		this.battleReports = new Battle[5];

		this.army = new ArrayList[9];
		for (int i = 0; i < 9; i++) {
			army[i] = new ArrayList<MilitaryUnit>();
		}
	}


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


	public void upgradeTechnologyDefense() throws ResourceException {
		if (food >= upgradeDefenseTechnologyFoodCost && wood >= upgradeDefenseTechnologyWoodCost
				&& iron >= upgradeDefenseTechnologyIronCost) {

			food -= upgradeDefenseTechnologyFoodCost;
			wood -= upgradeDefenseTechnologyWoodCost;
			iron -= upgradeDefenseTechnologyIronCost;
			technologyDefense++;

			upgradeDefenseTechnologyIronCost += upgradeDefenseTechnologyIronCost
					* UPGRADE_PLUS_DEFENSE_TECHNOLOGY_IRON_COST / 100;
			upgradeDefenseTechnologyWoodCost += upgradeDefenseTechnologyWoodCost
					* UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST / 100;
			upgradeDefenseTechnologyFoodCost += upgradeDefenseTechnologyFoodCost
					* UPGRADE_PLUS_DEFENSE_TECHNOLOGY_FOOD_COST / 100;

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
					* UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST / 100;
			upgradeAttackTechnologyWoodCost += upgradeAttackTechnologyWoodCost
					* UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST / 100;
			upgradeAttackTechnologyFoodCost += upgradeAttackTechnologyFoodCost
					* UPGRADE_PLUS_ATTACK_TECHNOLOGY_FOOD_COST / 100;

			System.out.println("Attack technology now level: " + technologyAttack);
		} else {
			throw new ResourceException("Not enough resources to upgrade Attack Technology.");
		}
	}


	public void newSwordsman(int n) throws ResourceException {
		int armor = ARMOR_SWORDSMAN + (technologyDefense * PLUS_ARMOR_SWORDSMAN_BY_TECHNOLOGY * ARMOR_SWORDSMAN) / 100;
		int damage = BASE_DAMAGE_SWORDSMAN
				+ (technologyAttack * PLUS_ATTACK_SWORDSMAN_BY_TECHNOLOGY * BASE_DAMAGE_SWORDSMAN) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (food >= FOOD_COST_SWORDSMAN && wood >= WOOD_COST_SWORDSMAN && iron >= IRON_COST_SWORDSMAN) {
				food -= FOOD_COST_SWORDSMAN;
				wood -= WOOD_COST_SWORDSMAN;
				iron -= IRON_COST_SWORDSMAN;
				army[0].add(new Swordsman(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Swordsman(s).");
				throw new ResourceException("Not enough resources to add more Swordsmen. Added: " + added);
			}
		}
	}

	public void newSpearman(int n) throws ResourceException {
		int armor = ARMOR_SPEARMAN + (technologyDefense * PLUS_ARMOR_SPEARMAN_BY_TECHNOLOGY * ARMOR_SPEARMAN) / 100;
		int damage = BASE_DAMAGE_SPEARMAN
				+ (technologyAttack * PLUS_ATTACK_SPEARMAN_BY_TECHNOLOGY * BASE_DAMAGE_SPEARMAN) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (food >= FOOD_COST_SPEARMAN && wood >= WOOD_COST_SPEARMAN && iron >= IRON_COST_SPEARMAN) {
				food -= FOOD_COST_SPEARMAN;
				wood -= WOOD_COST_SPEARMAN;
				iron -= IRON_COST_SPEARMAN;
				army[1].add(new Spearman(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Spearman(s).");
				throw new ResourceException("Not enough resources to add more Spearmen. Added: " + added);
			}
		}
	}

	public void newCrossbow(int n) throws ResourceException {
		int armor = ARMOR_CROSSBOW + (technologyDefense * PLUS_ARMOR_CROSSBOW_BY_TECHNOLOGY * ARMOR_CROSSBOW) / 100;
		int damage = BASE_DAMAGE_CROSSBOW
				+ (technologyAttack * PLUS_ATTACK_CROSSBOW_BY_TECHNOLOGY * BASE_DAMAGE_CROSSBOW) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (wood >= WOOD_COST_CROSSBOW && iron >= IRON_COST_CROSSBOW) {
				wood -= WOOD_COST_CROSSBOW;
				iron -= IRON_COST_CROSSBOW;
				army[2].add(new Crossbow(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Crossbow(s).");
				throw new ResourceException("Not enough resources to add more Crossbows. Added: " + added);
			}
		}
	}

	public void newCannon(int n) throws ResourceException {
		int armor = ARMOR_CANNON + (technologyDefense * PLUS_ARMOR_CANNON_BY_TECHNOLOGY * ARMOR_CANNON) / 100;
		int damage = BASE_DAMAGE_CANNON
				+ (technologyAttack * PLUS_ATTACK_CANNON_BY_TECHNOLOGY * BASE_DAMAGE_CANNON) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (wood >= WOOD_COST_CANNON && iron >= IRON_COST_CANNON) {
				wood -= WOOD_COST_CANNON;
				iron -= IRON_COST_CANNON;
				army[3].add(new Cannon(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Cannon(s).");
				throw new ResourceException("Not enough resources to add more Cannons. Added: " + added);
			}
		}
	}


	public void newArrowTower(int n) throws ResourceException {
		int armor = ARMOR_ARROWTOWER
				+ (technologyDefense * PLUS_ARMOR_ARROWTOWER_BY_TECHNOLOGY * ARMOR_ARROWTOWER) / 100;
		int damage = BASE_DAMAGE_ARROWTOWER
				+ (technologyAttack * PLUS_ATTACK_ARROWTOWER_BY_TECHNOLOGY * BASE_DAMAGE_ARROWTOWER) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (wood >= WOOD_COST_ARROWTOWER) {
				wood -= WOOD_COST_ARROWTOWER;
				army[4].add(new ArrowTower(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Arrow Tower(s).");
				throw new ResourceException("Not enough resources to add more Arrow Towers. Added: " + added);
			}
		}
	}

	public void newCatapult(int n) throws ResourceException {
		int armor = ARMOR_CATAPULT + (technologyDefense * PLUS_ARMOR_CATAPULT_BY_TECHNOLOGY * ARMOR_CATAPULT) / 100;
		int damage = BASE_DAMAGE_CATAPULT
				+ (technologyAttack * PLUS_ATTACK_CATAPULT_BY_TECHNOLOGY * BASE_DAMAGE_CATAPULT) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (wood >= WOOD_COST_CATAPULT && iron >= IRON_COST_CATAPULT) {
				wood -= WOOD_COST_CATAPULT;
				iron -= IRON_COST_CATAPULT;
				army[5].add(new Catapult(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Catapult(s).");
				throw new ResourceException("Not enough resources to add more Catapults. Added: " + added);
			}
		}
	}

	public void newRocketLauncher(int n) throws ResourceException {
		int armor = ARMOR_ROCKETLAUNCHERTOWER
				+ (technologyDefense * PLUS_ARMOR_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY * ARMOR_ROCKETLAUNCHERTOWER) / 100;
		int damage = BASE_DAMAGE_ROCKETLAUNCHERTOWER
				+ (technologyAttack * PLUS_ATTACK_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY * BASE_DAMAGE_ROCKETLAUNCHERTOWER)
						/ 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (wood >= WOOD_COST_ROCKETLAUNCHERTOWER && iron >= IRON_COST_ROCKETLAUNCHERTOWER) {
				wood -= WOOD_COST_ROCKETLAUNCHERTOWER;
				iron -= IRON_COST_ROCKETLAUNCHERTOWER;
				army[6].add(new RocketLauncherTower(armor, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Rocket Launcher(s).");
				throw new ResourceException("Not enough resources to add more Rocket Launchers. Added: " + added);
			}
		}
	}


	public void newMagician(int n) throws ResourceException, BuildingException {
		if (magicTower < 1) {
			throw new BuildingException("You need at least 1 Magic Tower to create Magicians.");
		}
		int damage = BASE_DAMAGE_MAGICIAN
				+ (technologyAttack * PLUS_ATTACK_MAGICIAN_BY_TECHNOLOGY * BASE_DAMAGE_MAGICIAN) / 100;
		int added = 0;
		for (int i = 0; i < n; i++) {
			if (food >= FOOD_COST_MAGICIAN && wood >= WOOD_COST_MAGICIAN && iron >= IRON_COST_MAGICIAN
					&& mana >= MANA_COST_MAGICIAN) {
				food -= FOOD_COST_MAGICIAN;
				wood -= WOOD_COST_MAGICIAN;
				iron -= IRON_COST_MAGICIAN;
				mana -= MANA_COST_MAGICIAN;
				army[7].add(new Magician(0, damage));
				added++;
			} else {
				System.out.println("Added " + added + " Magician(s).");
				throw new ResourceException("Not enough resources to add more Magicians. Added: " + added);
			}
		}
	}

	public void newPriest(int n) throws ResourceException, BuildingException {
		if (church < 1) {
			throw new BuildingException("You need at least 1 Church to create Priests.");
		}
		int currentPriests = army[8].size();
		int canAdd = church - currentPriests;
		if (canAdd <= 0) {
			throw new BuildingException("You need more Churches to have more Priests (1 Church per Priest).");
		}
		int toAdd = n;
		if (toAdd > canAdd) {
			toAdd = canAdd;
		}
		int added = 0;
		for (int i = 0; i < toAdd; i++) {
			if (food >= FOOD_COST_PRIEST && mana >= MANA_COST_PRIEST) {
				food -= FOOD_COST_PRIEST;
				mana -= MANA_COST_PRIEST;
				army[8].add(new Priest(0, 0));
				added++;
			} else {
				System.out.println("Added " + added + " Priest(s).");
				throw new ResourceException("Not enough resources to add more Priests. Added: " + added);
			}
		}
		if (toAdd < n) {
			throw new BuildingException("Only " + toAdd + " Priest(s) added. Build more Churches to train more.");
		}
	}


	public void sanctifyArmy(boolean sanctified) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < army[i].size(); j++) {
				MilitaryUnit unit = army[i].get(j);
				if (unit instanceof AttackUnit) {
					AttackUnit au = (AttackUnit) unit;
					au.setSanctified(sanctified);
				}
				if (unit instanceof DefenseUnit) {
					DefenseUnit du = (DefenseUnit) unit;
					du.setSanctified(sanctified);
				}
			}
		}
	}


	public ArrayList<MilitaryUnit>[] getAttackerArmy() {
		ArrayList<MilitaryUnit>[] attackerArmy = new ArrayList[9];
		for (int i = 0; i < 4; i++) {
			attackerArmy[i] = army[i];
		}
		for (int i = 4; i < 9; i++) {
			attackerArmy[i] = new ArrayList<MilitaryUnit>();
		}
		return attackerArmy;
	}

	public void resetArmyArmor() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < army[i].size(); j++) {
				army[i].get(j).resetArmor();
			}
		}
	}

	public int getTotalTroops() {
		int total = 0;
		for (int i = 0; i < 9; i++) {
			total += army[i].size();
		}
		return total;
	}

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


	public void addBattleReport(Battle report) {
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


	public void printStats() {
		System.out.println("\n ***************************CIVILIZATION STATS*************************** ");
		System.out.println(
				"--------------------------------------------------TECHNOLOGY----------------------------------------");
		System.out.println("  Attack       Defense");
		System.out.println("  " + technologyAttack + "            " + technologyDefense);

		System.out.println(
				"---------------------------------------------------BUILDINGS----------------------------------------");
		System.out.println("  Farm    Smithy  Carpentry  Magic Tower  Church");
		System.out.println("  " + farm + "       " + smithy + "       " + carpentry + "          " + magicTower
				+ "            " + church);

		System.out.println(
				"----------------------------------------------------DEFENSES----------------------------------------");
		System.out.println("  Arrow Tower    Catapult    Rocket Launcher");
		System.out.println("  " + army[4].size() + "              " + army[5].size() + "           " + army[6].size());

		System.out.println(
				"------------------------------------------------ATTACK UNITS----------------------------------------");
		System.out.println("  Swordsman    Spearman    Crossbow    Cannon");
		System.out.println("  " + army[0].size() + "            " + army[1].size() + "           " + army[2].size()
				+ "          " + army[3].size());

		System.out.println(
				"----------------------------------------------SPECIAL UNITS----------------------------------------");
		System.out.println("  Magician    Priest");
		System.out.println("  " + army[7].size() + "           " + army[8].size());

		System.out.println(
				"---------------------------------------------------RESOURCES----------------------------------------");
		System.out.println("  Food         Wood         Iron         Mana");
		System.out.println("  " + food + "     " + wood + "     " + iron + "     " + mana);

		System.out.println(
				"----------------------------------------GENERATION RESOURCES----------------------------------------");
		System.out.println("  Food         Wood         Iron         Mana");
		System.out.println("  " + getFoodGenerated() + "     " + getWoodGenerated() + "     " + getIronGenerated()
				+ "     " + getManaGenerated());
		System.out.println();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTechnologyDefense() {
		return technologyDefense;
	}

	public void setTechnologyDefense(int technologyDefense) {
		this.technologyDefense = technologyDefense;
	}

	public int getTechnologyAttack() {
		return technologyAttack;
	}

	public void setTechnologyAttack(int technologyAttack) {
		this.technologyAttack = technologyAttack;
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
		return battles;
	}

	public void setNBattles(int nBattles) {
		this.battles = nBattles;
	}

	public boolean isActiveThreat() {
		return isActiveThreat;
	}

	public void setActiveThreat(boolean isActiveThreat) {
		this.isActiveThreat = isActiveThreat;
	}

	public Battle getCurrentThreat() {
		return currentThreat;
	}

	public void setCurrentThreat(Battle currentThreat) {
		this.currentThreat = currentThreat;
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

	public String toString() {
		return "Civilization [name=" + name + ", tDefense=" + technologyDefense + ", tAttack=" + technologyAttack
				+ ", food=" + food + ", wood=" + wood + ", iron=" + iron + ", mana=" + mana + ", battles=" + battles
				+ "]";
	}
}