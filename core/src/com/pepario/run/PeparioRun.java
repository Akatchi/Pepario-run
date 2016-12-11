package com.pepario.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pepario.run.Levels.Level;
import com.pepario.run.Levels.LevelFactory;
import com.pepario.run.Scenes.GameHud;
import com.pepario.run.Screens.GameScreen;
import com.pepario.run.Screens.MenuScreen;

public class PeparioRun extends Game
{
	private SpriteBatch batch;
	private AssetManager assetManager;

	@Override
	public void create ()
    {
		batch = new SpriteBatch();

		assetManager = initAssetManager();

        setScreen(new MenuScreen(this));
	}

	@Override
	public void render ()
    {
		super.render();
	}
	
	@Override
	public void dispose ()
    {
        super.dispose();
		batch.dispose();
	}

	public AssetManager getAssetManager()
	{
		return assetManager;
	}

	public SpriteBatch getSpriteBatch()
    {
        return batch;
    }

    private AssetManager initAssetManager()
	{
		AssetManager assetManager = new AssetManager();
		assetManager.load("audio/music/farm_frolics.ogg", Music.class);
		assetManager.load("audio/music/game_over.ogg", Music.class);
		assetManager.load("audio/music/space_cadet.ogg", Music.class);
		assetManager.load("audio/sounds/coin.ogg", Sound.class);
		assetManager.load("audio/sounds/game_over_voice.ogg", Sound.class);
		assetManager.load("audio/sounds/gameover.ogg", Sound.class);
		assetManager.load("audio/sounds/levelup.ogg", Sound.class);
		assetManager.finishLoading();

		return assetManager;
	}
}
