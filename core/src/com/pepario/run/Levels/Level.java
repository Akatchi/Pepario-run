package com.pepario.run.Levels;

public class Level
{
    private String leaderboardKey;
    private String tmxLevelName;
    private String musicNameWithPath;
    private int nextLevelId;
    private float playerMaxRunningSpeed;
    private float deltaTimeRunningSpeedUpdateWeight;
    private float playerMovementSpeed;

    public Level() {}

    public String getLeaderboardKey()
    {
        return leaderboardKey;
    }

    public String getTmxLevelName()
    {
        return tmxLevelName;
    }

    public String getMusicNameWithPath()
    {
        return musicNameWithPath;
    }

    public int getNextLevelId()
    {
        return nextLevelId;
    }

    public float getPlayerMaxRunningSpeed()
    {
        return playerMaxRunningSpeed;
    }

    public float getDeltaTimeRunningSpeedUpdateWeight()
    {
        return deltaTimeRunningSpeedUpdateWeight;
    }

    public float getPlayerMovementSpeed()
    {
        return playerMovementSpeed;
    }

    public Level setLeaderboardKey(String leaderboardKey)
    {
        this.leaderboardKey = leaderboardKey;
        return this;
    }

    public Level setTmxLevelName(String tmxLevelName)
    {
        this.tmxLevelName = tmxLevelName;
        return this;
    }

    public Level setMusicNameWithPath(String musicNameWithPath)
    {
        this.musicNameWithPath = musicNameWithPath;
        return this;
    }

    public Level setNextLevelId(int nextLevelId)
    {
        this.nextLevelId = nextLevelId;
        return this;
    }

    public Level setPlayerMaxRunningSpeed(float playerMaxRunningSpeed)
    {
        this.playerMaxRunningSpeed = playerMaxRunningSpeed;
        return this;
    }

    public Level setDeltaTimeRunningSpeedUpdateWeight(float deltaTimeRunningSpeedUpdateWeight)
    {
        this.deltaTimeRunningSpeedUpdateWeight = deltaTimeRunningSpeedUpdateWeight;
        return this;
    }

    public Level setPlayerMovementSpeed(float playerMovementSpeed)
    {
        this.playerMovementSpeed = playerMovementSpeed;
        return this;
    }
}
