package fr.aumgn.tobenamed.region;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.tobenamed.game.TBNColor;
import fr.aumgn.tobenamed.region.patterns.FloorPattern;
import fr.aumgn.tobenamed.util.Vector;

public class TeamSpawn extends Region {

    public TeamSpawn(Vector pos) {
        super(pos.subtract(3, 1, 3), pos.add(3, 4, 3));
    }

    public void create(World world, TBNColor color) {
        removeEverythingInside(world);
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY(),
                Material.WOOL, color.getWoolColor(), true);
        base.create(world);
    }
}
