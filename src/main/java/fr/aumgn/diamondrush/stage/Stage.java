package fr.aumgn.diamondrush.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.bukkitutils.util.Timer;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.GameTimer;

public abstract class Stage {

    protected Game game;
    private Timer gameTimer = null;

    public Stage(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract List<Listener> getListeners();

    public boolean hasNextStageScheduled() {
        return gameTimer != null;
    }

    public void schedule(int seconds, Runnable runnable) {
        gameTimer  = new GameTimer(seconds, game, runnable);
        gameTimer.run();
    }

    public void scheduleNextStage(int seconds, final Stage nextStage) {
        this.schedule(seconds, new Runnable() {
            public void run() {
                game.nextStage(nextStage);
            }
        });
    }

    public void scheduleNextStageWithTransition(int seconds, Stage nextStage) {
        scheduleNextStage(seconds, new TransitionStage(game, nextStage));
    }

    public void cancelGameTimer() {
        gameTimer.cancel();
    }

    public void start() {
    }

    public void stop() {
    }

    public void pause() {
        if (hasNextStageScheduled()) {
            gameTimer.pause();
        }
    }

    public void resume() {
        if (hasNextStageScheduled()) {
            gameTimer.resume();
        }
    }
}
