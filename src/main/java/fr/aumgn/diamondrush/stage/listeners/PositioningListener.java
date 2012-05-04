package fr.aumgn.diamondrush.stage.listeners;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.PositioningStage;

public class PositioningListener implements Listener {

    private DiamondRush dr;
    private PositioningStage stage;
    private Set<Player> blockToGiveBackAtRespawn;

    public PositioningListener(DiamondRush dr, PositioningStage stage) {
        this.dr = dr;
        this.stage = stage;
        this.blockToGiveBackAtRespawn = new HashSet<Player>();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!dr.getGame().contains(player)) {
            return;
        }

        Iterator<ItemStack> it = event.getDrops().iterator();
        while (it.hasNext()) {
            ItemStack stack = it.next();
            if (stack.getType() == stage.getMaterial()) {
                it.remove();
                blockToGiveBackAtRespawn.add(player);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (blockToGiveBackAtRespawn.contains(player)) {
            stage.giveBlock(player);
            blockToGiveBackAtRespawn.remove(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Game game = dr.getGame();
        if (!game.contains(player)) {
            return;
        }

        Block block = event.getBlock();
        if (event.getBlock().getType() == stage.getMaterial()) {
            Team team = game.getTeam(event.getPlayer());
            stage.setPosition(team, new Vector(block));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = dr.getGame();
        if (!game.contains(player)) {
            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block =  event.getClickedBlock();
        if (block.getType() != stage.getMaterial()) {
            return;
        }

        if (!game.contains(player)) {
            return;
        }

        Team team = game.getTeam(player);
        if (!stage.isPosition(team, new Vector(block))) {
            return;
        }

        event.setCancelled(true);
        PlayerInventory inventory = event.getPlayer().getInventory();
        if (inventory.getItemInHand().getType() != Material.AIR) {
            inventory.addItem(new ItemStack(stage.getMaterial()));
        } else {
            inventory.setItemInHand(new ItemStack(stage.getMaterial()));
        }
        block.setType(Material.AIR);
        stage.setPlayerHoldingBlock(team, player);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        if (entity instanceof Player 
                && dr.getGame().contains((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!dr.getGame().contains(event.getPlayer())) {
            return;
        }

        ItemStack stack = event.getItemDrop().getItemStack();
        if (stack.getType() == stage.getMaterial()) {
            event.setCancelled(true);
        }
    }
}
