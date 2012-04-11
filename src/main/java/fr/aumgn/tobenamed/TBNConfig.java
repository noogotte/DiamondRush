package fr.aumgn.tobenamed;

import org.bukkit.inventory.ItemStack;

public class TBNConfig {

    private int timerMajorDelay = 2 * 60;
    private int timerMinorDelay = 20;
    
    private int totemDuration = 90;
    private int spawnDuration = 30;
    private int transitionDuration = 10;

    private int developmentFirstDuration = 20 * 60;
    private int developmentDurationDiff = - 3 * 60;
    private int fightFirstDuration = 5 * 60;
    private int fightDurationDiff = 3 * 60;
    private int applyDiffUntilTurn = 5;

    private int totemSpawnMinimumDistance = 20;

    private int spottedTotemDistance = 30;
    private int spottedSpawnDistance = 20;
    
    private int surrenderItemId = 339;
    private int deathNeededForSurrender = 1;

    private int maximumDiamondPerFight = 4;
    private int itemForKillId = 266;
    private int itemForKillAmount = 1;
    private int itemForDeathId = 265;
    private int itemForDeathAmount = 3;


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
        int diff = Math.min(applyDiffUntilTurn, turn) * developmentDurationDiff;
        return Math.max(developmentFirstDuration + diff, 0);
    }

    public int getFightDuration(int turn) {
        int diff = Math.min(applyDiffUntilTurn, turn) * fightDurationDiff;
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
}
