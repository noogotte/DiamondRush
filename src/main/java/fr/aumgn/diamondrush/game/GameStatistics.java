package fr.aumgn.diamondrush.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.aumgn.diamondrush.exception.NoStatistics;

public class GameStatistics extends Statistics {

    private final Map<String, Statistics> statsByTeam;
    private final Map<String, Statistics> statsByPlayer;

    public GameStatistics() {
        this.statsByTeam = new HashMap<String, Statistics>();
        this.statsByPlayer = new HashMap<String, Statistics>();
    }

    public void initTeam(Team team) {
        if (!statsByTeam.containsKey(team)) {
            statsByTeam.put(team.getName(), new Statistics());
        }
    }

    public void initPlayer(Player player) {
        if (!statsByPlayer.containsKey(player)) {
            statsByPlayer.put(player.getName(), new Statistics());
        }
    }

    public void registerkill(Team killerTeam, Player killer, Team targetTeam, Player target) {
        incrKills();
        incrDeaths();
        get(killerTeam).incrKills();
        get(killer).incrKills();
        get(targetTeam).incrDeaths();
        get(target).incrDeaths();
    }

    public void registerTotemBlockBreak(Team team, Player player) {
        incrBlocksBroken();
        get(team).incrBlocksBroken();
        get(player).incrBlocksBroken();
    }

    public Statistics getTeam(String name) {
        if (!statsByTeam.containsKey(name)) {
            throw NoStatistics.forTeam(name);
        }
        return statsByTeam.get(name);
    }

    public Statistics getPlayer(String name) {
        if (!statsByPlayer.containsKey(name)) {
            throw NoStatistics.forPlayer(name);
        }
        return statsByPlayer.get(name);
    }

    public Statistics get(Team team) {
        return getTeam(team.getName());
    }

    public Statistics get(Player player) {
        return getPlayer(player.getName());
    }


    public List<String> getMenOfTheGame() {
        List<String> menOfTheGame = new ArrayList<String>();
        int maxScore = Integer.MIN_VALUE;
        for (Map.Entry<String, Statistics> playerStats : statsByPlayer.entrySet()) {
            int score = playerStats.getValue().getScore();
            if (score > maxScore) {
                maxScore = score;
                menOfTheGame = new ArrayList<String>();
                menOfTheGame.add(playerStats.getKey());
            } else if (score == maxScore) {
                menOfTheGame.add(playerStats.getKey());
            }
        }

        return menOfTheGame;
    }
}
