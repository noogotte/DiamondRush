package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.TBNUtil;

public class JoinStage extends Stage {

    private boolean random;
    private Game game;

    public JoinStage(Game game, boolean random) {
        this.random = random;
        this.game = game;
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>emptyList();
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void start() {
        TBNUtil.broadcast(ChatColor.GREEN + "Une nouvelle partie de TBN va commencer !");
        TBNUtil.broadcast(ChatColor.GREEN + "Equipes : ");
        for (Team team : game.getTeams()) {
            TBNUtil.broadcast(" - " + ChatColor.GOLD + team.getName());
        }
    }

    public boolean isRandom() {
        return random;
    }

}
