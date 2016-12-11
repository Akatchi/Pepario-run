package com.pepario.run.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pepario.run.Config.Config;
import com.pepario.run.PeparioRun;

public class MenuScreen implements Screen
{
    private PeparioRun game;
    private Camera camera;
    private Viewport viewPort;

    private Texture background;
    private Stage stage;
    private Skin skin;

    private Music music;

    public MenuScreen(PeparioRun game)
    {
        this.game = game;

        setupSkin();

        camera = new OrthographicCamera();
        viewPort = new FitViewport(Config.WIDTH, Config.HEIGHT, camera);
        viewPort.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        background = new Texture("backgrounds/bg_castle.png");

        stage = new Stage(viewPort, game.getSpriteBatch());

        music = game.getAssetManager().get("audio/music/space_cadet.ogg", Music.class);
        music.setLooping(true);
        music.play();

        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);
    }

    private void setupSkin()
    {
        //Create a font
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        skin.add("default_button", new Texture("Buttons/buttonSquare_blue.png"));
        skin.add("pressed_button", new Texture("Buttons/buttonSquare_blue_pressed.png"));
        skin.add("play_icon", new Texture("Buttons/arrowSilver_right.png"));
        skin.add("coin_flag", new Texture("Buttons/medievalTile_123.png"));
        skin.add("swords_flag", new Texture("Buttons/medievalTile_124.png"));

        //Create a button style
        ImageButton.ImageButtonStyle startGameButtonStyle = new ImageButton.ImageButtonStyle();
        startGameButtonStyle.up = skin.newDrawable("default_button");
        startGameButtonStyle.down = skin.newDrawable("pressed_button");
        startGameButtonStyle.over = skin.newDrawable("default_button", Color.LIGHT_GRAY);
        startGameButtonStyle.imageUp = skin.newDrawable("play_icon");
        skin.add("start_game", startGameButtonStyle);

        ImageButton.ImageButtonStyle leaderboardButtonStyle = new ImageButton.ImageButtonStyle();
        leaderboardButtonStyle.up = skin.newDrawable("swords_flag");
        leaderboardButtonStyle.over = skin.newDrawable("swords_flag", Color.LIGHT_GRAY);
        skin.add("leaderboard", leaderboardButtonStyle);

        ImageButton.ImageButtonStyle shopButtonStyle = new ImageButton.ImageButtonStyle();
        shopButtonStyle.up = skin.newDrawable("coin_flag");
        shopButtonStyle.over = skin.newDrawable("coin_flag", Color.LIGHT_GRAY);
        skin.add("shop", shopButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle(
                new BitmapFont(Gdx.files.internal("Fonts/kenpixel_mini.fnt")),
                Color.WHITE
        );
        skin.add("default", labelStyle);
    }

    @Override
    public void show()
    {
        //Create buttons
        ImageButton playButton = new ImageButton(skin, "start_game");
        ImageButton leaderboardButton = new ImageButton(skin, "leaderboard");
        ImageButton shopButton = new ImageButton(skin, "shop");
        Label gameTitleText = new Label("Pepario Run!", skin);

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top();
        topTable.left();

        Table topTable2 = new Table();
        topTable2.setFillParent(true);
        topTable2.top();
        topTable2.right();

        Table topTable3 = new Table();
        topTable3.setFillParent(true);
        topTable3.top();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.bottom();

        //Add buttons to tables
        topTable.add(leaderboardButton).width(50).height(50);
        topTable2.add(shopButton).width(50).height(50);
        topTable3.add(gameTitleText).padTop(10f);
        mainTable.add(playButton).padBottom(10f);

        //Add table to stage
        stage.addActor(topTable);
        stage.addActor(topTable2);
        stage.addActor(topTable3);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Config.WIDTH, Config.HEIGHT);
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        viewPort.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose()
    {
        stage.dispose();
        music.dispose();
    }
}
