package fr.aumgn.diamondrush.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.game.TeamColor;
import fr.aumgn.diamondrush.region.patterns.FloorPattern;
import fr.aumgn.diamondrush.region.patterns.TotemPattern;

public class Totem extends Region {

    private List<Vector> blocks;

    public Totem(Vector pos, int worldHeight) {
        super(pos.subtract(3, 1, 3), pos.add(3, 0, 3).setY(worldHeight));
        this.blocks = new ArrayList<Vector>();
    }

    public void create(World world, TeamColor color, int lives) {
        removeEverythingInside(world);
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getBlockY(),
                Material.WOOL, color.getWoolColor(), true);
        base.create(world);
        Vector totemOrigin = getMiddle().setY(min.getY() + 1);
        TotemPattern totem = new TotemPattern(totemOrigin, lives);
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
