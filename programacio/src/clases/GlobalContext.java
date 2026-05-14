package clases;
import bbdd.*;

// This class will contain the objects that need to be accesed in many different classes
public class GlobalContext {
    public static Database database;
    public static int planet_id;
    public static int num_battle;
    public static BattleLogTable battleLogTable;
    //public static BattleStatsTable battleStatsTable;
    //public static EnemyArmyTable enemyArmyTable;
    //public static PlanetBattleArmyTable planetBattleArmyTable;
    //public static PlanetBattleDefenseTable planetBattleDefenseTable;
    //public static PlanetStatsTable planetStatsTable;

    private GlobalContext() {
        
    }
}