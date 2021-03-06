package fr.aumgn.diamondrush.game;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.playerref.map.PlayersRefHashMap;
import fr.aumgn.bukkitutils.playerref.map.PlayersRefMap;
import fr.aumgn.bukkitutils.playerref.set.PlayersRefHashSet;
import fr.aumgn.bukkitutils.playerref.set.PlayersRefSet;
import fr.aumgn.bukkitutils.util.Util;
import fr.aumgn.bukkitutils.geom.Vector;
import fr.aumgn.diamondrush.exception.NoSuchTeam;
import fr.aumgn.diamondrush.exception.NotEnoughTeams;
import fr.aumgn.diamondrush.exception.PlayerNotInGame;
import fr.aumgn.diamondrush.region.GameSpawn;

public class Game {

    private World world;
    private GameSpawn spawn;
    private Map<String, Team> teams;
    private PlayersRefSet players;
    private PlayersRefMap<Team> playersTeam;
    private Spectators spectators;
    private int turnCount;

    public Game(Map<String, TeamColor> teamsMap, World world, Vector spawnPoint, int lives) {
        this.teams = new LinkedHashMap<String, Team>();

        for (Map.Entry<String, TeamColor> teamEntry : teamsMap.entrySet()) {
            Team team = new Team(teamEntry.getKey(),
                    teamEntry.getValue(), lives);
            teams.put(teamEntry.getKey(), team);
        }
        if (teams.keySet().size() < 2) {
            throw new NotEnoughTeams();
        }

        this.world = world;
        spawn = GameSpawn.newFromTeamsNumber(spawnPoint,
                teams.values().size());
        players = new PlayersRefHashSet();
        playersTeam = new PlayersRefHashMap<Team>();
        spectators = new Spectators();
        turnCount = -1;
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
        if (!playersTeam.containsKey(player)) {
            throw new PlayerNotInGame();
        }

        return playersTeam.get(player);
    }

    public Spectators getSpectators() {
        return spectators;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void incrementTurnCount() {
        ++turnCount;
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
        return players.contains(player);
    }

    public List<Team> getTeams() {
        return new ArrayList<Team>(teams.values());
    }

    public Team getTeamWithMinimumPlayers() {
        int minimum = Integer.MAX_VALUE;
        List<Team> roulette = null; 
        for (Team team : getTeams()) {
            int size = 0;
            for (Player player : team.getPlayers()) {
                if (player.isOnline()) {
                    size++;
                }
            }

            if (size < minimum) {
                minimum = size;
                roulette = new ArrayList<Team>();
                roulette.add(team);
            } else if (size == minimum) {
                roulette.add(team);
            } 
        }

        return Util.pickRandom(roulette);
    }

    public void addPlayer(Player player, Team team) {
        team.addPlayer(player);
        players.add(player);
        playersTeam.put(player, team);
    }

    public void removePlayer(Player player) {
        Team team = getTeam(player);
        team.removePlayer(player);
        players.remove(player.getName());
        playersTeam.remove(player);
    }

    public void removeTeam(Team team) {
        teams.remove(team.getName());
        for (Player player : team.getPlayers()) {
            players.remove(player.getName());
            playersTeam.remove(player);
        }
    }

    public Location getRespawnFor(Player player) {
        return getRespawnFor(player, playersTeam.get(player));
    }

    public Location getRespawnFor(Player player, Team team) {
        if (team.getSpawn() != null) {
            return team.getSpawn().getTeleportLocation(world, spawn);
        }
        if (team.getTotem() != null) {
            return team.getTotem().getTeleportLocation(world, spawn);
        }
        return spawn.getTeleportLocation(world, new Vector(player.getLocation()));
    }
}
