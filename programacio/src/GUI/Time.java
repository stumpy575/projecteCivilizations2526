package GUI;

public class Time {
    public static int secondsCountdownBattle = 10;
    public static int secInMs                = 1000;
    public static int secondsBetweenBattles  = 180;   // 3 minutes as per spec
    public static int secondsBetweenResources = 60;   // 1 minute

    public static int countdownBattleTime  = secInMs * secondsCountdownBattle;
    public static int timeBetweenBattles   = secInMs * secondsBetweenBattles;
    public static int timeBetweenResources = secInMs * secondsBetweenResources;
}