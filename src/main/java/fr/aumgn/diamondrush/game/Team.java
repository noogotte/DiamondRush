package fr.aumgn.diamondrush.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.Util;
import fr.aumgn.diamondrush.region.TeamSpawn;
import fr.aumgn.diamondrush.region.Totem;

public class Team {

    private String name;
    private TeamColor color;
    private Player foreman;
    private List<Player> players;
    private Totem totem;
    private TeamSpawn spawn;
    private int lives;

    public Team(String name, TeamColor color, int lives) {
        this.name = name;
        this.color = color;
        this.foreman = null;
        this.players = new ArrayList<Player>();
        this.totem = null;
        this.spawn = null;
        this.lives = lives;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return color.getChatColor() + name + ChatColor.RESET;
    }

    public TeamColor getColor() {
        return color;
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

    public void setSpawn(Vector pos, World world) {
        spawn = new TeamSpawn(pos);
        for (Player player: players) {
            setCompassTarget(player, world);
        }
    }

    public int getLives() {
        return lives;
    }

    public void sendMessage(String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public int size() {
        return players.size();
    }

    public void setTeamName(Player player) {
        String rawName = ChatColor.stripColor(player.getDisplayName());
        String teamName = color.getChatColor() + rawName + ChatColor.RESET;
        player.setDisplayName(teamName);
        if (teamName.length() > 16) {
            player.setPlayerListName(teamName.substring(0, 16));
        } else {
            player.setPlayerListName(teamName);
        }
    }

    public void setCompassTarget(Player player, World world) {
        Location compassTarget = spawn.getMiddle().toLocation(world);
        player.setCompassTarget(compassTarget);
    }

    void addPlayer(Player player, World world) {
        players.add(player);
        setTeamName(player);
        if (spawn != null) {
            setCompassTarget(player, world);
        }
    }

    void removePlayer(Player player) {
        players.remove(player);
        if (player.equals(foreman)) {
            foreman = Util.pickRandom(players);
            if (foreman != null) {
                sendMessage(foreman.getDisplayName() + 
                    " est maintenant le chef d'equipe.");
            }
        }
    }

    void decreaseLives() {
        --lives;
    }
}
