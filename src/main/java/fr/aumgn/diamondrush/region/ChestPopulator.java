package fr.aumgn.diamondrush.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.bukkitutils.util.Vector2D;
import fr.aumgn.diamondrush.Util;

public class ChestPopulator {

    private static final Material[] BLACKLIST = {
        Material.AIR,
        Material.LEAVES,
        Material.WATER,
        Material.STATIONARY_WATER,
        Material.LONG_GRASS,
        Material.VINE,
        Material.TORCH,
        Material.YELLOW_FLOWER,
        Material.RED_ROSE,
        Material.SAPLING,
        Material.RED_MUSHROOM,
        Material.BROWN_MUSHROOM,
        Material.WEB
    };

    private Vector2D origin;
    private List<Vector2D> points;
    private int radiusOffset;

    public ChestPopulator(GameSpawn gameSpawn, List<Totem> totems, int radiusOffset) {
        this(gameSpawn.getMiddle().to2D(), totemsToPoints(totems), radiusOffset);
    }

    private static List<Vector2D> totemsToPoints(List<Totem> totems) {
        List<Vector2D> points = new ArrayList<Vector2D>();
        for (Totem totem : totems) {
            points.add(totem.getMiddle().to2D());
        }
        return points;
    }

    public ChestPopulator(Vector2D origin, List<Vector2D> points, int radiusOffset) {
        this.origin = origin;
        this.points = points;
        this.radiusOffset = radiusOffset;
    }

    public void populate(World world, List<ItemStack[]> contents) {
        int amount = contents.size();
        double maxRadius = getMaxRadius().length();

        Random rand = Util.getRandom();
        double angleDiff = 2 * Math.PI / amount;
        double angleOrigin = rand.nextDouble() * Math.PI;
        double maxAngleOffset = angleDiff / amount;
        int i = 0;
        for (ItemStack[] content : contents) {
            double radius = rand.nextDouble() * maxRadius + radiusOffset;
            double angleOffset = rand.nextDouble() * maxAngleOffset;
            double angle = angleOrigin + i * angleDiff + angleOffset;

            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            Vector2D pos = origin.add(x, z);

            Block block = world.getHighestBlockAt(pos.getBlockX(), pos.getBlockZ());
            Block blockDown = block.getRelative(BlockFace.DOWN);
            while (isBlackListed(blockDown.getType())) {
                block = blockDown;
                blockDown = block.getRelative(BlockFace.DOWN);
            } 
            createChest(block, content);
            i++;
        }
    }

    private Vector2D getMaxRadius() {
        double maxDistance = 0;
        Vector2D maxRadius = origin;
        for (Vector2D totem : points) {
            Vector2D radius = totem.subtract(origin);
            double distance = radius.lengthSq();
            if (distance > maxDistance) {
                maxDistance = distance;
                maxRadius = radius;
            }
        }

        return maxRadius;
    }

    private boolean isBlackListed(Material material) {
        for (Material blacklisted : BLACKLIST) {
            if (blacklisted == material) {
                return true;
            }
        }
        return false;
    }

    private void createChest(Block block, ItemStack[] content) {
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        chest.getBlockInventory().addItem(content);
    }
}
