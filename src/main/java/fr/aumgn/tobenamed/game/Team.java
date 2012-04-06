package fr.aumgn.tobenamed.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.aumgn.tobenamed.region.TeamSpawn;
import fr.aumgn.tobenamed.region.Totem;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class Team {

    private String name;
    private Player foreman;
    private List<Player> players;
    private Totem totem;
    private TeamSpawn spawn;
    private int lives;

    public Team(String name) {
        this.name = name;
        this.foreman = null;
        this.players = new ArrayList<Player>();
        this.totem = null;
        this.spawn = null;
        this.lives = 5;
    }

    public String getName() {
        return name;
    }

    public Player getForeman() {
        return foreman;
    }

    public void setForeman(Player foreman) {
        this.foreman = foreman;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Totem getTotem() {
        return totem;
    }

    public void setTotem(Vector pos, int worldHeight) {
        totem = new Totem(pos, worldHeight);
    }

    public TeamSpawn getSpawn() {
        return spawn;
    }

    public void setSpawn(Vector pos, int worldHeight) {
        spawn = new TeamSpawn(pos, worldHeight);
    }

    public void sendMessage(String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public int size() {
        return players.size();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public int getLives() {
        return lives;
    }

    public void looseLife() {
        if (--lives == 0) {
            TBNUtil.broadcast("L'equipe " + name + " a perdu.");
        }
    }
}
