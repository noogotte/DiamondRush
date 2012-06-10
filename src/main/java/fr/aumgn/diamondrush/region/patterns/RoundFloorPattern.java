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
        Vector2D glowstoneRadius = outlineRadius.divide(2.0).positive();

        for (Vector2D pos : baseMin.rectangle(baseMax)) {
            if (pos.equals(center)) {
                setCenter(world, pos);
            } else if (isGlowstone(center, glowstoneRadius, pos)) {
                setGlowstone(world, pos);
            } else if (isInside(center, radius, pos)) {
                setInside(world, pos);
            } else if (isInside(center, outlineRadius, pos)) {
                setOutline(world, pos);
            }
        }
    }

    private boolean isGlowstone(Vector2D center, Vector2D radius, Vector2D pos) {
        Vector2D diff = pos.subtract(center).positive();
        System.out.print(radius);
        System.out.print(diff);

        if (diff.equalsBlock(radius)) {
            return true;
        }

        if (diff.getBlockX() == 0
                && diff.getBlockZ() == radius.getBlockZ() + 1) {
            return true;
        }

        if (diff.getBlockZ() == 0
                && diff.getBlockX() == radius.getBlockX() + 1) {
            return true;
        }
        
        return false;
    }

    private boolean isInside(Vector2D center, Vector2D radius, Vector2D pos) {
        return pos.subtract(center).divide(radius).lengthSq() < 1;
    }

    private void setCenter(World world, Vector2D pos) {
        Block block = pos.to3D(y).toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
    }

    private void setInside(World world, Vector2D pos) {
        Block block = pos.to3D(y).toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
    }

    private void setGlowstone(World world, Vector2D pos) {
        Block block = pos.to3D(y).toBlock(world);
        block.setType(Material.GLOWSTONE);
    }

    private void setOutline(World world, Vector2D pos) {
        Block block = pos.to3D(y).toBlock(world);
        block.setType(Material.SMOOTH_BRICK);
        block.setData((byte) 3);
    }
}
