package fr.aumgn.diamondrush.stage;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.diamondrush.Util;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public abstract class JoinStage extends Stage {

    public JoinStage(Game game) {
        super(game);
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>emptyList();
    }

    @Override
    public void start() {
        Util.broadcast(ChatColor.YELLOW + "Une nouvelle partie de Diamond Rush va commencer !");
        Util.broadcast(ChatColor.YELLOW + "Equipes : ");
        for (Team team : game.getTeams()) {
            Util.broadcast(" - " + team.getDisplayName());
        }
    }

    public abstract boolean contains(Player player);

    public abstract void addPlayer(Player player, Team team);

    public abstract void removePlayer(Player player);

    public abstract void ensureIsReady();

    public void prepare() {
    }
}
