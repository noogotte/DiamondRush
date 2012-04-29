package fr.aumgn.diamondrush.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Statistics {

    private final Map<Team, Integer> killsByTeam;
    private final Map<Player, Integer> killsByPlayer;
    private final Map<Team, Integer> deathsByTeam;
    private final Map<Player, Integer> deathsByPlayer;

    private final Map<Team, Integer> blocksBrokenByTeam;
    private final Map<Player, Integer> blocksBrokenByPlayer;

    public Statistics() {
        this.killsByTeam = new HashMap<Team, Integer>();
        this.killsByPlayer = new HashMap<Player, Integer>();
        this.deathsByTeam = new HashMap<Team, Integer>();
        this.deathsByPlayer = new HashMap<Player, Integer>();

        this.blocksBrokenByTeam = new HashMap<Team, Integer>();
        this.blocksBrokenByPlayer = new HashMap<Player, Integer>();
    }

    public void initTeam(Team team) {
        killsByTeam.put(team, 0);
        deathsByTeam.put(team, 0);
        blocksBrokenByTeam.put(team, 0);
    }

    public void initPlayer(Player player) {
        killsByPlayer.put(player, 0);
        deathsByPlayer.put(player, 0);
        blocksBrokenByPlayer.put(player, 0);
    }

    public void registerkill(Team killerTeam, Player killer, Team targetTeam, Player target) {
        killsByTeam.put(killerTeam, killsByTeam.get(killerTeam) + 1);
        killsByPlayer.put(killer, killsByPlayer.get(killer) + 1);
        deathsByTeam.put(targetTeam, deathsByTeam.get(targetTeam) + 1);
        deathsByPlayer.put(target, deathsByPlayer.get(target) + 1);
    }

    public void registerTotemBlockBreak(Team team, Player player) {
        blocksBrokenByTeam.put(team, blocksBrokenByTeam.get(team) + 1);
        blocksBrokenByPlayer.put(player, blocksBrokenByPlayer.get(player) + 1);
    }

    public int getKills(Team team) {
        return killsByTeam.get(team);
    }

    public int getKills(Player player) {
        return killsByPlayer.get(player);
    }

    public int getDeaths(Team team) {
        return deathsByTeam.get(team);
    }

    public int getDeaths(Player player) {
        return deathsByPlayer.get(player);
    }

    public int getTotemBlockBroken(Team team) {
        return blocksBrokenByTeam.get(team);
    }

    public int getTotemBlockBroken(Player player) {
        return blocksBrokenByPlayer.get(player);
    }
}
