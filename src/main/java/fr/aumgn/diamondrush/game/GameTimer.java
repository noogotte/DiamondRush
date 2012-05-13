package fr.aumgn.diamondrush.game;

import fr.aumgn.bukkitutils.timer.Timer;
import fr.aumgn.diamondrush.DiamondRush;

public class GameTimer extends Timer {

    private Game game;

    public GameTimer(DiamondRush dr, int seconds, Game game, Runnable runnable) {
        super(dr.getPlugin(), 
                dr.getConfig().getTimerConfig(), seconds, runnable);
        this.game = game;
    }

    @Override
    public void sendTimeMessage(String string) {
        game.sendMessage(string);
    }
}
