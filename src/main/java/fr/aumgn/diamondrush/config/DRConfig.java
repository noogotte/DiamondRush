package fr.aumgn.diamondrush.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import fr.aumgn.diamondrush.util.Util;

public class DRConfig {

    private int timerMajorDelay = 2 * 60;
    private int timerMinorDelay = 20;
    
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

    private int surrenderItemId = 339;
    private int deathNeededForSurrender = 1;
    private int surrenderMalusDuration = 60;

    private int maximumDiamondPerFight = 4;
    private int itemForKillId = 266;
    private int itemForKillAmount = 1;
    private int itemForDeathId = 265;
    private int itemForDeathAmount = 3;

    private List<BonusChest> bonusChests;

    public DRConfig() {
        bonusChests = new ArrayList<BonusChest>();
        BonusChest bonusChest = new BonusChest();
        bonusChest.add(new Bonus(369, 1, 0));
        bonusChest.add(new Bonus(372, 10, 4));
        bonusChests.add(bonusChest);
        bonusChest = new BonusChest();
        bonusChest.add(new Bonus(47, 10, 4));
        bonusChest.add(new Bonus(384, 16, 6));
        bonusChests.add(bonusChest);
        bonusChest = new BonusChest();
        bonusChest.add(new Bonus(370, 8, 3));
        bonusChest.add(new Bonus(378, 12, 4));
        bonusChest.add(new Bonus(377, 6, 2));
        bonusChests.add(bonusChest);
    }

    public int getMajorDelay() {
        return timerMajorDelay;
    }

    public int getMinorDelay() {
        return timerMinorDelay;
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

    public int getSurrenderItem() {
        return surrenderItemId;
    }

    public int getDeathNeededForSurrender() {
        return deathNeededForSurrender;
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
        if (bonusChests.size() == 0) {
            return new ItemStack[0];
        }
        return Util.pickRandom(bonusChests).toItemStacks();
    }
}