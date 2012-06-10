package fr.aumgn.diamondrush.region.patterns;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Torch;

import fr.aumgn.bukkitutils.geom.Vector;
import fr.aumgn.bukkitutils.geom.Vector2D;

public class FloorPattern {

    private Vector2D baseMin;
    private Vector2D baseMax;
    private int y;
    private Material cornerType;
    private byte cornerData;
    private boolean torch;

    public FloorPattern(Vector2D min, Vector2D max, int y,
            Material cornerType, byte cornerData, boolean torch) {
        this.baseMin = min;
        this.baseMax = max;
        this.y = y;
        this.cornerType = cornerType;
        this.cornerData = cornerData;
        this.torch = torch;
    }

    public void create(World world) {
        Vector minCorner = baseMin.to3D(y);
        Vector maxCorner = baseMax.to3D(y);
        setCorner(world, minCorner);
        setCorner(world, maxCorner);
        setCorner(world, minCorner.setX(maxCorner.getX()));
        setCorner(world, minCorner.setZ(maxCorner.getZ()));

        Vector2D min = baseMin.add(1, 1);
        Vector2D max = baseMax.subtract(1, 1);
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            setEdge(world, x, y, baseMin.getBlockZ());
            setEdge(world, x, y, baseMax.getBlockZ());
        }

        for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
            setEdge(world, baseMin.getBlockX(), y, z);
            setEdge(world, baseMax.getBlockX(), y, z);
        }

        for (Vector2D pos : min.rectangle(max)) {
            setInside(world, pos.to3D(y));
        }
    }

    private void setCorner(World world, Vector pos) {
        Block corner = pos.toBlock(world);
        corner.setType(cornerType);
        corner.setData(cornerData);
        if (torch) {
            Block torchBlock = pos.addY(1).toBlock(world);
            torchBlock.setType(Material.TORCH);
            Torch torchData = new Torch(torchBlock.getType(), torchBlock.getData());
            torchData.setFacingDirection(BlockFace.UP);
            torchBlock.setData(torchData.getData());
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
