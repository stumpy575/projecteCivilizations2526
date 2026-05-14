package GUI;

import java.awt.Color;

public abstract class Globals {
    private static Color panelSecColor = new Color(30, 30, 30);

    public static String customFont = "./programacio/src/GUI/fonts/font.ttf";

    // Bottom shop bar
    public static Color bottomFontColor             = Color.WHITE;
    public static Color bottomImageBackgroundColor  = Color.BLACK;
    public static Color bottomBackgroundColor       = Color.BLACK;

    // Left panel (resources, technology, buildings)
    public static Color leftFontColor       = Color.WHITE;
    public static Color leftPanelColor      = Color.BLACK;
    public static Color leftPanelSecColor   = panelSecColor;
    public static Color leftFontSecColor    = Color.WHITE;

    // Right panel (buttons)
    public static Color rightButtonsColor       = Color.BLACK;
    public static Color rightButtonsFontColor   = Color.WHITE;
    public static Color rightSecPanelColor      = panelSecColor;
    public static Color rightSecFontColor       = Color.WHITE;

    // Settings
    public static Color settingsPanelColor      = Color.BLACK;
    public static Color settingsFontColor       = Color.WHITE;
    public static Color settingsSecPanelColor   = Color.BLACK;
    public static Color settingsSecFontColor    = Color.WHITE;
    public static Color settingsButtonColor     = Color.WHITE;
    public static Color settingsButtonFontColor = Color.BLACK;

    // Health bar
    public static Color healthBarHealthyColor = Color.WHITE;
    public static Color healthBarInjuredColor = new Color(255, 102, 102);
}s