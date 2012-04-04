package fr.aumgn.tobenamed.region;

import org.bukkit.Material;
import org.bukkit.World;

import fr.aumgn.tobenamed.util.Vector;

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

    public Vector getMiddle() {
        return min.to2D().getMiddle(max.to2D()).to3D(min.getY() + 1);
    }

    public Vector getTeleportPoint() {
        return getMiddle();
    }

    protected void removeEverythingAbove(World world, int y) {
        Vector min = this.min.setY(y);
        Vector max = this.max;

        for (Vector pos : min.rectangle(max)) {
            pos.toBlock(world).setType(Material.AIR);
        }
    }
}
