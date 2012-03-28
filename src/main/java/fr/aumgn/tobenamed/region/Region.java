package fr.aumgn.tobenamed.region;

import org.bukkit.util.Vector;

public class Region {

    private Vector min;
    private Vector max;

    public boolean isInside(Vector pos) {
        return pos.isInAABB(min, max);
    }
}
