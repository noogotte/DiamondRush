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

        createTotem(world);
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

    private void createTotem(World world) {
        Vector pos = min.getMiddle(max).setY(min.getY());
        for (int i = 1; i <= 3; i++) {
            setTotemBlock(world, pos.add(0, i, 0));
        }

        setTotemBlock(world, pos.add( 1, 3, 0));
        setTotemBlock(world, pos.add(-1, 3, 0));

        setTorchBlock(world, pos.add( 1, 3,  1));
        setTorchBlock(world, pos.add( 1, 3, -1));
        setTorchBlock(world, pos.add(-1, 3,  1));
        setTorchBlock(world, pos.add(-1, 3, -1));
    }

    private void setTotemBlock(World world, Vector pos) {
        Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        block.setType(Material.OBSIDIAN);
    }

    private void setTorchBlock(World world, Vector pos) {
        Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        block.setType(Material.TORCH);
    }

    @Override
    public Vector getTeleportPoint() {
        return super.getTeleportPoint().add(0, 0, 1);
    }

}
