package fr.aumgn.tobenamed.game;

import fr.aumgn.tobenamed.util.Timer;

public class GameTimer extends Timer {

    private Game game;

    public GameTimer(int seconds, Game game, Runnable runnable) {
        super(seconds, runnable);
        this.game = game;
    }

    @Override
    public void sendTimeMessage(String string) {
        game.sendMessage(string);
    }
}
