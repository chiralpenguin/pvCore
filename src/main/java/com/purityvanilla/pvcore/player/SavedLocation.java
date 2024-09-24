package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.Location;

import java.util.UUID;

public class SavedLocation {
    private final UUID playerID;
    private String label;
    private String world;
    private double x;
    private double y;
    private double z;

    public SavedLocation(UUID playerID, String label, String world, double x, double y, double z) {
        this.playerID = playerID;
        this.label = label;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Location getLocation(pvCore plugin) {
        return new Location(plugin.getServer().getWorld(world), x, y, z);
    }

    public void setLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.world = location.getWorld().getName();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
