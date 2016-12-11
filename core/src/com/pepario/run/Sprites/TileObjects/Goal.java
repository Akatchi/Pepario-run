package com.pepario.run.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.pepario.run.Config.Config;
import com.pepario.run.Screens.BaseGameScreen;
import com.pepario.run.Sprites.Player;

public class Goal extends InteractiveTileObject
{
    private BaseGameScreen screen;

    public Goal(BaseGameScreen screen, MapObject mapObject)
    {
        super(screen, mapObject);

        this.screen = screen;

        getFixture().setUserData(this);
        setCategoryFilter(Config.GOAL_BIT);
    }

    @Override
    public void onHit(Player player)
    {
        if(!player.completedLevel()) {
            // Play the pickup sound
            player.setCompletedLevel();
            screen.getHud().updateScore(Config.LEVEL_COMPLETED_SCORE);
        }
    }
}
