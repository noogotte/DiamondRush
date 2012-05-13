package fr.aumgn.diamondrush.region;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.bukkitutils.geom.Vector;
import fr.aumgn.diamondrush.game.TeamColor;
import fr.aumgn.diamondrush.region.patterns.FloorPattern;

public class TeamSpawn extends Region {

    public TeamSpawn(Vector pos) {
        super(pos.subtract(3, 1, 3), pos.add(3, 4, 3));
    }

    public void create(World world, TeamColor color) {
        removeEverythingInside(world);
        FloorPattern base = new FloorPattern(
                min.to2D(), max.to2D(), min.getBlockY(),
                Material.WOOL, color.getWoolColor(), true);
        base.create(world);
    }
}
