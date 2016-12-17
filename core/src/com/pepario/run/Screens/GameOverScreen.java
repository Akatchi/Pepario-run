package com.pepario.run.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pepario.run.Config.Config;
import com.pepario.run.Levels.Level;
import com.pepario.run.PeparioRun;

public class GameOverScreen implements Screen {
    private Level level = null;
    private Viewport viewport;
    private Stage stage;
    private PeparioRun game;
    private Music music;

    private Label gameOverLabel;
    private Label playAgainLabel;

    public GameOverScreen(PeparioRun game) {
        this.game = game;

        viewport = new FitViewport(Config.WIDTH, Config.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getSpriteBatch());

        initHudElements();
        stage.addActor(getTableWithHudElements());

        music = getScreenMusic();
        music.play();
    }

    public GameOverScreen(PeparioRun game, Level level) {
        this(game);
        this.level = level;
    }

    private void initHudElements() {
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        gameOverLabel = new Label("GAME OVER", font);
        playAgainLabel = new Label("Click to play again", font);
    }

    private Table getTableWithHudElements() {
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        return table;
    }

    private Music getScreenMusic() {
        music = game.getAssetManager().get("audio/music/game_over.ogg", Music.class);
        music.setLooping(true);

        return music;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            music.stop();
            if (level != null) {
                game.setScreen(new GameScreen(game, level));
            } else {
                game.setScreen(new EndlessGameScreen(game));
            }
            dispose();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
