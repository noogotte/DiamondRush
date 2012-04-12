package fr.aumgn.tobenamed.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.GameTimer;
import fr.aumgn.tobenamed.util.Timer;

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

    public void scheduleNextStage(int seconds, final Stage nextStage) {
        nextStageTimer  = new GameTimer(seconds, game, new Runnable() {
            public void run() {
                game.nextStage(nextStage);
            }
        });
        nextStageTimer.run();
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
