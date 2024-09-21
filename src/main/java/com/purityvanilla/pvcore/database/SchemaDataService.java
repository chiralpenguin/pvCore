package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SchemaDataService {
    private final pvCore plugin;

    public final int currentVersion = 1;

    public SchemaDataService(pvCore plugin) {
        this.plugin = plugin;
        createTable();
        updateSchemaVersion();
    }

    private void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS schema_version (
                    version INT PRIMARY KEY
                )
                """;
        plugin.getDatabase().executeUpdate(query);
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public int getDBVersion() {
        String query = "SELECT version FROM schema_version";
        ResultSet results = plugin.getDatabase().executeQuery(query);
    }

    private void updateSchemaVersion() {
        String query = "UPDATE TABLE schema_version SET version = ?";
        List<String> params = new ArrayList<>();
        params.add(currentVersion + "");
    }
}
