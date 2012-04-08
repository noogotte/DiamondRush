package fr.aumgn.tobenamed.listeners;

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
import fr.aumgn.tobenamed.util.Vector2D;

public class GameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!TBN.isRunning()) {
            return;
        }

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
        if (!TBN.isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Game game = TBN.getGame();
        if (!game.contains(player)) {
            return;
        }

        Team team = game.getTeam(player);
        if (team.getSpawn() == null) {
            Vector2D currentPos = new Vector(player.getLocation()).to2D();
            Vector pos = game.getSpawn().getMiddle();
            Vector2D dir = currentPos.subtract(pos.to2D());
            event.setRespawnLocation(pos.toLocation(game.getWorld(), dir));
        } else {
            Vector pos = team.getSpawn().getMiddle();
            Vector2D dir = game.getSpawn().getMiddle().to2D().subtract(pos.to2D());
            event.setRespawnLocation(pos.toLocation(game.getWorld(), dir));
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
