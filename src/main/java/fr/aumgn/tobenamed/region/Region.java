package fr.aumgn.tobenamed.region;

import fr.aumgn.tobenamed.util.Vector;

public class Region {

    protected final Vector min;
    protected final Vector max;

    public Region(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    public boolean isInside(Vector pos) {
        return pos.isInside(min, max);
    }

    public Vector getMiddle() {
        Vector middle = min.getMiddle(max);
        middle.setY(min.getY() + 1);
        return middle;
    }
}
