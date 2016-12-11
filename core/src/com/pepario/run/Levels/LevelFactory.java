package com.pepario.run.Levels;

public class LevelFactory
{
    public static Level getLevel(int levelId)
    {
        switch(levelId) {
            case 1:
            default:
                return createLevelOne();
        }
    }

    private static Level createLevelOne()
    {
        Level levelOne = new Level();
        levelOne.setMusicNameWithPath("audio/music/farm_frolics.ogg")
                .setLeaderboardKey("LEVEL_1")
                .setTmxLevelName("level1.tmx")
                .setNextLevelId(1)
                .setPlayerMaxRunningSpeed(0.7f)
                .setPlayerMovementSpeed(0.1f)
                .setDeltaTimeRunningSpeedUpdateWeight(150);

        return levelOne;
    }
}
