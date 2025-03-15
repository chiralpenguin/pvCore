package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.database.LocationDataService;
import com.purityvanilla.pvcore.pvCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SavedLocation {
    private final UUID playerID;
    private String label;
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public SavedLocation(UUID playerID, String label, String world, double x, double y, double z, float yaw, float pitch) {
        this.playerID = playerID;
        this.label = label;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    // Alternate constructor with yaw = pitch = 0
    public SavedLocation(UUID playerID, String label, String world, double x, double y, double z) {
        this(playerID, label, world, x, y, z, 0F, 0F);
    }

    // Constructor from Location object
    public SavedLocation(UUID playerID, String label, Location location) {
        this.playerID = playerID;
        this.label = label;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
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
        return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public void setLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void playerSave(pvCore plugin, Location location) {
        LocationDataService locationData = plugin.getLocationData();
        Player player = plugin.getServer().getPlayer(playerID);
        if (player == null) {
            return;
        }

        setLocation(location);
        if (!locationData.locationExists(playerID, label)) {
            locationData.addLocation(playerID, this);
            player.sendMessage(plugin.config().getMessage("location-saved",
                    CustomTagResolvers.locationLabelResolver(label)));
            return;
        }

        locationData.addLocation(playerID, this);
        player.sendMessage(plugin.config().getMessage("location-save-overwritten",
                CustomTagResolvers.locationLabelResolver(label)));
    }

    public void playerSave(pvCore plugin) {
        playerSave(plugin, getLocation(plugin));
    }
}
