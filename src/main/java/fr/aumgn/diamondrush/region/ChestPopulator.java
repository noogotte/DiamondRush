package fr.aumgn.diamondrush.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import fr.aumgn.bukkitutils.util.Vector2D;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.Util;

public class ChestPopulator {

    private Vector2D origin;
    private List<Vector2D> points;
    private int radiusOffset;

    public ChestPopulator(GameSpawn gameSpawn, List<Totem> totems, int radiusOffset) {
        this.origin = gameSpawn.getMiddle().to2D();
        this.points = new ArrayList<Vector2D>();
        for (Totem totem : totems) {
            this.points.add(totem.getMiddle().to2D());
        }
        this.radiusOffset = radiusOffset;
    }

    public ChestPopulator(Vector2D origin, List<Vector2D> points, int radiusOffset) {
        this.origin = origin;
        this.points = points;
        this.radiusOffset = radiusOffset;
    }

    public void populate(World world, int amount) {
        double maxRadius = getMaxRadius().length();

        Random rand = Util.getRandom();
        double angleDiff = 2 * Math.PI / amount;
        double angleOrigin = rand.nextDouble() * Math.PI;
        double angleOffset = angleDiff / amount;
        for (int i = 0; i < amount; i++) {
            double radius = rand.nextDouble() * maxRadius + radiusOffset;
            double offset = rand.nextDouble() * angleOffset;
            double angle = angleOrigin + i * angleDiff + offset;
            int x = (int) (Math.cos(angle) * radius);
            int z = (int) (Math.sin(angle) * radius);
            Vector2D pos = origin.add(x, z);

            Block block = world.getHighestBlockAt(pos.getX(), pos.getZ());
            createChest(block);
        }
    }

    private Vector2D getMaxRadius() {
        int maxDistance = 0;
        Vector2D maxRadius = origin;
        for (Vector2D totem : points) {
            Vector2D radius = totem.subtract(origin);
            int distance = radius.lengthSq();
            if (distance > maxDistance) {
                maxDistance = distance;
                maxRadius = radius;
            }
        }

        return maxRadius;
    }

    private void createChest(Block block) {
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        chest.getBlockInventory().addItem(
                DiamondRush.getConfig().getRandomBonus());
    }
}
