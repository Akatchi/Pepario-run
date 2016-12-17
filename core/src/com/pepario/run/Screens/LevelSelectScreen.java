package com.pepario.run.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pepario.run.Config.Config;
import com.pepario.run.Levels.LevelFactory;
import com.pepario.run.PagedScrollPane;
import com.pepario.run.PeparioRun;

public class LevelSelectScreen implements Screen {
    private PeparioRun game;
    private Camera camera;
    private Viewport viewPort;

    private Texture background;
    private Stage stage;
    private Skin skin;

    private Music music;

    private Table container;

    public LevelSelectScreen(PeparioRun game) {
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

        // Dennis
        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        PagedScrollPane scroll = new PagedScrollPane();
        scroll.setFlingTime(0.1f);
        scroll.setPageSpacing(0);
        scroll.setFillParent(true);

        int c = 1;
        for (int l = 0; l < 2; l++) {
            Table levels = new Table().pad(50).padBottom(0);
            levels.defaults().pad(20);

            for (int y = 0; y < 2; y++) {
                levels.row();
                for (int x = 0; x < 4; x++) {
                    levels.add(getLevelButton(c++)).expand().fill();
                }
            }
            scroll.addPage(levels);
        }
        container.add(scroll).expand().fill();

    }

    private void setupSkin() {
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
    public void show() {
        //Create buttons
        ImageButton playButton = new ImageButton(skin, "start_game");
        ImageButton leaderboardButton = new ImageButton(skin, "leaderboard");
        ImageButton shopButton = new ImageButton(skin, "shop");
        Label gameTitleText = new Label("Pepario Run!", skin);

        //Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                game.setScreen(new EndlessGameScreen(game));
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
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Config.WIDTH, Config.HEIGHT);
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        music.dispose();
    }

    /**
     * Creates a button to represent the level
     *
     * @param level
     * @return The button to use for the level
     */
    public Button getLevelButton(int level) {
        Button button = new ImageButton(skin, "start_game");
        Button.ButtonStyle style = button.getStyle();
        style.up = style.down = null;

        // Create the label to show the level number
        Label label = new Label(Integer.toString(level), skin);
//        label.setFontScale(2f);
        label.setAlignment(Align.center);

        // Stack the image and the label at the top of our button
        button.stack(label).expand().fill();

//        // Randomize the number of stars earned for demonstration purposes
//        int stars = MathUtils.random(-1, +3);
//        Table starTable = new Table();
//        starTable.defaults().pad(5);
//        if (stars >= 0) {
//            for (int star = 0; star < 3; star++) {
//                if (stars > star) {
//                    starTable.add(new Image(skin.getDrawable("star-filled"))).width(20).height(20);
//                } else {
//                    starTable.add(new Image(skin.getDrawable("star-unfilled"))).width(20).height(20);
//                }
//            }
//        }

        button.row();
//        button.add(starTable).height(30);

        button.setName(Integer.toString(level));
        button.addListener(levelClickListener);
        return button;
    }

    /**
     * Handle the click - in real life, we'd go to the level
     */
    public ClickListener levelClickListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println("Click: " + event.getListenerActor().getName());
            music.stop();
            game.setScreen(new GameScreen(game, LevelFactory.getLevel(Integer.parseInt(event.getListenerActor().getName()))));
            dispose();
        }
    };

}
