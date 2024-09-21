package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

public class PlayerDataService {
    private final pvCore plugin;

    public PlayerDataService(pvCore plugin) {
        this.plugin = plugin;
    }

    private void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS players (
                    uuid CHAR(36) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,   
                )
                """;
    }
}
