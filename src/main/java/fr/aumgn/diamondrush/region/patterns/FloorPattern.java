package fr.aumgn.diamondrush.region.patterns;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Torch;

import fr.aumgn.bukkit.util.Vector;
import fr.aumgn.bukkit.util.Vector2D;

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
        if (torch) {
            Block torch = pos.add(0, 1, 0).toBlock(world);
            torch.setType(Material.TORCH);
            Torch torchData = new Torch(torch.getType(), torch.getData());
            torchData.setFacingDirection(BlockFace.UP);
            torch.setData(torchData.getData());
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
