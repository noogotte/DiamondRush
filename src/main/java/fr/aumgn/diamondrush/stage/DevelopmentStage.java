package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.stage.listeners.NoPVPListener;
import fr.aumgn.diamondrush.stage.listeners.DevelopmentListener;

public class DevelopmentStage extends Stage {

    private List<Listener> listeners;

    public DevelopmentStage(DiamondRush dr) {
        super(dr);
        this.listeners = new ArrayList<Listener>();
        listeners.add(new NoPVPListener(dr.getGame()));
        listeners.add(new DevelopmentListener(dr));
    }

    @Override
    public List<Listener> getListeners() {
        return listeners;
    }

    @Override
    public void start() {
        dr.getGame().sendMessage(ChatColor.GREEN + "La phase de développement commence.");
        dr.getGame().incrementTurnCount();
        int duration = dr.getConfig().getDevDuration(dr.getGame().getTurnCount());
        scheduleNextStageWithTransition(duration, new FightStage(dr));
    }

    @Override
    public void stop() {
        dr.getGame().sendMessage(ChatColor.GREEN + "Fin de la phase de développement.");
    }
}
