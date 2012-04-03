package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.listeners.PositioningListener;
import fr.aumgn.tobenamed.util.Vector;

public abstract class PositioningStage extends Stage {

    protected Game game;
    private Map<Team, Block> blocks;
    private Listener listener = new PositioningListener(this);

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

    public Map<Team, Block> getBlocks() {
        return blocks;
    }

    @Override
    public void start() {
        for (Team team : game.getTeams()) {
            PlayerInventory inventory = team.getForeman().getInventory();
            inventory.setItemInHand(new ItemStack(getMaterial(), 1));
        }
    }

    @Override
    public void stop() {
        for (Team team : game.getTeams()) {
            Block block = blocks.get(team);
            Vector pos;
            if (block == null) {
                team.sendMessage("Le block n'a pas été placé a temps !");
                removeBlock(team);
                Player foreman = team.getForeman();
                pos = new Vector(foreman.getLocation());
                foreman.teleport(pos.add(3, 0, 0).toPlayerLocation(game.getWorld()));
            } else {
                pos = new Vector(block.getLocation());
                block.setType(Material.AIR);
            }
            initPosition(team, pos);
        }
    }

    private void removeBlock(Team team) {
        for (Player player : team.getPlayers()) {
            Inventory inventory = player.getInventory();
            int index = inventory.first(getMaterial());
            if (index != -1) {
                ItemStack stack = inventory.getItem(index);
                stack.setAmount(stack.getAmount() - 1);
                return;
            }
        }
    }

    public abstract void initPosition(Team team, Vector vector);

    public abstract Material getMaterial();
}
