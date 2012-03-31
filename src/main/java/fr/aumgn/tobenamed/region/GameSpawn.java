package fr.aumgn.tobenamed.region;

import java.util.ArrayList;
import java.util.List;

import fr.aumgn.tobenamed.util.Vector;

public class GameSpawn extends Region {

    public GameSpawn(Vector pt) {
        super(pt.subtract(3, 0, 3), pt.add(3, 6, 3));
    }

    public List<Vector> getDirections(int size) {
        List<Vector> list = new ArrayList<Vector>(size);
        list.add(min.add(3, 0, 0));
        list.add(max.subtract(3, 6, 0));

        Vector middle = min.getMiddle(max);
        for (int i = 2; i < size; i++) {
            list.add(middle);
        }
        return list;
    }
}
