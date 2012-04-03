package fr.aumgn.tobenamed.util;

public class Vector2D {

    private final int x, z;

    public Vector2D() {
        this.x = 0;
        this.z = 0;
    }

    public Vector2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Vector2D setX(int x) {
        return new Vector2D(x, z);
    }

    public Vector2D setZ(int z) {
        return new Vector2D(x, z);
    }

    public Vector2D add(int i) {
        return new Vector2D(this.x + i, this.z + i);
    }

    public Vector2D add(int ox, int oy, int oz) {
        return new Vector2D(x + ox, z + oz);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, z + other.z);
    }

    public Vector2D subtract(int i) {
        return new Vector2D(x - i, z - i);
    }

    public Vector2D subtract(int ox, int oy, int oz) {
        return new Vector2D(x - ox, z - oz);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, z - other.z);
    }

    public Vector2D getMiddle(Vector2D other) {
        return new Vector2D(
                (x + other.x) / 2,
                (z + other.z) / 2);
    }

    public boolean isInside(Vector2D min, Vector2D max) {
        return x >= min.x && x <= max.x
                && z >= min.z && z <= max.z;
    }

    public double length() {
        return Math.sqrt(x * x + z * z);
    }

    public float toYaw() {
        double radians = Math.acos(z / length());
        float yaw = (float) (radians * 180.0 / Math.PI);
        if (x > 0) {
            yaw += 180.0f;
            if (z > 0) {
                yaw += 90.0f;
            }
        }
        return yaw;
    }

    public Vector to3D() {
        return new Vector(x, 0, z);
    }

    public Vector to3D(int y) {
        return new Vector(x, y, z);
    }
}
