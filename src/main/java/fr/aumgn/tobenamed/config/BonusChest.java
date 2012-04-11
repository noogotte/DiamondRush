package fr.aumgn.tobenamed.config;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class BonusChest {

    private List<Bonus> contents;

    public ItemStack[] toItemStacks() {
        int size = Math.min(10, contents.size());
        ItemStack[] stacks = new ItemStack[size];
        int i = 0;
        for (Bonus bonus : contents) {
            if (i >= size) {
                break;
            }
            stacks[i] = bonus.toItemStack();
            i++;
        }
        return stacks;
    }
}
