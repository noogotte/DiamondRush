package fr.aumgn.tobenamed.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Spectators implements Iterable<Player> {

    private Game game;
    private Set<Player> spectators;

    public Spectators(Game game) {
        this.game = game;
        spectators = new HashSet<Player>();
    }

    @Override
    public Iterator<Player> iterator() {
        return spectators.iterator();
    }

    public boolean contains(Player player) {
        return spectators.contains(player);
    }

    public void add(Player spectator) {
        spectators.add(spectator);
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.hidePlayer(spectator);
            }
        }
        spectator.setGameMode(GameMode.CREATIVE);
        spectator.setSleepingIgnored(true);
    }

    public void remove(Player spectator) {
        spectators.remove(spectator);
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.showPlayer(spectator);
            }
        }
        spectator.setGameMode(GameMode.SURVIVAL);
        spectator.setSleepingIgnored(false);
    }

    public Collection<Player> asCollection() {
        return spectators;
    }
}
