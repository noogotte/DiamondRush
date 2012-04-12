package fr.aumgn.tobenamed.stage.listeners;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.DevelopmentStage;
import fr.aumgn.tobenamed.util.Vector;

public class DevelopmentListener implements Listener {

    public DevelopmentStage stage;

    public DevelopmentListener(DevelopmentStage stage) {
        this.stage = stage;
    }

    @EventHandler(ignoreCancelled = true)
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
        Game game = stage.getGame();
        if (!game.contains(damager) && !game.contains(target)) {
            return;
        }
        event.setCancelled(true);

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
        int totemDistanceSq = damagerTeam.getTotem().
                getMiddle().distanceSq(targetPos);
        if (totemDistanceSq < TBN.getConfig().getSpottedTotemDistance()) {
            handleSpottedPlayer(damager, target);
            return;
        }

        int spawnDistanceSq = damagerTeam.getSpawn().
                getMiddle().distanceSq(targetPos);
        if (spawnDistanceSq < TBN.getConfig().getSpottedSpawnDistance()) {
            handleSpottedPlayer(damager, target);
        }
    }

    private void handleSpottedPlayer(Player player, Player target) {
        Game game = stage.getGame();
        Team team = game.getTeam(target);
        target.teleport(team.getSpawn().getTeleportLocation(game.getWorld(), game.getSpawn()));
        game.sendMessage(target.getDisplayName() + ChatColor.YELLOW +
                " s'est fait voir par " + ChatColor.RESET + player.getDisplayName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        Game game = stage.getGame();
        if (game.contains(player)) {
            Set<Player> receivers = event.getRecipients();
            receivers.clear();
            Team team = game.getTeam(player);
            receivers.addAll(team.getPlayers());
            receivers.addAll(game.getSpectators().asCollection());
        }
    }
}
