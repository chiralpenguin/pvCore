package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.database.DataService;
import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.util.CacheHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocationDataService extends DataService {
    private final LocationOperator operator;
    private final ConcurrentHashMap<UUID, ConcurrentHashMap<String, SavedLocation>> locationCache;

    public LocationDataService(PVCore plugin, DatabaseConnector database) {
        super(plugin);
        operator = new LocationOperator(database);

        locationCache = new ConcurrentHashMap<>();
    }

    @Override
    public void saveAll() {
        for (ConcurrentHashMap<String, SavedLocation> locationList : locationCache.values()) {
            for (SavedLocation location : locationList.values()) {
                operator.saveLocationData(location);
            }
        }
    }

    @Override
    public void cleanCache() {
        for (UUID absentPlayer : CacheHelper.getAbsentUUIDs(locationCache.keySet(), plugin)) {
            unloadAllPlayerLocations(absentPlayer);
        }
    }

    public boolean isCached(UUID playerID, String label) {
        return locationCache.containsKey(playerID) && locationCache.get(playerID).containsKey(label.toLowerCase());
    }

    public void cacheLocation(SavedLocation location) {
        UUID playerID = location.getPlayerID();
        if (!locationCache.containsKey(playerID)) {
            locationCache.put(playerID, new ConcurrentHashMap<>());
        }
        locationCache.get(playerID).put(location.getLabel(), location);
    }

    public void unloadLocation(SavedLocation location) {
        UUID playerID = location.getPlayerID();
        if (!locationCache.containsKey(playerID)) {
            return;
        }

        operator.saveLocationData(location);
        locationCache.get(playerID).remove(location.getLabel());
        if (locationCache.get(playerID).isEmpty()) {
            locationCache.remove(playerID);
        }
    }

    /**
     * Get a location from the cache if possible, otherwise attempt to load the location's data from database
     * @return {@code SavedLocation} if it exists, otherwise null
     */
    public SavedLocation getLocation(UUID playerID, String label) {
        if (isCached(playerID, label)) {
            return locationCache.get(playerID).get(label.toLowerCase());
        }

        SavedLocation location = operator.getLocationData(playerID, label);
        if (location != null) {
            cacheLocation(location);
        }
        return location;
    }

    /**
     * @return False if the return value of getLocation() is not null, True otherwise
     */
    public boolean locationExists(UUID playerID, String label) {
        return getLocation(playerID, label) != null;
    }

    /**
     * Load all of a player's saved locations into the cache from the database, regardless of any already loaded
     * @return Number of locations loaded
     */
    public int loadAllPlayerLocations(UUID playerID) {
        List<SavedLocation> locations = operator.getAllPlayerLocationData(playerID);
        for (SavedLocation location : locations) {
            cacheLocation(location);
        }
        return locations.size();
    }

    /**
     * Remove all of a player's saved locations from the cache and save them in the database
     * @return Number of locations saved
     */
    public int unloadAllPlayerLocations(UUID playerID) {
        if (!locationCache.containsKey(playerID)) {
            return 0;
        }

        int unloadedLocations = 0;
        for (SavedLocation location : locationCache.get(playerID).values()) {
            unloadLocation(location);
            unloadedLocations += 1;
        }
        return unloadedLocations;
    }

    /**
     * Get a copy of a player's saved locations from the cache (no DB).
     * @return {@code List<SavedLocation>} containing all loaded player locations, empty list if none in cache
     */
    public List<SavedLocation> getPlayerLocations(UUID playerID) {
        if (!locationCache.containsKey(playerID)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(locationCache.get(playerID).values());
    }

    /**
     * Add a new saved player location by saving a record in the database and then loading it into the cache.
     */
    public void addLocation(UUID playerID, SavedLocation location) {
        operator.saveLocationData(location);
        cacheLocation(location);
    }

    /**
     * Remove a saved player location entirely by unloading it from the cache and then deleting the
     * record from the database.
     */
    public void removeLocation(UUID playerID, SavedLocation location) {
        unloadLocation(location);
        operator.removeLocationData(playerID, location.getLabel());
    }
}
