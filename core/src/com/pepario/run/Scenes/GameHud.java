package com.pepario.run.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pepario.run.Config.Config;
import com.pepario.run.Tools.PreferenceManager;

public class GameHud implements Disposable
{
    private PreferenceManager manager;

    private Stage stage;
    private Label scoreLabel;
    private Label coinLabel;
    private Label scoreCountLabel;
    private Label coinCountLabel;

    private int scoreCount;
    private float timeCount;

    public GameHud(SpriteBatch spriteBatch)
    {
        scoreCount = 0;
        timeCount = 0;
        manager = new PreferenceManager();

        Viewport viewPort = new FitViewport(Config.WIDTH, Config.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, spriteBatch);

        loadHudElementsOnStage(stage);
    }

    private void initHudElements()
    {
        scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinLabel = new Label("Coins", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreCountLabel = new Label(String.format("%06d", scoreCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinCountLabel = new Label(String.format("%06d", manager.getCoins()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    }

    private void loadHudElementsOnStage(Stage stage)
    {
        initHudElements();

        Table table = new Table();
        table.top(); // Move the table to the tob of our stage
        table.setFillParent(true); // This makes the table the size of our stage

        table.add(scoreLabel).padTop(10f);
        table.row();
        table.add(scoreCountLabel);

        Table table2 = new Table();
        table2.top(); // Move the table to the tob of our stage
        table2.left();
        table2.setFillParent(true); // This makes the table the size of our stage

        table2.add(coinLabel).padTop(10f).padLeft(10f);
        table2.row();
        table2.add(coinCountLabel).padLeft(10f);

        stage.addActor(table);
        stage.addActor(table2);
    }

    public void update(float deltaTime)
    {
        timeCount += deltaTime;

        // Every 1 second update the score
        if(timeCount >= 1) {
            timeCount = 0;
            updateScore(Config.SCORE_PER_INTERVAL);
        }
    }

    public void updateScore(int value)
    {
        scoreCount += value;
        scoreCountLabel.setText(String.format("%06d", scoreCount));
    }

    public void reloadCoinValue()
    {
        coinCountLabel.setText(String.format("%06d", manager.getCoins()));
    }

    public Stage getStage()
    {
        return stage;
    }

    public int getScoreCount()
    {
        return scoreCount;
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}
