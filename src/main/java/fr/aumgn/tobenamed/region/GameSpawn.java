package fr.aumgn.tobenamed.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.aumgn.tobenamed.util.Vector;

public class GameSpawn extends Region {

    public GameSpawn(Vector pt) {
        super(pt.subtract(3, 1, 3), pt.add(3, 5, 3));
    }

    public List<Vector> getDirections(int size) {
        List<Vector> list = new ArrayList<Vector>(size);
        list.add(min.add(3, 1, 0));
        list.add(min.add(3, 1, 6));

        if (list.size() > 2) {
            list.add(min.add(6, 1, 0));
        }
        if (list.size() > 3) {
            list.add(min.add(6, 1, 3));
        }

        Vector middle = min.getMiddle(max);
        for (int i = 4; i < size; i++) {
            list.add(middle);
        }
        return list;
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
