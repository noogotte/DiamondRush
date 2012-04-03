package fr.aumgn.tobenamed.stage.listeners;

import java.util.Map;

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
import fr.aumgn.tobenamed.util.Vector;

public class PositioningListener implements Listener {

    private PositioningStage stage;
    private Map<Team, Vector> positions;

    public PositioningListener(PositioningStage stage, Map<Team, Vector> positions) {
        this.stage = stage;
        this.positions = positions;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (event.getBlock().getType() == stage.getMaterial()) {
            Team team = stage.getGame().getTeam(event.getPlayer());
            if (team == null) {
                return;
            }
            positions.put(team, new Vector(block));
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
        if (!positions.containsKey(team)) {
            return;
        }

        if (positions.get(team).equals(new Vector(block))) {
            event.setCancelled(true);
            event.getPlayer().getInventory().addItem(
                    new ItemStack(stage.getMaterial()));
            block.setType(Material.AIR);
            positions.remove(team);
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
