package fr.aumgn.tobenamed.region.patterns;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.aumgn.tobenamed.util.Vector;
import fr.aumgn.tobenamed.util.Vector2D;

public class FloorPattern {

    private Vector2D baseMin;
    private Vector2D baseMax;
    private int y;

    public FloorPattern(Vector2D min, Vector2D max, int y) {
        this.baseMin = min;
        this.baseMax = max;
        this.y = y;
    }

    public void create(World world) {
        Vector2D min = baseMin;
        Vector2D max = baseMax;
        for (int x = min.getX(); x <= max.getX(); x++) {
            setEdge(world, x, y, min.getZ());
            setEdge(world, x, y, max.getZ());
        }

        min = min.add(0, 0, 1);
        max = max.subtract(0, 0, 1);
        for (int z = min.getZ(); z <= max.getZ(); z++) {
            setEdge(world, min.getX(), y, z);
            setEdge(world, max.getX(), y, z);
        }

        min = min.add(1, 0, 0);
        max = max.subtract(1, 0, 0);
        for (Vector2D pos : min.rectangle(max)) {
            setInside(world, pos.to3D(y));
        }
    }

    private void setEdge(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.SMOOTH_BRICK);
        block.setData((byte) 3);
    }

    private void setInside(World world, Vector pos) {
        Block block = pos.toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
    }

}
