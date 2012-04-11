package fr.aumgn.tobenamed.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.util.Vector2D;

public class ChestPopulator {

    private Vector2D gameSpawn;
    private List<Vector2D> totems;

    public ChestPopulator(GameSpawn gameSpawn, List<Totem> totems) {
        this.gameSpawn = gameSpawn.getMiddle().to2D();
        this.totems = new ArrayList<Vector2D>();
        for (Totem totem : totems) {
            this.totems.add(totem.getMiddle().to2D());
        }
    }

    public ChestPopulator(Vector2D gameSpawn, List<Vector2D> totems) {
        this.gameSpawn = gameSpawn;
        this.totems = totems;
    }

    public void populate(World world, int amount) {
        Vector2D maxRadius = getMaxRadius().positive().add(5, 5);

        for (int i = amount; i > 0; i--) {
            Vector2D radius = getRandomRadius(maxRadius);
            Vector2D pos = gameSpawn.add(radius);
            Block block = world.getHighestBlockAt(pos.getX(), pos.getZ());
            createChest(block);
        }
    }

    private Vector2D getMaxRadius() {
        int maxDistance = 0;
        Vector2D maxRadius = gameSpawn;
        for (Vector2D totem : totems) {
            Vector2D radius = totem.subtract(gameSpawn);
            int distance = radius.lengthSq();
            if (distance > maxDistance) {
                maxDistance = distance;
                maxRadius = radius;
            }
        }

        return maxRadius;
    }

    private Vector2D getRandomRadius(Vector2D radius) {
        Vector2D diameter = radius.add(radius);
        return new Vector2D(
            TBN.getRandom().nextInt(diameter.getX()),
            TBN.getRandom().nextInt(diameter.getZ())
        ).subtract(radius);
    }

    private void createChest(Block block) {
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        chest.getBlockInventory().addItem(
                TBN.getConfig().getRandomBonus());
    }
}
