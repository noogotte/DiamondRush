package fr.aumgn.tobenamed.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.util.Timer;

public abstract class Stage {

    public class StageTimer extends Timer {

        public StageTimer(int seconds, Runnable runnable) {
            super(seconds, runnable);
        }

        @Override
        public void sendTimeMessage(String time) {
            getGame().sendMessage(time);
        }
    }

    private Timer nextStageTimer = null;

    public abstract List<Listener> getListeners();

    public abstract Game getGame();

    public void start() {
    }

    public void stop() {
    }

    public void pause() {
        if (nextStageTimer != null) {
            nextStageTimer.pause();
        }
    }

    public void resume() {
        if (nextStageTimer != null) {
            nextStageTimer.resume();
        }
    }

    protected void scheduleNextStage(int seconds, Runnable nextStage) {
        nextStageTimer  = new StageTimer(seconds, nextStage);
    }
}
