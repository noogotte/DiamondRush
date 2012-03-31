package fr.aumgn.tobenamed.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.aumgn.tobenamed.exception.NoSuchTeam;
import fr.aumgn.tobenamed.exception.NotEnoughTeams;
import fr.aumgn.tobenamed.exception.PlayerNotInGame;
import fr.aumgn.tobenamed.region.GameSpawn;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class Game {

    private World world;
    private GameSpawn spawn;
    private Map<String, Team> teams;
    private Map<Player, Team> players;
    private Set<Player> spectators;

    public Game(List<String> teamsName, World world, Vector spawnPoint) {
        teams = new LinkedHashMap<String, Team>();
        for (String teamName : teamsName) {
            teams.put(teamName, new Team(teamName));
        }
        if (teams.keySet().size() < 2) {
            throw new NotEnoughTeams();
        }
        this.world = world;
        spawn = new GameSpawn(spawnPoint);
        players = new HashMap<Player, Team>();
        spectators = new HashSet<Player>();
    }

    public World getWorld() {
        return world;
    }

    public GameSpawn getSpawn() {
        return spawn;
    }

    public Team getTeam(String name) {
        if (!teams.containsKey(name)) {
            throw new NoSuchTeam(name);
        }
        return teams.get(name);
    }

    public Team getTeam(Player player) {
        if (!players.containsKey(player)) {
            throw new PlayerNotInGame();
        }
        return players.get(player);
    }

    public void sendMessage(String message) {
        for (Team team : getTeams()) {
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

    public List<Team> getTeams() {
        return new ArrayList<Team>(teams.values());
    }

    public void addPlayer(Player player) {
        int minimum = Integer.MAX_VALUE;
        List<Team> roulette = null; 
        for (Team team : getTeams()) {
            int size = team.size();
            if (size < minimum) {
                minimum = size;
                roulette = new ArrayList<Team>();
                roulette.add(team);
            } else if (size == minimum) {
                roulette.add(team);
            } 
        }

        Team team = TBNUtil.pickRandom(roulette);
        addPlayer(player, team);
    }

    public void addPlayer(Player player, Team team) {
        team.addPlayer(player);
        players.put(player, team);
        sendMessage(ChatColor.GOLD + player.getDisplayName() + 
                ChatColor.GREEN + " a rejoint l'équipe " + 
                ChatColor.GOLD + team.getName());
    }

    public boolean hasPlayer(Player player) {
        return players.containsKey(player);
    }

    public Iterable<Player> spectators() {
        return spectators;
    }

    public boolean hasSpectator(Player player) {
        return spectators.contains(player);
    }

    public void addSpectator(Player player) {
        spectators.add(player);
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }
}
