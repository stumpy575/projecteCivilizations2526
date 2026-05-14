package programacio;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    static Civilization civilization;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to Civilizations!");
        System.out.println("Enter your civilization name: ");
        String name = scanner.nextLine();

        civilization = new Civilization(name, 500000, 300000, 100000, 0);

        startResourceTimer();
        startThreatTimer();

        int choice = -1;
        while (choice != 0) {
            printMainMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    civilization.printStats();
                    break;
                case 2:
                    buildMenu();
                    break;
                case 3:
                    recruitMenu();
                    break;
                case 4:
                    technologyMenu();
                    break;
                case 5:
                    viewThreat();
                    break;
                case 6:
                    viewBattleReports();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Menus
    // -------------------------------------------------------------------------

    static void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1) View Civilization Stats");
        System.out.println("2) Build");
        System.out.println("3) Recruit Units");
        System.out.println("4) Upgrade Technology");
        if (civilization.isActiveThreat()) {
            System.out.println("5) View Incoming Threat  <<< ENEMY APPROACHING!");
        } else {
            System.out.println("5) View Incoming Threat");
        }
        System.out.println("6) View Battle Reports");
        System.out.println("0) Exit");
        System.out.print("Option > ");
    }

    static void buildMenu() {
        System.out.println("\n--- BUILD ---");
        System.out.println("1) Farm       (Food +" + Variables.CIVILIZATION_FOOD_GENERATED_PER_FARM + "/tick)");
        System.out.println("2) Carpentry  (Wood +" + Variables.CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY + "/tick)");
        System.out.println("3) Smithy     (Iron +" + Variables.CIVILIZATION_IRON_GENERATED_PER_SMITHY + "/tick)");
        System.out.println("4) Magic Tower (Mana +" + Variables.CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER + "/tick, enables Magicians & Churches)");
        System.out.println("5) Church     (enables 1 Priest slot)");
        System.out.println("0) Back");
        System.out.print("Option > ");
        int choice = scanner.nextInt();
        try {
            switch (choice) {
                case 1:
                    civilization.newFarm();
                    System.out.println("Farm built!");
                    break;
                case 2:
                    civilization.newCarpentry();
                    System.out.println("Carpentry built!");
                    break;
                case 3:
                    civilization.newSmithy();
                    System.out.println("Smithy built!");
                    break;
                case 4:
                    civilization.newMagicTower();
                    System.out.println("Magic Tower built!");
                    break;
                case 5:
                    civilization.newChurch();
                    System.out.println("Church built!");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } catch (ResourceException e) {
            System.out.println("Not enough resources: " + e.getMessage());
        } catch (BuildingException e) {
            System.out.println("Building requirement not met: " + e.getMessage());
        }
    }

    static void recruitMenu() {
        System.out.println("\n--- RECRUIT UNITS ---");
        System.out.println("-- Attack --");
        System.out.println("1) Swordsman  (Food: " + Variables.FOOD_COST_SWORDSMAN + " Wood: " + Variables.WOOD_COST_SWORDSMAN + " Iron: " + Variables.IRON_COST_SWORDSMAN + ")");
        System.out.println("2) Spearman   (Food: " + Variables.FOOD_COST_SPEARMAN  + " Wood: " + Variables.WOOD_COST_SPEARMAN  + " Iron: " + Variables.IRON_COST_SPEARMAN  + ")");
        System.out.println("3) Crossbow   (Wood: " + Variables.WOOD_COST_CROSSBOW  + " Iron: " + Variables.IRON_COST_CROSSBOW  + ")");
        System.out.println("4) Cannon     (Wood: " + Variables.WOOD_COST_CANNON    + " Iron: " + Variables.IRON_COST_CANNON    + ")");
        System.out.println("-- Defense --");
        System.out.println("5) Arrow Tower       (Wood: " + Variables.WOOD_COST_ARROWTOWER + ")");
        System.out.println("6) Catapult          (Wood: " + Variables.WOOD_COST_CATAPULT   + " Iron: " + Variables.IRON_COST_CATAPULT + ")");
        System.out.println("7) Rocket Launcher   (Wood: " + Variables.WOOD_COST_ROCKETLAUNCHERTOWER + " Iron: " + Variables.IRON_COST_ROCKETLAUNCHERTOWER + ")");
        System.out.println("-- Special --");
        System.out.println("8) Magician  (Food: " + Variables.FOOD_COST_MAGICIAN + " Mana: " + Variables.MANA_COST_MAGICIAN + ")");
        System.out.println("9) Priest    (Food: " + Variables.FOOD_COST_PRIEST   + " Mana: " + Variables.MANA_COST_PRIEST   + ")");
        System.out.println("0) Back");
        System.out.print("Option > ");
        int choice = scanner.nextInt();
        if (choice == 0) return;

        System.out.print("Amount: ");
        int amount = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    civilization.newSwordsman(amount);
                    System.out.println(amount + " Swordsman(s) recruited.");
                    break;
                case 2:
                    civilization.newSpearman(amount);
                    System.out.println(amount + " Spearman(s) recruited.");
                    break;
                case 3:
                    civilization.newCrossbow(amount);
                    System.out.println(amount + " Crossbow(s) recruited.");
                    break;
                case 4:
                    civilization.newCannon(amount);
                    System.out.println(amount + " Cannon(s) recruited.");
                    break;
                case 5:
                    civilization.newArrowTower(amount);
                    System.out.println(amount + " Arrow Tower(s) built.");
                    break;
                case 6:
                    civilization.newCatapult(amount);
                    System.out.println(amount + " Catapult(s) built.");
                    break;
                case 7:
                    civilization.newRocketLauncher(amount);
                    System.out.println(amount + " Rocket Launcher(s) built.");
                    break;
                case 8:
                    civilization.newMagician(amount);
                    System.out.println(amount + " Magician(s) recruited.");
                    break;
                case 9:
                    civilization.newPriest(amount);
                    System.out.println(amount + " Priest(s) recruited.");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } catch (ResourceException e) {
            System.out.println("Not enough resources: " + e.getMessage());
        } catch (BuildingException e) {
            System.out.println("Building requirement not met: " + e.getMessage());
        }
    }

    static void technologyMenu() {
        System.out.println("\n--- UPGRADE TECHNOLOGY ---");
        System.out.println("Current Defense Technology: " + civilization.getTechnologyDefense());
        System.out.println("Current Attack Technology:  " + civilization.getTechnologyAttack());
        System.out.println("Next Defense upgrade cost -> Iron: " + civilization.getUpgradeDefenseTechnologyIronCost());
        System.out.println("Next Attack  upgrade cost -> Iron: " + civilization.getUpgradeAttackTechnologyIronCost());
        System.out.println("1) Upgrade Defense Technology");
        System.out.println("2) Upgrade Attack Technology");
        System.out.println("0) Back");
        System.out.print("Option > ");
        int choice = scanner.nextInt();
        try {
            switch (choice) {
                case 1:
                    civilization.upgradeTechnologyDefense();
                    System.out.println("Defense Technology upgraded to level " + civilization.getTechnologyDefense());
                    break;
                case 2:
                    civilization.upgradeTechnologyAttack();
                    System.out.println("Attack Technology upgraded to level " + civilization.getTechnologyAttack());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } catch (ResourceException e) {
            System.out.println("Not enough resources: " + e.getMessage());
        }
    }

    static void viewThreat() {
        if (civilization.isActiveThreat()) {
            System.out.println(civilization.getCurrentThreat().getEnemyStats());
        } else {
            System.out.println("No incoming threat at the moment.");
        }
    }

    static void viewBattleReports() {
        System.out.println("\n--- BATTLE REPORTS ---");
        Battle[] reports = civilization.getBattleReports();
        boolean anyReport = false;
        for (int i = 0; i < reports.length; i++) {
            if (reports[i] != null) {
                System.out.println(i + ") Battle #" + reports[i].getBattleNumber());
                anyReport = true;
            }
        }
        if (!anyReport) {
            System.out.println("No battles yet.");
            return;
        }
        System.out.println("Select report (0 to go back): ");
        System.out.print("Option > ");
        int choice = scanner.nextInt();
        if (choice == 0) return;
        if (choice < 0 || choice >= reports.length || reports[choice] == null) {
            System.out.println("Invalid option.");
            return;
        }
        System.out.println(reports[choice].getBattleReport());
        System.out.println("View battle development? (1 = yes, 0 = no)");
        System.out.print("Option > ");
        if (scanner.nextInt() == 1) {
            System.out.println(reports[choice].getBattleDevelopment());
        }
    }

    // -------------------------------------------------------------------------
    // Create enemy army
    // Slots: [0]Swordsman [1]Spearman [2]Crossbow [3]Cannon [4-8] empty
    // Probabilities: Swordsman 35%, Spearman 25%, Crossbow 20%, Cannon 20%
    // Resources scale with number of battles
    // -------------------------------------------------------------------------

    public static ArrayList<MilitaryUnit>[] createEnemyArmy() {
        ArrayList<MilitaryUnit>[] army = new ArrayList[9];
        for (int i = 0; i < 9; i++) {
            army[i] = new ArrayList<MilitaryUnit>();
        }

        int food = FOOD_BASE_ENEMY_ARMY + (civilization.getNBattles() * FOOD_BASE_ENEMY_ARMY * ENEMY_FLEET_INCREASE / 100);
        int wood = WOOD_BASE_ENEMY_ARMY + (civilization.getNBattles() * WOOD_BASE_ENEMY_ARMY * ENEMY_FLEET_INCREASE / 100);
        int iron = IRON_BASE_ENEMY_ARMY + (civilization.getNBattles() * IRON_BASE_ENEMY_ARMY * ENEMY_FLEET_INCREASE / 100);

        // Keep creating units while we can afford the cheapest one (Swordsman)
        while (food >= FOOD_COST_SWORDSMAN && wood >= WOOD_COST_SWORDSMAN && iron >= IRON_COST_SWORDSMAN) {
            int r = (int)(Math.random() * 100);

            if (r < 35) {
                // Swordsman — 35%
                army[0].add(new Swordsman());
                food -= FOOD_COST_SWORDSMAN;
                wood -= WOOD_COST_SWORDSMAN;
                iron -= IRON_COST_SWORDSMAN;

            } else if (r < 60) {
                // Spearman — 25%
                if (food >= FOOD_COST_SPEARMAN && wood >= WOOD_COST_SPEARMAN && iron >= IRON_COST_SPEARMAN) {
                    army[1].add(new Spearman());
                    food -= FOOD_COST_SPEARMAN;
                    wood -= WOOD_COST_SPEARMAN;
                    iron -= IRON_COST_SPEARMAN;
                }

            } else if (r < 80) {
                // Crossbow — 20%
                if (wood >= WOOD_COST_CROSSBOW && iron >= IRON_COST_CROSSBOW) {
                    army[2].add(new Crossbow());
                    wood -= WOOD_COST_CROSSBOW;
                    iron -= IRON_COST_CROSSBOW;
                }

            } else {
                // Cannon — 20%
                if (wood >= WOOD_COST_CANNON && iron >= IRON_COST_CANNON) {
                    army[3].add(new Cannon());
                    wood -= WOOD_COST_CANNON;
                    iron -= IRON_COST_CANNON;
                }
            }
        }

        return army;
    }

    // -------------------------------------------------------------------------
    // Timers
    // -------------------------------------------------------------------------

    static void startResourceTimer() {
        TimerTask resourceTask = new TimerTask() {
            public void run() {
                civilization.setFood(civilization.getFood() + civilization.getFoodGenerated());
                civilization.setWood(civilization.getWood() + civilization.getWoodGenerated());
                civilization.setIron(civilization.getIron() + civilization.getIronGenerated());
                civilization.setMana(civilization.getMana() + civilization.getManaGenerated());
                System.out.println("\n[Resources updated] Food: " + civilization.getFood()
                        + " Wood: " + civilization.getWood()
                        + " Iron: " + civilization.getIron()
                        + " Mana: " + civilization.getMana());
            }
        };
        Timer timer = new Timer();
        timer.schedule(resourceTask, 60000, 60000); // every 60 seconds
    }

    static void startThreatTimer() {
        TimerTask threatTask = new TimerTask() {
            public void run() {
                if (civilization.getTotalTroops() == 0) {
                    System.out.println("\n[WARNING] No troops! Enemy skipped this round.");
                    return;
                }

                System.out.println("\n[ALERT] Enemy army approaching!");
                ArrayList<MilitaryUnit>[] enemyArmy = createEnemyArmy();
                int battleNumber = civilization.getNBattles() + 1;
                Battle battle = new Battle(civilization, enemyArmy, battleNumber);

                civilization.setActiveThreat(true);
                civilization.setCurrentThreat(battle);

                System.out.println(battle.getEnemyStats());
                System.out.println("[Battle starting...]");

                String report = battle.runBattle(civilization);

                civilization.setNBattles(battleNumber);
                civilization.addBattleReport(battle);
                civilization.setActiveThreat(false);
                civilization.setCurrentThreat(null);
                civilization.resetArmyArmor();

                if (battle.isCivilizationWon()) {
                    civilization.setWood(civilization.getWood() + battle.getWasteWood());
                    civilization.setIron(civilization.getIron() + battle.getWasteIron());
                    System.out.println("[Battle won! Rubble collected: Wood " + battle.getWasteWood() + " Iron " + battle.getWasteIron() + "]");
                } else {
                    System.out.println("[Battle lost!]");
                }
                System.out.println(report);
            }
        };
        Timer timer = new Timer();
        timer.schedule(threatTask, 180000, 180000); // every 3 minutes
    }

    // Needed so createEnemyArmy() can access Variables constants without an instance
    static final int FOOD_BASE_ENEMY_ARMY    = Variables.FOOD_BASE_ENEMY_ARMY;
    static final int WOOD_BASE_ENEMY_ARMY    = Variables.WOOD_BASE_ENEMY_ARMY;
    static final int IRON_BASE_ENEMY_ARMY    = Variables.IRON_BASE_ENEMY_ARMY;
    static final int ENEMY_FLEET_INCREASE    = Variables.ENEMY_FLEET_INCREASE;
    static final int FOOD_COST_SWORDSMAN     = Variables.FOOD_COST_SWORDSMAN;
    static final int WOOD_COST_SWORDSMAN     = Variables.WOOD_COST_SWORDSMAN;
    static final int IRON_COST_SWORDSMAN     = Variables.IRON_COST_SWORDSMAN;
    static final int FOOD_COST_SPEARMAN      = Variables.FOOD_COST_SPEARMAN;
    static final int WOOD_COST_SPEARMAN      = Variables.WOOD_COST_SPEARMAN;
    static final int IRON_COST_SPEARMAN      = Variables.IRON_COST_SPEARMAN;
    static final int WOOD_COST_CROSSBOW      = Variables.WOOD_COST_CROSSBOW;
    static final int IRON_COST_CROSSBOW      = Variables.IRON_COST_CROSSBOW;
    static final int WOOD_COST_CANNON        = Variables.WOOD_COST_CANNON;
    static final int IRON_COST_CANNON        = Variables.IRON_COST_CANNON;
}