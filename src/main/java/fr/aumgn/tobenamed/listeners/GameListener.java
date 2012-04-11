package fr.aumgn.tobenamed.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.Vector;

public class GameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Game game = TBN.getGame();
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
        Game game = TBN.getGame();
        if (!game.contains(player)) {
            return;
        }

        Team team = game.getTeam(player);
        if (team.getSpawn() == null) {
            Vector currentPos = new Vector(player.getLocation());
            Location loc = game.getSpawn().getTeleportLocation(game.getWorld(), currentPos);
            event.setRespawnLocation(loc);
        } else {
            team.getSpawn().getTeleportLocation(game.getWorld(), game.getSpawn());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity targetEntity = event.getEntity();
        Entity damagerEntity = event.getDamager();
        if (damagerEntity instanceof Projectile) {
            damagerEntity = ((Projectile) damagerEntity).getShooter();
        }

        if (!(targetEntity instanceof Player)
                || !(damagerEntity instanceof Player)) {
            return;
        }

        Player target = (Player) targetEntity;
        Player damager = (Player) damagerEntity;
        Game game = TBN.getGame();
        if ((game.contains(target) && !game.contains(damager))
                || (!game.contains(target) && game.contains(damager))) {
            event.setCancelled(true);
        }
    }
}
