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
    private Material cornerType;
    private byte cornerData;

    public FloorPattern(Vector2D min, Vector2D max, int y,
            Material cornerType, byte cornerData) {
        this.baseMin = min;
        this.baseMax = max;
        this.y = y;
        this.cornerType = cornerType;
        this.cornerData = cornerData;
    }

    public void create(World world) {
        Vector minCorner = baseMin.to3D(y);
        Vector maxCorner = baseMax.to3D(y);
        setCorner(world, minCorner);
        setCorner(world, maxCorner);
        setCorner(world, minCorner.setX(maxCorner.getX()));
        setCorner(world, minCorner.setZ(maxCorner.getZ()));

        Vector2D min = baseMin.add(1, 0, 1);
        Vector2D max = baseMax.subtract(1, 0, 1);
        for (int x = min.getX(); x <= max.getX(); x++) {
            setEdge(world, x, y, baseMin.getZ());
            setEdge(world, x, y, baseMax.getZ());
        }

        for (int z = min.getZ(); z <= max.getZ(); z++) {
            setEdge(world, baseMin.getX(), y, z);
            setEdge(world, baseMax.getX(), y, z);
        }

        for (Vector2D pos : min.rectangle(max)) {
            setInside(world, pos.to3D(y));
        }
    }

    private void setCorner(World world, Vector pos) {
        Block corner = pos.toBlock(world);
        corner.setType(cornerType);
        corner.setData(cornerData);
        pos.add(0, 1, 0).toBlock(world).setType(Material.TORCH);
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
