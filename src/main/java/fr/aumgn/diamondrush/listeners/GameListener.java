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

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.team.DRTeamSpawnSetEvent;
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
            Team team = game.getTeam(player);
            team.setTeamName(player);
            team.setCompassTarget(player, game.getWorld());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoinGame(DRPlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = event.getTeam();
        Vector pos;
        if (team.getTotem() != null) {
            pos = team.getTotem().getTeleportPoint();
        } else {
            pos = new Vector(team.getForeman().getLocation());
        } 
        player.teleport(pos.toLocation(DiamondRush.getGame().getWorld()));
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawnSetEvent(DRTeamSpawnSetEvent event) {
        for (Player player : event.getTeam().getPlayers()) {
            Location target = event.getRegion().getMiddle()
                    .toLocation(DiamondRush.getGame().getWorld());
            player.setCompassTarget(target);
        }
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
