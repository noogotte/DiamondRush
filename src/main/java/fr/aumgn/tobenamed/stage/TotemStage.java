package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class TotemStage extends Stage implements Listener {

    private Game game;
    private Map<Block, Team> blocks;

    public TotemStage(Game game) {
        this.game = game;
        this.blocks = new HashMap<Block, Team>();
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(this);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void start() {
        List<Team> teams = game.getTeams();
        Iterator<Vector> directions = game.getSpawn().
                getDirections(teams.size()).iterator();
        for (Team team : teams) {
            initTeam(team, game.getWorld(), directions.next());
        }

        game.getSpawn().create(game.getWorld());
        game.getWorld().setTime(0);
        game.sendMessage(ChatColor.GREEN + "La partie débute !");
    }

    private void initTeam(Team team, World world, Vector pos) {
        Player foreman = TBNUtil.pickRandom(team.getPlayers());
        team.setForeman(foreman);
        team.sendMessage(ChatColor.GREEN + foreman.getDisplayName() + " est le chef d'equipe.");

        for (Player player : team.getPlayers()) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
        }

        foreman.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 1));
        foreman.teleport(pos.toLocation(world, 0.5));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (event.getBlock().getType() == Material.OBSIDIAN) {
            Team team = game.getTeam(event.getPlayer());
            if (team == null) {
                return;
            }
            blocks.put(block, team);
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
            blocks.remove(block);
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
