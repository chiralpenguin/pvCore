package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LocationDataService extends DataService {
    HashMap<UUID, HashMap<String, SavedLocation>> locationCache;

    public LocationDataService(pvCore plugin, DatabaseHandler database) {
        super(plugin, database);

        locationCache = new HashMap<>();
    }

    @Override
    protected void createTables() {
        String query = """
                CREATE TABLE IF NOT EXISTS locations (
                    playerID CHAR(36) NOT NULL,
                    label VARCHAR(255) NOT NULL,
                    world VARCHAR(255) NOT NULL,
                    x DOUBLE NOT NULL,
                    y DOUBLE NOT NULL,
                    z DOUBLE NOT NULL,
                    yaw FLOAT NOT NULL DEFAULT 0.0,
                    pitch FLOAT NOT NULL DEFAULT 0.0,
                    PRIMARY KEY (uuid, label),
                    CONSTRAINT fk_uuid FOREIGN KEY (playerID) REFERENCES players (uuid) ON DELETE CASCADE
                )
                """;
        database.executeUpdate(query);
    }

    @Override
    public void saveAll() {
        for (HashMap<String, SavedLocation> locationList : locationCache.values()) {
            for (SavedLocation location : locationList.values()) {
                saveLocationData(location);
            }
        }
    }

    public SavedLocation getLocationData(UUID playerID, String label) {
        String query = "SELECT label, world, x, y, z, yaw, pitch FROM locations WHERE uuid = ? AND label = ?";
        List<Object> params = new ArrayList<>();
        params.add(playerID);
        params.add(label.toLowerCase());
        ResultSetProcessor<SavedLocation> locationProcessor = rs -> {
            if (rs.next()) {
                return new SavedLocation(playerID, rs.getString("label"), rs.getString("world"),
                        rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"),
                        rs.getFloat("yaw"), rs.getFloat("pitch"));
            }
            return null;
        };
        return database.executeQuery(query, params, locationProcessor);
    }

    public void saveLocationData(UUID playerID, String label, String world, double x, double y, double z, float yaw, float pitch) {
        String query = """
                INSERT INTO locations (playerID, label, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE last_seen = VALUES(last_seen)
                """;
        List<Object> params = new ArrayList<>();
        params.add(playerID);
        params.add(label.toLowerCase());
        params.add(world);
        params.add(x);
        params.add(y);
        params.add(z);
        params.add(yaw);
        params.add(pitch);
        database.executeUpdate(query, params);
    }

    public void saveLocationData(SavedLocation location) {
        saveLocationData(location.getPlayerID(), location.getLabel(), location.getWorld(),
                location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public void removeLocationData(UUID playerID, String label) {
        String query = "DELETE FROM locations WHERE uuid = ? AND label = ?";
        List<Object> params = new ArrayList<>();
        params.add(playerID);
        params.add(label.toLowerCase());
        database.executeUpdate(query, params);
    }

    public List<SavedLocation> getAllPlayerLocationData(UUID playerID) {
        String query = "SELECT label, world, x, y, z, yaw, pitch FROM locations WHERE uuid = ?";
        List<Object> params = new ArrayList<>();
        params.add(playerID);
        ResultSetProcessor<List<SavedLocation>> locationsProcessor = rs -> {
            List<SavedLocation> locations = new ArrayList<>();
            while (rs.next()) {
                locations.add(new SavedLocation(playerID, rs.getString("label"), rs.getString("world"),
                        rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"),
                        rs.getFloat("yaw"), rs.getFloat("pitch")));
            }
            return locations;
        };
        return database.executeQuery(query, params, locationsProcessor);
    }

    public boolean isCached(UUID playerID, String label) {
        return locationCache.containsKey(playerID) && locationCache.get(playerID).containsKey(label.toLowerCase());
    }

    public void addLocationToCache(SavedLocation location) {
        UUID playerID = location.getPlayerID();
        if (!locationCache.containsKey(playerID)) {
            locationCache.put(playerID, new HashMap<>());
        }
        locationCache.get(playerID).put(location.getLabel(), location);
    }

    public void removeLocationFromCache(SavedLocation location) {
        UUID playerID = location.getPlayerID();
        if (!locationCache.containsKey(playerID)) {
            return;
        }

        locationCache.get(playerID).remove(location.getLabel());
        if (locationCache.get(playerID).isEmpty()) {
            locationCache.remove(playerID);
        }
    }

    public SavedLocation getLocation(UUID playerID, String label) {
        if (isCached(playerID, label)) {
            return locationCache.get(playerID).get(label.toLowerCase());
        }

        SavedLocation location = getLocationData(playerID, label);
        locationCache.get(playerID).put(location.getLabel(), location);
        return location;
    }

    public int loadAllPlayerLocations(UUID playerID) {
        List<SavedLocation> locations = getAllPlayerLocationData(playerID);
        for (SavedLocation location : locations) {
            addLocationToCache(location);
        }
        return locations.size();
    }

    public int unloadAllPlayerLocations(UUID playerID) {
        for (SavedLocation location : locationCache.get(playerID).values()) {
            removeLocationFromCache(location);
        }
        return locationCache.get(playerID).size();
    }

    public int cleanCache() {
        // TODO Periodically clean cache of locations with players no longer connected
        return 0;
    }
}
