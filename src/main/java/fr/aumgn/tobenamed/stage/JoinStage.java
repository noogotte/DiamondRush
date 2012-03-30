package fr.aumgn.tobenamed.stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.TBNUtil;

public class JoinStage extends Stage {

    private boolean random;
    private HashSet<Player> players;
    private Map<String, List<Player>> teams;

    public JoinStage(List<String> teams, boolean random) {
        this.random = random;
        this.players = new HashSet<Player>();
        this.teams = new HashMap<String, List<Player>>();
        for (String team : teams) {
            this.teams.put(team, new ArrayList<Player>());
        }
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>emptyList();
    }

    @Override
    public void start() {
        TBNUtil.broadcast(ChatColor.GREEN + "Une nouvelle partie de TBN va commencer !");
        TBNUtil.broadcast(ChatColor.GREEN + "Equipes : ");
        for (String team : teams.keySet()) {
            TBNUtil.broadcast(ChatColor.GOLD + "  -" + team);
        }
    }

    public boolean isRandom() {
        return random;
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public boolean containsTeam(String team) {
        return teams.get(team) != null;
    }

    public void addPlayer(Player player) {
        int minimum = Integer.MAX_VALUE;
        List<String> roulette = null; 
        for (Map.Entry<String, List<Player>> team : teams.entrySet()) {
            int size = team.getValue().size();
            if (size < minimum) {
                roulette = new ArrayList<String>();
                roulette.add(team.getKey());
            } else {
                roulette.add(team.getKey());
            } 
        }

        int index = TBN.getRandom().nextInt(roulette.size());
        addPlayer(player, roulette.get(index));
    }

    public void addPlayer(Player player, String team) {
        players.add(player);
        teams.get(team).add(player);

        TBNUtil.broadcast(ChatColor.GOLD + player.getDisplayName() + 
                ChatColor.GREEN + " a rejoint l'équipe " + 
                ChatColor.GOLD + team);
    }
}
