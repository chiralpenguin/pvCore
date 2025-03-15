package com.purityvanilla.pvcore.database.migration;

import com.purityvanilla.pvcore.database.DatabaseConnector;

public class V2Migration implements Migration {
    private final DatabaseConnector database;

    public V2Migration(DatabaseConnector database) {
        this.database = database;
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public void migrate() {
        String query = """
            CREATE TABLE IF NOT EXISTS nicknames (
                uuid CHAR(36) PRIMARY KEY,
                nickname VARCHAR(1024) NOT NULL,
                CONSTRAINT fk_nicknames_uuid FOREIGN KEY (uuid) REFERENCES players (uuid) ON DELETE CASCADE
            )
            """;
        database.executeUpdate(query);
    }
}
