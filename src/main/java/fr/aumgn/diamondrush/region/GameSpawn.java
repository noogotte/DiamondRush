package fr.aumgn.diamondrush.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.region.patterns.RoundFloorPattern;

public class GameSpawn extends Region {

    public static GameSpawn createFromTeamsNumber(Vector pt, int teamsNumber) {
        int radius = teamsNumber * 2;
        return new GameSpawn(pt, radius);
    }

    private GameSpawn(Vector pt, int radius) {
        super(pt.subtract(radius, 1, radius), pt.add(radius, 5, radius));
    }

    public List<Vector> getStartPositions(int amount) {
        List<Vector> list = new ArrayList<Vector>(amount);
        int radius = Math.abs(max.getBlockX() - min.getBlockX() ) / 2 - 1;
        double angleDiff = 2 * Math.PI / amount;
        Vector middle = getMiddle();

        for (int i = 0; i < amount; i++) {
            double angle = angleDiff * i;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            list.add(middle.add(new Vector(x, 0, z)));
        }

        return list;
    }

    @Override
    public void removeEverythingInside(World world) {
        for (Vector pos : min.add(0, 1, 0).rectangle(max)) {
            pos.toBlock(world).setType(Material.AIR);
        }
    }

    public void create(World world) {
        removeEverythingInside(world);
        RoundFloorPattern base = new RoundFloorPattern(
                min.to2D(), max.to2D(), min.getBlockY());
        base.create(world);
    }
}
