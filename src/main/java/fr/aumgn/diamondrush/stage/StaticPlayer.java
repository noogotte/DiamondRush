package fr.aumgn.diamondrush.stage;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class StaticPlayer {

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

    public StaticPlayer(Player player) {
        Location loc = player.getLocation();
        location = new Location(loc.getWorld(), loc.getBlockX() + 0.5, 
                loc.getBlockY(), loc.getBlockZ() + 0.5,
                loc.getYaw(), loc.getPitch());
        Block block = location.getBlock().getRelative(BlockFace.DOWN);
        material = block.getType();
        data = block.getData();
        isInventoryHolder = block.getState() instanceof InventoryHolder;
        if (isInventoryHolder) {
            Inventory inventory = ((InventoryHolder) block.getState())
                    .getInventory();
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
            Inventory inventory = ((InventoryHolder) block.getState())
                    .getInventory();
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
            Inventory inventory = ((InventoryHolder) block.getState())
                    .getInventory();
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
