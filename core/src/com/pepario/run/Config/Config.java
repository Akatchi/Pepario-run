package com.pepario.run.Config;

public class Config
{
    public static final float PPM = 100;

    public static final int WIDTH = 400;
    public static final int HEIGHT = 208;

    public static final int SCORE_PER_INTERVAL = 10;
    public static final int COIN_SCORE_VALUE = 100;
    public static final int LEVEL_COMPLETED_SCORE = 500;

    // Bits
    public static final short NOTHING_BIT = 0;
    public static final short PLAYER_BIT = 1;
    public static final short COIN_BIT = 2;
    public static final short GROUND_BIT = 4;
    public static final short GOAL_BIT = 8;

    // TiledLayer config
    public static final String INTERACTIVE_GRAPHICS_LAYER = "interactive_graphics_layer";
    public static final String GROUND_LAYER = "ground";
    public static final String COIN_LAYER = "coins";
    public static final String GOAL_LAYER = "goal";

    public static final String PREFERENCE_NAME = "PeparioRun Preferences v1";
    public static final String LEADERBOARD_BASE_FILE_NAME = "leaderboard.json";
    public static final short LEADERBOARD_FILE_MAX_ENTRIES = 10;
}
