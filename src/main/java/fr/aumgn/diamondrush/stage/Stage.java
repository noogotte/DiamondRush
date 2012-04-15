package fr.aumgn.diamondrush.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.GameTimer;
import fr.aumgn.diamondrush.util.Timer;

public abstract class Stage {

    protected Game game;
    protected Timer nextStageTimer = null;

    public Stage(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract List<Listener> getListeners();

    public boolean hasNextStageScheduled() {
        return nextStageTimer != null;
    }

    public void schedule(int seconds, Runnable runnable) {
        nextStageTimer  = new GameTimer(seconds, game, runnable);
        nextStageTimer.run();
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

    public void start() {
    }

    public void stop() {
    }

    public void pause() {
        if (hasNextStageScheduled()) {
            nextStageTimer.pause();
        }
    }

    public void resume() {
        if (hasNextStageScheduled()) {
            nextStageTimer.resume();
        }
    }
}
