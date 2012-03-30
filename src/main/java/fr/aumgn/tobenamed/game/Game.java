package fr.aumgn.tobenamed.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import fr.aumgn.tobenamed.exception.NoSuchTeam;
import fr.aumgn.tobenamed.exception.NotEnoughTeams;

public class Game {

    private Map<String, Team> teams;
    private Map<Player, Team> players;
    private Set<Player> spectators;

    public Game(List<String> teamsName) {
        teams = new LinkedHashMap<String, Team>();
        for (String teamName : teamsName) {
            teams.put(teamName, new Team(teamName));
        }
        if (teams.keySet().size() < 2) {
            throw new NotEnoughTeams();
        }
        players = new HashMap<Player, Team>();
        spectators = new HashSet<Player>();
    }

    public Team getTeam(String name) {
        if (!teams.containsKey(name)) {
            throw new NoSuchTeam(name);
        }
        return teams.get(name);
    }

    public Team getTeam(Player player) {
        return players.get(player);
    }

    public void sendMessage(String message) {
        for (Team team : teams()) {
            team.sendMessage(message);
        }
        for (Player spectator : spectators) {
            spectator.sendMessage(message);
        }
    }

    public boolean contains(Player player) {
        return players.containsKey(player) 
                || spectators.contains(player);
    }

    public Iterable<Team> teams() {
        return teams.values();
    }

    public void addPlayer(Player player, Team team) {
        team.addPlayer(player);
        players.put(player, team);
    }

    public Iterable<Player> spectators() {
        return spectators;
    }

    public boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    public void addSpectator(Player player) {
        spectators.add(player);
    }
}
