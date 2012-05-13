package fr.aumgn.diamondrush.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.bukkitutils.timer.TimerConfig;
import fr.aumgn.bukkitutils.util.Util;

public class DRConfig {

    private TimerConfig timer;

    private int lives = 5;

    private int startDuration = 20;
    private int totemDuration = 90;
    private int spawnDuration = 30;
    private int transitionDuration = 10;

    private int developmentFirstDuration = 20 * 60;
    private int developmentDurationDiff = - 2 * 60;
    private int fightFirstDuration = 2 * 60;
    private int fightDurationDiff = 3 * 60;
    private int applyDiffUntilTurn = 7;

    private int totemSpawnMinimumDistance = 20;

    private int spottedTotemDistance = 30;
    private int spottedSpawnDistance = 20;

    private int deathMalusDuration = 5;

    private int surrenderItemId = 339;
    private int deathNeededForSurrender = 1;
    private int timeLeftAfterSurrender = 15;
    private int surrenderMalusDuration = 60;
    private int surrenderMalusStep = 1;

    private int maximumDiamondPerFight = 4;
    private int itemForKillId = 266;
    private int itemForKillAmount = 1;
    private int itemForDeathId = 265;
    private int itemForDeathAmount = 3;

    private int minItemsInBonusChest = 3;
    private int maxItemsInBonusChest = 3;
    private List<BonusItem> bonuses;

    public DRConfig() {
        timer = TimerConfig.DEFAULT;
        bonuses = new ArrayList<BonusItem>();
        bonuses.add(new BonusItem(Material.BLAZE_ROD,      1,   1,  0));
        bonuses.add(new BonusItem(Material.NETHER_STALK,   2,   5,  0));
        bonuses.add(new BonusItem(Material.EXP_BOTTLE,    30, 100,  1));
        bonuses.add(new BonusItem(Material.FIREBALL,       2,   6,  1));
        bonuses.add(new BonusItem(Material.MAGMA_CREAM,    8,  12,  1));
        bonuses.add(new BonusItem(Material.BLAZE_POWDER,   6,   8,  1));
    }

    public TimerConfig getTimerConfig() {
        return timer;
    }

    public int getLives() {
        return lives;
    }

    public int getStartDuration() {
        return startDuration;
    }

    public int getTotemDuration() {
        return totemDuration;
    }

    public int getSpawnDuration() {
        return spawnDuration;
    }

    public int getDevDuration(int turn) {
        int diff = Math.min(applyDiffUntilTurn - 1, turn) * developmentDurationDiff;
        return Math.max(developmentFirstDuration + diff, 0);
    }

    public int getFightDuration(int turn) {
        int diff = Math.min(applyDiffUntilTurn - 1, turn) * fightDurationDiff;
        return Math.max(fightFirstDuration + diff, 0);
    }

    public int getTransitionDuration() {
        return transitionDuration;
    }

    public int getTotemSpawnMinDistance() {
        return totemSpawnMinimumDistance
                * totemSpawnMinimumDistance ;
    }

    public int getSpottedTotemDistance() {
        return spottedTotemDistance
                * spottedTotemDistance;
    }

    public int getSpottedSpawnDistance() {
        return spottedSpawnDistance
                * spottedSpawnDistance;
    }

    public int getDeathMalusDuration() {
        return deathMalusDuration * 20;
    }

    public int getSurrenderItem() {
        return surrenderItemId;
    }

    public int getDeathNeededForSurrender() {
        return deathNeededForSurrender;
    }

    public int getTimeLeftAfterSurrender() {
        return timeLeftAfterSurrender;
    }

    public int getSurrenderMalusStep() {
        return surrenderMalusStep;
    }

    public int getSurrenderMalusDuration() {
        return surrenderMalusDuration * 20;
    }

    public int getMaxDiamond() {
        return maximumDiamondPerFight;
    }

    public ItemStack getItemForKill() {
        return new ItemStack(
                itemForKillId, itemForKillAmount);
    }

    public ItemStack getItemForDeath() {
        return new ItemStack(
                itemForDeathId, itemForDeathAmount);
    }

    public ItemStack[] getRandomBonus() {
        if (bonuses.size() == 0) {
            return new ItemStack[0];
        }

        int stacksSize = Util.getRandom().nextInt(
                minItemsInBonusChest, maxItemsInBonusChest);
        ItemStack[] stacks = new ItemStack[stacksSize];

        int bonusesSize = 0;
        for (BonusItem bonus : bonuses) {
            if (bonus.getWeight() > 0) {
                bonusesSize += bonus.getWeight();
            }
        }

        List<BonusItem> bonuses = new LinkedList<BonusItem>(this.bonuses);
        for (int i = 0; i < stacksSize; i++) {
            int j = -1;
            Iterator<BonusItem> it = bonuses.iterator();

            while (it.hasNext()) {
               BonusItem bonus = it.next();
               if (bonus.getWeight() < 1) {
                   stacks[i] = bonus.toItemStack();
                   it.remove();
                   break;
               } else {
                   if (j == -1) {
                       j = Util.getRandom().nextInt(bonusesSize);
                   }

                   j -= bonus.getWeight();
                   if (j <= 0) {
                       bonusesSize -= bonus.getWeight();
                       it.remove();
                       stacks[i] = bonus.toItemStack();
                       break;
                   }
               }
           }
        }

        return stacks;
    }
}