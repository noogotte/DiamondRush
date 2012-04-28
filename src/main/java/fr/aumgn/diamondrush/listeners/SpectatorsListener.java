package fr.aumgn.diamondrush.listeners;

import org.bukkit.GameMode;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.spectators.DRSpectatorJoinEvent;
import fr.aumgn.diamondrush.event.spectators.DRSpectatorQuitEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Spectators;
import fr.aumgn.diamondrush.game.Team;

public class SpectatorsListener implements Listener {

    private Game game;
    private Spectators spectators;

    public SpectatorsListener(DiamondRush diamondRush) {
        this.game = diamondRush.getGame();
        this.spectators = diamondRush.getGame().getSpectators();
    }

    private boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpectatorJoin(DRSpectatorJoinEvent event) {
        Player spectator = event.getSpectator();
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.hidePlayer(spectator);
            }
        }
        spectator.setAllowFlight(true);
        spectator.setFlying(true);
        spectator.setSleepingIgnored(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpectatorQuit(DRSpectatorQuitEvent event) {
        Player spectator = event.getSpectator();
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.showPlayer(spectator);
            }
        }
        spectator.setAllowFlight(false);
        spectator.setFlying(false);
        spectator.setSleepingIgnored(false);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerJoinGame(DRPlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (isSpectator(event.getPlayer())) {
            player.sendMessage(ChatColor.RED +
                    "Vous ne pouvez pas rejoindre la partie tant que vous êtes spectateur.");
            event.setCancelled(true);
        }
    }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onRealPlayerJoinGame(DRPlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player spectator : spectators) {
            player.hidePlayer(spectator);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (!isSpectator(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            BlockState state = event.getClickedBlock().getState();
            if (state instanceof InventoryHolder) {
                Inventory inventory = ((InventoryHolder) state).getInventory();
                event.getPlayer().openInventory(inventory);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!isSpectator(event.getPlayer())) {
            return;
        }

        Entity clicked = event.getRightClicked();
        if (clicked instanceof InventoryHolder) {
            Inventory inventory = ((InventoryHolder) clicked).getInventory();
            event.getPlayer().openInventory(inventory);
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        if (entity instanceof Player && isSpectator((Player) entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && isSpectator((Player) entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof Player && isSpectator((Player) entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getTarget();
        if (entity instanceof Player && isSpectator((Player) entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        if (event.getView().getType() == InventoryType.PLAYER &&
                player.getGameMode() == GameMode.CREATIVE) {
            // Returns or bad things would happen
            return;
        }

        if (isSpectator(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDropItem(PlayerDropItemEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEmptyBucket(PlayerBucketEmptyEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFillBucket(PlayerBucketFillEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onProjectileThrow(ProjectileLaunchEvent event) {
        LivingEntity shooter = event.getEntity().getShooter();
        if (shooter instanceof Player && isSpectator((Player) shooter)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onShootBow(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && isSpectator((Player) entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(PlayerChatEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.getRecipients().clear();
            event.getRecipients().addAll(spectators.asCollection());
        }
    }
}
