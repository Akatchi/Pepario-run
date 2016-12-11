package com.pepario.run.Sprites.TileObjects;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.pepario.run.Config.Config;
import com.pepario.run.Screens.BaseGameScreen;
import com.pepario.run.Sprites.Player;
import com.pepario.run.Tools.PreferenceManager;

public class Coin extends InteractiveTileObject
{
    private BaseGameScreen screen;

    public Coin(BaseGameScreen screen, MapObject mapObject)
    {
        super(screen, mapObject);

        this.screen = screen;

        getFixture().setUserData(this);
        setCategoryFilter(Config.COIN_BIT);
    }

    @Override
    public void onHit(Player player)
    {
        if(getCategoryFilter() == Config.COIN_BIT) {
            screen.getHud().updateScore(Config.COIN_SCORE_VALUE);
            setCategoryFilter(Config.NOTHING_BIT);
            getCell().setTile(null);

            // Play the pickup sound
            screen.getAssetManager().get("audio/sounds/coin.ogg", Sound.class).play();

            PreferenceManager manager = new PreferenceManager();
            manager.addSingleCoin();
            // Notify the hud that we incremented the coins as well
            screen.getHud().reloadCoinValue();
        }
    }
}
