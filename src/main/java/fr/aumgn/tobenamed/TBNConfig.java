package fr.aumgn.tobenamed;

import org.bukkit.inventory.ItemStack;

public class TBNConfig {

    private int timerMajorDelay = 2 * 60;
    private int timerMinorDelay = 20;
    
    private int totemDuration = 90;
    private int spawnDuration = 30;
    private int developmentDuration = 20 * 60;
    private int transitionDuration = 10;
    private int fightDuration = 6 * 60;

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

    public int getDevDuration() {
        return developmentDuration;
    }

    public int getFightDuration() {
        return fightDuration;
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
