package fr.aumgn.diamondrush.region;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.bukkitutils.geom.Vector;
import fr.aumgn.bukkitutils.geom.Vector2D;

public class Region {

    protected final Vector min;
    protected final Vector max;

    public Region(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(Vector pos) {
        return pos.isInside(min, max);
    }

    public boolean contains(Vector2D pos, int y) {
        return y >= min.getY() && pos.isInside(min.to2D(), max.to2D());
    }

    public Vector getMiddle() {
        return min.to2D().getMiddle(max.to2D()).to3D(min.getY() + 1);
    }

    public Vector getTeleportPoint() {
        return getMiddle();
    }

    public Location getTeleportLocation(World world, Vector dirTarget) {
        Vector pos = getTeleportPoint();
        Vector2D dir = dirTarget.subtract(pos).to2D();
        return pos.toLocation(world, dir);
    }

    public Location getTeleportLocation(World world, Region dirTarget) {
        return this.getTeleportLocation(world, dirTarget.getMiddle());
    }

    protected void removeEverythingInside(World world) {
        for (Vector pos : min.rectangle(max)) {
            pos.toBlock(world).setType(Material.AIR);
        }
    }
}
