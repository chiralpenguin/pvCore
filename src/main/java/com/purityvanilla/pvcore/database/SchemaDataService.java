package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class SchemaDataService {
    private final pvCore plugin;
    private final DatabaseHandler database;
    private final int currentVersion = 1;

    public SchemaDataService(pvCore plugin) {
        this.plugin = plugin;
        this.database = plugin.getDatabase();
        createTable();

        if (getDBVersion() < currentVersion) {
            migrateSchema();
        }
    }

    private void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS schema_version (
                    version INT PRIMARY KEY
                )
                """;
        database.executeUpdate(query);
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public int getDBVersion() {
        String query = "SELECT version FROM schema_version";
        ResultSetProcessor<Integer> versionProcessor = rs -> {
            if (rs.next()) {
                return rs.getInt("version");
            }
            return -1;
        };

        return database.executeQuery(query, versionProcessor);
    }

    private void updateSchemaVersion() {
        String query = "REPLACE INTO schema_version (version) VALUES (?)";
        List<String> params = new ArrayList<>();
        params.add(currentVersion + "");
        database.executeUpdate(query, params);
    }

    private void migrateSchema() {
        plugin.getLogger().info(PlainTextComponentSerializer.plainText().serialize(plugin.config().getMessage("database-migration")));
        updateSchemaVersion();
    }
}
