package fr.aumgn.tobenamed.config;

import org.bukkit.inventory.ItemStack;

public class Bonus {

    private int id;
    private byte data;
    private int amount;

    public ItemStack toItemStack() {
        return new ItemStack(id, amount, (short) 0, data);
    }
}
