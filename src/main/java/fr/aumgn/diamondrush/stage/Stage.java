package fr.aumgn.diamondrush.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.bukkitutils.util.Timer;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.GameTimer;

public abstract class Stage {

    protected DiamondRush dr;
    private Timer gameTimer = null;

    public Stage(DiamondRush dr) {
        this.dr = dr;
    }

    public DiamondRush getDiamondRush() {
        return dr;
    }

    public abstract List<Listener> getListeners();

    public boolean hasNextStageScheduled() {
        return gameTimer != null;
    }

    public void schedule(int seconds, Runnable runnable) {
        gameTimer  = new GameTimer(dr, seconds, dr.getGame(), runnable);
        gameTimer.run();
    }

    public void scheduleNextStage(int seconds, final Stage nextStage) {
        this.schedule(seconds, new Runnable() {
            public void run() {
                dr.nextStage(nextStage);
            }
        });
    }

    public void scheduleNextStageWithTransition(int seconds, Stage nextStage) {
        int duration = dr.getConfig().getTransitionDuration();
        if (duration > 0) {
            scheduleNextStage(seconds, new TransitionStage(
                    dr, nextStage, duration));
        } else {
            scheduleNextStage(seconds, nextStage);
        }
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
