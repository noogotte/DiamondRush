package fr.aumgn.diamondrush.config;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.bukkitutils.util.Util;

public class BonusItem {

    private Material type = Material.STONE;
    private byte data = 0;
    private short minAmount = 1;
    private short maxAmount = 1;
    private int weight = 1;

    public BonusItem() {
    }

    public BonusItem(Material type, int minAmount, int maxAmount, int weight) {
        this(type, 0, minAmount, maxAmount, weight);
    }

    public BonusItem(Material type, int data, int minAmount, int maxAmount, int weight) {
        this.type = type;
        this.data = (byte) data;
        this.minAmount = (short) minAmount;
        this.maxAmount = (short) maxAmount;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public ItemStack toItemStack() {
        short amount;
        if (minAmount == maxAmount) {
            amount = minAmount;
        } else {
            amount = (short) Util.getRandom().nextInt(minAmount, maxAmount);
            int max = type.getMaxStackSize();
            amount = (short) Math.min(max, amount);
        }
        return new ItemStack(type, amount, (short) 0, data);
    }
}
