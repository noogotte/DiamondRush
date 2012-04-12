package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.listeners.DevelopmentListener;

public class DevelopmentStage extends Stage {

    private DevelopmentListener listener;

    public DevelopmentStage(Game game) {
        super(game);
        this.listener = new DevelopmentListener(this);
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public void start() {
        game.sendMessage(ChatColor.GREEN + "La phase de développement commence.");
        game.incrementTurnCount();
        int duration = TBN.getConfig().getDevDuration(game.getTurnCount());
        scheduleNextStageWithTransition(duration, new FightStage(game));
    }

    @Override
    public void stop() {
        game.sendMessage(ChatColor.GREEN + "Fin de la phase de développement.");
    }
}
