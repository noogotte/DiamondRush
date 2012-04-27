package fr.aumgn.diamondrush.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.entity.Player;

public class Spectators implements Iterable<Player> {

    private Set<Player> spectators;

    public Spectators() {
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
    }

    public void remove(Player spectator) {
        spectators.remove(spectator);
    }

    public Collection<Player> asCollection() {
        return spectators;
    }
}
