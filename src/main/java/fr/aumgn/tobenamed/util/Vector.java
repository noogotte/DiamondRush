package fr.aumgn.tobenamed.util;

import org.bukkit.Location;
import org.bukkit.World;

public class Vector {

    private final int x, y, z;

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Location loc) {
        this(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Vector setX(int x) {
        return new Vector(x, y, z);
    }

    public Vector setY(int y) {
        return new Vector(x, y, z);
    }

    public Vector setZ(int z) {
        return new Vector(x, y, z);
    }

    public Vector add(int i) {
        return new Vector(this.x + i, this.y + i, this.z + i);
    }

    public Vector add(int ox, int oy, int oz) {
        return new Vector(x + ox, y + oy, z + oz);
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector subtract(int i) {
        return new Vector(x - i, y - i, z - i);
    }

    public Vector subtract(int ox, int oy, int oz) {
        return new Vector(x - ox, y - oy, z - oz);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector getMiddle(Vector other) {
        return new Vector(
                (x + other.x) / 2,
                (y + other.y) / 2,
                (z + other.z) / 2);
    }

    public boolean isInside(Vector min, Vector max) {
        return x >= min.x && x <= max.x
                && y >= min.y && y <= max.y
                && z >= min.z && z <= max.z;
    }

    public Vector2D to2D() {
        return new Vector2D(x, z);
    }

    public Location toPlayerLocation(World world) {
        return toPlayerLocation(world, new Vector2D(0, 1));
    }

    public Location toPlayerLocation(World world, Vector2D direction) {
        return new Location(world, x + 0.5, y + 0.5, z + 0.5, direction.toYaw(), 0.0f);
    }
}
