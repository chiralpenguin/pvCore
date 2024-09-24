package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LocationDataService extends DataService {
    HashMap<UUID, List<SavedLocation>> locationCache;

    public LocationDataService(pvCore plugin, DatabaseHandler database) {
        super(plugin, database);

        locationCache = new HashMap<>();
    }

    @Override
    protected void createTables() {
        String query = """
                CREATE TABLE IF NOT EXISTS locations (
                    uuid CHAR(36) NOT NULL,
                    label VARCHAR(255) NOT NULL,
                    world VARCHAR(255) NOT NULL,
                    x DOUBLE NOT NULL,
                    y DOUBLE NOT NULL,
                    z DOUBLE NOT NULL,
                    PRIMARY KEY (uuid, label),
                    CONSTRAINT fk_uuid FOREIGN KEY (uuid) REFERENCES players (uuid) ON DELETE CASCADE
                )
                """;
        database.executeUpdate(query);
    }

    @Override
    public void saveAll() {
        for (List<SavedLocation> locationList : locationCache.values()) {
            for (SavedLocation location : locationList) {

            }
        }
    }

    public SavedLocation getLocationData(UUID uuid, String label) {
        String query = """
                SELECT label, world, x, y, z FROM locations
                WHERE uuid = ? AND label = ?
                """;
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        params.add(label.toLowerCase());
        ResultSetProcessor<SavedLocation> locationProcessor = rs -> {
            if (rs.next()) {
                return new SavedLocation(uuid, rs.getString("label"), rs.getString("world"),
                        rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
            }
            return null;
        };
        return database.executeQuery(query, params, locationProcessor);
    }

    public void saveLocationData(SavedLocation location) {

    }

    public List<SavedLocation> getAllPlayerLocations(UUID uuid) {
        String query = "SELECT label, world, x, y, z FROM locations WHERE uuid = ?";
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        ResultSetProcessor<List<SavedLocation>> locationsProcessor = rs -> {
            List<SavedLocation> locations = new ArrayList<>();
            while (rs.next()) {
                locations.add(new SavedLocation(uuid, rs.getString("label"), rs.getString("world"),
                        rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")));
            }
            return locations;
        };
        return database.executeQuery(query, params, locationsProcessor);
    }


}
