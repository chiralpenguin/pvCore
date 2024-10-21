package com.purityvanilla.pvcore.database;

import java.util.ArrayList;
import java.util.List;

public class SchemaOperator extends DatabaseOperator {
    public SchemaOperator(DatabaseConnector database) {
        super(database);
    }

    @Override
    protected void createTables() {
        String query = """
                CREATE TABLE IF NOT EXISTS schema_version (
                    version INT PRIMARY KEY
                )
                """;
        database.executeUpdate(query);
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

    public void updateSchemaVersion(int currentVersion) {
        String query = "REPLACE INTO schema_version (version) VALUES (?)";
        List<Object> params = new ArrayList<>();
        params.add(currentVersion + "");
        database.executeUpdate(query, params);
    }
}
