package fr.aumgn.tobenamed.stage.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;

public class PauseListener implements Listener {

    private Game game;
    private Map<Player, Location> positions;

    public PauseListener(Game game) {
        this.game = game;
        this.positions = new HashMap<Player, Location>();
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                positions.put(player, player.getLocation());
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getFrom();
        if (from.getBlockX() != to.getBlockX()
                || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ()) {
            if (game.contains(player)) {
                event.setTo(positions.get(player));
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (game.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (game.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (game.contains(player)) {
            event.setCancelled(true);
        }
    }
}
