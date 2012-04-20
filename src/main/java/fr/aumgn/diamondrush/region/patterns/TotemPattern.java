package fr.aumgn.diamondrush.region.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.bukkit.util.Vector;

public class TotemPattern {

    private Vector origin;
    private int lives;
    private List<Vector> blocks;

    public TotemPattern(Vector origin, int lives) {
        this.origin = origin;
        this.lives = lives;
        initBlocks();
    }

    private void initBlocks() {
        blocks = new ArrayList<Vector>();

        int height;
        if (lives == 4) {
            height = 2;
        } else {
            height = (lives & 1) == 0 ? 4 : 3;
        }
        int y;
        for (y = 0 ;y < height; y++) {
            blocks.add(origin.add(0, y, 0));
        }

        y = height - 1;
        blocks.add(origin.add( 1, y, 0));
        blocks.add(origin.add(-1, y, 0));

        if (lives > 6) {
            blocks.add(origin.add(0, y,  1));
            blocks.add(origin.add(0, y, -1));
        }
    }

    public List<Vector> getBlocks() {
        return blocks;
    }

    public void create(World world) {
        for (Vector block : getBlocks()) {
            block.toBlock(world).setType(Material.OBSIDIAN);
        }
    }
}
