package fr.aumgn.tobenamed.region;

import org.bukkit.World;

import fr.aumgn.tobenamed.region.patterns.FloorPattern;
import fr.aumgn.tobenamed.region.patterns.TotemPattern;
import fr.aumgn.tobenamed.util.Vector;

public class Totem extends Region {

    public Totem(Vector pos, int worldHeight) {
        super(pos.subtract(3, 1, 3), pos.add(3, 0, 3).setY(worldHeight));
    }

    public void create(World world) {
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY());
        base.create(world);
        removeEverythingAbove(world, min.getY() + 1);
        Vector totemOrigin = min.getMiddle(max).setY(min.getY());
        TotemPattern totem = new TotemPattern(totemOrigin);
        totem.create(world);
    }

    @Override
    public Vector getTeleportPoint() {
        return super.getTeleportPoint().add(0, 0, 1);
    }
}
