package fr.aumgn.tobenamed.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.GameTimer;
import fr.aumgn.tobenamed.util.Timer;

public abstract class Stage {

    private Timer nextStageTimer = null;

    public abstract List<Listener> getListeners();

    public abstract Game getGame();

    public boolean hasNextStageScheduled() {
        return nextStageTimer != null;
    }

    public void scheduleNextStage(int seconds, Runnable nextStage) {
        nextStageTimer  = new GameTimer(seconds, getGame(), nextStage);
        nextStageTimer.run();
    }

    public void scheduleNextStageWithTransition(int seconds, final Runnable nextStage) {
        scheduleNextStage(seconds, new Runnable() {
            public void run() {
                new TransitionStage(getGame(), nextStage);
            }
        });
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
