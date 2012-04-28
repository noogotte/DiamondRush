package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.listeners.NoPVPListener;
import fr.aumgn.diamondrush.stage.listeners.PositioningListener;

public abstract class PositioningStage extends Stage {

    private Map<Team, Vector> positions;
    private List<Listener> listeners;

    public PositioningStage(DiamondRush dr) {
        super(dr);
        this.positions = new HashMap<Team, Vector>();
        this.listeners = new ArrayList<Listener>();
        this.listeners.add(new NoPVPListener(dr.getGame()));
        this.listeners.add(new PositioningListener(dr, this, positions));
    }

    @Override
    public List<Listener> getListeners() {
        return listeners;
    }

    @Override
    public void start() {
        giveBlocks();
    }

    @Override
    public void stop() {
        removeBlocksFromInventories();
        removeBlocksFromWorld();
        for (Team team : dr.getGame().getTeams()) {
            Vector pos = getPosition(team);
            initPosition(team, pos);
        }
    }

    public Vector getPosition(Team team) {
        Vector pos = positions.get(team);
        if (pos == null) {
            Player foreman = team.getForeman();
            pos = new Vector(foreman.getLocation());
            foreman.teleport(pos.add(0, 0, 1).toLocation(dr.getGame().getWorld()));
        }
        return pos;
    }

    public void clearPositions() {
        positions.clear();
    }

    protected void giveBlocks() {
        for (Team team : dr.getGame().getTeams()) {
            giveBlock(team.getForeman());
        }
    }

    public void giveBlock(Player player) {
        ItemStack stack = new ItemStack(getMaterial(), 1);
        if (player.getItemInHand().getType() == Material.AIR) {
            player.setItemInHand(stack);
        } else {
            player.getInventory().addItem(stack);
        }
    }

    protected void removeBlocksFromWorld() {
        for (Vector pos : positions.values()) {
            Block block = pos.toBlock(dr.getGame().getWorld());
            if (block.getType() == getMaterial()) {
                block.setType(Material.AIR);
            }
        }
    }

    protected void removeBlocksFromInventories() {
        for (Team team : dr.getGame().getTeams()) {
            for (Player player : team.getPlayers()) {
                if (removeBlockFromInventory(player)) {
                    break;
                }
            }
        }
    }

    public boolean removeBlockFromInventory(Player player) {
        Inventory inventory = player.getInventory();
        int index = inventory.first(getMaterial());
        if (index == -1) {
            return false;
        }
        ItemStack stack = inventory.getItem(index);
        if (stack.getAmount() > 1) {
            stack.setAmount(stack.getAmount() - 1);
        } else {
            inventory.clear(index);
        }
        return true;
    }

    public abstract void initPosition(Team team, Vector vector);

    public abstract Material getMaterial();
}
