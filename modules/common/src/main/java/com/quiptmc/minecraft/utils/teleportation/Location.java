package com.quiptmc.minecraft.utils.teleportation;

public class Location {

    private final String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Location(String world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public Location(String world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String world() {
        return world;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public float yaw() {
        return yaw;
    }

    public float pitch() {
        return pitch;
    }

    public void x(double x) {
        this.x = x;
    }

    public void y(double y) {
        this.y = y;
    }

    public void z(double z) {
        this.z = z;
    }

    public void yaw(float yaw) {
        this.yaw = yaw;
    }

    public void pitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public Location clone() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Location multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public Location divide(double d) {
        this.x /= d;
        this.y /= d;
        this.z /= d;
        return this;
    }

    public String serialize() {
        String r = world() + ":" + x() + ":" + y() + ":" + z() + ":" + pitch() + ":" + yaw();
        r = r.replaceAll("\\.", ",");
        r = "location:" + r;
        return r;
    }

    public static Location of(String s) {
        if (s.startsWith("location:")) s = s.replaceFirst("location:", "");

        if (s.contains(",")) s = s.replaceAll(",", ".");
        String[] args = s.split(":");
        Location r = new Location(args[0], Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        if (args.length >= 5) {
            r.pitch(Float.parseFloat(args[4]));
            r.yaw(Float.parseFloat(args[5]));
        }
        return r;
    }


}
