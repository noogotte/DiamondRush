package fr.aumgn.diamondrush.region.patterns;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.aumgn.bukkitutils.geom.Vector2D;

public class RoundFloorPattern {

    private Vector2D baseMin;
    private Vector2D baseMax;
    private int y;

    public RoundFloorPattern(Vector2D min, Vector2D max, int y) {
        this.baseMin = min;
        this.baseMax = max;
        this.y = y;
    }

    public void create(World world) {
        Vector2D center = baseMax.getMiddle(baseMin);
        Vector2D outlineRadius = baseMax.subtract(center).add(0.5);
        Vector2D radius = outlineRadius.subtract(1.0);

        for (Vector2D vec : baseMin.rectangle(baseMax)) {
            if (vec.equals(center)) {
                setCenter(world, vec);
            } else if (isInside(center, radius, vec)) {
                setInside(world, vec);
            } else if (isInside(center, outlineRadius, vec)) {
                setOutline(world, vec);
            }
        }
    }

    private boolean isInside(Vector2D center, Vector2D radius, Vector2D vec) {
        return vec.subtract(center).divide(radius).lengthSq() < 1;
    }

    private void setCenter(World world, Vector2D vec) {
        Block block = vec.to3D(y).toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
    }

    private void setInside(World world, Vector2D vec) {
        Block block = vec.to3D(y).toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
    }

    /*private void setGlowstone(World world, Vector2D vec) {
        Block block = vec.to3D(y).toBlock(world);
        block.setType(Material.GLOWSTONE);
    }*/

    private void setOutline(World world, Vector2D vec) {
        Block block = vec.to3D(y).toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
        block.setData((byte) 3);
    }
}
