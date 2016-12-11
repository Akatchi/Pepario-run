package com.pepario.run.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.pepario.run.Config.Config;
import com.pepario.run.Scenes.GameHud;

public abstract class BaseGameScreen implements Screen
{
    protected OrthogonalTiledMapRenderer getMapRendererForLevelName(String levelName)
    {
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load(levelName);
        return new OrthogonalTiledMapRenderer(map, 1 / Config.PPM);
    }

    protected void clearScreen()
    {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public abstract World getWorld();
    public abstract TiledMap getTiledMap();
    public abstract GameHud getHud();
    public abstract AssetManager getAssetManager();
    public abstract TextureAtlas getTextureAtlas();
}
