package fr.aumgn.diamondrush.region.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.diamondrush.util.Vector;

public class TotemPattern {

    private Vector origin;

    public TotemPattern(Vector origin) {
        this.origin = origin;
    }

    public List<Vector> getBlocks() {
        List<Vector> blocks = new ArrayList<Vector>();
        Vector pos = origin;
        for (int i = 0; i <= 2; i++) {
            blocks.add(pos.add(0, i, 0));
        }
        blocks.add(pos.add( 1, 2, 0));
        blocks.add(pos.add(-1, 2, 0));
        return blocks;
    }

    public void create(World world) {
        for (Vector block : getBlocks()) {
            block.toBlock(world).setType(Material.OBSIDIAN);
        }
    }
}
