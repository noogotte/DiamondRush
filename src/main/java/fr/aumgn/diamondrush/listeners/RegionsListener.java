package fr.aumgn.diamondrush.listeners;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.world.StructureGrowEvent;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.Totem;

public class RegionsListener implements Listener {

    private final DiamondRush dr;

    public RegionsListener(DiamondRush diamondRush) {
        this.dr = diamondRush;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Vector pos = new Vector(event.getBlock());
        Game game = dr.getGame();
        Player player = event.getPlayer();

        if (event.getBlock().getType() == Material.OBSIDIAN &&
                player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
            for (Team team : game.getTeams()) {
                Totem totem = team.getTotem();
                if (totem == null || !totem.isTotemBlock(pos)) {
                    continue;
                }

                if (dr.totemBreak(team, totem, player)) {
                    event.setCancelled(true);
                }
                return;
            }
        }

        if (isProtected(event.getBlock().getWorld(), pos)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (isProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        if (isProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Iterator<Block> it = event.blockList().iterator();
        while (it.hasNext()) {
            Block block = it.next();
            if (isProtected(block)) {
                it.remove();
            }
        }
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        if (isProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onForm(BlockFormEvent event) {
        if (isProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        Iterator<BlockState> it = event.getBlocks().iterator();
        while (it.hasNext()) {
            Block block = it.next().getBlock();
            if (isProtected(block)) {
                it.remove();
            }
        }
        if (event.getBlocks().size() < 1) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        if (isProtected(event.getToBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        Location location = event.getRetractLocation();
        if (isProtected(location.getWorld(), new Vector(location))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (isProtected(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked().
                getRelative(event.getBlockFace());
        if (isProtected(block)) {
            event.setCancelled(true);
        }
    }

    private boolean isProtected(Block block) {
        return isProtected(block.getWorld(), new Vector(block));
    }

    private boolean isProtected(World world, Vector pos) {
        Game game = dr.getGame();

        if (!world.equals(game.getWorld())) {
            return false;
        }

        if (game.getSpawn() != null 
                && game.getSpawn().contains(pos)) {
            return true;
        }

        for (Team team : game.getTeams()) {
            if (team.getSpawn() != null 
                    && team.getSpawn().contains(pos)) {
                return true;
            }
            if (team.getTotem() != null 
                    && team.getTotem().contains(pos)) {
                return true;
            }
        }

        return false;
    }
}
