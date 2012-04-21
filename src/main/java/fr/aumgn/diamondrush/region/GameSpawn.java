package fr.aumgn.diamondrush.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.region.patterns.FloorPattern;

public class GameSpawn extends Region {

    public GameSpawn(Vector pt) {
        super(pt.subtract(3, 1, 3), pt.add(3, 5, 3));
    }

    public List<Vector> getStartPositions(int amount) {
        List<Vector> list = new ArrayList<Vector>(amount);
        double angleDiff = 2 * Math.PI / amount;
        Vector middle = getMiddle();

        for (int i = 0; i < amount; i++) {
            double angle = angleDiff * i;
            int x = (int) Math.round(Math.cos(angle) * 3);
            int z = (int) Math.round(Math.sin(angle) * 3);
            list.add(middle.add(new Vector(x, 0, z)));
        }

        return list;
    }

    public void create(World world) {
        removeEverythingInside(world);
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getY(),
                Material.GLOWSTONE, (byte) 0, false);
        base.create(world);
    }
}
