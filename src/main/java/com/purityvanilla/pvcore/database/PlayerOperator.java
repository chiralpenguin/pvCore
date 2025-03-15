package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.CachedPlayer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerOperator extends DatabaseOperator {

    public PlayerOperator(DatabaseConnector database) {
        super(database);
    }

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
        query = """
            CREATE TABLE IF NOT EXISTS nicknames (
                uuid CHAR(36) PRIMARY KEY,
                nickname VARCHAR(1024) NOT NULL,
                CONSTRAINT fk_nicknames_uuid FOREIGN KEY (uuid) REFERENCES players (uuid) ON DELETE CASCADE
            )
            """;
        database.executeUpdate(query);
    }

    public CachedPlayer getPlayerData(UUID uuid) {
        String query = """
            SELECT u.name, u.last_seen, n.nickname FROM usernames u
            LEFT JOIN nicknames n ON u.uuid = n.uuid
            WHERE u.uuid = ? ORDER BY u.last_seen DESC LIMIT 1
            """;
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        ResultSetProcessor<CachedPlayer> playerProcessor = rs -> {
            if (rs.next()) {
                return new CachedPlayer(uuid, rs.getString("u.name"), rs.getTimestamp("u.last_seen"), rs.getString("n.nickname"));
            }
            return null;
        };
        return database.executeQuery(query, params, playerProcessor);
    }

    public void savePlayerData(UUID uuid, String name, Timestamp lastSeen, String nickString) {
        // Update players table
        String query = "INSERT IGNORE INTO players (uuid) VALUES (?)";
        List<Object> params = new ArrayList<>();
        params.add(uuid);
        database.executeUpdate(query, params);

        // Update usernames table, updating last_seen only if (uuid, username) combination already exists
        query = """
                INSERT INTO usernames (uuid, name, last_seen) VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE last_seen = VALUES(last_seen)
                """;
        params = new ArrayList<>();
        params.add(uuid);
        params.add(name);
        params.add(lastSeen);
        database.executeUpdate(query, params);

        // Update nicknames table (deleting old records if player has no nickname)
        params = new ArrayList<>();
        params.add(uuid);
        if (nickString == null) {
            // Could improve performance by preforming presence check query first if the majority of players have no nickname
            query = "DELETE FROM nicknames WHERE uuid = ?";
        } else {
            params.add(nickString);
            query = """
                    INSERT INTO nicknames (uuid, nickname) VALUES (?, ?)
                    ON DUPLICATE KEY UPDATE nickname = VALUES(nickname)
                    """;
        }
        database.executeUpdate(query, params);
    }

    public void savePlayerData(CachedPlayer cPlayer) {
        savePlayerData(cPlayer.uuid(), cPlayer.name(), cPlayer.lastSeen(), cPlayer.getNickString());
    }

    public UUID getUUIDFromName(String username) {
        String query = "SELECT uuid FROM usernames WHERE name = ? ORDER BY last_seen DESC LIMIT 1";
        List<Object> params = new ArrayList<>();
        params.add(username);
        ResultSetProcessor<UUID> uuidProcessor = rs -> {
            if (rs.next()) {
                return UUID.fromString(rs.getString("uuid"));
            }
            return null;
        };
        return database.executeQuery(query, params, uuidProcessor);
    }
}
