package classes;

import GUI.*;
import ddbb.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Battle {
    private ArrayList<MilitaryUnit>[] civilizationArmy; // → para almacenar el ejército de nuestra civilización
    private ArrayList<MilitaryUnit>[] enemyArmy; // → para almacenar el ejército enemigo
    private ArrayList[][] armies; // array de ArrayList de dos filas y nueve columnas, donde almacenaremos nuestro ejército en la primera fila, y el ejército enemigo en la segunda fila
    private String battleDevelopment; // Donde guardamos todo el desarrollo de la batalla paso a paso
    private String battleReport;
    private int[][] initialCostFleet; // coste de food, wood, iron de los ejércitos iniciales
    // initialCostFleet = [[food][wood][iron],[food][wood][iron]]; 
    // donde initialCostFleet[0] costes unidades de la civilización, initialCostFleet[1] costes unidades enemigas
    // Lo necesitamos para saber las pérdidas en materiales de cada ejército.

    private int initialNumberUnitsCivilization, initialNumberUnitsEnemy;
    // La batalla se acabará cuando uno de los dos ejércitos se quede con el 20% o menos de sus
    // unidades iniciales, por tanto es necesario saber la cantidad de unidades iniciales de cada ejército.
    
    private int[] wasteWoodIron;
    // residuos generados en la batalla [wood, iron].

    private int[] enemyDrops;
    private int[] civilizationDrops; 
    // necesarios para generar reporte de batalla, y para calcular las pérdidas materiales de cada ejército.
    
    private int[][] resourcesLosses;
    // array de dos filas y cuatro columnas
    // resourcesLosses[0] = {perdidas food civilización, perdidas wood civilización, perdidas iron civilización, 
    //                        perdidas iron + 5*wood + 10*food}
    // resourcesLosses[1] lo mismo pero para el ejército enemigo.
    // Lo de multiplicar por 5 las pérdidas de wood y por 10 las de food, es debido al mayor valor del hierro.
    // Para decidir el ganador, será el que tenga el número menor en la cuarta columna (pérdidas ponderadas).
    
    private int[][] initialArmies;
    // Array de dos filas y 9 columnas. Servirá para cuantificar cada tipo de unidad de los ejércitos iniciales.
    // InitialArmies[0] serán las unidades iniciales de nuestra civilización
    // InitialArmies[1] las unidades iniciales enemigas.
    // InitialArmies[0][0] swordsman en nuestra civilización antes de iniciar batalla.
    // InitialArmies[0][1] spearman en nuestra civilización antes de iniciar batalla.
    // Este array nos ayudará a calcular los costes de las flotas iniciales y por tanto, las pérdidas.

    private boolean hasCombatStarted;
    // arrays que cuantifican las unidades actuales de cada grupo, tanto para la civilización, como para el enemigo.
    // El orden seria:
    // actualNumberUnitsCivilization[0] --> swordsman
    // actualNumberUnitsCivilization[1] --> spearman
    // actualNumberUnitsCivilization[2] --> crossbow
    // actualNumberUnitsCivilization[3] --> cannon
    // actualNumberUnitsCivilization[4] --> arrow tower
    // actualNumberUnitsCivilization[5] --> catapult
    // actualNumberUnitsCivilization[6] --> rocket launcher
    // actualNumberUnitsCivilization[7] --> magician
    // actualNumberUnitsCivilization[8] --> priest

    private int civilizationArmyPercRemaining;
    private int enemyArmyPercRemaining;
    private int attackingArmy;
    private boolean skipBattle;
    private Civilization userCivilization;
    private Civilization enemyCivilization;
    private int battleType;
    private Battle thisBattle;
    private MainScreen ms;

    // Constructor for defending civilization
    public Battle(Civilization civilization, Civilization enemyCivilization, MainPanel mp, MainScreen ms) {
        this.civilizationArmy = civilization.getArmy();
        this.enemyArmy = enemyCivilization.getAttackerArmy();
        this.initialCostFleet = new int[2][3]; // [food][wood][iron]
        this.resourcesLosses = new int[2][4]; // [food][wood][iron][weighted]
        this.hasCombatStarted = false;
        this.civilizationArmyPercRemaining = 100;
        this.enemyArmyPercRemaining = 100;
        this.ms = ms;
        skipBattle = false;
        this.userCivilization = civilization;
        this.enemyCivilization = enemyCivilization;
        this.enemyCivilization.setActiveThreat(true);
        this.battleType = 0;
        this.battleReport = "";
        thisBattle = this;
        announceCombat();
        
        TimerTask task = new TimerTask() {
            public void run() {
                updateArmies();
                initInitialArmies();
                combat(civilization, enemyCivilization, mp, battleType);
                civilization.addBattleReport(thisBattle);
                civilization.setActiveThreat(false);
                hasCombatStarted = false;

                civilization.setNBattles(civilization.getNBattles() + 1);
                mp.getMiddlePanel().changeScreenToDefaultScene();
                civilization.setCurrentThreat(null);

                System.out.println("Constructor Battle (AMENAZA EXTERNA)");
                System.out.println(civilization);

                // DDBB operations
                bbddOperations(civilization);

                // Generate battle XML
                BattleXmlGenerator.generateXml(Battle.this, false);

                // Transform battle XML to HTML
                BattleHtmlTransformator.transform(GlobalContext.num_battle);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, Time.countdownBattleTime);
    }

    // Constructor for invading enemy civilization
    public Battle(Civilization civilization, Civilization enemyCivilization, MainPanel mp, MainScreen ms, int i) { 
        // int i is only to differentiate the constructors, there's no use for it
        this.civilizationArmy = enemyCivilization.getArmy();
        this.enemyArmy = civilization.getAttackerArmy();
        this.initialCostFleet = new int[2][3];
        this.resourcesLosses = new int[2][4];
        this.hasCombatStarted = false;
        this.civilizationArmyPercRemaining = 100;
        this.enemyArmyPercRemaining = 100;
        skipBattle = false;
        this.userCivilization = civilization;
        this.enemyCivilization = enemyCivilization;
        this.enemyCivilization.setActiveThreat(true);
        this.battleType = 1;
        this.battleReport = "";
        thisBattle = this;
        announceCombat();
        
        TimerTask task = new TimerTask() {
            public void run() {
                updateArmies();
                initInitialArmies();
                civilization.setIsInvading(true);
                combat(enemyCivilization, civilization, mp, battleType);
                civilization.addBattleReport(thisBattle);
                civilization.setActiveThreat(false);
                hasCombatStarted = false;

                civilization.setNBattles(civilization.getNBattles() + 1);
                mp.getMiddlePanel().changeScreenToDefaultScene();
                civilization.setCurrentThreat(null);
                civilization.setIsInvading(false);

                System.out.println("Constructor Battle (INVASIÓN)");
                System.out.println(civilization);

                // DDBB operations
                bbddOperations(civilization);

                // Generate battle XML
                BattleXmlGenerator.generateXml(Battle.this, true);

                // Transform battle XML to HTML
                BattleHtmlTransformator.transform(GlobalContext.num_battle);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, Time.countdownBattleTime);
    }

    public void initInitialArmies() {
        initialArmies = new int[2][9];
        // 0 --> civilization
        // 1 --> enemy
        // 0-8 --> Armies (Swordsman, Spearman, Crossbow, Cannon, ArrowTower, Catapult, RocketLauncher, Magician, Priest)

        for (int i = 0; i < initialArmies.length; i++) {
            initialArmies[i] = getArrayValuesFromArrayList(armies[i]);
        }
    }

    public void updateResourceLosses() {
        // Calculate losses for civilization army (drops array contains units lost)
        resourcesLosses[0][0] += Variables.FOOD_COST_SWORDSMAN * civilizationDrops[0];
        resourcesLosses[0][0] += Variables.FOOD_COST_SPEARMAN * civilizationDrops[1];
        resourcesLosses[0][0] += Variables.FOOD_COST_CROSSBOW * civilizationDrops[2];
        resourcesLosses[0][0] += Variables.FOOD_COST_CANNON * civilizationDrops[3];
        resourcesLosses[0][0] += Variables.FOOD_COST_ARROWTOWER * civilizationDrops[4];
        resourcesLosses[0][0] += Variables.FOOD_COST_CATAPULT * civilizationDrops[5];
        resourcesLosses[0][0] += Variables.FOOD_COST_ROCKETLAUNCHERTOWER * civilizationDrops[6];
        resourcesLosses[0][0] += Variables.FOOD_COST_MAGICIAN * civilizationDrops[7];
        resourcesLosses[0][0] += Variables.FOOD_COST_PRIEST * civilizationDrops[8];

        resourcesLosses[0][1] += Variables.WOOD_COST_SWORDSMAN * civilizationDrops[0];
        resourcesLosses[0][1] += Variables.WOOD_COST_SPEARMAN * civilizationDrops[1];
        resourcesLosses[0][1] += Variables.WOOD_COST_CROSSBOW * civilizationDrops[2];
        resourcesLosses[0][1] += Variables.WOOD_COST_CANNON * civilizationDrops[3];
        resourcesLosses[0][1] += Variables.WOOD_COST_ARROWTOWER * civilizationDrops[4];
        resourcesLosses[0][1] += Variables.WOOD_COST_CATAPULT * civilizationDrops[5];
        resourcesLosses[0][1] += Variables.WOOD_COST_ROCKETLAUNCHERTOWER * civilizationDrops[6];
        resourcesLosses[0][1] += Variables.WOOD_COST_MAGICIAN * civilizationDrops[7];
        resourcesLosses[0][1] += Variables.WOOD_COST_PRIEST * civilizationDrops[8];

        resourcesLosses[0][2] += Variables.IRON_COST_SWORDSMAN * civilizationDrops[0];
        resourcesLosses[0][2] += Variables.IRON_COST_SPEARMAN * civilizationDrops[1];
        resourcesLosses[0][2] += Variables.IRON_COST_CROSSBOW * civilizationDrops[2];
        resourcesLosses[0][2] += Variables.IRON_COST_CANNON * civilizationDrops[3];
        resourcesLosses[0][2] += Variables.IRON_COST_ARROWTOWER * civilizationDrops[4];
        resourcesLosses[0][2] += Variables.IRON_COST_CATAPULT * civilizationDrops[5];
        resourcesLosses[0][2] += Variables.IRON_COST_ROCKETLAUNCHERTOWER * civilizationDrops[6];
        resourcesLosses[0][2] += Variables.IRON_COST_MAGICIAN * civilizationDrops[7];
        resourcesLosses[0][2] += Variables.IRON_COST_PRIEST * civilizationDrops[8];

        // Calculate losses for enemy army
        resourcesLosses[1][0] += Variables.FOOD_COST_SWORDSMAN * enemyDrops[0];
        resourcesLosses[1][0] += Variables.FOOD_COST_SPEARMAN * enemyDrops[1];
        resourcesLosses[1][0] += Variables.FOOD_COST_CROSSBOW * enemyDrops[2];
        resourcesLosses[1][0] += Variables.FOOD_COST_CANNON * enemyDrops[3];
        resourcesLosses[1][0] += Variables.FOOD_COST_ARROWTOWER * enemyDrops[4];
        resourcesLosses[1][0] += Variables.FOOD_COST_CATAPULT * enemyDrops[5];
        resourcesLosses[1][0] += Variables.FOOD_COST_ROCKETLAUNCHERTOWER * enemyDrops[6];
        resourcesLosses[1][0] += Variables.FOOD_COST_MAGICIAN * enemyDrops[7];
        resourcesLosses[1][0] += Variables.FOOD_COST_PRIEST * enemyDrops[8];

        resourcesLosses[1][1] += Variables.WOOD_COST_SWORDSMAN * enemyDrops[0];
        resourcesLosses[1][1] += Variables.WOOD_COST_SPEARMAN * enemyDrops[1];
        resourcesLosses[1][1] += Variables.WOOD_COST_CROSSBOW * enemyDrops[2];
        resourcesLosses[1][1] += Variables.WOOD_COST_CANNON * enemyDrops[3];
        resourcesLosses[1][1] += Variables.WOOD_COST_ARROWTOWER * enemyDrops[4];
        resourcesLosses[1][1] += Variables.WOOD_COST_CATAPULT * enemyDrops[5];
        resourcesLosses[1][1] += Variables.WOOD_COST_ROCKETLAUNCHERTOWER * enemyDrops[6];
        resourcesLosses[1][1] += Variables.WOOD_COST_MAGICIAN * enemyDrops[7];
        resourcesLosses[1][1] += Variables.WOOD_COST_PRIEST * enemyDrops[8];

        resourcesLosses[1][2] += Variables.IRON_COST_SWORDSMAN * enemyDrops[0];
        resourcesLosses[1][2] += Variables.IRON_COST_SPEARMAN * enemyDrops[1];
        resourcesLosses[1][2] += Variables.IRON_COST_CROSSBOW * enemyDrops[2];
        resourcesLosses[1][2] += Variables.IRON_COST_CANNON * enemyDrops[3];
        resourcesLosses[1][2] += Variables.IRON_COST_ARROWTOWER * enemyDrops[4];
        resourcesLosses[1][2] += Variables.IRON_COST_CATAPULT * enemyDrops[5];
        resourcesLosses[1][2] += Variables.IRON_COST_ROCKETLAUNCHERTOWER * enemyDrops[6];
        resourcesLosses[1][2] += Variables.IRON_COST_MAGICIAN * enemyDrops[7];
        resourcesLosses[1][2] += Variables.IRON_COST_PRIEST * enemyDrops[8];

        // Calculate weighted losses: iron + 5*wood + 10*food
        resourcesLosses[0][3] = resourcesLosses[0][2] + 5 * resourcesLosses[0][1] + 10 * resourcesLosses[0][0];
        resourcesLosses[1][3] = resourcesLosses[1][2] + 5 * resourcesLosses[1][1] + 10 * resourcesLosses[1][0];
    }

    public int fleetResourceCost(ArrayList<MilitaryUnit>[] army) {
        int cost = 0;
        for (int i = 0; i < army.length; i++) {
            for (int j = 0; j < army[i].size(); j++) {
                cost += army[i].get(j).getIronCost();
            }
        }
        return cost;
    }

    public int initialFleetNumber(ArrayList<MilitaryUnit>[] army) {
        int total = 0;
        for (int i = 0; i < army.length; i++) {
            total += army[i].size();
        }
        return total;
    }

    public int remainderPercentageFleet(ArrayList<MilitaryUnit>[] army) {
        int total = 0;
        for (int i = 0; i < army.length; i++) {
            total += army[i].size();
        }

        if (army.equals(armies[0])) {
            return (total * 100) / initialNumberUnitsCivilization;
        } else {
            return (total * 100) / initialNumberUnitsEnemy;
        }
    }

    public int remainderPercentageFleetCivilization() {
        int total = 0;
        for (int i = 0; i < civilizationArmy.length; i++) {
            total += civilizationArmy[i].size();
        }
        return (total * 100) / initialNumberUnitsCivilization;
    }
    
    public Civilization getUserCivilization() {
        return userCivilization;
    }

    public void setUserCivilization(Civilization userCivilization) {
        this.userCivilization = userCivilization;
    }

    public Civilization getEnemyCivilization() {
        return enemyCivilization;
    }

    public void setEnemyCivilization(Civilization enemyCivilization) {
        this.enemyCivilization = enemyCivilization;
    }

    public int remainderPercentageFleetEnemy() {
        int total = 0;
        for (int i = 0; i < enemyArmy.length; i++) {
            total += enemyArmy[i].size();
        }
        return (total * 100) / initialNumberUnitsEnemy;
    }

    public int getGroupDefender(ArrayList<MilitaryUnit>[] army) {
        // Algorithm to choose a defender group randomly based on unit count
        float randMultiplier;
        int[] array = new int[9];
        int totalSum = 0;
        
        for(int i = 0; i < army.length; i++) {
            array[i] = army[i].size() * 20;
            totalSum += army[i].size() * 20;
        }
        
        do {
            randMultiplier = (float) Math.random();
        } while(randMultiplier < 0.1);
        
        int randomNumber = (int) (randMultiplier * totalSum);

        for(int i = 0; i < array.length; i++) {
            int j = i;
            int sum = 0;
            while (j >= 0) {
                sum += array[j];
                j--;
            }
            if (sum >= randomNumber) {
                return i;
            }
        }

        return -1;
    }

    public int getCivilizationGroupAttacker() {
        // Use predefined attack chances from Variables interface
        float rand = (float) Math.random() * 100;
        float accumulated = 0;
        
        for(int i = 0; i < Variables.CHANCE_ATTACK_CIVILIZATION_UNITS.length; i++) {
            accumulated += Variables.CHANCE_ATTACK_CIVILIZATION_UNITS[i];
            if(rand <= accumulated) {
                // Check if this group has units
                if(civilizationArmy[i].size() > 0) {
                    return i;
                }
            }
        }
        
        // Fallback: return any group with units
        return getGroupDefender(civilizationArmy);
    }

    public int getEnemyGroupAttacker() {
        // Use predefined attack chances for enemy
        float rand = (float) Math.random() * 100;
        float accumulated = 0;
        
        for(int i = 0; i < Variables.CHANCE_ATTACK_ENEMY_UNITS.length; i++) {
            accumulated += Variables.CHANCE_ATTACK_ENEMY_UNITS[i];
            if(rand <= accumulated) {
                if(enemyArmy[i].size() > 0) {
                    return i;
                }
            }
        }
        
        return getGroupDefender(enemyArmy);
    }

    public void resetArmyArmor() {
        for (int i = 0; i < civilizationArmy.length; i++) {
            for (int j = 0; j < civilizationArmy[i].size(); j++) {
                civilizationArmy[i].get(j).resetArmor();
            }
        }
    }

    public int[] getArrayValuesFromArrayList(ArrayList[] array) {
        int[] result = new int[array.length];
        for(int i = 0; i < array.length; i++) {
            result[i] = array[i].size();
        }
        return result;
    }
    
    public void updateArmies() {
        armies[0] = new ArrayList[9];
        for(int i = 0; i < civilizationArmy.length; i++) {
            armies[0][i] = new ArrayList<>(civilizationArmy[i]);
        }
        armies[1] = new ArrayList[9];
        for(int i = 0; i < enemyArmy.length; i++) {
            armies[1][i] = new ArrayList<>(enemyArmy[i]);
        }
    }

    public void announceCombat() {
        this.armies = new ArrayList[2][9];
        
        armies[0] = new ArrayList[9];
        for(int i = 0; i < civilizationArmy.length; i++) {
            armies[0][i] = new ArrayList<>(civilizationArmy[i]);
        }
        armies[1] = new ArrayList[9];
        for(int i = 0; i < enemyArmy.length; i++) {
            armies[1][i] = new ArrayList<>(enemyArmy[i]);
        }

        initialNumberUnitsCivilization = initialFleetNumber(civilizationArmy);
        initialNumberUnitsEnemy = initialFleetNumber(enemyArmy);
        battleDevelopment = "";
        wasteWoodIron = new int[2];
        enemyDrops = new int[9];
        civilizationDrops = new int[9];

        initInitialArmies();

        initialCostFleet[0][0] = getFoodCostOfArmy(civilizationArmy);
        initialCostFleet[0][1] = getWoodCostOfArmy(civilizationArmy);
        initialCostFleet[0][2] = getIronCostOfArmy(civilizationArmy);
        initialCostFleet[1][0] = getFoodCostOfArmy(enemyArmy);
        initialCostFleet[1][1] = getWoodCostOfArmy(enemyArmy);
        initialCostFleet[1][2] = getIronCostOfArmy(enemyArmy);

        if(battleType == 0) {
            System.out.println("NEW THREAT IS COMING");
        } else {
            System.out.println("INVADING ANOTHER CIVILIZATION");
        }
    }
    
    public void combat(Civilization civilization, Civilization enemyCivilization, MainPanel mainPanel, int battleType) {
        // battleType 0 = getting invaded, 1 = invading.
        initialNumberUnitsCivilization = initialFleetNumber(civilizationArmy);
        MiddlePanel screen = mainPanel.getMiddlePanel();
        
        if(civilization.isActiveThreat() || enemyCivilization.isActiveThreat()){
            hasCombatStarted = true;
            
            //Change the screen
            screen.changeScreenToBattleScene();

            String winner = "";
            // Selecting randomly who starts the combat
            attackingArmy = (int) (Math.random() * 2);
            int defendingArmy = 1;

            if(attackingArmy == 1) {
                defendingArmy = 0;
            } else if (attackingArmy == 0) {
                defendingArmy = 1;
            }

            int attacking_group;
            int defending_group;
            String attackerStr;
            String defenderStr;
            boolean isAttackingAgain;
            
            while(remainderPercentageFleetCivilization() > 20 && remainderPercentageFleetEnemy() > 20) {
                mainPanel.getMiddlePanel().requestFocusInWindow();
                civilizationArmyPercRemaining = remainderPercentageFleetCivilization();
                enemyArmyPercRemaining = remainderPercentageFleetEnemy();
                isAttackingAgain = true;

                while(isAttackingAgain && (remainderPercentageFleetCivilization() > 20 && remainderPercentageFleetEnemy() > 20)) {
                    int indexAttackingUnit;
                    int indexDefendingUnit;
                    MilitaryUnit attackingUnit;

                    // UPDATING THESE VALUES AGAIN SO IT GET'S REFRESHED VISUALLY
                    civilizationArmyPercRemaining = remainderPercentageFleetCivilization();
                    enemyArmyPercRemaining = remainderPercentageFleetEnemy();
                    
                    if (attackingArmy == 0) {
                        attacking_group = getCivilizationGroupAttacker();
                        attackerStr = civilization.getCivilizationName();
                        defenderStr = enemyCivilization.getCivilizationName();
                    } else {
                        attacking_group = getEnemyGroupAttacker();
                        attackerStr = enemyCivilization.getCivilizationName();
                        defenderStr = civilization.getCivilizationName();
                    }

                    if (attackingArmy == 0) {
                        indexAttackingUnit = (int) (Math.random() * civilizationArmy[attacking_group].size());
                        attackingUnit = (MilitaryUnit) (civilizationArmy[attacking_group].get((indexAttackingUnit)));
                    } else {
                        indexAttackingUnit = (int) (Math.random() * enemyArmy[attacking_group].size());
                        attackingUnit = (MilitaryUnit) (enemyArmy[attacking_group].get((indexAttackingUnit)));
                    }

                    MilitaryUnit defendingUnit;
                    // Selecting defending group
                    if (defendingArmy == 0) {
                        defending_group = getGroupDefender(civilizationArmy);
                        indexDefendingUnit = (int) (Math.random() * civilizationArmy[defending_group].size());
                        defendingUnit = (MilitaryUnit) (civilizationArmy[defending_group].get(indexDefendingUnit));
                    } else {
                        defending_group = getGroupDefender(enemyArmy);
                        indexDefendingUnit = (int) (Math.random() * enemyArmy[defending_group].size());
                        defendingUnit = (MilitaryUnit) (enemyArmy[defending_group].get(indexDefendingUnit));
                    }

                    // Making them fight - Applying damage
                    battleDevelopment += attackerStr + " attacks " + defenderStr + "\n" + 
                                        defendingUnit.getName() + " receives " + attackingUnit.attack() + " dmg.\n\n";
                    defendingUnit.takeDamage(attackingUnit.attack());

                    if (attackingArmy == 0) {
                        mainPanel.getMiddlePanel().paintCurrentBattleState(this, attackingUnit, defendingUnit);
                    } else {
                        mainPanel.getMiddlePanel().paintCurrentBattleState(this, defendingUnit, attackingUnit);
                    }

                    // Checking if it's destroyed
                    if(defendingUnit.getActualArmor() <= 0) {
                        battleDevelopment += defendingUnit.getName() + " has " + defendingUnit.getActualArmor() + 
                                            " armor, it gets destroyed.\n";

                        // Before eliminating it, checking if waste gets generated
                        boolean isGeneratingWaste = false;
                        if (Math.random()*100 <= defendingUnit.getChanceGeneratingWaste()) {
                            isGeneratingWaste = true;
                        }
                        
                        if(isGeneratingWaste) {
                            wasteWoodIron[0] += defendingUnit.getWoodCost() * Variables.PERCENTATGE_WASTE / 100;
                            wasteWoodIron[1] += defendingUnit.getIronCost() * Variables.PERCENTATGE_WASTE / 100;
                            battleDevelopment += defendingUnit.getWoodCost() * Variables.PERCENTATGE_WASTE / 100 + 
                                                " wood waste has been generated\n";
                            battleDevelopment += defendingUnit.getIronCost() * Variables.PERCENTATGE_WASTE / 100 + 
                                                " iron waste has been generated\n";
                        }

                        // Removing from arrays
                        if (defendingArmy == 0) {
                            civilizationArmy[defending_group].remove(defendingUnit);
                        } else {
                            enemyArmy[defending_group].remove(defendingUnit);
                        }

                        // Playing explosion sound
                        if(!isSkipBattle()){
                            AudioPlayer.doExplosion();
                        }
                    }

                    // If it's not destroyed / or after destroying it
                    if (attackingArmy == 0) {
                        mainPanel.getMiddlePanel().paintCurrentBattleState(this, attackingUnit, defendingUnit);
                    } else {
                        mainPanel.getMiddlePanel().paintCurrentBattleState(this, defendingUnit, attackingUnit);
                    }

                    // If it attacks again
                    if( (int) (Math.random() * 100) <= attackingUnit.getChanceAttackAgain() && 
                        (remainderPercentageFleetCivilization() > 20 && remainderPercentageFleetEnemy() > 20)) {
                        battleDevelopment += attackerStr + " gets to attack again!\n";
                        isAttackingAgain = true;
                    } else {
                        if (remainderPercentageFleetCivilization() > 20 && remainderPercentageFleetEnemy() > 20){
                            battleDevelopment += "--------------------------------------------\nTURN OVER, SWITCHING ROLES\n" +
                                                "--------------------------------------------\n\n";
                        } else{
                            break;
                        }
                        isAttackingAgain = false;
                        
                        // Switching the turns
                        int var = attackingArmy;
                        attackingArmy = defendingArmy;
                        defendingArmy = var;
                    }

                    try {
                        if (!skipBattle){
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            // After the combat is over
            updateDrops();
            updateResourceLosses();
            battleDevelopment += "\n\n------------------- COMBAT RESULTS -----------------------\n";
            battleDevelopment += "Resources lost by " + civilization.getCivilizationName() + ": " + 
                                resourcesLosses[0][3] + "\n";
            battleDevelopment += "Resources lost by " + enemyCivilization.getCivilizationName() +": " + 
                                resourcesLosses[1][3] + "\n\n";

            if (getWinner() == 0 && battleType == 0) {
                winner = civilization.getCivilizationName();
                battleDevelopment += civilization.getCivilizationName() + " collects " + wasteWoodIron[0] + 
                                    " wood and " + wasteWoodIron[1] + " iron\n\n";
                civilization.setWood(civilization.getWood() + wasteWoodIron[0]);
                civilization.setIron(civilization.getIron() + wasteWoodIron[1]);
            } else {
                if(battleType == 1 && getWinner() == 1) {
                    winner = civilization.getCivilizationName();
                    battleDevelopment += enemyCivilization.getCivilizationName() + " collects " + wasteWoodIron[0] + 
                                        " wood and " + wasteWoodIron[1] + " iron\n\n";
                    enemyCivilization.setWood(enemyCivilization.getWood() + wasteWoodIron[0]);
                    enemyCivilization.setIron(enemyCivilization.getIron() + wasteWoodIron[1]);
                } else {
                    winner = enemyCivilization.getCivilizationName();
                }
            }

            battleDevelopment += winner + " wins!";
        }

        updateBattleReport();
        System.out.println(battleReport);
        new ResultFrame(this);
        new ThreatTimer(civilization, ms);
    }

    public void updateBattleReport() {
        String divider = "----------------------------------------------------\n";
        battleReport += divider;
        
        if(battleType == 0) {
            battleReport += "Army Civilization " + userCivilization.getCivilizationName() + "\n";
        } else {
            battleReport += "Army Civilization " + enemyCivilization.getCivilizationName() + "\n";
        }
        
        String[] unitNames = {"Swordsman", "Spearman", "Crossbow", "Cannon", "Arrow Tower", 
                             "Catapult", "Rocket Launcher", "Magician", "Priest"};
        
        for(int i = 0; i < armies[0].length; i++) {
            if(armies[0][i].size() > 0){
                battleReport += unitNames[i] + ": " + armies[0][i].size() + " -" + civilizationDrops[i] +"\n";
            }
        }
        battleReport += divider;

        if(battleType == 0) {
            battleReport += "Army Civilization " + enemyCivilization.getCivilizationName() + "\n";
        } else {
            battleReport += "Army Civilization " + userCivilization.getCivilizationName() + "\n";
        }
        
        for(int i = 0; i < armies[1].length; i++) {
            if(armies[1][i].size() > 0){
                battleReport += unitNames[i] + ": " + armies[1][i].size() + " -" + enemyDrops[i] +"\n";
            }
        }

        battleReport += divider;
        battleReport += "Cost Army " + userCivilization.getCivilizationName() + "\n";
        battleReport += "Food: " + initialCostFleet[0][0] + "\n";
        battleReport += "Wood: " + initialCostFleet[0][1] + "\n";
        battleReport += "Iron: " + initialCostFleet[0][2] + "\n";

        battleReport += divider;
        battleReport += "Cost Army " + enemyCivilization.getCivilizationName() + "\n";
        battleReport += "Food: " + initialCostFleet[1][0] + "\n";
        battleReport += "Wood: " + initialCostFleet[1][1] + "\n";
        battleReport += "Iron: " + initialCostFleet[1][2] + "\n";

        battleReport += divider;

        if (battleType == 0) {
            battleReport += "Losses Army " + userCivilization.getCivilizationName() + "\n";
        } else {
            battleReport += "Losses Army " + enemyCivilization.getCivilizationName() + "\n";
        }
        battleReport += "Food: " + resourcesLosses[0][0] + "\n";
        battleReport += "Wood: " + resourcesLosses[0][1] + "\n";
        battleReport += "Iron: " + resourcesLosses[0][2] + "\n";
        battleReport += "Weighted: " + resourcesLosses[0][3] + "\n";

        battleReport += divider;

        if(battleType == 0) {
            battleReport += "Losses Army " + enemyCivilization.getCivilizationName() + "\n";
        } else {
            battleReport += "Losses Army " + userCivilization.getCivilizationName() + "\n";
        }

        battleReport += "Food: " + resourcesLosses[1][0] + "\n";
        battleReport += "Wood: " + resourcesLosses[1][1] + "\n";
        battleReport += "Iron: " + resourcesLosses[1][2] + "\n";
        battleReport += "Weighted: " + resourcesLosses[1][3] + "\n";

        battleReport += divider;
        battleReport += "Waste Generated:\n";
        battleReport += "Wood: " + wasteWoodIron[0] + "\n";
        battleReport += "Iron: " + wasteWoodIron[1] + "\n";
    }

    public void printEnemyStats() {
        System.out.println("\nCURRENT THREAT\n");
        System.out.println("Swordsman = " + enemyArmy[0].size());
        System.out.println("Spearman = " + enemyArmy[1].size());
        System.out.println("Crossbow = " + enemyArmy[2].size());
        System.out.println("Cannon = " + enemyArmy[3].size());
        System.out.println();
    }

    public void printArmies() {
        for(int i = 0; i < armies.length; i++) {
            for(int j = 0; j < armies[i].length; j++) {
                if(armies[i][j] != null) {
                    System.out.println(armies[i][j].size());
                }
            }
        }
    }

    public void updateDrops() {
        civilizationDrops[0] = initialArmies[0][0] - civilizationArmy[0].size();
        civilizationDrops[1] = initialArmies[0][1] - civilizationArmy[1].size();
        civilizationDrops[2] = initialArmies[0][2] - civilizationArmy[2].size();
        civilizationDrops[3] = initialArmies[0][3] - civilizationArmy[3].size();
        civilizationDrops[4] = initialArmies[0][4] - civilizationArmy[4].size();
        civilizationDrops[5] = initialArmies[0][5] - civilizationArmy[5].size();
        civilizationDrops[6] = initialArmies[0][6] - civilizationArmy[6].size();
        civilizationDrops[7] = initialArmies[0][7] - civilizationArmy[7].size();
        civilizationDrops[8] = initialArmies[0][8] - civilizationArmy[8].size();

        enemyDrops[0] = initialArmies[1][0] - enemyArmy[0].size();
        enemyDrops[1] = initialArmies[1][1] - enemyArmy[1].size();
        enemyDrops[2] = initialArmies[1][2] - enemyArmy[2].size();
        enemyDrops[3] = initialArmies[1][3] - enemyArmy[3].size();
        enemyDrops[4] = initialArmies[1][4] - enemyArmy[4].size();
        enemyDrops[5] = initialArmies[1][5] - enemyArmy[5].size();
        enemyDrops[6] = initialArmies[1][6] - enemyArmy[6].size();
        enemyDrops[7] = initialArmies[1][7] - enemyArmy[7].size();
        enemyDrops[8] = initialArmies[1][8] - enemyArmy[8].size();
    }

    public int getCostOfGroup(ArrayList<MilitaryUnit> group) {
        int total = 0;
        for (int i = 0; i < group.size(); i++) {
            total += group.get(i).getIronCost();
        }
        return total;
    }

    public int getFoodCostOfArmy(ArrayList<MilitaryUnit>[] army) {
        int total = 0;
        for (int i = 0; i < army.length; i++) {
            for(int j = 0; j < army[i].size(); j++) {
                total += army[i].get(j).getFoodCost();
            }
        }
        return total;
    }

    public int getWoodCostOfArmy(ArrayList<MilitaryUnit>[] army) {
        int total = 0;
        for (int i = 0; i < army.length; i++) {
            for(int j = 0; j < army[i].size(); j++) {
                total += army[i].get(j).getWoodCost();
            }
        }
        return total;
    }

    public int getIronCostOfArmy(ArrayList<MilitaryUnit>[] army) {
        int total = 0;
        for (int i = 0; i < army.length; i++) {
            for(int j = 0; j < army[i].size(); j++) {
                total += army[i].get(j).getIronCost();
            }
        }
        return total;
    }

    public int getWinner() {
        if (resourcesLosses[0][3] < resourcesLosses[1][3]) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isHasCombatStarted() {
        return hasCombatStarted;
    }

    public int getAttackingArmy() {
        return attackingArmy;
    }

    public int getBattleType() {
        return battleType;
    }

    public String getBattleReport() {
        return battleReport;
    }

    public void setBattleReport(String battleReport) {
        this.battleReport = battleReport;
    }

    public void setBattleType(int battleType) {
        this.battleType = battleType;
    }

    public int getCivilizationArmyPercRemaining() {
        return civilizationArmyPercRemaining;
    }

    public int getEnemyArmyPercRemaining() {
        return enemyArmyPercRemaining;
    }

    public int[][] getInitialArmies() {
        return initialArmies;
    }

    public boolean isSkipBattle() {
        return skipBattle;
    }

    public void setSkipBattle(boolean skipBattle) {
        this.skipBattle = skipBattle;
    }

    public String getBattleDevelopment() {
        return battleDevelopment;
    }

    public ArrayList<MilitaryUnit>[] getEnemyArmy() {
        return enemyArmy;
    }

    public ArrayList<MilitaryUnit>[] getCivilizationArmy() {
        return civilizationArmy;
    }

    public int[][] getInitialCostFleet() {
        return initialCostFleet;
    }

    public int[][] getResourcesLosses() {
        return resourcesLosses;
    }

    public int[] getWasteWoodIron() {
        return wasteWoodIron;
    }

    public void bbddOperations(Civilization civilization) {
        // CivilizationStatsTable
        GlobalContext.civilizationStatsTable.updateAttributes(civilization);
        
        // BattleStatsTable
        GlobalContext.battleStatsTable = new BattleStatsTable(
            GlobalContext.database,
            GlobalContext.civilization_id,
            wasteWoodIron[0],
            wasteWoodIron[1]
        );
        GlobalContext.battleStatsTable.insertRow();
        
        // BattleLogTable
        GlobalContext.battleLogTable = new BattleLogTable(
            GlobalContext.database,
            GlobalContext.num_battle,
            battleDevelopment
        );
        GlobalContext.battleLogTable.insertRow();
        
        // CivilizationBattleDefense
        GlobalContext.civilizationBattleDefenseTable = new CivilizationBattleDefenseTable(
            GlobalContext.database,
            GlobalContext.num_battle,
            Battle.this
        );
        GlobalContext.civilizationBattleDefenseTable.insertRow();
        
        // CivilizationBattleArmy
        GlobalContext.civilizationBattleArmyTable = new CivilizationBattleArmyTable(
            GlobalContext.database,
            GlobalContext.num_battle,
            Battle.this
        );
        GlobalContext.civilizationBattleArmyTable.insertRow();
        
        // EnemyArmyTable
        GlobalContext.enemyArmyTable = new EnemyArmyTable(
            GlobalContext.database,
            GlobalContext.num_battle,
            Battle.this
        );
        GlobalContext.enemyArmyTable.insertRow();
    }
}