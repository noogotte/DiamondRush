package fr.aumgn.diamondrush.stage;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.Util;
import fr.aumgn.diamondrush.game.Team;

public abstract class JoinStage extends Stage {

    public JoinStage(DiamondRush dr) {
        super(dr);
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>emptyList();
    }

    @Override
    public void start() {
        Util.broadcast(ChatColor.YELLOW + "Une nouvelle partie de Diamond Rush va commencer !");
        Util.broadcast(ChatColor.YELLOW + "Equipes : ");
        for (Team team : dr.getGame().getTeams()) {
            Util.broadcast(" - " + team.getDisplayName());
        }
    }
}
