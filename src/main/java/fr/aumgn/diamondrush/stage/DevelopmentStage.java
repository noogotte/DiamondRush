package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.stage.listeners.NoPVPListener;
import fr.aumgn.diamondrush.stage.listeners.DevelopmentListener;

public class DevelopmentStage extends Stage {

    private List<Listener> listeners;

    public DevelopmentStage(Game game) {
        super(game);
        this.listeners = new ArrayList<Listener>();
        listeners.add(new NoPVPListener(this));
        listeners.add(new DevelopmentListener(this));
    }

    @Override
    public List<Listener> getListeners() {
        return listeners;
    }

    @Override
    public void start() {
        game.sendMessage(ChatColor.GREEN + "La phase de développement commence.");
        game.incrementTurnCount();
        int duration = DiamondRush.getConfig().getDevDuration(game.getTurnCount());
        scheduleNextStageWithTransition(duration, new FightStage(game));
    }

    @Override
    public void stop() {
        game.sendMessage(ChatColor.GREEN + "Fin de la phase de développement.");
    }
}
