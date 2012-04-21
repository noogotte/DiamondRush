package fr.aumgn.diamondrush.game;

import fr.aumgn.bukkitutils.util.Timer;
import fr.aumgn.diamondrush.DiamondRush;

public class GameTimer extends Timer {

    private Game game;

    public GameTimer(int seconds, Game game, Runnable runnable) {
        super(DiamondRush.getPlugin(), 
                DiamondRush.getConfig().getTimerConfig(), seconds, runnable);
        this.game = game;
    }

    @Override
    public void sendTimeMessage(String string) {
        game.sendMessage(string);
    }
}
