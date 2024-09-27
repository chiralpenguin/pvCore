package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerDataService extends DataService {
    private final HashMap<UUID, CachedPlayer> playerCache;

    public PlayerDataService(pvCore plugin, DatabaseHandler database) {
        super(plugin, database);

        playerCache = new HashMap<>();
    }

    // DataServices base methods
    @Override
    protected void createTables() {
        String query = """
                CREATE TABLE IF NOT EXISTS players (
                    uuid CHAR(36) PRIMARY KEY
                )
                """;
        database.executeUpdate(query);
        query = """
                CREATE TABLE IF NOT EXISTS usernames (
                    uuid CHAR(36) NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    last_seen TIMESTAMP NOT NULL,
                    PRIMARY KEY (uuid, name),
                    CONSTRAINT fk_usernames_uuid FOREIGN KEY (uuid) REFERENCES players (uuid) ON DELETE CASCADE
                )
                """;
        database.executeUpdate(query);
    }

    @Override
    public void saveAll() {
        for (CachedPlayer cPlayer : playerCache.values()) {
            savePlayerData(cPlayer);
        }
    }

    // Database methods
    public CachedPlayer getPlayerData(UUID uuid) {
        String query = "SELECT name, last_seen FROM usernames WHERE uuid = ? ORDER BY last_seen DESC LIMIT 1";
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        ResultSetProcessor<CachedPlayer> playerProcessor = rs -> {
            if (rs.next()) {
                return new CachedPlayer(uuid, rs.getString("name"), rs.getTimestamp("last_seen"));
            }
            return null;
        };
        return database.executeQuery(query, params, playerProcessor);
    }

    public void savePlayerData(UUID uuid, String name, Timestamp lastSeen) {
        String query = "INSERT IGNORE INTO players (uuid) VALUES (?)";
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        database.executeUpdate(query, params);

        query = """
                INSERT INTO usernames (uuid, name, last_seen) VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE last_seen = VALUES(last_seen)
                """;
        params = new ArrayList<>();
        params.add(uuid);
        params.add(name);
        params.add(lastSeen);
        database.executeUpdate(query, params);
    }

    public void savePlayerData(CachedPlayer cPlayer) {
        savePlayerData(cPlayer.uuid(), cPlayer.name(), cPlayer.lastSeen());
    }

    // Cache methods
    public boolean isCached(UUID uuid) {
        return playerCache.containsKey(uuid);
    }

    public CachedPlayer getPlayer(UUID uuid) {
        if (isCached(uuid)) {
            return playerCache.get(uuid);
        }

        OfflinePlayer player = plugin.getServer().getOfflinePlayer(uuid);
        // If UUID invalid or player has not joined server
        if (player.getName() == null) {
            return null;
        }

        CachedPlayer cPlayer = getPlayerData(uuid);
        // Initialise player if valid but not yet stored in database
        if (cPlayer == null) {
            cPlayer = new CachedPlayer(player.getUniqueId(), player.getName(), new Timestamp(player.getLastSeen()));
            savePlayerData(cPlayer);
        }

        playerCache.put(uuid, cPlayer);
        return cPlayer;
    }

    public void unloadPlayer(UUID uuid) {
        CachedPlayer cPlayer = getPlayer(uuid);
        savePlayerData(cPlayer);
        playerCache.remove(uuid);
    }
}
