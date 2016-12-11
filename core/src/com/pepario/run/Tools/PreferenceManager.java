package com.pepario.run.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.pepario.run.Config.Config;

public class PreferenceManager
{
    private Preferences preferences;

    private final String KEY_COINS = "coins";

    public PreferenceManager()
    {
        preferences = Gdx.app.getPreferences(Config.PREFERENCE_NAME);
    }

    public int getCoins()
    {
        return preferences.getInteger(KEY_COINS, 0);
    }

    public void addCoins(int value)
    {
        int currentCoins = getCoins();

        preferences.putInteger(KEY_COINS, currentCoins + value);
        preferences.flush();
    }

    public void addSingleCoin()
    {
        addCoins(1);
    }
}
