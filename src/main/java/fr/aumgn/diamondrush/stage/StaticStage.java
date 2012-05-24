package fr.aumgn.diamondrush.stage;

import java.util.Collection;
import java.util.Collections;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import fr.aumgn.bukkitutils.playerid.PlayerId;
import fr.aumgn.bukkitutils.playerid.map.PlayersIdHashMap;
import fr.aumgn.bukkitutils.playerid.map.PlayersIdMap;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.listeners.StaticListener;

public class StaticStage extends Stage {

    public static class PlayerStatus {

        private Location location;
        private Material material;
        private byte data;
        private boolean isInventoryHolder;
        private ItemStack[] blockContents;

        private ItemStack[] inventory;
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
                blockContents = inventory.getContents();
            }

            this.inventory = new ItemStack[9];
            PlayerInventory playerInventory = player.getInventory();
            for (int i = 0; i < 9; i++) {
                this.inventory[i] = playerInventory.getItem(i);
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

            PlayerInventory playerInventory = player.getInventory();
            for (int i = 0; i < 9; i++) {
                playerInventory.clear(i);
            }
            for (PotionEffect activeEffect : potionEffects) {
                player.removePotionEffect(activeEffect.getType());
            }
        }

        public void restorePosition(Player player) {
            player.teleport(location);
        }

        public void restoreEnvironment() {
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            block.setTypeIdAndData(material.getId(), data, true);
            if (isInventoryHolder) {
                Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
                inventory.setContents(blockContents);
            }
        }

        public void restore(Player player) {
            PlayerInventory playerInventory = player.getInventory();
            for (int i = 0; i < 9; i++) {
                playerInventory.setItem(i, inventory[i]);
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
    private PlayersIdMap<PlayerStatus> status;

    public StaticStage(DiamondRush dr) {
        super(dr);
        this.listener = new StaticListener(this, dr.getGame());
        this.status = new PlayersIdHashMap<PlayerStatus>();
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
        for (final Map.Entry<PlayerId, PlayerStatus> entry : status.entrySet()) {
            entry.getValue().restoreEnvironment();

            final PlayerId playerId = entry.getKey();
            if (playerId.isOffline()) {
                final PlayerStatus playerStatus = entry.getValue();
                dr.onReconnect(playerId, new Runnable() {
                    public void run() {
                        playerStatus.restore(playerId.getPlayer());
                    }
                });
            } else {
                entry.getValue().restore(playerId.getPlayer());
            }
        }
    }

    public PlayerStatus getPlayerStatus(Player player) {
        return status.get(player);
    }
}
