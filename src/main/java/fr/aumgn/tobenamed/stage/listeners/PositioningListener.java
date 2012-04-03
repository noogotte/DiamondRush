package fr.aumgn.tobenamed.stage.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.PositioningStage;

public class PositioningListener implements Listener {

    private PositioningStage stage;

    public PositioningListener(PositioningStage stage) {
        this.stage = stage;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (event.getBlock().getType() == stage.getMaterial()) {
            Team team = stage.getGame().getTeam(event.getPlayer());
            if (team == null) {
                return;
            }
            stage.getBlocks().put(team, block);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block =  event.getClickedBlock();
        if (block.getType() != stage.getMaterial()) {
            return;
        }

        Team team = stage.getGame().getTeam(event.getPlayer());
        if (!stage.getBlocks().containsKey(team)) {
            return;
        }

        if (block.equals(stage.getBlocks().get(team))) {
            event.setCancelled(true);
            event.getPlayer().getInventory().addItem(
                    new ItemStack(stage.getMaterial()));
            block.setType(Material.AIR);
            stage.getBlocks().remove(team);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        if (entity instanceof Player && stage.getGame().contains((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!stage.getGame().contains(event.getPlayer())) {
            return;
        }

        ItemStack stack = event.getItemDrop().getItemStack();
        if (stack.getType() == stage.getMaterial()) {
            event.setCancelled(true);
        }
    }
}
