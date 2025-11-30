package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.database.DatabaseOperator;
import com.purityvanilla.pvlib.database.ResultSetProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationOperator extends DatabaseOperator {

    public LocationOperator(DatabaseConnector database) {
        super(database);
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
                    PRIMARY KEY (playerID, label),
                    CONSTRAINT fk_locations_playerID FOREIGN KEY (playerID) REFERENCES players (uuid) ON DELETE CASCADE
                )
                """;
        database.executeUpdate(query);
    }

    public SavedLocation getLocationData(UUID playerID, String label) {
        String query = "SELECT label, world, x, y, z, yaw, pitch FROM locations WHERE playerID = ? AND label = ?";
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
                ON DUPLICATE KEY UPDATE label = VALUES(label), world = VALUES(world),
                 x = VALUES(x), y = VALUES(y), z = VALUES(z), yaw = VALUES(yaw), pitch = VALUES(pitch)
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
        String query = "DELETE FROM locations WHERE playerID = ? AND label = ?";
        List<Object> params = new ArrayList<>();
        params.add(playerID);
        params.add(label.toLowerCase());
        database.executeUpdate(query, params);
    }

    public List<SavedLocation> getAllPlayerLocationData(UUID playerID) {
        String query = "SELECT label, world, x, y, z, yaw, pitch FROM locations WHERE playerID = ?";
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
}
