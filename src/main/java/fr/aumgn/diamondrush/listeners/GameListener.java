package fr.aumgn.diamondrush.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.aumgn.bukkit.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class GameListener implements Listener {

    private boolean handleMove = false;
    private Set<Player> playersInSpawn = new HashSet<Player>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Game game = DiamondRush.getGame();
        if (game.contains(player)) {
            for (Player spectator : game.getSpectators()) {
                player.hidePlayer(spectator);
            }
            Team team = game.getTeam(player);
            team.setTeamName(player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Game game = DiamondRush.getGame();
        if (!game.contains(player)) {
            return;
        }

        Team team = game.getTeam(player);
        Location loc;
        if (team.getSpawn() == null) {
            Vector currentPos = new Vector(player.getLocation());
            loc = game.getSpawn().getTeleportLocation(
                    game.getWorld(), currentPos);
        } else {
            loc = team.getSpawn().getTeleportLocation(
                    game.getWorld(), game.getSpawn());
            playersInSpawn.add(player);
            handleMove = true;
        }
        event.setRespawnLocation(loc);

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!handleMove) {
            return;
        }

        Game game = DiamondRush.getGame();
        if (game.isPaused()) {
            return;
        }

        Player player = event.getPlayer();
        if (!playersInSpawn.contains(player)) {
            return;
        }

        if (!game.contains(player)) {
            playersInSpawn.remove(player);
            handleMove = playersInSpawn.size() > 0;
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()
                && to.getWorld().equals(game.getWorld())) {
            return;
        }

        Vector toPos = new Vector(to);
        if (!game.getTeam(player).getSpawn().contains(toPos)) {
            playersInSpawn.remove(player);
            handleMove = playersInSpawn.size() > 0;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        Entity targetEntity = event.getEntity();

        if (!(targetEntity instanceof Player)) {
            return;
        }

        Player target = (Player) targetEntity;
        if (playersInSpawn.contains(target)) {
            event.setCancelled(true);
            return;
        }

        if (!(event instanceof EntityDamageByEntityEvent)) {
            return;
        }

        Entity damagerEntity = ((EntityDamageByEntityEvent) event).getDamager();
        if (!(damagerEntity instanceof Player)) {
            return;
        }

        Player damager = (Player) damagerEntity;
        Game game = DiamondRush.getGame();
        if ((game.contains(target) != game.contains(damager))) {
            event.setCancelled(true);
        }
    }
}
