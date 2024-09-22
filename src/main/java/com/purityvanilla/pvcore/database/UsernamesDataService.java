package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;

public class UsernamesDataService extends DataService {

    public UsernamesDataService(pvCore plugin, DatabaseHandler database) {
        super(plugin, database);
    }

    @Override
    protected void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS usernames (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    uuid CHAR(36) NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    joined TIMESTAMP NOT NULL,
                    UNIQUE (uuid, name),
                    CONSTRAINT fk_uuid FOREIGN KEY (uuid) REFERENCES players (uuid) ON DELETE CASCADE
                )
                """;
        database.executeUpdate(query);
    }
}
