package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

public class PlayerDataService extends DataService {

    public PlayerDataService(pvCore plugin, DatabaseHandler database) {
        super(plugin, database);
    }

    @Override
    protected void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS players (
                    uuid CHAR(36) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    last_seen TIMESTAMP NOT NULL
                )
                """;
        database.executeUpdate(query);
    }
}
