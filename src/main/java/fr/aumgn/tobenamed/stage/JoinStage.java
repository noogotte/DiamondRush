package fr.aumgn.tobenamed.stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.TBNUtil;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;

public class JoinStage extends Stage {

    private boolean random;
    private Game game;

    public JoinStage(List<String> teams, boolean random) {
        this.random = random;
        this.game = new Game(teams);
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
        for (Team team : game.teams()) {
            TBNUtil.broadcast(" - " + ChatColor.GOLD + team.getName());
        }
    }

    public void addPlayer(Player player) {
        int minimum = Integer.MAX_VALUE;
        List<Team> roulette = null; 
        for (Team team : game.teams()) {
            int size = team.size();
            if (size < minimum) {
                minimum = size;
                roulette = new ArrayList<Team>();
                roulette.add(team);
            } else if (size == minimum) {
                roulette.add(team);
            } 
        }

        int index = TBN.getRandom().nextInt(roulette.size());
        Team team = roulette.get(index);
        game.addPlayer(player, team);
        game.sendMessage(ChatColor.GOLD + player.getDisplayName() + 
                ChatColor.GREEN + " a rejoint l'équipe " + 
                ChatColor.GOLD + team.getName());
    }

    public void addPlayer(Player player, String teamName) {
        if (random) {
            addPlayer(player);
        } else {
            Team team = game.getTeam(teamName);
            team.addPlayer(player);
            game.addPlayer(player, team);
            game.sendMessage(ChatColor.GOLD + player.getDisplayName() + 
                    ChatColor.GREEN + " a rejoint l'équipe " + 
                    ChatColor.GOLD + team.getName());
        }
    }
}
