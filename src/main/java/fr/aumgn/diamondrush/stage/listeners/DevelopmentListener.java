package fr.aumgn.diamondrush.stage.listeners;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.aumgn.bukkitutils.geom.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class DevelopmentListener implements Listener {

    public DiamondRush dr;

    public DevelopmentListener(DiamondRush dr) {
        this.dr = dr;
    }

    @EventHandler(priority = EventPriority.MONITOR)
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
        Game game = dr.getGame();
        if (!game.contains(damager) || !game.contains(target)) {
            return;
        }

        Team damagerTeam = game.getTeam(damager);
        if (game.getTeam(target) == damagerTeam) {
            return;
        }

        if (!target.getWorld().equals(game.getWorld())) {
            return;
        }

        Vector targetPos = new Vector(damager.getLocation());
        double totemDistanceSq = damagerTeam.getTotem().
                getMiddle().distanceSq(targetPos);
        if (totemDistanceSq < dr.getConfig().getSpottedTotemDistance()) {
            handleSpottedPlayer(damager, target);
            return;
        }

        double spawnDistanceSq = damagerTeam.getSpawn().
                getMiddle().distanceSq(targetPos);
        if (spawnDistanceSq < dr.getConfig().getSpottedSpawnDistance()) {
            handleSpottedPlayer(damager, target);
        }
    }

    private void handleSpottedPlayer(Player player, Player target) {
        Game game = dr.getGame();
        Team team = game.getTeam(target);
        target.teleport(team.getSpawn().getTeleportLocation(game.getWorld(), game.getSpawn()));
        game.sendMessage(target.getDisplayName() + ChatColor.YELLOW +
                " s'est fait voir par " + ChatColor.RESET + player.getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Game game = dr.getGame();
        if (game.contains(player)) {
            String message = event.getMessage();
            if (message.charAt(0) == '!') {
                event.setMessage(message.substring(1));
            } else {
                event.setMessage(ChatColor.ITALIC + event.getMessage());
                Set<Player> receivers = event.getRecipients();
                receivers.clear();
                Team team = game.getTeam(player);
                receivers.addAll(team.getPlayers());
                receivers.addAll(game.getSpectators().asCollection());
            }
        }
    }
}
