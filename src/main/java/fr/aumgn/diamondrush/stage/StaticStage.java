package fr.aumgn.diamondrush.stage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.listeners.StaticListener;

public class StaticStage extends Stage {

    public static class PlayerStatus {

        private Location location;
        private Material material;
        private byte data;
        private boolean isInventoryHolder;
        private ItemStack[] inventoryContents;
        private Collection<PotionEffect> potionEffects;
        private float fallDistance;
        private int remainingAir;
        private int fireTicks;

        public PlayerStatus(Player player) {
            Location loc = player.getLocation();
            location = new Location(loc.getWorld(), loc.getBlockX() + 0.5, 
                    loc.getBlockY(), loc.getBlockZ() + 0.5,
                    loc.getYaw(), loc.getPitch());
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            material = block.getType();
            data = block.getData();
            isInventoryHolder = block.getState() instanceof InventoryHolder;
            if (isInventoryHolder) {
                Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
                inventoryContents = inventory.getContents();
            }

            this.potionEffects = player.getActivePotionEffects();
            this.fallDistance = player.getFallDistance();
            this.remainingAir = player.getRemainingAir();
            this.fireTicks = player.getFireTicks();
        }

        public void init(Player player) {
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            if (isInventoryHolder) {
                Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
                inventory.clear();
            }
            block.setType(Material.GLASS);
            restorePosition(player);

            for (PotionEffect activeEffect : potionEffects) {
                player.removePotionEffect(activeEffect.getType());
            }
        }

        public void restorePosition(Player player) {
            player.teleport(location);
        }

        public void restore(Player player) {
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            block.setTypeIdAndData(material.getId(), data, true);
            if (isInventoryHolder) {
                Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
                inventory.setContents(inventoryContents);
            }

            for (PotionEffect potionEffect : potionEffects) {
                player.addPotionEffect(potionEffect, true);
            }

            player.setFallDistance(fallDistance);
            player.setRemainingAir(remainingAir);
            player.setFireTicks(fireTicks);
        }
    }

    private StaticListener listener;
    private long time;
    private Map<Player, PlayerStatus> status;

    public StaticStage(DiamondRush dr) {
        super(dr);
        this.listener = new StaticListener(this, dr.getGame());
        this.status = new HashMap<Player, PlayerStatus>();
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public void start() {
        this.time = dr.getGame().getWorld().getTime();
        for (Team team : dr.getGame().getTeams()) {
            for (Player player : team.getPlayers()) {
                status.put(player, new PlayerStatus(player));
            }
        }
        for (Team team : dr.getGame().getTeams()) {
            for (Player player : team.getPlayers()) {
                status.get(player).init(player);
            }
        }
    }

    @Override
    public void stop() {
        dr.getGame().getWorld().setTime(time);
        for (Map.Entry<Player, PlayerStatus> playerStatus : status.entrySet()) {
            Player player = playerStatus.getKey();
            playerStatus.getValue().restore(player);
        }
    }

    public PlayerStatus getPlayerStatus(Player player) {
        return status.get(player);
    }
}
