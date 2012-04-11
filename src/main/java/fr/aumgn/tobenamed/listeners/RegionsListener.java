package fr.aumgn.tobenamed.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
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

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.Vector;

public class RegionsListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Vector pos = new Vector(event.getBlock());
        Game game = TBN.getGame();
        for (Team team : game.getTeams()) {
            if (team.getTotem() != null && team.getTotem().isTotemBlock(pos) 
                    && event.getBlock().getType() == Material.OBSIDIAN) {
                if (event.getPlayer().getItemInHand().getType() 
                        == Material.DIAMOND_PICKAXE) {
                    game.decreaseLives(team);
                } else {
                    event.setCancelled(true);
                }
                return;
            }
        }

        if (isProtected(pos)) {
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
        for (Block block : event.blockList()) {
            if (isProtected(block)) {
                event.setCancelled(true);
                return;
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
    public void onFromTo(BlockFromToEvent event) {
        if (isProtected(event.getToBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (isProtected(new Vector(event.getRetractLocation()))) {
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
        return isProtected(new Vector(block));
    }

    private boolean isProtected(Vector pos) {
        Game game = TBN.getGame();
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
