package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.Vector;

public abstract class PositioningStage extends Stage {

    public class PositioningListener implements Listener {

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent event) {
            Block block = event.getBlock();
            if (event.getBlock().getType() == Material.OBSIDIAN) {
                Team team = game.getTeam(event.getPlayer());
                if (team == null) {
                    return;
                }
                blocks.put(team, block);
            }
        }

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
                return;
            }

            Block block =  event.getClickedBlock();
            if (blocks.containsKey(block)) {
                block.breakNaturally();
                Team team = game.getTeam(event.getPlayer());
                blocks.remove(team);
            }
        }

        @EventHandler
        public void onFoodLevelChange(FoodLevelChangeEvent event) {
            HumanEntity entity = event.getEntity();
            if (entity instanceof Player && game.contains((Player) event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    protected Game game;
    protected Map<Team, Block> blocks;
    private Listener listener = new PositioningListener();

    public PositioningStage(Game game) {
        this.game = game;
        this.blocks = new HashMap<Team, Block>();
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void stop() {
        for (Team team : game.getTeams()) {
            Block block = blocks.get(team);
            Location loc;
            if (block == null) {
                team.sendMessage("Le block n'a pas été placé a temps !");
                loc = team.getForeman().getLocation();
            } else {
                loc = block.getLocation();
            }
            initPosition(team, new Vector(loc));
        }
    }

    public abstract void initPosition(Team team, Vector vector);
}
