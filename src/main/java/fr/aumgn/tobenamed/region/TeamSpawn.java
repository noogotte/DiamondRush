package fr.aumgn.tobenamed.region;

import org.bukkit.World;

import fr.aumgn.tobenamed.region.patterns.FloorPattern;
import fr.aumgn.tobenamed.util.Vector;

public class TeamSpawn extends Region {

    public TeamSpawn(Vector pos) {
        super(pos.subtract(3, 1, 3), pos.add(3, 5, 3));
    }

    public void create(World world) {
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY());
        base.create(world);
    }
}
