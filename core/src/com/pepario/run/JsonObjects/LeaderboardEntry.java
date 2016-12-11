package com.pepario.run.JsonObjects;


public class LeaderboardEntry implements Comparable<LeaderboardEntry>
{
    private String key;
    private String name;
    private int value;

    public LeaderboardEntry()
    {
    }

    public LeaderboardEntry(String key, String name, int value)
    {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public int compareTo(LeaderboardEntry otherEntry)
    {
        // The LeaderboardEntry object with the highest value should come first
        if(getValue() > otherEntry.getValue()) {
            return -1;
        } else if(getValue() < otherEntry.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }
}
