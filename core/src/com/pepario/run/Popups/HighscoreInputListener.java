package com.pepario.run.Popups;

import com.badlogic.gdx.Input;
import com.pepario.run.Tools.LeaderboardManager;

public class HighscoreInputListener implements Input.TextInputListener
{
    private String leaderboardKey;
    private int score;

    public HighscoreInputListener(String leaderboardKey, int score)
    {
        this.leaderboardKey = leaderboardKey;
        this.score = score;
    }

    @Override
    public void input(String text)
    {
        LeaderboardManager.addLine(this.leaderboardKey, text, score);
    }

    @Override
    public void canceled()
    {
    }
}
