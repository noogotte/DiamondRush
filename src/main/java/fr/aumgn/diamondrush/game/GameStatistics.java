package fr.aumgn.diamondrush.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class GameStatistics extends Statistics {

    private final Map<Team, Statistics> statsByTeam;
    private final Map<Player, Statistics> statsByPlayer;

    public GameStatistics() {
        this.statsByTeam = new HashMap<Team, Statistics>();
        this.statsByPlayer = new HashMap<Player, Statistics>();
    }

    public void initTeam(Team team) {
        if (!statsByTeam.containsKey(team)) {
            statsByTeam.put(team, new Statistics());
        }
    }

    public void initPlayer(Player player) {
        if (!statsByPlayer.containsKey(player)) {
            statsByPlayer.put(player, new Statistics());
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

    public Statistics get(Team team) {
        return statsByTeam.get(team);
    }

    public Statistics get(Player player) {
        return statsByPlayer.get(player);
    }

    public List<Player> getMenOfTheGame() {
        List<Player> menOfTheGame = new ArrayList<Player>();
        int maxScore = Integer.MIN_VALUE;
        for (Map.Entry<Player, Statistics> playerStats : statsByPlayer.entrySet()) {
            int score = playerStats.getValue().getScore();
            if (score > maxScore) {
                maxScore = score;
                menOfTheGame = new ArrayList<Player>();
                menOfTheGame.add(playerStats.getKey());
            } else if (score == maxScore) {
                menOfTheGame.add(playerStats.getKey());
            }
        }

        return menOfTheGame;
    }
}
