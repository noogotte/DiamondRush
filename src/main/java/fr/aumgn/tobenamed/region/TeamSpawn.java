package fr.aumgn.tobenamed.region;

import org.bukkit.World;

import fr.aumgn.tobenamed.region.patterns.FloorPattern;
import fr.aumgn.tobenamed.util.Vector;

public class TeamSpawn extends Region {

    public TeamSpawn(Vector pos, int worldHeight) {
        super(pos.subtract(3, 1, 3), pos.add(3, 0, 3).setY(worldHeight));
    }

    public void create(World world) {
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY());
        removeEverythingAbove(world, min.getY() + 1);
        base.create(world);
    }
}
