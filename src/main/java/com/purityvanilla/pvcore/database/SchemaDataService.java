package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.PVCore;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class SchemaDataService extends DataService {
    private final SchemaOperator operator;
    private final int currentVersion = 3;

    public SchemaDataService(PVCore plugin, DatabaseConnector database) {
        super(plugin);
        operator = new SchemaOperator(database);
        int dbVersion = operator.getDBVersion();

        // Set stored schema version to current version if no results (assumes entire database is fresh)
        if (dbVersion == 0) {
            plugin.getLogger().info(PlainTextComponentSerializer.plainText().serialize(plugin.config().getMessage("database-creation")));
            updateSchemaVersion();
        }
    }

    @Override
    public void saveAll() {
        operator.updateSchemaVersion(currentVersion);
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public int getDBVersion() {
        return operator.getDBVersion();
    }

    public void updateSchemaVersion() {
        operator.updateSchemaVersion(currentVersion);
    }
}
