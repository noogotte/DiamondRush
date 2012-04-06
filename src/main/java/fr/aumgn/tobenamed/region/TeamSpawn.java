package fr.aumgn.tobenamed.region;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.tobenamed.TBNColor;
import fr.aumgn.tobenamed.region.patterns.FloorPattern;
import fr.aumgn.tobenamed.util.Vector;

public class TeamSpawn extends Region {

    public TeamSpawn(Vector pos, int worldHeight) {
        super(pos.subtract(3, 1, 3), pos.add(3, 0, 3).setY(worldHeight));
    }

    public void create(World world, TBNColor color) {
        removeEverythingAbove(world);
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY(),
                Material.WOOL, color.getWoolColor());
        base.create(world);
    }
}
