package fr.aumgn.tobenamed.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Spectators;

public class SpectatorsListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEmptyBucket(PlayerBucketEmptyEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFillBucket(PlayerBucketFillEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && isSpectator((Player) entity)) {
            event.setCancelled(true);
        }
    }

    private boolean isSpectator(Player player) {
        return TBN.getGame().getSpectators().contains(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.getRecipients().clear();
            Spectators spectators = TBN.getGame().getSpectators();
            event.getRecipients().addAll(spectators.asCollection());
        }
    }
}
