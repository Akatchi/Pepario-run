package com.pepario.run.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pepario.run.Config.Config;
import com.pepario.run.Levels.Level;
import com.pepario.run.PeparioRun;
import com.pepario.run.Scenes.GameHud;
import com.pepario.run.Sprites.Player;
import com.pepario.run.Tools.B2WorldCreator;
import com.pepario.run.Tools.WorldContactListener;

public class GameScreen extends BaseGameScreen {
    private PeparioRun game;

    private OrthographicCamera camera;
    private Viewport viewPort;

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private TextureAtlas textureAtlas;

    // Scenes
    private GameHud hud;

    // Sprites
    private Player player;

    private Music music;

    private Level level;

    public GameScreen(PeparioRun game, Level level) {
        this.game = game;
        this.level = level;

        textureAtlas = new TextureAtlas("GFX/player_and_enemies.pack");

        camera = new OrthographicCamera();
        viewPort = new FitViewport(Config.WIDTH / Config.PPM, Config.HEIGHT / Config.PPM, camera);

        hud = new GameHud(game.getSpriteBatch());

        mapRenderer = getMapRendererForLevelName(level.getTmxLevelName());
        map = mapRenderer.getMap();

        camera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);
        player = new Player(this);

        // Initialise config based on level values
        player.setMaxRunningSpeed(level.getPlayerMaxRunningSpeed())
                .setDeltaTimeRunningSpeedUpdateWeight(level.getDeltaTimeRunningSpeedUpdateWeight())
                .setMovementSpeed(level.getPlayerMovementSpeed());

        world.setContactListener(new WorldContactListener());

        music = game.getAssetManager().get(level.getMusicNameWithPath(), Music.class);
        music.setLooping(true);
        music.play();
    }

    private void update(float deltaTime) {
        handleInput();

        player.update(deltaTime);

        if (!player.isDead()) {
            // Only update the hud if the player is alive
            hud.update(deltaTime);
            // Follow the player if it's alive -> @TODO should become autoscrolling in the future
            camera.position.x = player.getBody().getPosition().x;
        }

        doOneWorldStep(world);

        camera.update();
        mapRenderer.setView(camera);

        // If the player is dead for at least 3 seconds, we will show the game over screen
        if (player.isDead() && player.getStateTimer() > 3) {
            music.stop();
            game.setScreen(new GameOverScreen(game, level));
            dispose();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            player.jump();
        }
        if (Gdx.input.isTouched()) {
            player.jump();
        }

        // Always move right
        player.moveRight();
    }

    private void doOneWorldStep(World world) {
        // 60fps
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);
        clearScreen();

        // Render our map
        mapRenderer.render();

        // Load the b2dr debug lines
        b2dr.render(getWorld(), camera.combined);

        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();
        player.draw(game.getSpriteBatch());
        game.getSpriteBatch().end();

        game.getSpriteBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update our viewport after the screen is resized
        viewPort.update(width, height);
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
        // TODO fix the dispose
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public TiledMap getTiledMap() {
        return map;
    }

    @Override
    public GameHud getHud() {
        return hud;
    }

    @Override
    public AssetManager getAssetManager() {
        return game.getAssetManager();
    }

    @Override
    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
}
