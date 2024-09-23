package com.purityvanilla.pvcore.database;

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

    @Override
    protected void createTables() {
        String query = """
                CREATE TABLE IF NOT EXISTS players (
                    uuid CHAR(36) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    last_seen TIMESTAMP NOT NULL
                )
                """;
        database.executeUpdate(query);
        query = """
                CREATE TABLE IF NOT EXISTS usernames (
                    uuid CHAR(36) NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    last_seen TIMESTAMP NOT NULL,
                    PRIMARY KEY (uuid, name),
                    CONSTRAINT fk_uuid FOREIGN KEY (uuid) REFERENCES players (uuid) ON DELETE CASCADE
                )
                """;
        database.executeUpdate(query);
    }

    public record CachedPlayer(UUID uuid, String name, Timestamp lastSeen) {

    }

    public CachedPlayer loadPlayer(UUID uuid) {
        String query = "SELECT uuid, name, last_seen FROM players WHERE uuid = ?";
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

    public void savePlayer(UUID uuid, String name, Timestamp lastSeen) {
        String query = """
                INSERT INTO players (uuid, name, last_seen) VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE name = VALUES(name), last_seen = VALUES(last_seen)
                """;
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        params.add(name);
        params.add(lastSeen);
        database.executeUpdate(query, params);
    }

    public void savePlayer(CachedPlayer cPlayer) {
        savePlayer(cPlayer.uuid, cPlayer.name, cPlayer.lastSeen);
    }

    public CachedPlayer getPlayer(UUID uuid) {
        if (playerCache.containsKey(uuid)) {
            return playerCache.get(uuid);
        }

        // If UUID invalid or player has not joined server
        OfflinePlayer player = plugin.getServer().getOfflinePlayer(uuid);
        if (player.getName() == null) {
            return null;
        }

        CachedPlayer cPlayer = loadPlayer(uuid);
        // If player exists but not stored in database
        if (cPlayer == null) {
            cPlayer = new CachedPlayer(uuid, player.getName(), new Timestamp(player.getLastSeen()));
            savePlayer(cPlayer);
        }

        playerCache.put(uuid, cPlayer);
        return cPlayer;
    }
}
