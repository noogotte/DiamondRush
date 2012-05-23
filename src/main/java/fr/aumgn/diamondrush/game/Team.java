package fr.aumgn.diamondrush.game;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.playerid.list.PlayersIdArrayList;
import fr.aumgn.bukkitutils.playerid.list.PlayersIdList;
import fr.aumgn.diamondrush.region.TeamSpawn;
import fr.aumgn.diamondrush.region.Totem;

public class Team {

    private String name;
    private TeamColor color;
    private PlayersIdList players;
    private Totem totem;
    private TeamSpawn spawn;
    private int lives;
    private int surrenders;

    public Team(String name, TeamColor color, int lives) {
        this.name = name;
        this.color = color;
        this.players = new PlayersIdArrayList();
        this.totem = null;
        this.spawn = null;
        this.lives = lives;
        this.surrenders = 0;
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

    public List<Player> getPlayers() {
        return players.getPlayers();
    }

    public Totem getTotem() {
        return totem;
    }

    public void setTotem(Totem totem) {
        this.totem = totem;
    }

    public TeamSpawn getSpawn() {
        return spawn;
    }

    public void setSpawn(TeamSpawn spawn) {
        this.spawn = spawn;
    }

    public int getLives() {
        return lives;
    }

    public int getSurrenders() {
        return surrenders;
    }

    public void sendMessage(String message) {
        for (Player player : players.players()) {
            player.sendMessage(message);
        }
    }

    public int size() {
        return players.size();
    }

    public String getTeamName(Player player) {
        String rawName = ChatColor.stripColor(player.getDisplayName());
        return color.getChatColor() + rawName + ChatColor.RESET;
    }

    void addPlayer(Player player) {
        players.add(player);
    }

    void removePlayer(Player player) {
        players.remove(player);
    }

    public void decreaseLives() {
        --lives;
    }

    public void incrementSurrenders() {
        ++surrenders;
    }
}
