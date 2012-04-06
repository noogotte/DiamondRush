package fr.aumgn.tobenamed.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.util.TBNUtil;

public abstract class Stage {

    protected class NextStageTask implements Runnable {
        @Override
        public void run() {
            getGame().nextStage(nextStage());
        }
    }

    public abstract List<Listener> getListeners();

    public abstract Game getGame();

    public void start() {
    }

    public void stop() {
    }

    protected void scheduleNextStage(int ticks) {
        TBNUtil.scheduleDelayed(ticks, new NextStageTask());
    }

    protected Stage nextStage() {
        return null;
    }
}
