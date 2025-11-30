package com.purityvanilla.pvcore.database.migration;

import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.database.migration.Migration;

public class V3Migration implements Migration {
    private final DatabaseConnector database;

    public V3Migration(DatabaseConnector database) {
        this.database = database;
    }

    @Override
    public int getVersion() {
        return 3;
    }

    @Override
    public void migrate() {
        String query = """
            CREATE TABLE IF NOT EXISTS player_ignores (
                player_uuid CHAR(36) NOT NULL,
                ignored_uuid CHAR(36) NOT NULL,
                PRIMARY KEY (player_uuid, ignored_uuid),
                CONSTRAINT fk_player_uuid FOREIGN KEY (player_uuid) REFERENCES players (uuid) ON DELETE CASCADE,
                CONSTRAINT fk_ignored_uuid FOREIGN KEY (ignored_uuid) REFERENCES players (uuid) ON DELETE CASCADE,
                CONSTRAINT player_self_reference CHECK (player_uuid <> ignored_uuid)
            )
            """;
        database.executeUpdate(query);
    }
}
