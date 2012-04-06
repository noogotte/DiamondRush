package fr.aumgn.tobenamed.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.tobenamed.TBNColor;
import fr.aumgn.tobenamed.region.patterns.FloorPattern;
import fr.aumgn.tobenamed.region.patterns.TotemPattern;
import fr.aumgn.tobenamed.util.Vector;

public class Totem extends Region {

    private List<Vector> blocks;

    public Totem(Vector pos, int worldHeight) {
        super(pos.subtract(3, 1, 3), pos.add(3, 0, 3).setY(worldHeight));
        this.blocks = new ArrayList<Vector>();
    }

    public void create(World world, TBNColor color) {
        removeEverythingInside(world);
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY(),
                Material.WOOL, color.getWoolColor());
        base.create(world);
        Vector totemOrigin = getMiddle().setY(min.getY() + 1);
        TotemPattern totem = new TotemPattern(totemOrigin);
        blocks = totem.getBlocks();
        totem.create(world);
    }

    @Override
    public Vector getTeleportPoint() {
        return super.getTeleportPoint().add(0, 0, 1);
    }

    public boolean isTotemBlock(Vector pos) {
        return blocks.contains(pos);
    }
}
