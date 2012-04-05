package fr.aumgn.tobenamed.region.patterns;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.aumgn.tobenamed.util.Vector;

public class TotemPattern {

    private Vector origin;

    public TotemPattern(Vector origin) {
        this.origin = origin;
    }

    public void create(World world) {
        Vector pos = origin;
        for (int i = 0; i <= 2; i++) {
            setTotemBlock(world, pos.add(0, i, 0));
        }

        setTotemBlock(world, pos.add( 1, 2, 0));
        setTotemBlock(world, pos.add(-1, 2, 0));

        setTorchBlock(world, pos.add( 1, 2,  1));
        setTorchBlock(world, pos.add( 1, 2, -1));
        setTorchBlock(world, pos.add(-1, 2,  1));
        setTorchBlock(world, pos.add(-1, 2, -1));
    }

    private void setTotemBlock(World world, Vector pos) {
        Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        block.setType(Material.OBSIDIAN);
    }

    private void setTorchBlock(World world, Vector pos) {
        Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        block.setType(Material.TORCH);
    }
}
