package fr.aumgn.tobenamed.region;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.aumgn.tobenamed.util.Vector;

public class Totem extends Region {

    public Totem(Vector pos) {
        super(pos.subtract(3, 1, 3), pos.add(3, 5, 3));
    }

    public void create(World world) {
        int y = min.getY();
        Vector fMin = min;
        Vector fMax = max;
        for (int x = fMin.getX(); x <= fMax.getX(); x++) {
            setEdge(world, x, y, fMin.getZ());
            setEdge(world, x, y, fMax.getZ());
        }

        fMin = fMin.add(0, 0, 1);
        fMax = fMax.subtract(0, 0, 1);
        for (int z = fMin.getZ(); z <= fMax.getZ(); z++) {
            setEdge(world, fMin.getX(), y, z);
            setEdge(world, fMax.getX(), y, z);
        }

        fMin = fMin.add(1, 0, 0);
        fMax = fMax.subtract(1, 0, 0);
        for (int x = fMin.getX(); x <= fMax.getX(); x++) {
            for (int z = fMin.getZ(); z <= fMax.getZ(); z++) {
                setInside(world, x, y, z);
            }
        }
    }

    private void setEdge(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.SMOOTH_BRICK);
        block.setData((byte) 3);
    }

    private void setInside(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.SMOOTH_BRICK);
    }
}
